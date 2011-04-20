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

import net.pterodactylus.util.collection.Converter;
import net.pterodactylus.util.filter.Filter;
import net.pterodactylus.util.validation.Validation;

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

	/**
	 * Creates a new container and stores the given elements.
	 *
	 * @param elements
	 *            The elements to store
	 */
	public Container(T[] elements) {
		this.elements = new Object[elements.length];
		System.arraycopy(elements, 0, this.elements, 0, elements.length);
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

	/**
	 * Returns whether this container is empty.
	 *
	 * @return {@code true} if this container is empty, {@code false} if it
	 *         contains at least one element
	 */
	public boolean isEmpty() {
		return elements.length == 0;
	}

	/**
	 * Returns the first element of this container, or {@code null} if this
	 * container is empty.
	 *
	 * @return The first element of this container, or {@code null} if this
	 *         container is empty
	 */
	public T get() {
		return get(null);
	}

	/**
	 * Returns the first element of this container, or the default value if this
	 * container is empty.
	 *
	 * @param defaultValue
	 *            The default value to return if the container is empty
	 * @return The first element of this container, or the default value if this
	 *         container is empty
	 */
	@SuppressWarnings("unchecked")
	public T get(T defaultValue) {
		if (elements.length == 0) {
			return defaultValue;
		}
		return (T) elements[0];
	}

	/**
	 * Returns the index of the given element in this container.
	 *
	 * @param element
	 *            The element to locate in this container
	 * @return The index of the element, or {@code -1} if the given element can
	 *         not be located in this container
	 */
	public int indexOf(T element) {
		for (int index = 0; index < elements.length; ++index) {
			if (((element == null) && (elements[index] == null)) || ((element != null) && element.equals(elements[index]))) {
				return index;
			}
		}
		return -1;
	}

	//
	// ACTIONS
	//

	/**
	 * Creates and returns a new container that contains all elements of this
	 * container and the given element, which is appended at the end.
	 *
	 * @param element
	 *            The element to add
	 * @return The container with all elements
	 */
	public Container<T> add(T element) {
		return add(elements.length, element);
	}

	/**
	 * Creates and returns a new container that contains the elements of this
	 * container with the given element inserted at the given index.
	 *
	 * @param index
	 *            The index at which to insert the new element
	 * @param element
	 *            The element to insert
	 * @return A container with the new elements
	 */
	@SuppressWarnings("unchecked")
	public Container<T> add(int index, T element) {
		Validation.begin().isGreaterOrEqual("Index", index, 0).isLessOrEqual("Index", index, elements.length).check();
		Object[] newElements = new Object[elements.length + 1];
		System.arraycopy(elements, 0, newElements, 0, index);
		newElements[index] = element;
		System.arraycopy(elements, index, newElements, index + 1, elements.length - index);
		return new Container<T>((T[]) newElements);
	}

	/**
	 * Filters the elements of this container through the given filter. The
	 * returned container will only contain elements for which the given filter
	 * matched.
	 *
	 * @param filter
	 *            The filter to apply on the elements
	 * @return A container containing the filtered elements
	 */
	@SuppressWarnings("unchecked")
	public Container<T> filter(Filter<T> filter) {
		Object[] filteredElements = new Object[elements.length];
		int filteredElementCount = 0;
		for (Object object : elements) {
			if (filter.filterObject((T) object)) {
				filteredElements[filteredElementCount++] = object;
			}
		}
		if (filteredElementCount == elements.length) {
			return this;
		} else if (filteredElementCount == 0) {
			return new Container<T>();
		}
		Object[] finalElements = new Object[filteredElementCount];
		System.arraycopy(filteredElements, 0, finalElements, 0, filteredElementCount);
		return new Container<T>((T[]) finalElements);
	}

	/**
	 * Maps all elements in this container using the given mapper and returns a
	 * container containing the mapped elements.
	 *
	 * @param <O>
	 *            The type of the mapped elements
	 * @param mapper
	 *            The mapper to use
	 * @return A container with the mapped elements
	 */
	@SuppressWarnings("unchecked")
	public <O> Container<O> map(Converter<T, O> mapper) {
		if (isEmpty()) {
			return (Container<O>) this;
		}
		Object[] mappedElements = new Object[elements.length];
		int index = 0;
		for (Object object : elements) {
			mappedElements[index++] = mapper.convert((T) object);
		}
		return new Container<O>((O[]) mappedElements);
	}

	/**
	 * Processes all elements in this container.
	 *
	 * @param processor
	 *            The processor to apply to all elements
	 * @return This container
	 */
	@SuppressWarnings("unchecked")
	public Container<T> process(Processor<T> processor) {
		for (Object object : elements) {
			processor.process((T) object);
		}
		return this;
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
