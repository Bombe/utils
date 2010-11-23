/*
 * utils - ConfigurationBackend.java - Copyright © 2007-2009 David Roden
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
 * Interface for backends that can read and (optionally) write values at given
 * attribute names.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public interface ConfigurationBackend {

	/**
	 * Returns the value of the given attribute from the backend.
	 *
	 * @param attribute
	 *            The name of the attribute
	 * @return The string representation of the value
	 * @throws ConfigurationException
	 *             if the attribute could not be found
	 */
	public String getValue(String attribute) throws ConfigurationException;

	/**
	 * Sets the value of the given attribute within the backend.
	 *
	 * @param attribute
	 *            The name of the attribute to set
	 * @param value
	 *            The string representation of the value
	 * @throws ConfigurationException
	 *             if the value could not be set
	 */
	public void putValue(String attribute, String value) throws ConfigurationException;

	/**
	 * Saves the configuration.
	 *
	 * @throws ConfigurationException
	 *             if the configuration could not be saved
	 */
	public void save() throws ConfigurationException;

}
