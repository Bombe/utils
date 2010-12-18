/*
 * utils - ItemCache.java - Copyright © 2010 David Roden
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
 * Cache for a single item.
 *
 * @param <T>
 *            The type of the cached item
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public interface ItemCache<T> {

	/**
	 * Returns the cached item.
	 *
	 * @return The cached item
	 * @throws CacheException
	 *             if the item was not cached before and retrieving the item for
	 *             the first time failed
	 */
	public T get() throws CacheException;

	/**
	 * Clears the cache.
	 */
	public void clear();

}
