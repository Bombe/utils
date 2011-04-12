/*
 * utils - BasicWhitespaceRemover.java - Copyright © 2011 David Roden
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

package net.pterodactylus.util.template;

/**
 * Removes basic whitespace characters from the beginning of a {@link String}.
 * The removed characters are tab ({@code 0x09}), carriage return ({@code 0x0d}
 * ), line feed ({@code 0x0a}), and space ({@code 0x20}).
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class BasicWhitespaceRemover implements WhitespaceRemover {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String removeWhitespace(String source) {
		StringBuilder stringBuilder = new StringBuilder(source);
		while (stringBuilder.length() > 0) {
			char firstChar = stringBuilder.charAt(0);
			if ((firstChar == 0x0009) || (firstChar == 0x000a) || (firstChar == 0x000d) || (firstChar == 0x0020)) {
				stringBuilder.deleteCharAt(0);
			} else {
				break;
			}
		}
		return stringBuilder.toString();
	}

}
