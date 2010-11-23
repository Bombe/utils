/*
 * utils - Configuration.java - Copyright © 2007-2009 David Roden
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

import java.util.HashMap;
import java.util.Map;

/**
 * A configuration contains all necessary methods to read integral data types
 * from a {@link ConfigurationBackend}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Configuration {

	/** The backend backing this configuration. */
	final ConfigurationBackend configurationBackend;

	/** The cache for boolean values. */
	private final Map<String, BooleanValue> booleanCache = new HashMap<String, BooleanValue>();

	/** The cache for double values. */
	private final Map<String, DoubleValue> doubleCache = new HashMap<String, DoubleValue>();

	/** The cache for string values. */
	private final Map<String, StringValue> stringCache = new HashMap<String, StringValue>();

	/** The cache for integer values. */
	private final Map<String, IntegerValue> integerCache = new HashMap<String, IntegerValue>();

	/** The cache for long values. */
	private final Map<String, LongValue> longCache = new HashMap<String, LongValue>();

	/**
	 * Creates a new configuration that operates on the given backend.
	 *
	 * @param configurationBackend
	 *            The backend backing this configuration
	 */
	public Configuration(ConfigurationBackend configurationBackend) {
		this.configurationBackend = configurationBackend;
	}

	/**
	 * Returns the boolean value stored at the given attribute.
	 *
	 * @param attribute
	 *            The name of the attribute
	 * @return The boolean value stored at the given attribute
	 */
	public Value<Boolean> getBooleanValue(String attribute) {
		BooleanValue booleanValue = booleanCache.get(attribute);
		if (booleanValue == null) {
			booleanValue = new BooleanValue(this, attribute);
			booleanCache.put(attribute, booleanValue);
		}
		return booleanValue;
	}

	/**
	 * Returns the double value stored at the given attribute.
	 *
	 * @param attribute
	 *            The name of the attribute
	 * @return The double value stored at the given attribute
	 */
	public Value<Double> getDoubleValue(String attribute) {
		DoubleValue doubleValue = doubleCache.get(attribute);
		if (doubleValue == null) {
			doubleValue = new DoubleValue(this, attribute);
			doubleCache.put(attribute, doubleValue);
		}
		return doubleValue;
	}

	/**
	 * Returns the integer value stored at the given attribute.
	 *
	 * @param attribute
	 *            The name of the attribute
	 * @return The integer value stored at the given attribute
	 */
	public Value<Integer> getIntValue(String attribute) {
		IntegerValue integerValue = integerCache.get(attribute);
		if (integerValue == null) {
			integerValue = new IntegerValue(this, attribute);
			integerCache.put(attribute, integerValue);
		}
		return integerValue;
	}

	/**
	 * Returns the long value stored at the given attribute.
	 *
	 * @param attribute
	 *            The name of the attribute
	 * @return The long value stored at the given attribute
	 */
	public Value<Long> getLongValue(String attribute) {
		LongValue longValue = longCache.get(attribute);
		if (longValue == null) {
			longValue = new LongValue(this, attribute);
			longCache.put(attribute, longValue);
		}
		return longValue;
	}

	/**
	 * Returns the string value stored at the given attribute.
	 *
	 * @param attribute
	 *            The name of the attribute
	 * @return The double value stored at the given attribute
	 */
	public Value<String> getStringValue(String attribute) {
		StringValue stringValue = stringCache.get(attribute);
		if (stringValue == null) {
			stringValue = new StringValue(this, attribute);
			stringCache.put(attribute, stringValue);
		}
		return stringValue;
	}

	/**
	 * Saves the configuration. This request is usually forwarded to the
	 * {@link ConfigurationBackend}.
	 *
	 * @see ConfigurationBackend#save()
	 * @throws ConfigurationException
	 *             if the configuration can not be saved
	 */
	public void save() throws ConfigurationException {
		configurationBackend.save();
	}

}
