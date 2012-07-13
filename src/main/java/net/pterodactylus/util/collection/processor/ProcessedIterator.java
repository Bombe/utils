/*
 * utils - ProcessedIterator.java - Copyright © 2011–2012 David Roden
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
package net.pterodactylus.util.collection.processor;

import java.util.Iterator;

/**
 * {@link Iterator} implementation that applies a {@link Processor} to each
 * element before returning from the call to {@link #next()}.
 *
 * @param <T>
 *            The type of the iterator’s elements
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ProcessedIterator<T> implements Iterator<T> {

	/** The iterator. */
	private final Iterator<T> iterator;

	/** The processor. */
	private final Processor<? super T> processor;

	/**
	 * Creates a new processed iterator.
	 *
	 * @param iterator
	 *            The iterator to wrap
	 * @param processor
	 *            The processor to apply to all elements
	 */
	public ProcessedIterator(Iterator<T> iterator, Processor<? super T> processor) {
		this.iterator = iterator;
		this.processor = processor;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T next() {
		T next = iterator.next();
		processor.process(next);
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
