/*
 * utils - LongValue.java - Copyright © 2007-2009 David Roden
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
 * A wrapper around a long value.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class LongValue extends AbstractValue<Long> {

	/**
	 * Creates a new long value that updates its value to the given attribute
	 * from the given configuration.
	 *
	 * @param configuration
	 *            The configuration that created this value
	 * @param attribute
	 *            The name of this value’s attribute name
	 */
	public LongValue(Configuration configuration, String attribute) {
		super(configuration, attribute);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see net.pterodactylus.util.config.Value#getValue()
	 */
	public Long getValue() throws ConfigurationException {
		String value = null;
		try {
			value = configuration.configurationBackend.getValue(attribute);
			long longValue = Long.valueOf(value);
			return longValue;
		} catch (NumberFormatException nfe1) {
			throw new ValueFormatException("could not parse attribute \"" + value + "\".", nfe1);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see net.pterodactylus.util.config.Value#getValue(java.lang.Object)
	 */
	public Long getValue(Long defaultValue) {
		String value = null;
		try {
			value = configuration.configurationBackend.getValue(attribute);
			long longValue = Long.valueOf(value);
			return longValue;
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
	public void setValue(Long newValue) throws ConfigurationException {
		configuration.configurationBackend.putValue(attribute, String.valueOf(newValue));
	}

}
