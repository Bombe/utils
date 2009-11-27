/*
 * utils - Value.java - Copyright © 2007-2009 David Roden
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
 * Wrapper for a configuration value. Returning this wrapper instead of the
 * integral types allows the value to be updated (e.g. when the configuration
 * file changes) or written back to the underlying configuraiton backend (which
 * might in turn support persisting itself).
 *
 * @param <T>
 *            The type of the wrapped data
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public interface Value<T> {

	/**
	 * Returns the wrapped value.
	 *
	 * @return The wrapped value
	 * @throws ConfigurationException
	 *             if a configuration error occurs
	 */
	public T getValue() throws ConfigurationException;

	/**
	 * Returns the wrapped value, if possible. If the value can not be retrieved
	 * or parsed, the default value is returned instead.
	 *
	 * @param defaultValue
	 *            The default value to return in case of an error
	 * @return The wrapped value, or the default value if the wrapped value can
	 *         not be retrieved or parsed
	 */
	public T getValue(T defaultValue);

	/**
	 * Sets a new wrapped value.
	 *
	 * @param newValue
	 *            The new wrapped value
	 * @throws ConfigurationException
	 *             if a configuration error occurs
	 */
	public void setValue(T newValue) throws ConfigurationException;

}
