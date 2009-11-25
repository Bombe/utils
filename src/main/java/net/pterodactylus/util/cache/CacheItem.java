/*
 * utils - CacheItem.java - Copyright © 2009 David Roden
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
 * Wrapper interface for cached items.
 *
 * @param <V>
 *            The type of the value
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public interface CacheItem<V> {

	/**
	 * Returns the wrapped item.
	 *
	 * @return The wrapped item
	 */
	public V getItem();

	/**
	 * Notifies the item that it is removed from the cache and can free any
	 * resources it uses.
	 */
	public void remove();

}
