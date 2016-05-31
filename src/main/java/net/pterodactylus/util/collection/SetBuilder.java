/*
 * utils - SetBuilder.java - Copyright © 2011–2016 David Roden
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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Convenience class to help construct {@link Set}s in a single command without
 * extending an existing {@link Set} class.
 *
 * @param <T>
 *            The type of the set’s elements
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class SetBuilder<T> {

	/** The set to populate. */
	private final Set<T> set;

	/**
	 * Creates a new set builder that will populate a {@link HashSet} and return
	 * it on {@link #get()}.
	 */
	public SetBuilder() {
		this(new HashSet<T>());
	}

	/**
	 * Creates a new set builder that will populate the given set and return it
	 * on {@link #get()}.
	 *
	 * @param set
	 *            The set to populate
	 */
	public SetBuilder(Set<T> set) {
		this.set = set;
	}

	/**
	 * Returns the populated set.
	 *
	 * @return The populated set
	 */
	public Set<T> get() {
		return set;
	}

	/**
	 * Adds the given element to the set.
	 *
	 * @param element
	 *            The element to add
	 * @return This set builder
	 */
	public SetBuilder<T> add(T element) {
		set.add(element);
		return this;
	}

	/**
	 * Adds all elements from the given collection to the set.
	 *
	 * @param elements
	 *            The elements to add
	 * @return This set builder
	 */
	public SetBuilder<T> addAll(Collection<? extends T> elements) {
		set.addAll(elements);
		return this;
	}

}
