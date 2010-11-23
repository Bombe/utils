/*
 * utils - CachingConfigurationBackend.java - Copyright © 2007-2009 David Roden
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
 * Configuration backend proxy that caches the attribute values it retrieves.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class CachingConfigurationBackend implements ConfigurationBackend {

	/** The real configuration backend. */
	private final ConfigurationBackend realConfigurationBackend;

	/** The cache for the attribute values. */
	private final Map<String, String> attributeCache = new HashMap<String, String>();

	/**
	 * Creates a new caching configuration backend that works as a proxy for the
	 * specified configuration backend and caches all values it retrieves.
	 *
	 * @param realConfigurationBackend
	 *            The configuration backend to proxy
	 */
	public CachingConfigurationBackend(ConfigurationBackend realConfigurationBackend) {
		this.realConfigurationBackend = realConfigurationBackend;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see net.pterodactylus.util.config.ConfigurationBackend#getValue(java.lang.String)
	 */
	@Override
	public synchronized String getValue(String attribute) throws ConfigurationException {
		if (attributeCache.containsKey(attribute)) {
			return attributeCache.get(attribute);
		}
		String value = realConfigurationBackend.getValue(attribute);
		attributeCache.put(attribute, value);
		return value;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see net.pterodactylus.util.config.ConfigurationBackend#putValue(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public synchronized void putValue(String attribute, String value) throws ConfigurationException {
		attributeCache.put(attribute, value);
		realConfigurationBackend.putValue(attribute, value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void save() throws ConfigurationException {
		realConfigurationBackend.save();
	}

	/**
	 * Clears the current cache, causing the all further lookups to be repeated.
	 */
	public synchronized void clear() {
		attributeCache.clear();
	}

}
