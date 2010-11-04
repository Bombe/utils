/*
 * utils - Numbers.java - Copyright © 2010 David Roden
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

package net.pterodactylus.util.number;

/**
 * Collection of various helper methods that deal with numbers.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Numbers {

	/**
	 * Tries to parse the {@link String} representation of the given object (as
	 * per {@link String#valueOf(Object)}) as an {@link Integer}.
	 *
	 * @param object
	 *            The object to parse
	 * @return The parsed {@link Integer}, or {@code null} if the object could
	 *         not be parsed
	 */
	public static Integer safeParseInteger(Object object) {
		return safeParseInteger(object, null);
	}

	/**
	 * Tries to parse the {@link String} representation of the given object (as
	 * per {@link String#valueOf(Object)}) as an {@link Integer}.
	 *
	 * @param object
	 *            The object to parse
	 * @param defaultValue
	 *            The value to return if the object is {@code null} or can not
	 *            be parsed as an {@link Integer}
	 * @return The parsed Integer, or {@code null} if the object could not be
	 *         parsed
	 */
	public static Integer safeParseInteger(Object object, Integer defaultValue) {
		if (object == null) {
			return defaultValue;
		}
		try {
			return Integer.parseInt(String.valueOf(object));
		} catch (NumberFormatException nfe1) {
			/* ignore. */
		}
		return defaultValue;
	}

	/**
	 * Tries to parse the {@link String} representation of the given object (as
	 * per {@link String#valueOf(Object)}) as a {@link Long}.
	 *
	 * @param object
	 *            The object to parse
	 * @return The parsed {@link Long}, or {@code null} if the object could not
	 *         be parsed
	 */
	public static Long safeParseLong(Object object) {
		return safeParseLong(object, null);
	}

	/**
	 * Tries to parse the {@link String} representation of the given object (as
	 * per {@link String#valueOf(Object)}) as a {@link Long}.
	 *
	 * @param object
	 *            The object to parse
	 * @param defaultValue
	 *            The value to return if the object is {@code null} or can not
	 *            be parsed as an {@link Long}
	 * @return The parsed Long, or {@code null} if the object could not be
	 *         parsed
	 */
	public static Long safeParseLong(Object object, Long defaultValue) {
		if (object == null) {
			return defaultValue;
		}
		try {
			return Long.parseLong(String.valueOf(object));
		} catch (NumberFormatException nfe1) {
			/* ignore. */
		}
		return defaultValue;
	}

}
