/*
 * utils - ValidatorContainer.java - Copyright © 2011 David Roden
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

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for {@link Validator} implementation that can contain one
 * or more other {@link Validator}s.
 *
 * @see AndValidator
 * @see OrValidator
 * @param <T>
 *            The type of the object being validated
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public abstract class ValidatorContainer<T> implements Validator<T> {

	/** The list of validators. */
	private final List<Validator<T>> validators = new ArrayList<Validator<T>>();

	/**
	 * Adds a validator to this validator container.
	 *
	 * @param validator
	 *            The validator to add
	 */
	public void addValidator(Validator<T> validator) {
		validators.add(validator);
	}

	//
	// PROTECTED METHODS
	//

	/**
	 * Returns the list of validators.
	 *
	 * @return The list of validators
	 */
	protected List<Validator<T>> getValidators() {
		return validators;
	}

}
