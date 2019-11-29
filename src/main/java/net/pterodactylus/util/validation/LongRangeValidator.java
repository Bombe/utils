/*
 * utils - LongRangeValidator.java - Copyright © 2011–2019 David Roden
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
 * {@link Validator} implementation that validates that a {@link Long} is within
 * a given range. Both lower and upper bound of this range are inclusive.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class LongRangeValidator implements Validator<Long> {

	/** The lower bound of the range. */
	private final long lowerBound;

	/** The upper bound of the range. */
	private final long upperBound;

	/**
	 * Creates a new long range validator.
	 *
	 * @param lowerBound
	 *            The lower bound of the range
	 * @param upperBound
	 *            The upper bound of the range
	 */
	public LongRangeValidator(long lowerBound, long upperBound) {
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}

	//
	// VALIDATOR METHODS
	//

	/**
	 * {@inheritDoc}
	 */
	public boolean validate(Long value) {
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
