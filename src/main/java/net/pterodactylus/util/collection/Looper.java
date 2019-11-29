/*
 * utils - Looper.java - Copyright © 2011–2019 David Roden
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

/**
 * A looper wraps around an {@link Iterable} and adds some valuable properties
 * to the elements being looped over. The elements returned by this wrapper are
 * instances of {@link Looped} and contains properties such as “first” or “last”
 * so you don’t have to keep track of those yourself.
 *
 * @param <T>
 *            The type of the elements being iterated over
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Looper<T> implements Iterable<Looper.Looped<T>> {

	/** The original iterable. */
	private Iterable<T> originalIterable;

	/**
	 * Creates a new looper.
	 *
	 * @param originalIterable
	 *            The original iterable to wrap
	 */
	public Looper(Iterable<T> originalIterable) {
		this.originalIterable = originalIterable;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<Looped<T>> iterator() {
		return new LooperIterator<T>(originalIterable.iterator());
	}

	/**
	 * Wrapper around an element of the original iterable.
	 *
	 * @param <T>
	 *            The type of the wrapped element
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	public static class Looped<T> {

		/** The original element. */
		private final T originalElement;

		/** Whether this is the first element. */
		private final boolean first;

		/** Whether this is the last element. */
		private final boolean last;

		/** The index of this element ({@code 0}-based). */
		private final int index;

		/**
		 * Creates a new wrapper around the given element.
		 *
		 * @param originalElement
		 *            The original element
		 * @param first
		 *            {@code true} if this element is the first element, {@code
		 *            false} otherwise
		 * @param last
		 *            {@code true} if this element is the last element, {@code
		 *            false} otherwise
		 * @param index
		 *            The {@code 0}-based index of the element
		 */
		public Looped(T originalElement, boolean first, boolean last, int index) {
			this.originalElement = originalElement;
			this.first = first;
			this.last = last;
			this.index = index;
		}

		//
		// ACCESSORS
		//

		/**
		 * Returns the wrapped element.
		 *
		 * @return The wrapped element
		 */
		public T get() {
			return originalElement;
		}

		/**
		 * Returns whether the wrapped element is the first element of the
		 * iterator.
		 *
		 * @return {@code true} if the wrapped element is the first element of
		 *         the iterator, {@code false} otherwise
		 */
		public boolean first() {
			return first;
		}

		/**
		 * Returns whether the wrapped element is the last element of the
		 * iterator.
		 *
		 * @return {@code true} if the wrapped element is the last element of
		 *         the iterator, {@code false} otherwise
		 */
		public boolean last() {
			return last;
		}

		/**
		 * Returns the index of the wrapped element.
		 *
		 * @return The {@code 0}-based index of the wrapped element
		 */
		public int index() {
			return index;
		}

	}

	/**
	 * Wrapper around the {@link Iterator} returned by the original
	 * {@link Iterable} which wraps all returned elements into
	 * {@link Looper.Looped}s.
	 *
	 * @param <T>
	 *            The type of the elements being wrapped
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	public static class LooperIterator<T> implements Iterator<Looped<T>> {

		/** The original iterator. */
		private final Iterator<T> originalIterator;

		/** The index of the next returned element. */
		private int index;

		/**
		 * Creates a new wrapper around the given iterator.
		 *
		 * @param originalIterator
		 *            The original iterator
		 */
		public LooperIterator(Iterator<T> originalIterator) {
			this.originalIterator = originalIterator;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean hasNext() {
			return originalIterator.hasNext();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Looped<T> next() {
			T next = originalIterator.next();
			Looped<T> looped = new Looped<T>(next, index == 0, !hasNext(), index);
			++index;
			return looped;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void remove() {
			originalIterator.remove();
		}

	}

}
