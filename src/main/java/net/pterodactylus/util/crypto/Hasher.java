/*
 * utils - Hasher.java - Copyright © 2011–2012 David Roden
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

package net.pterodactylus.util.crypto;

import java.io.IOException;
import java.io.InputStream;

/**
 * Interface for objects that can create hashes of byte-based data.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public interface Hasher {

	/**
	 * Hashes the given data.
	 *
	 * @param data
	 *            The data to hash
	 * @return The hash of the data
	 */
	public byte[] hash(byte[] data);

	/**
	 * Hashes the data from the given input stream. The input stream will be
	 * read until {@link InputStream#read()} returns {@code -1}, or throws an
	 * exception.
	 *
	 * @param inputStream
	 *            The input stream to read from
	 * @return The hash of the data
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public byte[] hash(InputStream inputStream) throws IOException;

}
