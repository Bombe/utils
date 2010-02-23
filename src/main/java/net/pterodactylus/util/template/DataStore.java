/*
 * utils - DataStore.java - Copyright © 2010 David Roden
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

package net.pterodactylus.util.template;

import java.util.HashMap;
import java.util.Map;

/**
 * Interface for {@link DataProvider} backends.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public interface DataStore {

	/**
	 * Returns the object with the given key.
	 *
	 * @param name
	 *            The key of the data
	 * @return The data, or {@code null} if there is no data under the given key
	 */
	public Object get(String name);

	/**
	 * Stores an object under the given key.
	 *
	 * @param name
	 *            The key under which to store the object
	 * @param data
	 *            The object to store
	 */
	public void set(String name, Object data);

	/**
	 * Default {@link Map}-based implementation of a {@link DataStore}.
	 *
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	public class MapDataStore implements DataStore {

		/** The backing store. */
		private final Map<String, Object> objectStore = new HashMap<String, Object>();

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Object get(String name) {
			return objectStore.get(name);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void set(String name, Object data) {
			objectStore.put(name, data);
		}

	}

}
