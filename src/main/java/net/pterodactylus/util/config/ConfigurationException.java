/*
 * utils - ConfigurationException.java - Copyright © 2007–2016 David Roden
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

package net.pterodactylus.util.config;

/**
 * Parent exception for all exceptions that can occur during configuration file
 * parsing.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ConfigurationException extends Exception {

	/**
	 * Constructs a new configuration exception.
	 */
	public ConfigurationException() {
		super();
	}

	/**
	 * Constructs a new configuration exception with the specified message
	 *
	 * @param message
	 *            The message of the exception
	 */
	public ConfigurationException(String message) {
		super(message);
	}

	/**
	 * Constructs a new configuration exception with the specified message and
	 * cause.
	 *
	 * @param message
	 *            The message of the exception
	 * @param cause
	 *            The cause of the exception
	 */
	public ConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a new configuration exception with the specified cause.
	 *
	 * @param cause
	 *            The cause of the exception
	 */
	public ConfigurationException(Throwable cause) {
		super(cause);
	}

}
