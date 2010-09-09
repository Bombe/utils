/*
 * utils - TemporaryInputStream.java - Copyright © 2010 David Roden
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * An input stream implementation that copies a given input stream to a
 * temporary file and delivers the content of the temporary file at a later
 * time.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class TemporaryInputStream extends FilterInputStream {

	/**
	 * Maps input streams to temporary files, for deletion on {@link #close()}.
	 */
	private static final Map<InputStream, File> streamFiles = new HashMap<InputStream, File>();

	/** Counter for streams per file. */
	private static final Map<File, Integer> fileCounts = new HashMap<File, Integer>();

	/**
	 * Creates a new temporary input stream.
	 *
	 * @param sourceInputStream
	 *            The input stream to copy to a temporary file
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public TemporaryInputStream(InputStream sourceInputStream) throws IOException {
		super(createFileInputStream(sourceInputStream));
	}

	/**
	 * Creates a new temporary input stream from the given temporary file.
	 *
	 * @param tempFile
	 *            The temporary file
	 * @throws FileNotFoundException
	 *             if the file can not be found
	 */
	private TemporaryInputStream(File tempFile) throws FileNotFoundException {
		super(new FileInputStream(tempFile));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() throws IOException {
		super.close();
		synchronized (fileCounts) {
			File tempFile = streamFiles.remove(in);
			if (tempFile != null) {
				if (fileCounts.get(tempFile) > 0) {
					fileCounts.put(tempFile, fileCounts.get(tempFile) - 1);
				} else {
					fileCounts.remove(tempFile);
					tempFile.delete();
				}
			}
		}
	}

	/**
	 * Creates a new input stream from the temporary file that is backing this
	 * input stream. If the file has already been removed, this method will
	 * throw an exception.
	 *
	 * @return A new input stream
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public InputStream reopen() throws IOException {
		synchronized (fileCounts) {
			File tempFile = streamFiles.get(in);
			if (tempFile != null) {
				fileCounts.put(tempFile, fileCounts.get(tempFile) + 1);
				return new TemporaryInputStream(tempFile);
			}
			throw new FileNotFoundException("Temporary file has already disappeared.");
		}
	}

	/**
	 * Creates a temporary file, copies the given input stream to the temporary
	 * file, and creates an input stream reading from the temporary file. The
	 * returned input stream will delete the temporary file when its
	 * {@link #close()} method is called.
	 *
	 * @param sourceInputStream
	 *            The input stream to copy
	 * @return The copied input stream, ready for consumption
	 * @throws IOException
	 */
	private static InputStream createFileInputStream(InputStream sourceInputStream) throws IOException {
		File tempFile = File.createTempFile("utils-temp-", ".tmp");
		tempFile.deleteOnExit();
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(tempFile);
			StreamCopier.copy(sourceInputStream, fileOutputStream);
		} catch (IOException ioe1) {
			throw ioe1;
		} finally {
			Closer.close(fileOutputStream);
		}
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(tempFile);
			streamFiles.put(fileInputStream, tempFile);
			synchronized (fileCounts) {
				fileCounts.put(tempFile, 0);
			}
			return fileInputStream;
		} catch (IOException ioe1) {
			Closer.close(fileInputStream);
			tempFile.delete();
			throw ioe1;
		}
	}

}
