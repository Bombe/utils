/*
 * utils - FilteredIterator.java - Copyright © 2011–2012 David Roden
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */
package net.pterodactylus.util.collection.filter;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Wrapper around an {@link Iterator} that applies a {@link Filter} on the
 * wrapper iterator, suppressing items that do not match the filter. This may
 * result in an iterator that does not yield any items!
 *
 * @param <T>
 *            The type of the items in the iterator
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class FilteredIterator<T> implements Iterator<T> {

	/** The iterator to wrap. */
	private final Iterator<T> iterator;

	/** The filter to apply. */
	private final Filter<? super T> filter;

	/** Whether we already checked for the next item. */
	private boolean checked;

	/** Whether we have a next item. */
	private boolean hasNext;

	/** The next item. */
	private T next;

	/**
	 * Creates a new filtered iterator.
	 *
	 * @param iterator
	 *            The iterator to wrap
	 * @param filter
	 *            The filter to apply
	 */
	public FilteredIterator(Iterator<T> iterator, Filter<? super T> filter) {
		this.iterator = iterator;
		this.filter = filter;
	}

	//
	// PRIVATE METHODS
	//

	/**
	 * Checks if we have a next item in the original iterator that is accepted
	 * by the filter. This method will set {@link #checked} to {@code true}
	 * after a next item has been found (or not), and it will return without
	 * checking if {@link #checked} was {@code true} before this method was
	 * called. {@link #checked} is reset to {@code false} on the next call to
	 * {@link #next()}.
	 */
	private void getNext() {
		if (checked) {
			return;
		}
		hasNext = false;
		while (iterator.hasNext()) {
			next = iterator.next();
			if (filter.filterObject(next)) {
				hasNext = true;
				break;
			}
		}
		checked = true;
	}

	//
	// INTERFACE Iterator
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasNext() {
		getNext();
		return hasNext;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T next() {
		getNext();
		checked = false;
		if (!hasNext) {
			throw new NoSuchElementException();
		}
		return next;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove() {
		iterator.remove();
	}

}
