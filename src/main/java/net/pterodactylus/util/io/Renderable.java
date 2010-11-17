/*
 * utils - Renderable.java - Copyright © 2010 David Roden
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

import java.io.IOException;
import java.io.Writer;

/**
 * Interface for objects that can render themselves to a {@link Writer}. It is
 * not suitable for objects that want to write binary output.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public interface Renderable {

	/**
	 * Renders this object to the given writer.
	 *
	 * @param writer
	 *            The writer to render the object to
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public void render(Writer writer) throws IOException;

}
