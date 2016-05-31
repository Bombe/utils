/*
 * utils - Validator.java - Copyright © 2011–2016 David Roden
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

package net.pterodactylus.util.validation;

/**
 * Interface for objects that can validate other objects.
 * <p>
 * It is recommend that {@link #toString()} is overridden to return a concise
 * representation of this validator so that the error messages in
 * {@link Validation} are useful.
 *
 * @param <T>
 *            The type of the object being validated
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public interface Validator<T> {

	/**
	 * Validates the given object.
	 *
	 * @param value
	 *            The object to validate
	 * @return {@code true} if the given object passed validation, {@code false}
	 *         otherwise
	 */
	public boolean validate(T value);

}
