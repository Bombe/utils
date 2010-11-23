/*
 * utils - IntegerValue.java - Copyright © 2007-2009 David Roden
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
 * A wrapper around an integer value.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class IntegerValue extends AbstractValue<Integer> {

	/**
	 * Creates a new integer value that updates its value to the given attribute
	 * from the given configuration.
	 *
	 * @param configuration
	 *            The configuration that created this value
	 * @param attribute
	 *            The name of this value’s attribute name
	 */
	public IntegerValue(Configuration configuration, String attribute) {
		super(configuration, attribute);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see net.pterodactylus.util.config.Value#getValue()
	 */
	@Override
	public Integer getValue() throws ConfigurationException {
		if (configuration.configurationBackend instanceof ExtendedConfigurationBackend) {
			return ((ExtendedConfigurationBackend) configuration.configurationBackend).getIntegerValue(attribute);
		}
		String value = null;
		try {
			value = configuration.configurationBackend.getValue(attribute);
			int integerValue = Integer.valueOf(value);
			return integerValue;
		} catch (NumberFormatException nfe1) {
			throw new ValueFormatException("could not parse attribute \"" + value + "\".", nfe1);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see net.pterodactylus.util.config.Value#getValue(java.lang.Object)
	 */
	@Override
	public Integer getValue(Integer defaultValue) {
		String value = null;
		try {
			if (configuration.configurationBackend instanceof ExtendedConfigurationBackend) {
				return ((ExtendedConfigurationBackend) configuration.configurationBackend).getIntegerValue(attribute);
			}
			value = configuration.configurationBackend.getValue(attribute);
			int integerValue = Integer.valueOf(value);
			return integerValue;
		} catch (NumberFormatException nfe1) {
			return defaultValue;
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
	public void setValue(Integer newValue) throws ConfigurationException {
		if (configuration.configurationBackend instanceof ExtendedConfigurationBackend) {
			((ExtendedConfigurationBackend) configuration.configurationBackend).setIntegerValue(attribute, newValue);
		}
		configuration.configurationBackend.putValue(attribute, (newValue != null) ? String.valueOf(newValue) : null);
	}

}
