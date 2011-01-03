/*
 * utils - WritableCache.java - Copyright © 2011 David Roden
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

package net.pterodactylus.util.cache;

/**
 * Interface for caches that can not only retrieve values from a
 * {@link ValueRetriever} but can also store arbitrary key-value pairs.
 *
 * @param <K>
 *            The type of the keys
 * @param <V>
 *            The type of the values
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public interface WritableCache<K, V> extends Cache<K, V> {

	/**
	 * Stores the given key-value pair in this cache without updating any
	 * backends.
	 *
	 * @param key
	 *            The key of the pair
	 * @param value
	 *            The value of the pair
	 */
	public void put(K key, V value);

}
