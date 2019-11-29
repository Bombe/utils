/*
 * utils - MappedIterable.java - Copyright © 2011–2019 David Roden
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
 * Wrapper around an {@link Iterable} that will apply a {@link Mapper} to all
 * {@link Iterator}s it creates.
 *
 * @see MappedIterator
 * @param <I>
 *            The type of the original iterable’s items
 * @param <O>
 *            The type of the mapped items
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class MappedIterable<I, O> implements Iterable<O> {

	/** The wrapped iterable. */
	private final Iterable<I> inputIterable;

	/** The mapper to apply. */
	private final Mapper<? super I, ? extends O> mapper;

	/**
	 * Creates a new iterable mapper.
	 *
	 * @param inputIterable
	 *            The iterable to wrap
	 * @param mapper
	 *            The mapper to apply to all items
	 */
	public MappedIterable(Iterable<I> inputIterable, Mapper<? super I, ? extends O> mapper) {
		this.inputIterable = inputIterable;
		this.mapper = mapper;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<O> iterator() {
		return new MappedIterator<I, O>(inputIterable.iterator(), mapper);
	}

}
