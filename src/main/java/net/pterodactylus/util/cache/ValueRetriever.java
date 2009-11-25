/*
 * utils - ValueRetriever.java - Copyright © 2009 David Roden
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
 * Interface for objects that can fill a {@link Cache} from arbitrary sources.
 *
 * @param <K>
 *            The type of the key
 * @param <V>
 *            The type of the value
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public interface ValueRetriever<K, V> {

	/**
	 * Retrieves the value for the given key.
	 *
	 * @param key
	 *            The key to retrieve the value for
	 * @return The value of the key, or {@code null} if there is no value
	 * @throws CacheException
	 *             if an error occurs retrieving the value
	 */
	public CacheItem<V> retrieve(K key) throws CacheException;

}
