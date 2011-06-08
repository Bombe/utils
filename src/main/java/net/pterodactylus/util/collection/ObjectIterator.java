/*
 * utils - ObjectIterator.java - Copyright © 2009 David Roden
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
import java.util.NoSuchElementException;

/**
 * {@link Iterator} implementation that iterates over a single object.
 *
 * @param <T>
 *            The type of the object
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ObjectIterator<T> implements Iterator<T> {

	/** The value to iterate over. */
	private final T value;

	/** Whether the value was already iterated over. */
	private boolean retrieved;

	/**
	 * Creates a new object iterator.
	 *
	 * @param value
	 *            The value to iterate over
	 */
	public ObjectIterator(T value) {
		this.value = value;
	}

	//
	// ITERATOR METHODS
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasNext() {
		return !retrieved;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T next() {
		if (retrieved) {
			throw new NoSuchElementException();
		}
		retrieved = true;
		return value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove() {
		/* ignore. */
	}

}
