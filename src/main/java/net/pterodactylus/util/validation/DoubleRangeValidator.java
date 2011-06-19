/*
 * utils - LongRangeValidator.java - Copyright © 2011 David Roden
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
 * {@link Validator} that validates that a {@link Double} is within a specified
 * range.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class DoubleRangeValidator implements Validator<Double> {

	/** The lower bound of the range. */
	private final double lowerBound;

	/** Whether the lower bound is included in the range. */
	private final boolean includeLowerBound;

	/** The upper bound of the range. */
	private final double upperBound;

	/** Whether the upper bound is included in the range. */
	private final boolean includeUpperBound;

	/**
	 * Creates a new double range validator.
	 *
	 * @param lowerBound
	 *            The lower bound of the range
	 * @param includeLowerBound
	 *            {@code true} to include the lower bound in the range, {@code
	 *            false} otherwise
	 * @param upperBound
	 *            The upper bound of the range
	 * @param includeUpperBound
	 *            {@code true} to include the upper bound in the range, {@code
	 *            false} otherwise
	 */
	public DoubleRangeValidator(double lowerBound, boolean includeLowerBound, double upperBound, boolean includeUpperBound) {
		this.lowerBound = lowerBound;
		this.includeLowerBound = includeLowerBound;
		this.upperBound = upperBound;
		this.includeUpperBound = includeUpperBound;
	}

	//
	// VALIDATOR METHODS
	//

	/**
	 * {@inheritDoc}
	 */
	public boolean validate(Double value) {
		return (includeLowerBound ? (value >= lowerBound) : (value > lowerBound)) && (includeUpperBound ? (value <= upperBound) : (value < upperBound));
	}

	//
	// OBJECT METHODS
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "((x " + (includeLowerBound ? ">=" : ">") + " " + lowerBound + ") && (x " + (includeUpperBound ? "<=" : "<") + " " + upperBound + "))";
	}

}
