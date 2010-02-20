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

	/**
	 * Creates a new template exception.
	 */
	public TemplateException() {
		super();
	}

	/**
	 * Creates a new template exception.
	 *
	 * @param message
	 *            The message of the exception
	 */
	public TemplateException(String message) {
		super(message);

	}

	/**
	 * Creates a new template exception.
	 *
	 * @param cause
	 *            The cause of the exception
	 */
	public TemplateException(Throwable cause) {
		super(cause);

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
		super(message, cause);

	}

}
