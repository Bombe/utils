/*
 * utils - DefaultCacheItem.java - Copyright © 2009 David Roden
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
 * Default implementation of a {@link CacheItem} that simply stores a value and
 * does nothing when {@link CacheItem#remove()} is called.
 *
 * @param <V>
 *            The type of the item to store
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class DefaultCacheItem<V> implements CacheItem<V> {

	/** The item to store. */
	private final V item;

	/**
	 * Creates a new cache item.
	 *
	 * @param item
	 *            The item to store
	 */
	public DefaultCacheItem(V item) {
		this.item = item;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public V getItem() {
		return item;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove() {
		/* does nothing. */
	}

}
