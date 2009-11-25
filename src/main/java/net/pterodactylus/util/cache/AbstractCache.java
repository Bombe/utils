/*
 * utils - AbstractCache.java - Copyright © 2009 David Roden
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
 * Abstract base implementation of a {@link Cache}. All implementations should
 * extend this base class.
 *
 * @param <K>
 *            The type of the key
 * @param <V>
 *            The value of the key
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public abstract class AbstractCache<K, V> implements Cache<K, V> {

	/** The value retriever. */
	private final ValueRetriever<K, V> valueRetriever;

	/**
	 * Creates a new abstract cache.
	 *
	 * @param valueRetriever
	 *            The value retriever
	 */
	protected AbstractCache(ValueRetriever<K, V> valueRetriever) {
		this.valueRetriever = valueRetriever;
	}

	/**
	 * Retrieves a value from the value retriever.
	 *
	 * @param key
	 *            The key of the value to retrieve
	 * @return The value of the key, or {@code null} if there is no value
	 * @throws CacheException
	 *             if an error occurs retrieving the value
	 */
	protected CacheItem<V> retrieveValue(K key) throws CacheException {
		return valueRetriever.retrieve(key);
	}

}
