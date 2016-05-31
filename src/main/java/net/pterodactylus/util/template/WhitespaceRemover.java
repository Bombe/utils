/*
 * utils - WhitespaceRemover.java - Copyright © 2011–2016 David Roden
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

import java.io.Reader;

/**
 * Removes whitespace from non-template tag parts of a {@link Template}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public interface WhitespaceRemover {

	/**
	 * Removes whitespace from the given source string.
	 *
	 * @param source
	 *            The soure string
	 * @return The source string with whitespace removed
	 */
	public String removeWhitespace(String source);

	/**
	 * {@link WhitespaceRemover} implementation that does not actually remove
	 * any whitespace. It is used as a default whitespace remover in
	 * {@link TemplateParser#parse(Reader)}.
	 *
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	public static class NoWhitespaceRemover implements WhitespaceRemover {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String removeWhitespace(String source) {
			return source;
		}

	}

}
