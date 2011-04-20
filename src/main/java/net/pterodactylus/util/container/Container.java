/*
 * utils - Container.java - Copyright © 2011 David Roden
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

package net.pterodactylus.util.container;

import java.util.Iterator;

/**
 * A container stores an arbitrary amounts of elements, allowing easy filtering,
 * mapping, and processing of the elements.
 *
 * A container is immutable. Once a container is created, the stored elements
 * can not be changed. All methods that seem to modify the container in fact
 * return a new container containing different elements; if an operation does
 * not have to change a container (such as removing an element from an already
 * empty container), all methods are free to return the container they are
 * operating on (i.e. {@code this}).
 *
 * @param <T>
 *            The type of the elements to store
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Container<T> implements Iterable<T> {

	/** The elements to store. */
	private final Object[] elements;

	/**
	 * Creates an empty container.
	 */
	public Container() {
		this.elements = new Object[0];
	}

	/**
	 * Creates a container that stores the given element.
	 *
	 * @param element
	 *            The element to store
	 */
	public Container(T element) {
		this.elements = new Object[] { element };
	}

	//
	// ACCESSORS
	//

	/**
	 * Returns the number of elements stored in this container.
	 *
	 * @return The number of elements stored in this container
	 */
	public int size() {
		return elements.length;
	}

	//
	// INTERFACE Iterable
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {

			private int index = 0;

			@Override
			@SuppressWarnings("synthetic-access")
			public boolean hasNext() {
				return index < elements.length;
			}

			@Override
			@SuppressWarnings({ "unchecked", "synthetic-access" })
			public T next() {
				return (T) elements[index++];
			}

			@Override
			public void remove() {
				/* ignore. */
			}

		};
	}

}
