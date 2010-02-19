/*
 * utils - OverrideDataProvider.java - Copyright © 2010 David Roden
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

package net.pterodactylus.util.template;

import java.util.HashMap;
import java.util.Map;

/**
 * {@link DataProvider} implementation that uses a parent data provider but can
 * override objects.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
class OverrideDataProvider implements DataProvider {

	/** The parent data provider. */
	private final DataProvider parentDataProvider;

	/** Map with override objects. */
	private final Map<String, Object> overrideObjects = new HashMap<String, Object>();

	/**
	 * Creates a new override data provider.
	 *
	 * @param parentDataProvider
	 *            The parent data provider
	 * @param name
	 *            The name of the object to override
	 * @param object
	 *            The object
	 */
	public OverrideDataProvider(DataProvider parentDataProvider, String name, Object object) {
		this.parentDataProvider = parentDataProvider;
		overrideObjects.put(name, object);
	}

	/**
	 * Creates a new override data provider.
	 *
	 * @param parentDataProvider
	 *            The parent data provider
	 * @param overrideObjects
	 *            The override objects
	 */
	public OverrideDataProvider(DataProvider parentDataProvider, Map<String, Object> overrideObjects) {
		this.parentDataProvider = parentDataProvider;
		this.overrideObjects.putAll(overrideObjects);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getData(String name) {
		if (overrideObjects.containsKey(name)) {
			return overrideObjects.get(name);
		}
		return parentDataProvider.getData(name);
	}

}
