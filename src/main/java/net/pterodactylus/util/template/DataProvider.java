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

	/** The accessor bundle. */
	private final AccessorLocator accessorLocator;

	/** The template provider. */
	private volatile TemplateProvider templateProvider;

	/**
	 * Creates a new data provider.
	 *
	 * @param accessorLocator
	 *            The accessor locator
	 */
	public DataProvider(AccessorLocator accessorLocator) {
		this(accessorLocator, new DataStore.MapDataStore(), new DataTemplateProvider());
	}

	/**
	 * Creates a new data provider using the given data store as backend.
	 *
	 * @param accessorLocator
	 *            The accessor locator
	 * @param dataStore
	 *            The data store
	 * @param templateProvider
	 *            The template provider
	 */
	public DataProvider(AccessorLocator accessorLocator, DataStore dataStore, TemplateProvider templateProvider) {
		this.dataStore = dataStore;
		this.accessorLocator = accessorLocator;
		this.templateProvider = templateProvider;
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
	 * Returns the accessor locator used by this data provider.
	 *
	 * @return The accessor locator
	 */
	protected AccessorLocator getAccessorLocator() {
		return accessorLocator;
	}

	/**
	 * Returns the current template provider.
	 *
	 * @return The current template provider
	 */
	protected TemplateProvider getTemplateProvider() {
		return templateProvider;
	}

	/**
	 * Sets the new template provider.
	 *
	 * @param templateProvider
	 *            The new template provider
	 */
	public void setTemplateProvider(TemplateProvider templateProvider) {
		this.templateProvider = templateProvider;
	}

	/**
	 * Retrieves the template with the given name from the
	 * {@link #getTemplateProvider() current template provider} and returns it.
	 *
	 * @param templateName
	 *            The name of the template
	 * @return The retrieved template, or {@code null} if a template could not
	 *         be found
	 */
	public Part getTemplate(String templateName) {
		if (templateProvider != null) {
			Part template = templateProvider.getTemplate(this, templateName);
			return template;
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
	public Object get(String name) throws TemplateException {
		return getData(name);
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
	 * @deprecated Use {@link #get(String)} instead
	 */
	@Deprecated
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
				Accessor accessor = accessorLocator.findAccessor(object.getClass());
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
	public void set(String name, Object data) {
		setData(name, data);
	}

	/**
	 * Sets data in this data provider.
	 *
	 * @param name
	 *            The key under which to store the data
	 * @param data
	 *            The data to store
	 * @deprecated Use {@link #set(String, Object)} instead
	 */
	@Deprecated
	public void setData(String name, Object data) {
		getDataStore().set(name, data);
	}

}
