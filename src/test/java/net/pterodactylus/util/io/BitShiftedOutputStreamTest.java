/*
 * utils - BitShiftedOutputStreamTest.java - Copyright © 2006–2016 David Roden
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

import junit.framework.TestCase;

/**
 * JUnit test case for {@link BitShiftedOutputStream}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class BitShiftedOutputStreamTest extends TestCase {

	/**
	 * Tests for various methods in {@link BitShiftedOutputStream}.
	 *
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public void testBitShiftedOutputStream() throws IOException {
		ByteArrayOutputStream byteArrayOutputStream;
		BitShiftedOutputStream bitShiftedOutputStream;
		byte[] byteArray;

		byteArrayOutputStream = new ByteArrayOutputStream();
		bitShiftedOutputStream = new BitShiftedOutputStream(byteArrayOutputStream, 4);
		bitShiftedOutputStream.write(0x04);
		bitShiftedOutputStream.write(0x08);
		bitShiftedOutputStream.write(0x0c);
		bitShiftedOutputStream.flush(0x01);
		byteArray = byteArrayOutputStream.toByteArray();
		assertEquals("length", 2, byteArray.length);
		assertEquals("byte 0", (byte) 0x84, byteArray[0]);
		assertEquals("byte 1", (byte) 0xfc, byteArray[1]);

		byteArrayOutputStream = new ByteArrayOutputStream();
		bitShiftedOutputStream = new BitShiftedOutputStream(byteArrayOutputStream, 12);
		bitShiftedOutputStream.write(0x04);
		bitShiftedOutputStream.write(0x08);
		bitShiftedOutputStream.write(0x0c);
		bitShiftedOutputStream.flush(0x01);
		byteArray = byteArrayOutputStream.toByteArray();
		assertEquals("length", 5, byteArray.length);
		assertEquals("byte 0", (byte) 0x04, byteArray[0]);
		assertEquals("byte 1", (byte) 0x80, byteArray[1]);
		assertEquals("byte 2", (byte) 0x00, byteArray[2]);
		assertEquals("byte 3", (byte) 0x0c, byteArray[3]);
		assertEquals("byte 4", (byte) 0xf0, byteArray[4]);

		byteArrayOutputStream = new ByteArrayOutputStream();
		bitShiftedOutputStream = new BitShiftedOutputStream(byteArrayOutputStream, 12);
		bitShiftedOutputStream.write(0x004);
		bitShiftedOutputStream.write(0x008);
		bitShiftedOutputStream.write(0x00c);
		bitShiftedOutputStream.write(0x555);
		bitShiftedOutputStream.flush(0x01);
		byteArray = byteArrayOutputStream.toByteArray();
		assertEquals("length", 6, byteArray.length);
		assertEquals("byte 0", (byte) 0x04, byteArray[0]);
		assertEquals("byte 1", (byte) 0x80, byteArray[1]);
		assertEquals("byte 2", (byte) 0x00, byteArray[2]);
		assertEquals("byte 3", (byte) 0x0c, byteArray[3]);
		assertEquals("byte 4", (byte) 0x50, byteArray[4]);
		assertEquals("byte 5", (byte) 0x55, byteArray[5]);

		byteArrayOutputStream = new ByteArrayOutputStream();
		bitShiftedOutputStream = new BitShiftedOutputStream(byteArrayOutputStream, 12);
		bitShiftedOutputStream.write(0x004);
		bitShiftedOutputStream.write(0x008);
		bitShiftedOutputStream.writePadding(7);
		bitShiftedOutputStream.write(0x00c);
		bitShiftedOutputStream.write(0x555);
		bitShiftedOutputStream.flush(0x01);
		byteArray = byteArrayOutputStream.toByteArray();
		assertEquals("length", 7, byteArray.length);
		assertEquals("byte 0", (byte) 0x04, byteArray[0]);
		assertEquals("byte 1", (byte) 0x80, byteArray[1]);
		assertEquals("byte 2", (byte) 0x00, byteArray[2]);
		assertEquals("byte 3", (byte) 0x00, byteArray[3]);
		assertEquals("byte 4", (byte) 0x06, byteArray[4]);
		assertEquals("byte 5", (byte) 0xa8, byteArray[5]);
		assertEquals("byte 6", (byte) 0xaa, byteArray[6]);
	}

}
