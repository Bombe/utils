/*
 * utils - Closer.java - Copyright © 2009 David Roden
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

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;
import java.util.jar.JarFile;
import java.util.zip.ZipFile;

/**
 * Closes various resources. The resources are checked for being
 * <code>null</code> before being closed, and every possible execption is
 * swallowed. That makes this class perfect for use in the finally blocks of
 * try-catch-finally blocks.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Closer {

	/**
	 * Closes the given closeable.
	 *
	 * @param closeable
	 *            The closeable to close
	 */
	public static void close(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException ioe1) {
				/* ignore. */
			}
		}
	}

	/**
	 * Closes the given socket.
	 *
	 * @param socket
	 *            The socket to close
	 */
	public static void close(Socket socket) {
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException ioe1) {
				/* ignore. */
			}
		}
	}

	/**
	 * Closes the given jar file.
	 *
	 * @param jarFile
	 *            The jar file to close
	 */
	public static void close(JarFile jarFile) {
		if (jarFile != null) {
			try {
				jarFile.close();
			} catch (IOException e) {
				/* ignore. */
			}
		}
	}

	/**
	 * Closes the given zip file.
	 *
	 * @param zipFile
	 *            The zip file to close
	 */
	public static void close(ZipFile zipFile) {
		if (zipFile != null) {
			try {
				zipFile.close();
			} catch (IOException e) {
				/* ignore. */
			}
		}
	}

}
