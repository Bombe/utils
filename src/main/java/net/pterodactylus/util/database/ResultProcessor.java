/*
 * utils - ResultProcessor.java - Copyright © 2006-2009 David Roden
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

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A result processor is similar to an {@link ObjectCreator} but it does not
 * return created objects but processes results in an arbitrary way.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public interface ResultProcessor {

	/**
	 * Processes the current row of the result set.
	 *
	 * @param resultSet
	 *            The result set to process
	 * @throws SQLException
	 *             if an SQL error occurs
	 */
	public void processResult(ResultSet resultSet) throws SQLException;

}
