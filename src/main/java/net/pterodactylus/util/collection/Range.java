/*
 * utils - Range.java - Copyright © 2011 David Roden
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
 * Helper methods to create {@link Iterable}s to use with the enhanced for loop.
 * <p>
 * Instead of
 * </p>
 *
 * <pre>
 *   for (int index = 0; index < 10; ++index) {
 *     …
 *   }
 * </pre>
 * <p>
 * you can now write
 * </p>
 *
 * <pre>
 *   for (int index : Range.create(10)) {
 *   	…
 *   }
 * </pre>
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Range {

	/**
	 * Creates a new range from {@code 0} (inclusive) to the given upper bound
	 * (exclusive).
	 *
	 * @param upperBound
	 *            The upper bound of the range (exclusive)
	 * @return The requested range
	 */
	public static Iterable<Integer> create(int upperBound) {
		return create(0, upperBound);
	}

	/**
	 * Creates a new range from the given lower bound (inclusive) to the given
	 * upper bound (exclusive).
	 *
	 * @param lowerBound
	 *            The lower bound of the range (inclusive)
	 * @param upperBound
	 *            The upper bound of the range (exclusive)
	 * @return The requested range
	 */
	public static Iterable<Integer> create(final int lowerBound, final int upperBound) {
		return new Iterable<Integer>() {

			@Override
			public Iterator<Integer> iterator() {
				return new Iterator<Integer>() {

					/** The current index. */
					private int current = lowerBound;

					@Override
					public boolean hasNext() {
						return current < upperBound;
					}

					@Override
					public Integer next() {
						return current++;
					}

					@Override
					public void remove() {
						/* do nothing. */
					}
				};
			}
		};
	}

	/**
	 * Creates a new range from {@code 0} (inclusive) to the given upper bound
	 * (exclusive).
	 *
	 * @param upperBound
	 *            The upper bound of the range (exclusive)
	 * @return The requested range
	 */
	public static Iterable<Long> create(long upperBound) {
		return create(0, upperBound);
	}

	/**
	 * Creates a new range from the given lower bound (inclusive) to the given
	 * upper bound (exclusive).
	 *
	 * @param lowerBound
	 *            The lower bound of the range (inclusive)
	 * @param upperBound
	 *            The upper bound of the range (exclusive)
	 * @return The requested range
	 */
	public static Iterable<Long> create(final long lowerBound, final long upperBound) {
		return new Iterable<Long>() {

			@Override
			public Iterator<Long> iterator() {
				return new Iterator<Long>() {

					/** The current index. */
					private long current = lowerBound;

					@Override
					public boolean hasNext() {
						return current < upperBound;
					}

					@Override
					public Long next() {
						return current++;
					}

					@Override
					public void remove() {
						/* do nothing. */
					}
				};
			}
		};
	}

}
