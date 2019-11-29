/*
 * utils - TeeOutputStreamTest.java - Copyright © 2010–2019 David Roden
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import junit.framework.TestCase;

/**
 * JUnit test case for {@link TeeOutputStream}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class TeeOutputStreamTest extends TestCase {

	/**
	 * Tests the {@code write()} methods of the {@link TeeOutputStream}.
	 *
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public void test() throws IOException {
		ByteArrayOutputStream buffer1 = new ByteArrayOutputStream(10);
		ByteArrayOutputStream buffer2 = new ByteArrayOutputStream(10);
		TeeOutputStream teeOutputStream = new TeeOutputStream(buffer1, buffer2);
		try {
			teeOutputStream.write(1);
			teeOutputStream.flush();
			assertTrue(Arrays.equals(new byte[] { 1 }, buffer1.toByteArray()));
			assertTrue(Arrays.equals(new byte[] { 1 }, buffer2.toByteArray()));

			teeOutputStream.write(new byte[] { 2, 3 });
			teeOutputStream.flush();
			assertTrue(Arrays.equals(new byte[] { 1, 2, 3 }, buffer1.toByteArray()));
			assertTrue(Arrays.equals(new byte[] { 1, 2, 3 }, buffer2.toByteArray()));

			teeOutputStream.write(new byte[] { 0, 1, 2, 3, 4, 5, 6 }, 4, 2);
			teeOutputStream.flush();
			assertTrue(Arrays.equals(new byte[] { 1, 2, 3, 4, 5 }, buffer1.toByteArray()));
			assertTrue(Arrays.equals(new byte[] { 1, 2, 3, 4, 5 }, buffer2.toByteArray()));
		} finally {
			teeOutputStream.close();
		}
	}
}
