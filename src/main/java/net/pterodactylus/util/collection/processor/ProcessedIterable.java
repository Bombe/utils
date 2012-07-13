/*
 * utils - ProcessedIterable.java - Copyright © 2011–2012 David Roden
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
 * {@link Iterable} implementation that applies a {@link Processor} to all
 * elements of all iterators it {@link #iterator() creates}.
 *
 * @param <T>
 *            The type of the elements
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ProcessedIterable<T> implements Iterable<T> {

	/** The iterable. */
	private final Iterable<T> iterable;

	/** The processor. */
	private final Processor<? super T> processor;

	/**
	 * Creates a new processed iterable.
	 *
	 * @param iterable
	 *            The iterable to wrap
	 * @param processor
	 *            The processor to apply to all elements of all created
	 *            iterators
	 */
	public ProcessedIterable(Iterable<T> iterable, Processor<? super T> processor) {
		this.iterable = iterable;
		this.processor = processor;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<T> iterator() {
		return new ProcessedIterator<T>(iterable.iterator(), processor);
	}

}
