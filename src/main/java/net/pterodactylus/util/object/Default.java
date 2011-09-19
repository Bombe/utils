/*
 * utils - Default.java - Copyright © 2011 David Roden
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

/**
 * Helper class that can return different values for an object if it matches
 * certain criteria. Its primary use is simplification of code such as
 *
 * <pre>
 * Object someObject = provider.getObject();
 * if (someObject == null) {
 * 	someObject = someDefaultObject;
 * }
 * </pre>
 *
 * It can be written as:
 *
 * <pre>
 * Object someObject = Default.forNull(provider.getObject(), someDefaultObject);
 * </pre>
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Default {

	/**
	 * Returns the given value if it is non-{@code null}, or the default value
	 * if the given value is {@code null}.
	 *
	 * @param <T>
	 *            The type of the value
	 * @param value
	 *            The value
	 * @param defaultValue
	 *            The default value
	 * @return The value if it is non-{@code null}, or the default value if the
	 *         value is {@code null}
	 */
	public static <T> T forNull(T value, T defaultValue) {
		return (value != null) ? value : defaultValue;
	}

}
