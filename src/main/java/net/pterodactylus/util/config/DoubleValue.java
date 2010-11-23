/*
 * utils - DoubleValue.java - Copyright © 2007-2009 David Roden
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
 * A wrapper around a double value.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class DoubleValue extends AbstractValue<Double> {

	/**
	 * Creates a new double value that updates its value to the given attribute
	 * from the given configuration.
	 *
	 * @param configuration
	 *            The configuration that created this value
	 * @param attribute
	 *            The name of this value’s attribute name
	 */
	public DoubleValue(Configuration configuration, String attribute) {
		super(configuration, attribute);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see net.pterodactylus.util.config.Value#getValue()
	 */
	@Override
	public Double getValue() throws ConfigurationException {
		if (configuration.configurationBackend instanceof ExtendedConfigurationBackend) {
			return ((ExtendedConfigurationBackend) configuration.configurationBackend).getDoubleValue(attribute);
		}
		String value = null;
		try {
			value = configuration.configurationBackend.getValue(attribute);
			double doubleValue = Double.valueOf(value);
			return doubleValue;
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
	public Double getValue(Double defaultValue) {
		String value = null;
		try {
			if (configuration.configurationBackend instanceof ExtendedConfigurationBackend) {
				return ((ExtendedConfigurationBackend) configuration.configurationBackend).getDoubleValue(attribute);
			}
			value = configuration.configurationBackend.getValue(attribute);
			double doubleValue = Double.valueOf(value);
			return doubleValue;
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
	public void setValue(Double newValue) throws ConfigurationException {
		if (configuration.configurationBackend instanceof ExtendedConfigurationBackend) {
			((ExtendedConfigurationBackend) configuration.configurationBackend).setDoubleValue(attribute, newValue);
		}
		configuration.configurationBackend.putValue(attribute, (newValue != null) ? String.valueOf(newValue) : null);
	}

}
