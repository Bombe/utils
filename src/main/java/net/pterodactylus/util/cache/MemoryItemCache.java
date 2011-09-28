/*
 * utils - MemoryItemCache.java - Copyright © 2010 David Roden
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
 * Basic implementation of an {@link ItemCache} that stores the retrieved item
 * in memory.
 *
 * @param <T>
 *            The type of the cached item
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class MemoryItemCache<T> extends AbstractItemCache<T> {

	/** Object used for synchronization. */
	private final Object syncObject = new Object();

	/** Whether an object has been retrieved. */
	private boolean set;

	/** The cached value. */
	private T cachedValue;

	/**
	 * Creates a new memory item cache.
	 *
	 * @param itemValueRetriever
	 *            The item value retriever
	 */
	public MemoryItemCache(ItemValueRetriever<T> itemValueRetriever) {
		super(itemValueRetriever);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T get() throws CacheException {
		synchronized (syncObject) {
			if (!set) {
				cachedValue = retrieveValue();
				set = true;
			}
			return cachedValue;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear() {
		synchronized (syncObject) {
			cachedValue = null;
			set = false;
		}
	}

}
