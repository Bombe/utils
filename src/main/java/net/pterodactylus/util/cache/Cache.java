/*
 * utils - Cache.java - Copyright © 2009 David Roden
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

import java.util.WeakHashMap;

/**
 * Interface for caches with different strategies.
 *
 * @param <K>
 *            The type of the key
 * @param <V>
 *            The type of the value
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public interface Cache<K, V> {

	/**
	 * Checks whether this cache contains a value for the given key. No query to
	 * the underlying {@link ValueRetriever} will be made! Note that it is legal
	 * for this method to return {@code true} but for a following
	 * {@link #get(Object)} to return {@code null} (see {@link WeakHashMap}) for
	 * a possible explanation).
	 *
	 * @param key
	 *            The key to check for
	 * @return {@code true} if this cache contains a value for the given key,
	 *         {@code false} otherwise
	 */
	public boolean contains(K key);

	/**
	 * Returns a value from the cache. If this cache does not contain a value
	 * for the given key, the underlying {@link ValueRetriever} is asked to
	 * retrieve the value. This operation may result in a {@link CacheException}
	 * to be thrown. The returned value is cached if it is non-{@code null}.
	 *
	 * @param key
	 *            The key to get the value for
	 * @return The value of the key, or {@code null} if there is no value for
	 *         the key
	 * @throws CacheException
	 *             if an error occurs retrieving the value from the underlying
	 *             {@link ValueRetriever}
	 */
	public V get(K key) throws CacheException;

	/**
	 * Removes all cached values. For non-memory based caches this operation may
	 * be slow.
	 */
	public void clear();

	/**
	 * Returns the number of currently cached values. For non-memory based
	 * caches this operation may be slow.
	 *
	 * @return The number of cached values
	 */
	public int size();

}
