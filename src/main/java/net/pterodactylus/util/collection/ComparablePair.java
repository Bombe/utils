/*
 * utils - ComparablePair.java - Copyright © 2006-2009 David Roden
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

/**
 * Container for two {@link Comparable} objects that are tied together.
 * Comparisons are done by first comparing the left object and only comparing
 * the right objects if the left objects are considered the same (i.e.
 * {@link Comparable#compareTo(Object)} returns {@code 0}).
 *
 * @param <S>
 *            The type of the left value
 * @param <T>
 *            The type of the right value
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ComparablePair<S extends Comparable<S>, T extends Comparable<T>> extends Pair<S, T> implements Comparable<ComparablePair<S, T>> {

	/**
	 * Creates a new pair consisting of the two values.
	 *
	 * @param left
	 *            The left value
	 * @param right
	 *            The right value
	 */
	public ComparablePair(S left, T right) {
		super(left, right);
	}

	/**
	 * @see java.lang.Comparable#compareTo(Object)
	 */
	@Override
	public int compareTo(ComparablePair<S, T> pair) {
		int leftDifference = left.compareTo(pair.left);
		return (leftDifference == 0) ? right.compareTo(pair.right) : leftDifference;
	}

}
