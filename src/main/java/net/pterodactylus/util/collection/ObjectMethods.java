/*
 * utils - ObjectMethods.java - Copyright © 2009 David Roden
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
 * Contains helper methods for implementating {@link Object} methods such as
 * {@link Object#equals(Object)} and {@link Object#hashCode()} for objects that
 * may be {@code null}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ObjectMethods {

	/**
	 * Checks whether the two objects are equal.
	 *
	 * @param first
	 *            The first object
	 * @param second
	 *            The second object
	 * @return {@code true} if the objects are equal (according to
	 *         {@link Object#equals(Object)}) or both {@code null}
	 */
	public static boolean equal(Object first, Object second) {
		return (first == null) ? (second == null) : first.equals(second);
	}

	/**
	 * Calculates the hash code for the given object.
	 * @param object The object to get the hash code for
	 * @return The hash code of the object, or
	 */
	public static int hashCode(Object object) {
		return (object == null) ? 0xCF018C37 : object.hashCode();
	}

}
