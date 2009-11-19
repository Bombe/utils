/*
 * utils - SI.java - Copyright © 2006-2009 David Roden
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
 * Formats a decimal number with SI units.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class SI {

	/** The units (up to 2^24). */
	private static final String[] units = { "", "K", "M", "G", "T", "P", "E", "Z", "Y" };

	/**
	 * Formats the specified number using 1000-based units.
	 *
	 * @param number
	 *            The number to encode
	 * @return The converted number with a unit postfix
	 */
	public static String format(long number) {
		return format(number, false, false);
	}

	/**
	 * Formats the specified number using 1024-based units.
	 *
	 * @param number
	 *            The number to encode
	 * @return The converted number with a unit postfix
	 */
	public static String formatBinary(long number) {
		return format(number, true, false);
	}

	/**
	 * Formats the specified number using the specified units. If
	 * <code>useBinaryUnits</code> is <code>true</code>, 1024-based units
	 * (marked by an 'i' after the unit character, e.g. 'Ki' for 1024) will be
	 * used; if it is <code>false</code>, 1000-based units will be used.
	 *
	 * @param number
	 *            The number to encode
	 * @param useBinaryUnits
	 *            Whether to use binary or decimal units
	 * @return The converted number with a unit postfix
	 */
	public static String format(long number, boolean useBinaryUnits) {
		return format(number, 0, useBinaryUnits, false);
	}

	/**
	 * Formats the specified number using the specified units. If
	 * <code>useBinaryUnits</code> is <code>true</code>, 1024-based units
	 * (marked by an 'i' after the unit character, e.g. 'Ki' for 1024) will be
	 * used; if it is <code>false</code>, 1000-based units will be used.
	 *
	 * @param number
	 *            The number to encode
	 * @param useBinaryUnits
	 *            Whether to use binary or decimal units
	 * @param addSpace
	 *            Whether to add a space between the number and the unit
	 * @return The converted number with a unit postfix
	 */
	public static String format(long number, boolean useBinaryUnits, boolean addSpace) {
		return format(number, 0, useBinaryUnits, addSpace);
	}

	/**
	 * Formats the specified number using the specified units. If
	 * <code>useBinaryUnits</code> is <code>true</code>, 1024-based units
	 * (marked by an 'i' after the unit character, e.g. 'Ki' for 1024) will be
	 * used; if it is <code>false</code>, 1000-based units will be used.
	 *
	 * @param number
	 *            The number to encode
	 * @param digits
	 *            The number of digits after the decimal point
	 * @param useBinaryUnits
	 *            Whether to use binary or decimal units
	 * @param addSpace
	 *            Whether to add a space between the number and the unit
	 * @return The converted number with a unit postfix
	 */
	public static String format(long number, int digits, boolean useBinaryUnits, boolean addSpace) {
		int unit = 0;
		double realNumber = number;
		while ((unit < units.length) && (realNumber >= (useBinaryUnits ? 1024 : 1000))) {
			realNumber /= (useBinaryUnits ? 1024 : 1000);
			unit++;
		}
		return Digits.formatFractions(realNumber, digits, false) + (addSpace ? " " : "") + units[unit] + ((useBinaryUnits && (unit > 0)) ? "i" : "");
	}

}
