/*
 * utils - MappedIterator.java - Copyright © 2011–2019 David Roden
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
package net.pterodactylus.util.collection.mapper;

import java.util.Iterator;

/**
 * Wrapper around an {@link Iterator} that will apply a {@link Mapper} to all
 * returned items. The {@link #remove()} method will by delegated to the
 * original iterator.
 *
 * @param <I>
 *            The type of the original iterator’s items
 * @param <O>
 *            The type of the mapped items
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class MappedIterator<I, O> implements Iterator<O> {

	/** The wrapped iterator. */
	private final Iterator<I> inputIterator;

	/** The mapper to apply to all items. */
	private final Mapper<? super I, ? extends O> mapper;

	/**
	 * Creates a new iterator mapper.
	 *
	 * @param inputIterator
	 *            The iterator to map
	 * @param mapper
	 *            The mapper to apply
	 */
	public MappedIterator(Iterator<I> inputIterator, Mapper<? super I, ? extends O> mapper) {
		this.inputIterator = inputIterator;
		this.mapper = mapper;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasNext() {
		return inputIterator.hasNext();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public O next() {
		return mapper.map(inputIterator.next());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove() {
		inputIterator.remove();
	}

}
