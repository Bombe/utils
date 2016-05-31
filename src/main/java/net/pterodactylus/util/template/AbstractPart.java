/*
 * utils - AbstractPart.java - Copyright © 2011–2016 David Roden
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
 * Abstract implementation of a {@link Part} that adds a line number and column
 * number which can be used for {@link TemplateException}s.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public abstract class AbstractPart implements Part {

	/** The line number of the tag. */
	private final int line;

	/** The column number of the tag. */
	private final int column;

	/**
	 * Creates a new abstract part that was started by a tag at the given
	 * location in the {@link TemplateParser}’s input.
	 *
	 * @param line
	 *            The line number of the tag
	 * @param column
	 *            The column number of the tag
	 */
	protected AbstractPart(int line, int column) {
		this.line = line;
		this.column = column;
	}

	/**
	 * Returns the line number of the tag. Line numbers starts at {@code 1}.
	 *
	 * @return The line number of the tag, if any
	 */
	public int getLine() {
		return line;
	}

	/**
	 * Returns the column number of the tag. Column numbers starts at {@code 1}.
	 *
	 * @return The column number of the tag, if any
	 */
	public int getColumn() {
		return column;
	}

}
