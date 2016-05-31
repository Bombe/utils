/*
 * utils - IteratorWrapper.java - Copyright © 2011–2016 David Roden
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

import net.pterodactylus.util.collection.filter.Filter;
import net.pterodactylus.util.collection.filter.FilteredIterator;
import net.pterodactylus.util.collection.mapper.MappedIterator;
import net.pterodactylus.util.collection.mapper.Mapper;
import net.pterodactylus.util.collection.processor.ProcessedIterator;
import net.pterodactylus.util.collection.processor.Processor;

/**
 * Wrapper around an {@link Iterator} that allows easy filtering and mapping of
 * the returned elements. To achieve this, the original iterable is wrapped into
 * more and more layers of {@link FilteredIterator}s and {@link MappedIterator}s
 * so that the evaluation of additional layers is only ever performed when the
 * iterator is actually iterated over. The {@link #remove()} method is delegated
 * to the original iterator’s method.
 *
 * @param <T>
 *            The type of the wrapped elements
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class IteratorWrapper<T> implements Iterator<T> {

	/** The original iterator. */
	private final Iterator<T> iterator;

	/**
	 * Creates a new wrapper around the given iterator.
	 *
	 * @param iterator
	 *            The iterator to wrap
	 */
	public IteratorWrapper(Iterator<T> iterator) {
		this.iterator = iterator;
	}

	//
	// ACTIONS
	//

	/**
	 * Creates a new iterator wrapper that will apply the given filter to all
	 * values returned by this wrapper’s iterator. Depending on the filter, the
	 * number of elements returned by the returned wrapper can be smaller than
	 * the number of elements contained in the original iterator.
	 *
	 * @param filter
	 *            The filter to apply on this iterator
	 * @return A new wrapper around this iterator using the given filter
	 */
	public IteratorWrapper<T> filter(Filter<? super T> filter) {
		return new IteratorWrapper<T>(new FilteredIterator<T>(iterator, filter));
	}

	/**
	 * Creates a new iterator wrapper that will apply the given mapper to all
	 * values returned by this wrapper’s iterator.
	 *
	 * @param <O>
	 *            The type of the mapped elements
	 * @param mapper
	 *            The mapper to apply on this iterator
	 * @return A new wrapper around this iterator using the given mapper
	 */
	public <O> IteratorWrapper<O> map(Mapper<? super T, ? extends O> mapper) {
		return new IteratorWrapper<O>(new MappedIterator<T, O>(iterator, mapper));
	}

	/**
	 * Creates a new iterator wrapper that will process all values returned by
	 * this wrapper’s iterator with the given processor.
	 *
	 * @param processor
	 *            The processor to apply on this iterator
	 * @return A new wrapper around this iterator using the given processor
	 */
	public IteratorWrapper<T> process(Processor<? super T> processor) {
		return new IteratorWrapper<T>(new ProcessedIterator<T>(iterator, processor));
	}

	/**
	 * Convenience method that creates a new {@link IteratorWrapper} around the
	 * given iterator.
	 *
	 * @param iterator
	 *            The iterator to wrap
	 * @return The wrapped iterator
	 */
	public static <T> IteratorWrapper<T> wrap(Iterator<T> iterator) {
		return new IteratorWrapper<T>(iterator);
	}

	//
	// INTERFACE Iterator
	//

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
		return iterator.next();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove() {
		iterator.remove();
	}

}
