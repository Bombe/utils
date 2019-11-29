/*
 * utils - NullOutputStream.java - Copyright © 2012–2019 David Roden
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package net.pterodactylus.util.io;

import java.io.OutputStream;

/**
 * {@link OutputStream} that discards all written bytes.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class NullOutputStream extends OutputStream {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void write(int b) {
		/* do nothing. */
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void write(byte[] b) {
		/* do nothing. */
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void write(byte[] b, int off, int len) {
		/* do nothing. */
	}

}
