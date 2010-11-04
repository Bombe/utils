/*
 * utils - DataProvider.java - Copyright © 2010 David Roden
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
import java.util.StringTokenizer;

/**
 * Interface for objects that need to supply data to a {@link Template}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class DataProvider {

	/** Object store. */
	private final DataStore dataStore;

	/** Accessors. */
	private final Map<Class<?>, Accessor> classAccessors = new HashMap<Class<?>, Accessor>();

	/**
	 * Creates a new data provider.
	 */
	public DataProvider() {
		this(new DataStore.MapDataStore());
	}

	/**
	 * Creates a new data provider using the given data store as backend.
	 *
	 * @param dataStore
	 *            The data store
	 */
	public DataProvider(DataStore dataStore) {
		this.dataStore = dataStore;
		classAccessors.put(Map.class, Accessor.MAP_ACCESSOR);
	}

	/**
	 * Returns the data provider’s data store.
	 *
	 * @return The data store
	 */
	protected DataStore getDataStore() {
		return dataStore;
	}

	/**
	 * Adds an accessor for objects of the given class.
	 *
	 * @param clazz
	 *            The class of the objects to handle with the accessor
	 * @param accessor
	 *            The accessor to handle the objects with
	 */
	public void addAccessor(Class<?> clazz, Accessor accessor) {
		classAccessors.put(clazz, accessor);
	}

	/**
	 * Finds an accessor that can handle the given class. If
	 * {@link #classAccessors} does not contain a perfect match, a match to a
	 * superclass or superinterface is searched.
	 *
	 * @param clazz
	 *            The class to get an accessor for
	 * @return The accessor for the given class, or {@code null} if no accessor
	 *         could be found
	 */
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
			for (Class<?> interfaceClass : classToCheck.getInterfaces()) {
				if (classAccessors.containsKey(interfaceClass)) {
					return classAccessors.get(interfaceClass);
				}
			}
			classToCheck = classToCheck.getSuperclass();
		}
		return null;
	}

	/**
	 * Returns the object stored under the given name. The name can contain
	 * hierarchical structures separated by a dot (“.”), such as “loop.count” in
	 * which case a {@link Map} must be stored under “loop”.
	 *
	 * @param name
	 *            The name of the object to get
	 * @return The object
	 * @throws TemplateException
	 *             if the name or some objects can not be parsed or evaluated
	 */
	public Object getData(String name) throws TemplateException {
		if (name.indexOf('.') == -1) {
			return getDataStore().get(name);
		}
		StringTokenizer nameTokens = new StringTokenizer(name, ".");
		Object object = null;
		while (nameTokens.hasMoreTokens()) {
			String nameToken = nameTokens.nextToken();
			if (object == null) {
				object = getDataStore().get(nameToken);
			} else {
				Accessor accessor = findAccessor(object.getClass());
				if (accessor != null) {
					object = accessor.get(this, object, nameToken);
				} else {
					throw new TemplateException("no accessor found for " + object.getClass());
				}
			}
			if (object == null) {
				return null;
			}
		}
		return object;
	}

	/**
	 * Sets data in this data provider.
	 *
	 * @param name
	 *            The key under which to store the data
	 * @param data
	 *            The data to store
	 */
	public void setData(String name, Object data) {
		getDataStore().set(name, data);
	}

}
