/*
 * utils - Iterators.java - Copyright © 2012–2016 David Roden
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

import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Helper methods to turn arbitary collections of elements into {@link Iterator}s.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Iterators {

	/**
	 * Returns an {@link Iterator} over the given elements. The
	 * {@link Iterator#remove()} method is not implemented.
	 *
	 * @param elements
	 *            The elements to wrap
	 * @return An iterator over the given elements
	 */
	public static <T> Iterator<T> fromArray(final T[] elements) {
		return new Iterator<T>() {

			private int currentIndex = 0;

			@Override
			public boolean hasNext() {
				return currentIndex < elements.length;
			}

			@Override
			public T next() {
				if (currentIndex < elements.length) {
					return elements[currentIndex++];
				}
				throw new NoSuchElementException();
			}

			@Override
			public void remove() {
				/* ignore. */
			}

		};
	}

	/**
	 * Returns an iterator wrapped around the given enumeration. The
	 * {@link Iterator#remove()} does not do anything.
	 *
	 * @param <T>
	 *            The type of the elements to enumerate
	 * @param enumeration
	 *            The enumeration to wrap
	 * @return The iterator wrapped around the given enumeration
	 */
	public static <T> Iterator<T> fromEnumeration(final Enumeration<T> enumeration) {
		return new Iterator<T>() {

			@Override
			public boolean hasNext() {
				return enumeration.hasMoreElements();
			}

			@Override
			public T next() {
				return enumeration.nextElement();
			}

			@Override
			public void remove() {
				/* ignore. */
			}
		};
	}

}
