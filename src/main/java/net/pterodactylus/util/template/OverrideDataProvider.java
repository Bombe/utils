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
class OverrideDataProvider extends DataProvider {

	/** The parent data provider. */
	private final DataProvider parentDataProvider;

	/** Accessors. */
	private final Map<Class<?>, Accessor> classAccessors = new HashMap<Class<?>, Accessor>();

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
		super(new OverrideDataStore(parentDataProvider, name, object));
		this.parentDataProvider = parentDataProvider;
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
		super(new OverrideDataStore(parentDataProvider, overrideObjects));
		this.parentDataProvider = parentDataProvider;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addAccessor(Class<?> clazz, Accessor accessor) {
		classAccessors.put(clazz, accessor);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Accessor findAccessor(Class<?> clazz) {
		if (classAccessors.containsKey(clazz)) {
			return classAccessors.get(clazz);
		}
		for (Class<?> interfaceClass : clazz.getInterfaces()) {
			if (classAccessors.containsKey(interfaceClass)) {
				return classAccessors.get(interfaceClass);
			}
		}
		Class<?> classToCheck = clazz.getSuperclass();
		while (classToCheck != null) {
			if (classAccessors.containsKey(classToCheck)) {
				return classAccessors.get(classToCheck);
			}
			classToCheck = classToCheck.getSuperclass();
		}
		return parentDataProvider.findAccessor(clazz);
	}

	/**
	 * {@link DataStore} implementation that can override objects and redirects
	 * requests for not-overridden objects to a parent {@link DataProvider}.
	 *
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	private static class OverrideDataStore implements DataStore {

		/** The parent data provider. */
		private final DataProvider parentDataProvider;

		/** The store containing the overridden objects. */
		private final Map<String, Object> overrideDataStore = new HashMap<String, Object>();

		/**
		 * Creates a new overriding data store.
		 *
		 * @param parentDataProvider
		 *            The parent data provider
		 * @param name
		 *            The key of the object to override
		 * @param data
		 *            The object to override
		 */
		public OverrideDataStore(DataProvider parentDataProvider, String name, Object data) {
			this.parentDataProvider = parentDataProvider;
			overrideDataStore.put(name, data);
		}

		/**
		 * Creates a new overriding data store.
		 *
		 * @param parentDataProvider
		 *            The parent data provider
		 * @param overrideDataStore
		 *            {@link Map} containing all overriding objects
		 */
		public OverrideDataStore(DataProvider parentDataProvider, Map<String, Object> overrideDataStore) {
			this.parentDataProvider = parentDataProvider;
			this.overrideDataStore.putAll(overrideDataStore);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Object get(String name) {
			if (overrideDataStore.containsKey(name)) {
				return overrideDataStore.get(name);
			}
			return parentDataProvider.getData(name);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void set(String name, Object data) {
			parentDataProvider.setData(name, data);
		}

	}

}
