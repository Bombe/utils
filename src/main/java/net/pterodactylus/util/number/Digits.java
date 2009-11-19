/*
 * utils - Digits.java - Copyright © 2006-2009 David Roden
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
 * Utility class for decimal number strings.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Digits {

	/**
	 * Zero-pads the given value until it is at least the specified length in
	 * digits. This will only work with positive values!
	 *
	 * @param value
	 *            The value to pad
	 * @param digits
	 *            The number of digits for the padded value
	 * @return The zero-padded value
	 */
	public static String format(long value, int digits) {
		String formattedValue = String.valueOf(value);
		while (formattedValue.length() < digits) {
			formattedValue = "0" + formattedValue;
		}
		return formattedValue;
	}

	/**
	 * Returns the given value formatted with the given number of fractional
	 * digits, showing final zeroes as well.
	 *
	 * @param value
	 *            The value to format
	 * @param fractionDigits
	 *            The number of fractional digits
	 * @param round
	 *            <code>true</code> to round the formatted value,
	 *            <code>false</code> to truncate it
	 * @return The formatted value
	 */
	public static String formatFractions(double value, int fractionDigits, boolean round) {
		double factor = Math.pow(10, fractionDigits);
		int tempValue = (int) (value * factor + (round ? 0.5 : 0));
		String formattedValue = String.valueOf(tempValue / factor);
		if (formattedValue.indexOf('.') == -1) {
			formattedValue += ".";
			for (int count = 0; count < fractionDigits; count++) {
				formattedValue += "0";
			}
		} else {
			while (formattedValue.length() - formattedValue.indexOf('.') <= fractionDigits) {
				formattedValue += "0";
			}
		}
		return formattedValue;
	}

	/**
	 * Parses the given string into a long, throwing an exception if the string
	 * contains invalid characters.
	 *
	 * @param digits
	 *            The number string to parse
	 * @return A long value representing the number string
	 * @throws NumberFormatException
	 *             if the number string contains invalid characters
	 */
	public static long parseLong(String digits) throws NumberFormatException {
		return Long.parseLong(digits);
	}

	/**
	 * Parses the given string into a long, returning the given default value if
	 * the string contains invalid characters.
	 *
	 * @param digits
	 *            The number string to parse
	 * @param defaultValue
	 *            The value to return if the string can not be parsed
	 * @return The long value represented by the string, or the default value if
	 *         the string can not be parsed
	 */
	public static long parseLong(String digits, long defaultValue) {
		try {
			return Long.parseLong(digits);
		} catch (NumberFormatException nfe1) {
			return defaultValue;
		}
	}

	/**
	 * Parses the given string into an int, throwing an exception if the string
	 * contains invalid characters.
	 *
	 * @param digits
	 *            The number string to parse
	 * @return A int value representing the number string
	 * @throws NumberFormatException
	 *             if the number string contains invalid characters
	 */
	public static int parseInt(String digits) throws NumberFormatException {
		return Integer.parseInt(digits);
	}

	/**
	 * Parses the given string into an int, returning the given default value if
	 * the string contains invalid characters.
	 *
	 * @param digits
	 *            The number string to parse
	 * @param defaultValue
	 *            The value to return if the string can not be parsed
	 * @return The int value represented by the string, or the default value if
	 *         the string can not be parsed
	 */
	public static int parseInt(String digits, int defaultValue) {
		try {
			return Integer.parseInt(digits);
		} catch (NumberFormatException nfe1) {
			return defaultValue;
		}
	}

}
