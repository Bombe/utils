/*
 * utils - MessageDigestHasher.java - Copyright © 2012 David Roden
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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * {@link Hasher} implementation that uses a {@link MessageDigest} to create the
 * hash.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class MessageDigestHasher implements Hasher {

	/** The message digest. */
	private final MessageDigest messageDigest;

	/**
	 * Creates a new message digest hasher.
	 *
	 * @param messageDigest
	 *            The message digest
	 */
	public MessageDigestHasher(MessageDigest messageDigest) {
		this.messageDigest = messageDigest;
	}

	//
	// HASHER METHODS
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] hash(byte[] data) {
		synchronized (messageDigest) {
			messageDigest.reset();
			return messageDigest.digest(data);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] hash(InputStream inputStream) throws IOException {
		synchronized (messageDigest) {
			messageDigest.reset();
			byte[] buffer = new byte[65536];
			while (true) {
				int read = inputStream.read(buffer);
				if (read == -1) {
					break;
				}
				messageDigest.update(buffer, 0, read);
			}
			return messageDigest.digest();
		}
	}

	//
	// STATIC METHODS
	//

	/**
	 * Returns a hasher using the SHA-1 algorithm.
	 *
	 * @return An SHA-1 hasher
	 */
	public static Hasher getSHA1Hasher() {
		try {
			return new MessageDigestHasher(MessageDigest.getInstance("SHA-1"));
		} catch (NoSuchAlgorithmException nsae1) {
			throw new RuntimeException(nsae1);
		}
	}

	/**
	 * Returns a hasher using the SHA-256 algorithm.
	 *
	 * @return An SHA-1 hasher
	 */
	public static Hasher getSHA256Hasher() {
		try {
			return new MessageDigestHasher(MessageDigest.getInstance("SHA-256"));
		} catch (NoSuchAlgorithmException nsae1) {
			throw new RuntimeException(nsae1);
		}
	}

	/**
	 * Returns a hasher using the SHA-512 algorithm.
	 *
	 * @return An SHA-1 hasher
	 */
	public static Hasher getSHA512Hasher() {
		try {
			return new MessageDigestHasher(MessageDigest.getInstance("SHA-512"));
		} catch (NoSuchAlgorithmException nsae1) {
			throw new RuntimeException(nsae1);
		}
	}

}
