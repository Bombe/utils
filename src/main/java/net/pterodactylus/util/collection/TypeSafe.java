/*
 * utils - TypeSafe.java - Copyright © 2011–2019 David Roden
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
 * Helper methods to simplify handling of unknown types when handling generics.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class TypeSafe {

	/**
	 * Verifies that the given object can be assigned to a variable with the
	 * given class, returning a type-cast object with the correct type, or
	 * {@code null} if the object does not have a matching type.
	 *
	 * @param <T>
	 *            The desired type of the object
	 * @param object
	 *            The object
	 * @param clazz
	 *            The class of the type
	 * @return The type-cast object, or {@code null} if the object does not have
	 *         a matching type
	 */
	public static <T> T ensureType(Object object, Class<T> clazz) {
		return ensureType(object, clazz, null);
	}

	/**
	 * Verifies that the given object can be assigned to a variable with the
	 * given class, returning a type-cast object with the correct type, or the
	 * default value if the object does not have a matching type.
	 *
	 * @param <T>
	 *            The desired type of the object
	 * @param object
	 *            The object
	 * @param clazz
	 *            The class of the type
	 * @param defaultValue
	 *            The default value for non-matching objects
	 * @return The type-cast object, or the default value if the object does not
	 *         have a matching type
	 */
	@SuppressWarnings("unchecked")
	public static <T> T ensureType(Object object, Class<T> clazz, T defaultValue) {
		if (object == null) {
			return null;
		}
		if (clazz.isAssignableFrom(object.getClass())) {
			return (T) object;
		}
		return defaultValue;
	}

}
