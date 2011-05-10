/*
 * utils - InvertingValidator.java - Copyright © 2011 David Roden
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
 * {@link Validator} implementation that inverts the result of another
 * {@link Validator}.
 *
 * @param <T>
 *            The type of the object being validated
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class NotValidator<T> implements Validator<T> {

	/** The validator to invert. */
	private final Validator<T> validator;

	/**
	 * Creates a new inverting validator.
	 *
	 * @param validator
	 *            The validator to invert
	 */
	public NotValidator(Validator<T> validator) {
		this.validator = validator;
	}

	//
	// VALIDATOR METHODS
	//

	/**
	 * {@inheritDoc}
	 */
	public boolean validate(T value) {
		return !validator.validate(value);
	}

	//
	// OBJECT METHODS
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "!" + validator;
	}

}
