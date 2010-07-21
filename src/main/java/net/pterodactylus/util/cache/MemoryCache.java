/*
 * utils - MemoryCache.java - Copyright © 2009 David Roden
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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.pterodactylus.util.logging.Logging;

/**
 * Memory-based {@link Cache} implementation.
 *
 * @param <K>
 *            The type of the key
 * @param <V>
 *            The type of the value
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class MemoryCache<K, V> extends AbstractCache<K, V> {

	/** The logger. */
	private static Logger logger = Logging.getLogger(MemoryCache.class.getName());

	/** The number of values to cache. */
	private volatile int cacheSize;

	/** The cache for the values. */
	private final Map<K, CacheItem<V>> cachedValues = new LinkedHashMap<K, CacheItem<V>>() {

		/**
		 * @see java.util.LinkedHashMap#removeEldestEntry(java.util.Map.Entry)
		 */
		@Override
		@SuppressWarnings("synthetic-access")
		protected boolean removeEldestEntry(Map.Entry<K, CacheItem<V>> eldest) {
			if (super.size() > cacheSize) {
				eldest.getValue().remove();
				return true;
			}
			return false;
		}
	};

	/** The lock for cache accesses. */
	private final ReadWriteLock cacheLock = new ReentrantReadWriteLock();

	/**
	 * Creates a new memory-based cache.
	 *
	 * @param valueRetriever
	 *            The value retriever
	 */
	public MemoryCache(ValueRetriever<K, V> valueRetriever) {
		this(valueRetriever, 50);
	}

	/**
	 * Creates a new memory-based cache.
	 *
	 * @param valueRetriever
	 *            The value retriever
	 * @param cacheSize
	 *            The number of values to cache
	 */
	public MemoryCache(ValueRetriever<K, V> valueRetriever, int cacheSize) {
		super(valueRetriever);
		this.cacheSize = cacheSize;
	}

	/**
	 * Sets the logger to use.
	 *
	 * @param logger
	 *            The logger to use
	 */
	public static void setLogger(Logger logger) {
		MemoryCache.logger = logger;
	}

	/**
	 * @see net.pterodactylus.util.cache.Cache#clear()
	 */
	@Override
	public void clear() {
		cacheLock.writeLock().lock();
		try {
			cachedValues.clear();
		} finally {
			cacheLock.writeLock().unlock();
		}
	}

	/**
	 * @see net.pterodactylus.util.cache.Cache#contains(java.lang.Object)
	 */
	@Override
	public boolean contains(K key) {
		cacheLock.readLock().lock();
		try {
			return cachedValues.containsKey(key);
		} finally {
			cacheLock.readLock().unlock();
		}
	}

	/**
	 * @see net.pterodactylus.util.cache.Cache#get(java.lang.Object)
	 */
	@Override
	public V get(K key) throws CacheException {
		cacheLock.readLock().lock();
		try {
			if (cachedValues.containsKey(key)) {
				logger.log(Level.FINE, "Value for Key “%1$s” is in cache.", key);
				return cachedValues.get(key).getItem();
			}
			logger.log(Level.INFO, "Retrieving Value for Key “%1$s”...", key);
			CacheItem<V> value = retrieveValue(key);
			if (value != null) {
				cacheLock.readLock().unlock();
				cacheLock.writeLock().lock();
				try {
					cachedValues.put(key, value);
				} finally {
					cacheLock.readLock().lock();
					cacheLock.writeLock().unlock();
				}
			}
			return (value != null) ? value.getItem() : null;
		} finally {
			cacheLock.readLock().unlock();
			logger.log(Level.FINE, "Retrieved Value for Key “%1$s”.", key);
		}
	}

	/**
	 * @see net.pterodactylus.util.cache.Cache#size()
	 */
	@Override
	public int size() {
		cacheLock.readLock().lock();
		try {
			return cachedValues.size();
		} finally {
			cacheLock.readLock().unlock();
		}
	}

}
