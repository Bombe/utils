/*
 * utils - HashCode.java - Copyright © 2011–2016 David Roden
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

package net.pterodactylus.util.object;

import java.util.Collection;

/**
 * Calculates hash codes for {@link Collection}s.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class HashCode {

	/**
	 * Calculates a hash code for all objects in the given collection.
	 *
	 * @param collection
	 *            The collection to create a hash code for
	 * @return The hash code for the collection
	 */
	public static int hashCode(Collection<?> collection) {
		int hashCode = 1;
		for (Object object : collection) {
			hashCode = hashCode * 31 + ((object == null) ? 0 : object.hashCode());
		}
		return hashCode;
	}

}
