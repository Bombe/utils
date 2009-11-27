/*
 * utils - AttributeNotFoundException.java - Copyright © 2007-2009 David Roden
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
 * Exception that will be thrown when a non-existing attribute is requested.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class AttributeNotFoundException extends ConfigurationException {

	/** The requested, non-existing attribute. */
	private final String attribute;

	/**
	 * Constructs a new exception.
	 */
	public AttributeNotFoundException() {
		super();
		attribute = null;
	}

	/**
	 * Constructs a new exception with the specified message.
	 *
	 * @param attribute
	 *            The name of the attribute that could not be found
	 */
	public AttributeNotFoundException(String attribute) {
		super("attribute not found: " + attribute);
		this.attribute = attribute;
	}

	/**
	 * Returns the requested, non-existing attribute's name.
	 *
	 * @return The name of the attribute that does not exist
	 */
	public String getAttribute() {
		return attribute;
	}

}
