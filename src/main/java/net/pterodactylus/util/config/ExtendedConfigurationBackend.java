/*
 * utils - ExtendedConfigurationBackend.java - Copyright © 2010 David Roden
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
 * The extended configuration backend allows a backend to specify ways to
 * retrieve other types than strings. Defined in this backend are all types are
 * used in {@link Configuration}, too: {@link Boolean}, {@link Double},
 * {@link Integer}, {@link Long}, and {@link String}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public interface ExtendedConfigurationBackend extends ConfigurationBackend {

	/**
	 * Returns the value of the given attribute from the backend.
	 *
	 * @param attribute
	 *            The name of the attribute
	 * @return The stored value
	 * @throws ConfigurationException
	 *             if the attribute could not be found
	 */
	public Boolean getBooleanValue(String attribute) throws ConfigurationException;

	/**
	 * Sets the value of the given attribute within the backend.
	 *
	 * @param attribute
	 *            The name of the attribute to set
	 * @param value
	 *            The value to store
	 * @throws ConfigurationException
	 *             if the value could not be set
	 */
	public void setBooleanValue(String attribute, Boolean value) throws ConfigurationException;

	/**
	 * Returns the value of the given attribute from the backend.
	 *
	 * @param attribute
	 *            The name of the attribute
	 * @return The stored value
	 * @throws ConfigurationException
	 *             if the attribute could not be found
	 */
	public Double getDoubleValue(String attribute) throws ConfigurationException;

	/**
	 * Sets the value of the given attribute within the backend.
	 *
	 * @param attribute
	 *            The name of the attribute to set
	 * @param value
	 *            The value to store
	 * @throws ConfigurationException
	 *             if the value could not be set
	 */
	public void setDoubleValue(String attribute, Double value) throws ConfigurationException;

	/**
	 * Returns the value of the given attribute from the backend.
	 *
	 * @param attribute
	 *            The name of the attribute
	 * @return The stored value
	 * @throws ConfigurationException
	 *             if the attribute could not be found
	 */
	public Integer getIntegerValue(String attribute) throws ConfigurationException;

	/**
	 * Sets the value of the given attribute within the backend.
	 *
	 * @param attribute
	 *            The name of the attribute to set
	 * @param value
	 *            The value to store
	 * @throws ConfigurationException
	 *             if the value could not be set
	 */
	public void setIntegerValue(String attribute, Integer value) throws ConfigurationException;

	/**
	 * Returns the value of the given attribute from the backend.
	 *
	 * @param attribute
	 *            The name of the attribute
	 * @return The stored value
	 * @throws ConfigurationException
	 *             if the attribute could not be found
	 */
	public Long getLongValue(String attribute) throws ConfigurationException;

	/**
	 * Sets the value of the given attribute within the backend.
	 *
	 * @param attribute
	 *            The name of the attribute to set
	 * @param value
	 *            The value to store
	 * @throws ConfigurationException
	 *             if the value could not be set
	 */
	public void setLongValue(String attribute, Long value) throws ConfigurationException;

}
