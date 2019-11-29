/*
 * utils - MapBuilder.java - Copyright © 2011–2019 David Roden
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

package net.pterodactylus.util.collection;

import java.util.HashMap;
import java.util.Map;

/**
 * Convenience class to help build maps in a single command without extending
 * the used {@link Map} class.
 *
 * @param <K>
 *            The type of the key
 * @param <V>
 *            The type of the value
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class MapBuilder<K, V> {

	/** The map being populated. */
	private final Map<K, V> map;

	/**
	 * Creates a new map builder that returns a new {@link HashMap} on
	 * {@link #get()}.
	 */
	public MapBuilder() {
		this(new HashMap<K, V>());
	}

	/**
	 * Creates a new map builder that returns the given map on {@link #get()}.
	 *
	 * @param map
	 *            The map to populate
	 */
	public MapBuilder(Map<K, V> map) {
		this.map = map;
	}

	/**
	 * Returns the populated map.
	 *
	 * @return The populated map
	 */
	public Map<K, V> get() {
		return map;
	}

	/**
	 * Stores the given key value pair in the map.
	 *
	 * @param key
	 *            The key of the value
	 * @param value
	 *            The value to store
	 * @return This map builder
	 */
	public MapBuilder<K, V> put(K key, V value) {
		map.put(key, value);
		return this;
	}

	/**
	 * Stores all elements of the given map in the map being populated.
	 *
	 * @param map
	 *            The map to copy all elements from
	 * @return This map builder
	 */
	public MapBuilder<K, V> putAll(Map<? extends K, ? extends V> map) {
		this.map.putAll(map);
		return this;
	}

}
