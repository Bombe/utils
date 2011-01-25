/*
 * utils - DefaultTemplateFactory.java - Copyright © 2010 David Roden
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Default {@link TemplateContext} factory implementation that creates
 * {@link TemplateContext}s with {@link Accessor}s, {@link Filter}s,
 * {@link Plugin}s, {@link Provider}s, and arbitrary objects that are set on all
 * created {@link TemplateContext}s.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class TemplateContextFactory {

	/** The default instance. */
	private static TemplateContextFactory instance;

	/** Filters that will be added to all created templates. */
	private final Map<String, Filter> filters = Collections.synchronizedMap(new HashMap<String, Filter>());

	/** Plugins that will be added to all created templates. */
	private final Map<String, Plugin> plugins = Collections.synchronizedMap(new HashMap<String, Plugin>());

	/** Accessors that will be added to all created templates. */
	private final Map<Class<?>, Accessor> accessors = Collections.synchronizedMap(new HashMap<Class<?>, Accessor>());

	/** The provider for all created templates. */
	private List<Provider> providers = Collections.synchronizedList(new ArrayList<Provider>());

	/** Additional objects to set in all templates. */
	private final Map<String, Object> templateObjects = Collections.synchronizedMap(new HashMap<String, Object>());

	/**
	 * Adds an accessor that will be added to all created templates.
	 *
	 * @param clazz
	 *            The class to add the accessor for
	 * @param accessor
	 *            The accessor to add
	 */
	public void addAccessor(Class<?> clazz, Accessor accessor) {
		accessors.put(clazz, accessor);
	}

	/**
	 * Adds the given filter to all created templates.
	 *
	 * @param name
	 *            The name of the filter
	 * @param filter
	 *            The filter to add
	 */
	public void addFilter(String name, Filter filter) {
		filters.put(name, filter);
	}

	/**
	 * Adds the given plugin to all created templates.
	 *
	 * @param name
	 *            The name of the plugin
	 * @param plugin
	 *            The plugin to add
	 */
	public void addPlugin(String name, Plugin plugin) {
		plugins.put(name, plugin);
	}

	/**
	 * Sets the provider that is set on all created templates.
	 *
	 * @param provider
	 *            The provider to set
	 */
	public void addProvider(Provider provider) {
		providers.add(provider);
	}

	/**
	 * Adds an object that will be stored in all created templates.
	 *
	 * @param name
	 *            The name of the template variable
	 * @param object
	 *            The object to store
	 */
	public void addTemplateObject(String name, Object object) {
		templateObjects.put(name, object);
	}

	/**
	 * Returns the static default instance of this template factory.
	 *
	 * @return The default template factory
	 */
	public synchronized static TemplateContextFactory getInstance() {
		if (instance == null) {
			instance = new TemplateContextFactory();
		}
		return instance;
	}

	/**
	 * Creates a new template context that contains the accessors, filters,
	 * plugins, providers, and objects set in this factory.
	 *
	 * @return The new context
	 */
	public TemplateContext createTemplateContext() {
		TemplateContext templateContext = new TemplateContext();
		for (Entry<String, Filter> filterEntry : filters.entrySet()) {
			templateContext.addFilter(filterEntry.getKey(), filterEntry.getValue());
		}
		for (Entry<String, Plugin> pluginEntry : plugins.entrySet()) {
			templateContext.addPlugin(pluginEntry.getKey(), pluginEntry.getValue());
		}
		for (Entry<Class<?>, Accessor> accessorEntry : accessors.entrySet()) {
			templateContext.addAccessor(accessorEntry.getKey(), accessorEntry.getValue());
		}
		for (Provider provider : providers) {
			templateContext.addProvider(provider);
		}
		for (Entry<String, Object> objectEntry : templateObjects.entrySet()) {
			templateContext.set(objectEntry.getKey(), objectEntry.getValue());
		}
		return templateContext;
	}

}
