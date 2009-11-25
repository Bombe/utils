/*
 * utils - Triplet.java - Copyright © 2009 David Roden
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

import net.pterodactylus.util.number.Bits;

/**
 * 3-tuple.
 *
 * @param <T>
 *            The type of the first element
 * @param <U>
 *            The type of the second element
 * @param <V>
 *            The type of the third element
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Triplet<T, U, V> {

	/** The first element. */
	private final T first;

	/** The second element. */
	private final U second;

	/** The third element. */
	private final V third;

	/**
	 * Creates a new triplet.
	 *
	 * @param first
	 *            The first element
	 * @param second
	 *            The second element
	 * @param third
	 *            The third element
	 */
	public Triplet(T first, U second, V third) {
		this.first = first;
		this.second = second;
		this.third = third;
	}

	/**
	 * Returns the first element.
	 *
	 * @return The first element
	 */
	public T getFirst() {
		return first;
	}

	/**
	 * Returns the second element.
	 *
	 * @return The second element
	 */
	public U getSecond() {
		return second;
	}

	/**
	 * Returns the third element.
	 *
	 * @return The third element
	 */
	public V getThird() {
		return third;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Triplet<?, ?, ?>)) {
			return false;
		}
		Triplet<?, ?, ?> triplet = (Triplet<?, ?, ?>) obj;
		return ObjectMethods.equal(first, triplet.first) && ObjectMethods.equal(second, triplet.second) && ObjectMethods.equal(third, triplet.third);
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Bits.rotateLeft(ObjectMethods.hashCode(first), 8) ^ Bits.rotateLeft(ObjectMethods.hashCode(second), 16) ^ Bits.rotateLeft(ObjectMethods.hashCode(third), 24);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "<" + first + "," + second + "," + third + ">";
	}

}
