/*
 * utils - DataObjectFactory.java - Copyright © 2010 David Roden
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


/**
 * The data object factory knows details about the specifics of how a
 * {@link DataObject} is persisted to the database.
 *
 * @param <D>
 *            The type of the data object to create
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public interface DataObjectFactory<D extends DataObject<D>> extends ObjectCreator<D> {

	/**
	 * Returns the table the data object is stored in.
	 *
	 * @return The name of the table
	 */
	public String getTable();

	/**
	 * Returns the name of the column that holds the identity.
	 *
	 * @return The name of the identity column
	 */
	public String getIdentityColumn();

}
