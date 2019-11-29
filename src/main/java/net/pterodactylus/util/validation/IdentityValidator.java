/*
 * utils - IdentityValidator.java - Copyright © 2011–2019 David Roden
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
 * {@link Validator} implementation that requires an object to be the same
 * object as another object. If you only want to check for equality, use
 * {@link EqualityValidator}.
 *
 * @param <T>
 *            The type of the object being validated
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class IdentityValidator<T> implements Validator<T> {

	/** The expected value. */
	private final T expectedValue;

	/**
	 * Creates a new identity validator.
	 *
	 * @param expectedValue
	 *            The expected value
	 */
	public IdentityValidator(T expectedValue) {
		this.expectedValue = expectedValue;
	}

	//
	// VALIDATOR METHODS
	//

	/**
	 * {@inheritDoc}
	 */
	public boolean validate(T value) {
		return value == expectedValue;
	}

	//
	// OBJECT METHODS
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "(x == " + expectedValue + ")";
	}

}
