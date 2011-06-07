/*
 * utils - ReverseComparator.java - Copyright © 2009 David Roden
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

import java.util.Comparator;

/**
 * This {@link Comparator} implementation compares to {@link Comparable}s but
 * reverses the result of the comparison.
 *
 * @param <T>
 *            The type to compare
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ReverseComparator<T> implements Comparator<T> {

	/** The comparator to reverse. */
	private final Comparator<T> comparator;

	/**
	 * Creates a new comparator that reverse the given comparator.
	 *
	 * @param comparator
	 *            The comparator to reverse
	 */
	public ReverseComparator(Comparator<T> comparator) {
		this.comparator = comparator;
	}

	/**
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(T o1, T o2) {
		return -comparator.compare(o1, o2);
	}

}
