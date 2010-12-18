/*
 * utils - TimedMemoryItemCache.java - Copyright © 2010 David Roden
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

import net.pterodactylus.util.thread.Ticker;

/**
 * A timed memory item cache is an {@link ItemCache} implementation that stores
 * the item only for a certain time.
 *
 * @param <T>
 *            The type of the cached item
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class TimedMemoryItemCache<T> extends MemoryItemCache<T> {

	/** The maximum time for the cached item to stay in memory. */
	private final long maxCacheTime;

	/** The ticker object. */
	private Object tickerObject;

	/**
	 * Creates a new timed memory item cache.
	 *
	 * @param itemValueRetriever
	 *            The item value retriever
	 * @param maxCacheTime
	 *            The maximum time for the cached item to stay in memory (in
	 *            milliseconds)
	 */
	public TimedMemoryItemCache(ItemValueRetriever<T> itemValueRetriever, long maxCacheTime) {
		super(itemValueRetriever);
		this.maxCacheTime = maxCacheTime;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T get() throws CacheException {
		T value = super.get();
		if (value != null) {
			if (tickerObject == null) {
				tickerObject = Ticker.getInstance().registerEvent(System.currentTimeMillis() + maxCacheTime, new Runnable() {

					@Override
					public void run() {
						clear();
					}
				}, "Memory Item Cache Cleaner");
			} else {
				Ticker.getInstance().changeExecutionTime(tickerObject, System.currentTimeMillis() + maxCacheTime);
			}
		}
		return value;
	}

}
