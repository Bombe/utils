/*
 * utils - TemplateException.java - Copyright © 2010 David Roden
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
 * Exception that is thrown when a {@link Template} can not be rendered because
 * its input can not be parsed correctly, or when its template variables can not
 * be parsed or evaluated.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class TemplateException extends RuntimeException {

	/** The line number of the erroneous tag, if any. */
	private final int line;

	/** The column number of the erroneous tag, if any. */
	private final int column;

	/**
	 * Creates a new template exception.
	 */
	public TemplateException() {
		this(0, 0);
	}

	/**
	 * Creates a new template exception.
	 *
	 * @param line
	 *            The line number of the erroneous tag, if any
	 * @param column
	 *            The column number of the erroneous tag, if any
	 */
	public TemplateException(int line, int column) {
		super();
		this.line = line;
		this.column = column;
	}

	/**
	 * Creates a new template exception.
	 *
	 * @param message
	 *            The message of the exception
	 */
	public TemplateException(String message) {
		this(0, 0, message);
	}

	/**
	 * Creates a new template exception.
	 *
	 * @param line
	 *            The line number of the erroneous tag, if any
	 * @param column
	 *            The column number of the erroneous tag, if any
	 * @param message
	 *            The message of the exception
	 */
	public TemplateException(int line, int column, String message) {
		super(message + " (line " + line + ", column " + column + ")");
		this.line = line;
		this.column = column;
	}

	/**
	 * Creates a new template exception.
	 *
	 * @param cause
	 *            The cause of the exception
	 */
	public TemplateException(Throwable cause) {
		this(0, 0, cause);
	}

	/**
	 * Creates a new template exception.
	 *
	 * @param line
	 *            The line number of the erroneous tag, if any
	 * @param column
	 *            The column number of the erroneous tag, if any
	 * @param cause
	 *            The cause of the exception
	 */
	public TemplateException(int line, int column, Throwable cause) {
		super(cause);
		this.line = line;
		this.column = column;
	}

	/**
	 * Creates a new template exception.
	 *
	 * @param message
	 *            The message of the exception
	 * @param cause
	 *            The cause of the exception
	 */
	public TemplateException(String message, Throwable cause) {
		this(0, 0, message, cause);
	}

	/**
	 * Creates a new template exception.
	 *
	 * @param line
	 *            The line number of the erroneous tag, if any
	 * @param column
	 *            The column number of the erroneous tag, if any
	 * @param message
	 *            The message of the exception
	 * @param cause
	 *            The cause of the exception
	 */
	public TemplateException(int line, int column, String message, Throwable cause) {
		super(message + " (line " + line + ", column " + column + ")", cause);
		this.line = line;
		this.column = column;
	}

	//
	// ACCESSORS
	//

	/**
	 * Returns the line number of the erroneous tag, if any. Line numbers start
	 * at {@code 1}.
	 *
	 * @return The line number of the erroneous tag, if any
	 */
	public int getLine() {
		return line;
	}

	/**
	 * Returns the column number of the erroneous tag, if any. Column numbers
	 * start at {@code 1}.
	 *
	 * @return The column number of the erroneous tag, if any
	 */
	public int getColumn() {
		return column;
	}

}
