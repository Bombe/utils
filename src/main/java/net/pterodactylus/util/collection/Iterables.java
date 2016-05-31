/*
 * utils - Iterables.java - Copyright © 2012–2016 David Roden
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

package net.pterodactylus.util.collection;

import java.util.Iterator;

/**
 * Helper methods to turn arbitary collections of elements into {@link Iterable}s.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Iterables {

	/**
	 * Returns an {@link Iterable} that returns an
	 * {@link Iterators#fromArray(Object[]) Iterator} over the given elements.
	 * The {@link Iterator#remove()} method is not implemented.
	 *
	 * @param elements
	 *            The elements to wrap
	 * @return An iterable over the given elements
	 */
	public static <T> Iterable<T> fromArray(final T[] elements) {
		return new Iterable<T>() {

			@Override
			public Iterator<T> iterator() {
				return Iterators.fromArray(elements);
			}

		};
	}

}
