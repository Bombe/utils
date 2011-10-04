/*
 * utils - StreamReader.java - Copyright © 2011 David Roden
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

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Helper methods that read certain numbers of bytes from an {@link InputStream}
 * and throw an {@link EOFException} if the input stream is depleted before the
 * requested number of bytes is read.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class StreamReader {

	/**
	 * Reads a single byte from the given input stream.
	 *
	 * @param inputStream
	 *            The input stream to read from
	 * @return The read byte
	 * @throws IOException
	 *             if an I/O error occurs or EOF is reached
	 */
	public static byte read(InputStream inputStream) throws IOException {
		int result = inputStream.read();
		if (result == -1) {
			throw new EOFException();
		}
		return (byte) result;
	}

	/**
	 * Fills the given buffer completely from the given input stream. If EOF is
	 * reached before the buffer is completely filled, contents of the buffer
	 * may have partially been overwritten.
	 *
	 * @param inputStream
	 *            The input stream to read from
	 * @param buffer
	 *            The buffer to fill
	 * @throws IOException
	 *             if an I/O error occurs or EOF is reached before the buffer is
	 *             filled
	 */
	public static void readFully(InputStream inputStream, byte[] buffer) throws IOException {
		readFully(inputStream, buffer, 0, buffer.length);
	}

	/**
	 * Fills the given part of the buffer from the given input stream. If EOF is
	 * reached before the buffer is completely filled, contents of the buffer
	 * may have partially been overwritten.
	 *
	 * @param inputStream
	 *            The input stream to read from
	 * @param buffer
	 *            The buffer to fill
	 * @param position
	 *            The position at which to start filling the buffer
	 * @param length
	 *            The number of bytes to read
	 * @throws IOException
	 *             if an I/O error occurs or EOF is reached before the buffer is
	 *             filled
	 */
	public static void readFully(InputStream inputStream, byte[] buffer, int position, int length) throws IOException {
		int totalRead = 0;
		while (totalRead < length) {
			int read = inputStream.read(buffer, position + totalRead, length - totalRead);
			if (read == -1) {
				throw new EOFException();
			}
			totalRead += read;
		}
	}

}
