/*
 * utils - Hex.java - Copyright © 2006-2009 David Roden
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
 * Contains methods to convert byte arrays to hex strings and vice versa.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Hex {

	/**
	 * Converts the given string to a hexadecimal string.
	 *
	 * @param buffer
	 *            The string to convert
	 * @return A hexadecimal string
	 * @see #toHex(byte[], int, int)
	 */
	public static String toHex(String buffer) {
		return toHex(buffer.getBytes());
	}

	/**
	 * Converts the given buffer to a hexadecimal string.
	 *
	 * @param buffer
	 *            The buffer to convert
	 * @return A hexadecimal string
	 * @see #toHex(byte[], int, int)
	 */
	public static String toHex(byte[] buffer) {
		return toHex(buffer, 0, buffer.length);
	}

	/**
	 * Converts <code>length</code> bytes of the given buffer starting at index
	 * <code>start</code> to a hexadecimal string.
	 *
	 * @param buffer
	 *            The buffer to convert
	 * @param start
	 *            The index to start
	 * @param length
	 *            The length to convert
	 * @return A hexadecimal string
	 * @throws ArrayIndexOutOfBoundsException
	 *             if <code>start</code> and/or <code>start + length</code> are
	 *             outside the valid bounds of <code>buffer</code>
	 * @see #toHex(byte[], int, int)
	 */
	public static String toHex(byte[] buffer, int start, int length) {
		return toHex(buffer, start, length, false);
	}

	/**
	 * Converts the given string to a hexadecimal string, using upper-case
	 * characters for the digits &lsquo;a&rsquo; to &lsquo;f&rsquo;, if desired.
	 *
	 * @param buffer
	 *            The string to convert
	 * @param upperCase
	 *            if the digits 'a' to 'f' should be in upper-case characters
	 * @return A hexadecimal string
	 * @see #toHex(byte[], int, int, boolean)
	 */
	public static String toHex(String buffer, boolean upperCase) {
		return toHex(buffer.getBytes(), upperCase);
	}

	/**
	 * Converts the given buffer to a hexadecimal string, using upper-case
	 * characters for the digits &lsquo;a&rsquo; to &lsquo;f&rsquo;, if desired.
	 *
	 * @param buffer
	 *            The buffer to convert
	 * @param upperCase
	 *            if the digits 'a' to 'f' should be in upper-case characters
	 * @return A hexadecimal string
	 * @see #toHex(byte[], int, int)
	 */
	public static String toHex(byte[] buffer, boolean upperCase) {
		return toHex(buffer, 0, buffer.length, upperCase);
	}

	/**
	 * Converts <code>length</code> bytes of the given buffer starting at index
	 * <code>start</code> to a hexadecimal string, using upper-case characters
	 * for the digits &lsquo;a&rsquo; to &lsquo;f&rsquo;, if desired.
	 *
	 * @param buffer
	 *            The buffer to convert
	 * @param start
	 *            The index to start
	 * @param length
	 *            The length to convert
	 * @param upperCase
	 *            if the digits 'a' to 'f' should be in upper-case characters
	 * @return A hexadecimal string
	 * @throws ArrayIndexOutOfBoundsException
	 *             if <code>start</code> and/or <code>start + length</code> are
	 *             outside the valid bounds of <code>buffer</code>
	 * @see #toHex(byte[], int, int)
	 */
	public static String toHex(byte[] buffer, int start, int length, boolean upperCase) {
		StringBuilder hexBuffer = new StringBuilder(length * 2);
		for (int index = start; index < length; index++) {
			String hexByte = Integer.toHexString(buffer[index] & 0xff);
			if (upperCase) {
				hexByte = hexByte.toUpperCase();
			}
			if (hexByte.length() < 2) {
				hexBuffer.append('0');
			}
			hexBuffer.append(hexByte);
		}
		return hexBuffer.toString();
	}

	/**
	 * Formats the given byte as a 2-digit hexadecimal value.
	 *
	 * @param value
	 *            The byte to encode
	 * @return The encoded 2-digit hexadecimal value
	 */
	public static String toHex(byte value) {
		return toHex(value, 2);
	}

	/**
	 * Formats the given shoirt as a 4-digit hexadecimal value.
	 *
	 * @param value
	 *            The short to encode
	 * @return The encoded 4-digit hexadecimal value
	 */
	public static String toHex(short value) {
		return toHex(value, 4);
	}

	/**
	 * Formats the given int as a 8-digit hexadecimal value.
	 *
	 * @param value
	 *            The int to encode
	 * @return The encoded 8-digit hexadecimal value
	 */
	public static String toHex(int value) {
		return toHex(value, 8);
	}

	/**
	 * Formats the given int as a 16-digit hexadecimal value.
	 *
	 * @param value
	 *            The long to encode
	 * @return The encoded 16-digit hexadecimal value
	 */
	public static String toHex(long value) {
		return toHex(value, 16);
	}

	/**
	 * Formats the given value with as a hexadecimal number with at least the
	 * specified number of digits. The value will be padded with zeroes if it is
	 * shorter than <code>digits</code>.
	 *
	 * @param value
	 *            The value to encode
	 * @param digits
	 *            The minimum number of digits
	 * @return The zero-padded hexadecimal value
	 */
	public static String toHex(long value, int digits) {
		String hexValue = Long.toHexString(value);
		if (hexValue.length() > digits) {
			hexValue = hexValue.substring(hexValue.length() - digits, hexValue.length());
		}
		while (hexValue.length() < digits) {
			hexValue = "0" + hexValue;
		}
		return hexValue;
	}

	/**
	 * Decodes a hexadecimal string into a byte array.
	 *
	 * @param hexString
	 *            The hexadecimal representation to decode
	 * @return The decoded byte array
	 * @see Integer#parseInt(java.lang.String, int)
	 */
	public static byte[] toByte(String hexString) {
		if ((hexString.length() & 0x01) == 0x01) {
			/* odd length, this is not correct. */
			throw new IllegalArgumentException("hex string must have even length.");
		}
		byte[] dataBytes = new byte[hexString.length() / 2];
		for (int stringIndex = 0; stringIndex < hexString.length(); stringIndex += 2) {
			String hexNumber = hexString.substring(stringIndex, stringIndex + 2);
			byte dataByte = (byte) Integer.parseInt(hexNumber, 16);
			dataBytes[stringIndex / 2] = dataByte;
		}
		return dataBytes;
	}

}
