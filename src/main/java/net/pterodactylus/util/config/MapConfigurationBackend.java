/*
 * utils - MapConfigurationBackend.java - Copyright © 2007-2009 David Roden
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

import java.util.Map;

/**
 * Configuration backend that is backed by a {@link Map}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class MapConfigurationBackend implements ConfigurationBackend {

	/** The backing map. */
	private final Map<String, String> values;

	/**
	 * Creates a new configuration backend that is backed by the given map.
	 *
	 * @param values
	 *            The backing map
	 */
	public MapConfigurationBackend(Map<String, String> values) {
		this.values = values;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see net.pterodactylus.util.config.ConfigurationBackend#getValue(java.lang.String)
	 */
	@Override
	public String getValue(String attribute) {
		return values.get(attribute);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see net.pterodactylus.util.config.ConfigurationBackend#putValue(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public void putValue(String attribute, String value) {
		values.put(attribute, value);
	}

}
