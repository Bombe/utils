/*
 * utils - FilteredIterable.java - Copyright © 2011–2016 David Roden
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

/**
 * Wrapper around an {@link Iterable} that applies a {@link Filter} on the
 * iterator returned by {@link #iterator()}.
 *
 * @param <T>
 *            The type of the items to iterate over
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class FilteredIterable<T> implements Iterable<T> {

	/** The iterable to wrap. */
	private final Iterable<T> iterable;

	/** The filter to apply. */
	private final Filter<? super T> filter;

	/**
	 * Creates a new iterable filter.
	 *
	 * @param iterable
	 *            The iterable to wrap
	 * @param filter
	 *            The filter to apply
	 */
	public FilteredIterable(Iterable<T> iterable, Filter<? super T> filter) {
		this.iterable = iterable;
		this.filter = filter;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<T> iterator() {
		return new FilteredIterator<T>(iterable.iterator(), filter);
	}

}
