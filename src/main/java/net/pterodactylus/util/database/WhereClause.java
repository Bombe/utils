/*
 * utils - WhereClause.java - Copyright © 2010 David Roden
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

package net.pterodactylus.util.database;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * Interface for a WHERE clause that can be used to specify criteria for
 * matching {@link DataObject}s.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public interface WhereClause {

	/**
	 * Returns all parameters of this WHERE clause, in the same order as
	 * placeholders are specified.
	 *
	 * @return The parameters of this WHERE clause
	 */
	public List<Parameter<?>> getParameters();

	/**
	 * Writes this WHERE clause to the given writer.
	 *
	 * @param writer
	 *            The writer to write to
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public void render(Writer writer) throws IOException;

}
