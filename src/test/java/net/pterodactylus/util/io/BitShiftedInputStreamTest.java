/*
 * utils - BitShiftedInputStreamTest.java - Copyright © 2006–2019 David Roden
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.pterodactylus.util.io;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import junit.framework.TestCase;

/**
 * JUnit test case for {@link BitShiftedInputStream}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class BitShiftedInputStreamTest extends TestCase {

	/**
	 * Test method for {@link BitShiftedInputStream#read()} and
	 * {@link BitShiftedInputStream#skipBits(int)}.
	 *
	 * @throws Exception
	 *             if an error occurs
	 */
	public void testOne() throws Exception {
		InputStream inputStream = new ByteArrayInputStream(new byte[] { (byte) 0xff, 0x00, (byte) 0xff, 0x00, (byte) 0xff });
		BitShiftedInputStream bitInputStream = new BitShiftedInputStream(inputStream, 5);
		assertEquals("value 0", 0x1f, bitInputStream.read());
		assertEquals("value 1", 0x07, bitInputStream.read());
		assertEquals("value 2", 0x00, bitInputStream.read());
		assertEquals("value 3", 0x1e, bitInputStream.read());
		assertEquals("value 4", 0x0f, bitInputStream.read());
		bitInputStream.skipBits(6);
		assertEquals("value 5", 0x1e, bitInputStream.read());
	}

}
