/*
 * utils - Arrays.java - Copyright © 2011–2012 David Roden
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

import net.pterodactylus.util.validation.Validation;

/**
 * Helper functions for working with arrays.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Arrays {

	/**
	 * Merges the two arrays, returning an array that contains the elements of
	 * the first array followed by the elements of the second array.
	 *
	 * @param first
	 *            The first array
	 * @param second
	 *            The second array
	 * @return The merged array
	 * @throws IllegalArgumentException
	 *             if either array is {@code null}, or if the combined length of
	 *             the arrays is greater than {@link Integer#MAX_VALUE}
	 */
	public static byte[] merge(byte[] first, byte[] second) throws IllegalArgumentException {
		Validation.begin().isNotNull("First Array", first).isNotNull("Second Array", second).check().isLessOrEqual("Combined Length", (long) first.length + second.length, Integer.MAX_VALUE).check();
		if (first.length == 0) {
			return second;
		}
		if (second.length == 0) {
			return first;
		}
		byte[] result = new byte[first.length + second.length];
		System.arraycopy(first, 0, result, 0, first.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}

}
