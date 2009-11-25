/*
 * utils - Pair.java - Copyright © 2006-2009 David Roden
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
 * Container for two objects that are tied together.
 *
 * @param <S>
 *            The type of the left value
 * @param <T>
 *            The type of the right value
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Pair<S, T> {

	/** The left value. */
	protected final S left;

	/** The right value. */
	protected final T right;

	/**
	 * Creates a new pair consisting of the two values. None of the values may
	 * be {@code null}.
	 *
	 * @param left
	 *            The left value
	 * @param right
	 *            The right value
	 */
	public Pair(S left, T right) {
		if ((left == null) || (right == null)) {
			throw new NullPointerException("null is not allowed in a pair");
		}
		this.left = left;
		this.right = right;
	}

	/**
	 * Returns the left value.
	 *
	 * @return The left value
	 */
	public S getLeft() {
		return left;
	}

	/**
	 * Returns the right value.
	 *
	 * @return The right value
	 */
	public T getRight() {
		return right;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if ((obj == null) || (obj.getClass() != getClass())) {
			return false;
		}
		Pair<?, ?> pair = (Pair<?, ?>) obj;
		return left.equals(pair.left) && right.equals(pair.right);
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int leftHashCode = left.hashCode();
		return ((leftHashCode << 16) | (leftHashCode >>> 16)) ^ ~right.hashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "<" + left + "," + right + ">";
	}

}
