/*
 * utils - IntegerRangeValidator.java - Copyright © 2011–2016 David Roden
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
 * {@link Validator} implementation that validates that an {@link Integer} is
 * within a given range. Both lower and upper bound of this range are inclusive.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class IntegerRangeValidator implements Validator<Integer> {

	/** The lower bound of the range. */
	private final int lowerBound;

	/** The upper bound of the range. */
	private final int upperBound;

	/**
	 * Creates a new integer range validator.
	 *
	 * @param lowerBound
	 *            The lower bound of the range
	 * @param upperBound
	 *            The upper bound of the range
	 */
	public IntegerRangeValidator(int lowerBound, int upperBound) {
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}

	//
	// VALIDATOR METHODS
	//

	/**
	 * {@inheritDoc}
	 */
	public boolean validate(Integer value) {
		return (lowerBound <= value) && (upperBound >= value);
	}

	//
	// OBJECT METHODS
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "((x >= " + lowerBound + ") && (x <= " + upperBound + "))";
	}

}
