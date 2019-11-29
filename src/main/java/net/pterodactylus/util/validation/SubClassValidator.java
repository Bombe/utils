/*
 * utils - SubClassValidator.java - Copyright © 2011–2019 David Roden
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
 * {@link Validator} implementation that validates that an object‘s
 * {@link Object#getClass() class} is {@link Class#isAssignableFrom(Class)
 * assignable from} a given class, i.e. that its class is the given class or a
 * subclass of it.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class SubClassValidator implements Validator<Object> {

	/** The class to match. */
	private final Class<?> targetClass;

	/**
	 * Creates a new subclass validator.
	 *
	 * @param targetClass
	 *            The class to match
	 */
	public SubClassValidator(Class<?> targetClass) {
		this.targetClass = targetClass;
	}

	//
	// VALIDATOR METHODS
	//

	/**
	 * {@inheritDoc}
	 */
	public boolean validate(Object value) {
		return targetClass.isAssignableFrom(value.getClass());
	}

	//
	// OBJECT METHODS
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "(x instanceof " + targetClass.getName() + ")";
	}

}
