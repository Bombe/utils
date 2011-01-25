/*
 * utils - TemplateContext.java - Copyright © 2011 David Roden
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
import java.util.StringTokenizer;

/**
 * A template context bundles together all providers of information that are
 * necessary to render a template.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class TemplateContext {

	/** The accessors for this context. */
	private final Map<Class<?>, Accessor> accessors = Collections.synchronizedMap(new HashMap<Class<?>, Accessor>());

	/** The filters for this context. */
	private final Map<String, Filter> filters = Collections.synchronizedMap(new HashMap<String, Filter>());

	/** The plugins for this context. */
	private final Map<String, Plugin> plugins = Collections.synchronizedMap(new HashMap<String, Plugin>());

	/** The providers for this context. */
	private final List<Provider> providers = Collections.synchronizedList(new ArrayList<Provider>());

	/** The template objects for this context. */
	private final Map<String, Object> objects = Collections.synchronizedMap(new HashMap<String, Object>());

	/** The parent context. */
	private final TemplateContext parentContext;

	/**
	 * Creates a new template context.
	 */
	public TemplateContext() {
		this(null);
	}

	/**
	 * Creates a new template context with the given parent context.
	 *
	 * @param parentContext
	 *            The parent context
	 */
	public TemplateContext(TemplateContext parentContext) {
		this.parentContext = parentContext;
		accessors.put(Map.class, Accessor.MAP_ACCESSOR);
	}

	//
	// ACCESSORS
	//

	/**
	 * Adds an accessor to this context.
	 *
	 * @param clazz
	 *            The class to add the accessor for
	 * @param accessor
	 *            The accessor to add
	 * @return This template context
	 */
	public TemplateContext addAccessor(Class<?> clazz, Accessor accessor) {
		accessors.put(clazz, accessor);
		return this;
	}

	/**
	 * Adds a filter to this context.
	 *
	 * @param name
	 *            The name of the filter to add
	 * @param filter
	 *            The filter to add
	 * @return This template context
	 */
	public TemplateContext addFilter(String name, Filter filter) {
		filters.put(name, filter);
		return this;
	}

	/**
	 * Adds a plugin to this context.
	 *
	 * @param name
	 *            The name of the plugin to add
	 * @param plugin
	 *            The plugin to add
	 * @return This template context
	 */
	public TemplateContext addPlugin(String name, Plugin plugin) {
		plugins.put(name, plugin);
		return this;
	}

	/**
	 * Adds a provider to this context.
	 *
	 * @param provider
	 *            The provider to add
	 * @return This template context
	 */
	public TemplateContext addProvider(Provider provider) {
		providers.add(provider);
		return this;
	}

	//
	// CONTEXT METHODS
	//

	/**
	 * Returns the accessor for the given class. If there is no accessor is
	 * found for the given class, all its interfaces, its superclasses, and
	 * their interfaces are compared to. If no accessor is found, the parent
	 * context is asked.
	 *
	 * @param clazz
	 *            The class to get the accessor for
	 * @return The accessor for the given class, or {@code null} if no accessor
	 *         could be found for the given class
	 */
	public Accessor getAccessor(Class<?> clazz) {
		Accessor accessor = null;
		TemplateContext context = this;
		while ((context != null) && (accessor == null)) {
			Class<?> classToCheck = clazz;
			while ((classToCheck != null) && (accessor == null)) {
				if (context.accessors.containsKey(classToCheck)) {
					accessor = context.accessors.get(classToCheck);
					break;
				}
				for (Class<?> interfaceClass : classToCheck.getInterfaces()) {
					if (context.accessors.containsKey(interfaceClass)) {
						accessor = context.accessors.get(interfaceClass);
						break;
					}
				}
				classToCheck = classToCheck.getSuperclass();
			}
			context = context.parentContext;
		}
		return accessor;
	}

	/**
	 * Returns the filter with the given name. If this context does not have a
	 * filter for the given name, the parent context is asked.
	 *
	 * @param name
	 *            The name of the filter to get
	 * @return The filter, or {@code null} if no filter could be found
	 */
	public Filter getFilter(String name) {
		Filter filter = null;
		TemplateContext context = this;
		while ((context != null) && (filter == null)) {
			filter = context.filters.get(name);
			context = context.parentContext;
		}
		return filter;
	}

	/**
	 * Returns the plugin with the given name. If this context does not have a
	 * plugin for the given name, the parent context is asked.
	 *
	 * @param name
	 *            The name of the plugin to get
	 * @return The plugin, or {@code null} if no plugin could be found
	 */
	public Plugin getPlugin(String name) {
		Plugin plugin = null;
		TemplateContext context = this;
		while ((context != null) && (plugin == null)) {
			plugin = context.plugins.get(name);
			context = context.parentContext;
		}
		return plugin;
	}

	/**
	 * Returns the object with the given name. Properties of an object can be
	 * accessed by a “.” in the name. If this context does not have an object
	 * with the given name, the parent context is asked.
	 *
	 * @param name
	 *            The name of the object to get
	 * @return The object, or {@code null} if no object could be found
	 */
	public Object get(String name) {
		StringTokenizer nameTokens = new StringTokenizer(name, ".");
		Object object = null;
		while (nameTokens.hasMoreTokens()) {
			String nameToken = nameTokens.nextToken();
			if (object == null) {
				TemplateContext context = this;
				while ((context != null) && (object == null)) {
					object = context.objects.get(nameToken);
					context = context.parentContext;
				}
			} else {
				Accessor accessor = getAccessor(object.getClass());
				if (accessor != null) {
					object = accessor.get(this, object, nameToken);
				} else {
					throw new TemplateException(0, 0, "no accessor found for " + object.getClass());
				}
			}
			if (object == null) {
				return null;
			}
		}
		return object;
	}

	/**
	 * Sets the object with the given name to the given value.
	 *
	 * @param name
	 *            The name of the object
	 * @param value
	 *            The value of the object
	 */
	public void set(String name, Object value) {
		objects.put(name, value);
	}

	/**
	 * Returns the template with the given name. If the providers of this
	 * context can not find a template with the given name, the parent context
	 * is asked.
	 *
	 * @param name
	 *            The name of the template
	 * @return The template, or {@code null} if no template could be found
	 */
	public Template getTemplate(String name) {
		Template template = null;
		TemplateContext context = this;
		while ((context != null) && (template == null)) {
			for (Provider provider : context.providers) {
				template = provider.getTemplate(this, name);
				if (template != null) {
					break;
				}
			}
			context = context.parentContext;
		}
		return template;
	}

	/**
	 * Merges the accessors, filters, plugins, providers, and template objects
	 * into this context.
	 *
	 * @param templateContext
	 *            The context to merge
	 * @return This template context
	 */
	public TemplateContext mergeContext(TemplateContext templateContext) {
		accessors.putAll(templateContext.accessors);
		filters.putAll(templateContext.filters);
		plugins.putAll(templateContext.plugins);
		providers.addAll(templateContext.providers);
		objects.putAll(templateContext.objects);
		return this;
	}

}
