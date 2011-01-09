/*
 * utils - SimpleStringEscaper.java - Copyright © 2011 David Roden
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

package net.pterodactylus.util.text;

/**
 * A more simple approach to {@link String} escaping, compared to
 * {@link StringEscaper} (which is actually more concerned with parsing words
 * from a line).
 * <p>
 * Only CR (ASCII 13), LF (ASCII 10), NUL (ASCII 0), and backslash (ASCII 92)
 * are escaped in this approach.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class SimpleStringEscaper {

	/**
	 * Escapes the given string.
	 *
	 * @param string
	 *            The string to escape
	 * @return The escaped string
	 */
	public static String escapeString(String string) {
		return string.replace("\\", "\\\\").replace("\r", "\\r").replace("\n", "\\n").replace("\u0000", "\\0");
	}

	/**
	 * Unescapes the given string, reversing the effects of
	 * {@link #escapeString(String)}.
	 *
	 * @param string
	 *            The string to unescape
	 * @return The original string
	 */
	public static String unescapeString(String string) {
		StringBuilder unescapedString = new StringBuilder();
		boolean backslash = false;
		for (char c : string.toCharArray()) {
			if (backslash) {
				if (c == 'r') {
					unescapedString.append('\r');
				} else if (c == 'n') {
					unescapedString.append('\n');
				} else if (c == '0') {
					unescapedString.append('\u0000');
				} else {
					unescapedString.append(c);
				}
				backslash = false;
			} else {
				if (c == '\\') {
					backslash = true;
				} else {
					unescapedString.append(c);
				}
			}
		}
		return unescapedString.toString();
	}

}
