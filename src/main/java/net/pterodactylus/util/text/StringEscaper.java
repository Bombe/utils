/*
 * utils - StringEscaper.java - Copyright © 2008-2009 David Roden
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.pterodactylus.util.number.Hex;

/**
 * Contains different methods to escape strings, e.g. for storage in a
 * text-based medium like databases or an XML file.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class StringEscaper {

	/**
	 * Converts the string into a form that is suitable for storage in
	 * text-based medium.
	 *
	 * @param original
	 *            The original string
	 * @return A string that can be used for text-based persistence
	 */
	public static String persistString(String original) {
		if (original == null) {
			return "";
		}
		StringBuilder persistedString = new StringBuilder();
		persistedString.append(original.length()).append(':');
		for (char c : original.toCharArray()) {
			persistedString.append(Hex.toHex(c, 4));
		}
		return persistedString.toString();
	}

	/**
	 * Recovers the original string from a string that has been created with
	 * {@link #persistString(String)}.
	 *
	 * @param persistedString
	 *            The persisted string
	 * @return The original string
	 * @throws TextException
	 *             if the persisted string can not be parsed
	 */
	public static String unpersistString(String persistedString) throws TextException {
		if (persistedString.length() == 0) {
			return null;
		}
		int colon = persistedString.indexOf(':');
		if (colon == -1) {
			throw new TextException("no colon in persisted string");
		}
		if (((persistedString.length() - (colon + 1)) % 4) != 0) {
			throw new TextException("invalid length of persisted string");
		}
		int length = -1;
		try {
			length = Integer.parseInt(persistedString.substring(0, colon));
		} catch (NumberFormatException nfe1) {
			throw new TextException("could not parse length", nfe1);
		}
		if (length < 0) {
			throw new TextException("invalid length: " + length);
		}
		StringBuilder unpersistedString = new StringBuilder(length);
		try {
			for (int charIndex = colon + 1; charIndex < persistedString.length(); charIndex += 4) {
				char c = (char) Integer.parseInt(persistedString.substring(charIndex, charIndex + 4), 16);
				unpersistedString.append(c);
			}
		} catch (NumberFormatException nfe1) {
			throw new TextException("invalid character in persisted string", nfe1);
		}
		return unpersistedString.toString();
	}

	/**
	 * Splits a string into words. Words are separated by space characters.
	 *
	 * @param line
	 *            The line to parse
	 * @return The parsed words
	 * @throws TextException
	 *             if quotes are not closed
	 */
	public static List<String> parseLine(String line) throws TextException {
		boolean inDoubleQuote = false;
		boolean inSingleQuote = false;
		boolean backslashed = false;
		List<String> words = new ArrayList<String>();
		boolean wordEmpty = true;
		StringBuilder currentWord = new StringBuilder();
		for (char c : line.toCharArray()) {
			if (c == '"') {
				if (inSingleQuote || backslashed) {
					currentWord.append(c);
					backslashed = false;
				} else {
					inDoubleQuote ^= true;
					wordEmpty = false;
				}
			} else if (c == '\'') {
				if (inDoubleQuote || backslashed) {
					currentWord.append(c);
					backslashed = false;
				} else {
					inSingleQuote ^= true;
					wordEmpty = false;
				}
			} else if (c == '\\') {
				if (inSingleQuote || backslashed) {
					currentWord.append(c);
					backslashed = false;
				} else {
					backslashed = true;
				}
			} else if (c == ' ') {
				if (inDoubleQuote || inSingleQuote || backslashed) {
					currentWord.append(c);
					backslashed = false;
				} else {
					if ((currentWord.length() > 0) || !wordEmpty) {
						words.add(currentWord.toString());
						currentWord.setLength(0);
						wordEmpty = true;
					}
				}
			} else {
				if (backslashed && (c == 'n')) {
					currentWord.append('\n');
				} else {
					currentWord.append(c);
				}
				backslashed = false;
			}
		}
		if (inSingleQuote || inDoubleQuote || backslashed) {
			throw new TextException("open quote");
		}
		if (currentWord.length() > 0) {
			words.add(currentWord.toString());
		}
		return words;
	}

	/**
	 * Escapes the given word in a way that {@link #parseLine(String)} can
	 * correctly unescape it. The following rules are applied to the word:
	 * <ul>
	 * <li>If the word is the empty string, it is surrounded by double quotes.</li>
	 * <li>If the word does not contain single quotes, double quotes,
	 * backslashes, or space characters, it is returned as is.</li>
	 * <li>If the word contains space characters and single quotes but none of
	 * the other characters, it is surrounded by double quotes.</li>
	 * <li>If the word contains space characters and double quotes and
	 * backslashes, it is surrounded by single quotes.</li>
	 * <li>Otherwise single quotes, double quotes, backslashes and space
	 * characters are escaped by prefixing them with a backslash.</li>
	 * </ul>
	 *
	 * @param word
	 *            The word to escape
	 * @return The escaped word
	 */
	public static String escapeWord(String word) {
		if (word == null) {
			return "";
		}
		if (word.length() == 0) {
			return "\"\"";
		}
		boolean containsSingleQuote = word.indexOf('\'') != -1;
		boolean containsDoubleQuote = word.indexOf('"') != -1;
		boolean containsBackslash = word.indexOf('\\') != -1;
		boolean containsSpace = word.indexOf(' ') != -1;
		boolean containsLineBreak = word.indexOf('\n') != -1;
		if (!containsSingleQuote && !containsDoubleQuote && !containsBackslash && !containsSpace) {
			return word.replace("\n", "\\n");
		}
		if (!containsDoubleQuote && !containsBackslash) {
			return "\"" + word.replace("\n", "\\n") + "\"";
		}
		if (!containsSingleQuote && !containsLineBreak) {
			return "'" + word.replace("\n", "\\n") + "'";
		}
		return word.replace("\\", "\\\\").replace(" ", "\\ ").replace("\"", "\\\"").replace("'", "\\'").replace("\n", "\\n");
	}

	/**
	 * Escapes all words in the given collection and joins them separated by a
	 * space.
	 *
	 * @param words
	 *            The words to escape
	 * @return A line with all escaped word separated by a space
	 */
	public static String escapeWords(Collection<String> words) {
		StringBuilder wordBuilder = new StringBuilder();
		for (String word : words) {
			if (wordBuilder.length() > 0) {
				wordBuilder.append(' ');
			}
			wordBuilder.append(escapeWord(word));
		}
		return wordBuilder.toString();
	}

}
