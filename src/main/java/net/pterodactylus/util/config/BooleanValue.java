/*
 * utils - BooleanValue.java - Copyright © 2007-2009 David Roden
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
 * A wrapper around a boolean value.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class BooleanValue extends AbstractValue<Boolean> {

	/**
	 * Creates a new boolean value that updates its value to the given attribute
	 * from the given configuration.
	 *
	 * @param configuration
	 *            The configuration that created this value
	 * @param attribute
	 *            The name of this value’s attribute name
	 */
	public BooleanValue(Configuration configuration, String attribute) {
		super(configuration, attribute);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see net.pterodactylus.util.config.Value#getValue()
	 */
	@Override
	public Boolean getValue() throws ConfigurationException {
		if (configuration.configurationBackend instanceof ExtendedConfigurationBackend) {
			return ((ExtendedConfigurationBackend) configuration.configurationBackend).getBooleanValue(attribute);
		}
		String value = configuration.configurationBackend.getValue(attribute);
		return Boolean.valueOf(("true".equalsIgnoreCase(value)) || ("yes".equalsIgnoreCase(value)) || ("1".equalsIgnoreCase(value)) || ("on".equalsIgnoreCase(value)));
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see net.pterodactylus.util.config.Value#getValue(java.lang.Object)
	 */
	@Override
	public Boolean getValue(Boolean defaultValue) {
		try {
			if (configuration.configurationBackend instanceof ExtendedConfigurationBackend) {
				return ((ExtendedConfigurationBackend) configuration.configurationBackend).getBooleanValue(attribute);
			}
			String value = configuration.configurationBackend.getValue(attribute);
			return Boolean.valueOf(("true".equalsIgnoreCase(value)) || ("yes".equalsIgnoreCase(value)) || ("1".equalsIgnoreCase(value)) || ("on".equalsIgnoreCase(value)));
		} catch (ConfigurationException ce1) {
			return defaultValue;
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see net.pterodactylus.util.config.Value#setValue(java.lang.Object)
	 */
	@Override
	public void setValue(Boolean newValue) throws ConfigurationException {
		if (configuration.configurationBackend instanceof ExtendedConfigurationBackend) {
			((ExtendedConfigurationBackend) configuration.configurationBackend).setBooleanValue(attribute, newValue);
		}
		configuration.configurationBackend.putValue(attribute, (newValue != null) ? String.valueOf(newValue) : null);
	}

}
