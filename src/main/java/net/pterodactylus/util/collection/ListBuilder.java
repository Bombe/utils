/*
 * utils - ListBuilder.java - Copyright © 2011–2019 David Roden
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Convenience class to help construct {@link List}s in a single command without
 * extending an existing {@link List} class.
 *
 * @param <T>
 *            The type of the list’s elements
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ListBuilder<T> {

	/** The list to populate. */
	private final List<T> list;

	/**
	 * Creates a new list builder that will populate an {@link ArrayList} and
	 * return it on {@link #get()}.
	 */
	public ListBuilder() {
		this(new ArrayList<T>());
	}

	/**
	 * Creates a new list builder that will populate the given list and return
	 * it on {@link #get()}.
	 *
	 * @param list
	 *            The list to populate
	 */
	public ListBuilder(List<T> list) {
		this.list = list;
	}

	/**
	 * Returns the populated list.
	 *
	 * @return The populated list
	 */
	public List<T> get() {
		return list;
	}

	/**
	 * Adds the given element to the list.
	 *
	 * @param element
	 *            The element to add
	 * @return This list builder
	 */
	public ListBuilder<T> add(T element) {
		list.add(element);
		return this;
	}

	/**
	 * Adds all elements from the given collection to the list.
	 *
	 * @param elements
	 *            The elements to add
	 * @return This list builder
	 */
	public ListBuilder<T> addAll(Collection<? extends T> elements) {
		list.addAll(elements);
		return this;
	}

	/**
	 * Sorts the current list contents. This method requires that all elements
	 * in this list implement the {@link Comparable} interface!
	 *
	 * @return This list builder
	 * @throws ClassCastException
	 *             if the list contains elements that are not <i>mutually
	 *             comparable</i> (for example, strings and integers).
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ListBuilder<T> sort() throws ClassCastException {
		Collections.sort((List) list);
		return this;
	}

	/**
	 * Sorts the current list contents using the given comparator.
	 *
	 * @param comparator
	 *            The comparator to use
	 * @return This list builder
	 */
	public ListBuilder<T> sort(Comparator<? super T> comparator) {
		Collections.sort(list, comparator);
		return this;
	}

}
