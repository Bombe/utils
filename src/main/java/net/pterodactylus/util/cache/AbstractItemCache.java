/*
 * utils - AbstractItemCache.java - Copyright © 2010 David Roden
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
 * Base implementation of an {@link ItemCache}. This base implementation takes
 * care of storing an {@link ItemValueRetriever} that can be used in subclasses
 * to retrieve the item to cache.
 *
 * @param <T>
 *            The type of the item
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public abstract class AbstractItemCache<T> implements ItemCache<T> {

	/** The item value retriever. */
	private final ItemValueRetriever<T> itemValueRetriever;

	/**
	 * Creates a new abstract item cache.
	 *
	 * @param itemValueRetriever
	 *            The item value retriever
	 */
	protected AbstractItemCache(ItemValueRetriever<T> itemValueRetriever) {
		this.itemValueRetriever = itemValueRetriever;
	}

	/**
	 * Retrieves the value of the item to cache.
	 *
	 * @return The value of the item to cache
	 * @throws CacheException
	 *             if the value can not be retrieved
	 */
	protected T retrieveValue() throws CacheException {
		return itemValueRetriever.retrieve();
	}

}
