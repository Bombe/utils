/*
 * utils - LimitedInputStream.java - Copyright © 2008-2010 David Roden
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

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * A wrapper around an {@link InputStream} that only supplies a limit number of
 * bytes from the underlying input stream.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class LimitedInputStream extends FilterInputStream {

	/** The remaining number of bytes that can be read. */
	private long remaining;

	/**
	 * Creates a new LimitedInputStream that supplies at most
	 * <code>length</code> bytes from the given input stream.
	 *
	 * @param inputStream
	 *            The input stream
	 * @param length
	 *            The number of bytes to read
	 */
	public LimitedInputStream(InputStream inputStream, long length) {
		super(inputStream);
		remaining = length;
	}

	/**
	 * @see java.io.FilterInputStream#available()
	 */
	@Override
	public synchronized int available() throws IOException {
		if (remaining == 0) {
			return 0;
		}
		return (int) Math.min(super.available(), Math.min(Integer.MAX_VALUE, remaining));
	}

	/**
	 * @see java.io.FilterInputStream#read()
	 */
	@Override
	public synchronized int read() throws IOException {
		int read = -1;
		if (remaining > 0) {
			read = super.read();
			remaining--;
		}
		return read;
	}

	/**
	 * @see java.io.FilterInputStream#read(byte[], int, int)
	 */
	@Override
	public synchronized int read(byte[] b, int off, int len) throws IOException {
		if (remaining == 0) {
			return -1;
		}
		int toCopy = (int) Math.min(len, Math.min(remaining, Integer.MAX_VALUE));
		int read = super.read(b, off, toCopy);
		remaining -= read;
		return read;
	}

	/**
	 * @see java.io.FilterInputStream#skip(long)
	 */
	@Override
	public synchronized long skip(long n) throws IOException {
		if ((n < 0) || (remaining == 0)) {
			return 0;
		}
		long skipped = super.skip(Math.min(n, remaining));
		remaining -= skipped;
		return skipped;
	}

	/**
	 * {@inheritDoc} This method does nothing, as {@link #mark(int)} and
	 * {@link #reset()} are not supported.
	 *
	 * @see java.io.FilterInputStream#mark(int)
	 */
	@Override
	public void mark(int readlimit) {
		/* do nothing. */
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.io.FilterInputStream#markSupported()
	 * @return <code>false</code>
	 */
	@Override
	public boolean markSupported() {
		return false;
	}

	/**
	 * {@inheritDoc} This method does nothing, as {@link #mark(int)} and
	 * {@link #reset()} are not supported.
	 *
	 * @see java.io.FilterInputStream#reset()
	 */
	@Override
	public void reset() throws IOException {
		/* do nothing. */
	}

	/**
	 * Consumes the input stream, i.e. read all bytes until the limit is
	 * reached.
	 *
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public void consume() throws IOException {
		while (remaining > 0) {
			skip(remaining);
		}
	}

}
