/*
 * utils - IterableWrapper.java - Copyright © 2011–2012 David Roden
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
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import net.pterodactylus.util.collection.filter.Filter;
import net.pterodactylus.util.collection.filter.FilteredIterable;
import net.pterodactylus.util.collection.mapper.MappedIterable;
import net.pterodactylus.util.collection.mapper.Mapper;
import net.pterodactylus.util.collection.processor.ProcessedIterable;
import net.pterodactylus.util.collection.processor.Processor;

/**
 * Wrapper around an {@link Iterable} that can apply {@link Filter}s and
 * {@link Mapper}s to the returned {@link Iterator}s.
 *
 * @see FilteredIterable
 * @see MappedIterable
 * @param <T>
 *            The type of the iterable’s elements
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class IterableWrapper<T> implements Iterable<T> {

	/** The iterable to wrap. */
	private final Iterable<T> iterable;

	/**
	 * Creates a new iterable wrapper.
	 *
	 * @param iterable
	 *            The iterable to wrap
	 */
	public IterableWrapper(Iterable<T> iterable) {
		this.iterable = iterable;
	}

	//
	// ACTIONS
	//

	/**
	 * Creates a new wrapper around the iterable that will have its elements
	 * filtered by the given filter. The returned iterable wrapper can be used
	 * to create another iterable wrapper (using either this method,
	 * {@link #map(Mapper)}, or {@link #process(Processor)}) which performs more
	 * than one operation on its iterators.
	 *
	 * @param filter
	 *            The filter to apply
	 * @return A new iterable wrapper that uses the given filter
	 */
	public IterableWrapper<T> filter(Filter<? super T> filter) {
		return new IterableWrapper<T>(new FilteredIterable<T>(iterable, filter));
	}

	/**
	 * Creates a new wrapper around the iterable that will have its elements
	 * mapped by the given mapper. The returned iterable wrapper can be used to
	 * create another iterable wrapper (using either this method,
	 * {@link #filter(Filter)}, or {@link #process(Processor)}) which performs
	 * more than one operation on its iterators.
	 *
	 * @param <O>
	 *            The type of the mapped elements
	 * @param mapper
	 *            The mapper to apply
	 * @return A new iterable wrapper that uses the given filter
	 */
	public <O> IterableWrapper<O> map(Mapper<? super T, ? extends O> mapper) {
		return new IterableWrapper<O>(new MappedIterable<T, O>(iterable, mapper));
	}

	/**
	 * Creates a new wrapper around the iterable that will have its elements
	 * processed by the given processor. The returned iterable wrapper can be
	 * used to create another iterable wrapper (using either this method,
	 * {@link #filter(Filter)} or {@link #map(Mapper)}) which performs more than
	 * one operation on its iterators.
	 *
	 * @param processor
	 *            The processor to apply to all returned elements
	 * @return A new iterable wrapper that uses the given processor
	 */
	public IterableWrapper<T> process(Processor<? super T> processor) {
		return new IterableWrapper<T>(new ProcessedIterable<T>(iterable, processor));
	}

	/**
	 * Checks whether the wrapped iterable contains at least one element. This
	 * will create a new iterator each time it is called!
	 *
	 * @return {@code true} if the wrapped iterable produces iterators without
	 *         any elements in them
	 */
	public boolean isEmpty() {
		return !iterable.iterator().hasNext();
	}

	/**
	 * Returns the first element of an iterator created by the wrapped iterable.
	 * This will create a new iterator each time it is called!
	 *
	 * @return The first element returned by an iterator created by the iterable
	 * @throws NoSuchElementException
	 *             if the wrapped iterable’s iterator does not produce any
	 *             objects
	 */
	public T get() throws NoSuchElementException {
		Iterator<T> iterator = iterable.iterator();
		if (iterator.hasNext()) {
			return iterator.next();
		}
		throw new NoSuchElementException();
	}

	/**
	 * Returns the first element of an iterator created by the wrapped iterable,
	 * or the given default value if the iterator does not contain any elements.
	 * This will create a new iterator each time it is called!
	 *
	 * @param defaultValue
	 *            The value to return if the wrapped iterable does not contain
	 *            any elements
	 * @return The first element of the wrapped iterable, or the given default
	 *         value if the wrapped iterable does not contain any elements
	 */
	public T get(T defaultValue) {
		Iterator<T> iterator = iterable.iterator();
		return iterator.hasNext() ? iterator.next() : defaultValue;
	}

	/**
	 * Returns all elements contained in the wrapped iterable as a new
	 * {@link Collection}. The returned collection is a snapshot, i.e.
	 * modifications to the underlying {@link Iterable}s of this wrapper will
	 * not modify the returned collection.
	 *
	 * @return The elements contained in the wrapped iterable.
	 */
	public Collection<T> collection() {
		Collection<T> collection = new ArrayList<T>();
		for (T object : iterable) {
			collection.add(object);
		}
		return collection;
	}

	/**
	 * Returns all elements contained in the wrapped iterable as a new
	 * {@link List}. The returned collection is a snapshot, i.e. modifications
	 * to the underlying {@link Iterable}s of this wrapper will not modify the
	 * returned list.
	 *
	 * @return The elements contained in the wrapped iterable.
	 */
	public List<T> list() {
		return (List<T>) collection();
	}

	//
	// STATIC METHODS
	//

	/**
	 * Creates a new iterable wrapper that returns iterators that do not return
	 * any elements.
	 *
	 * @param <T>
	 *            The type of the elements to (not) return
	 * @return The iterable wrapper
	 */
	public static <T> IterableWrapper<T> emptyIterable() {
		return new IterableWrapper<T>(new Iterable<T>() {

			@Override
			public Iterator<T> iterator() {
				return new Iterator<T>() {

					@Override
					public boolean hasNext() {
						return false;
					}

					@Override
					public T next() {
						throw new NoSuchElementException();
					}

					@Override
					public void remove() {
						/* do nothing. */
					}

				};
			}
		});
	}

	/**
	 * Convenience method that creates a new {@link IterableWrapper} around the
	 * given iterable.
	 *
	 * @param iterable
	 *            The iterable to wrap
	 * @return The wrapped iterable
	 */
	public static <T> IterableWrapper<T> wrap(Iterable<T> iterable) {
		return new IterableWrapper<T>(iterable);
	}

	//
	// INTERFACE Iterable
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<T> iterator() {
		return iterable.iterator();
	}

}
