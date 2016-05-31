/*
 * utils - TemplateContext.java - Copyright © 2011–2016 David Roden
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
import java.util.Arrays;
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

	/** The template providers for this context. */
	private final List<TemplateProvider> templateProviders = Collections.synchronizedList(new ArrayList<TemplateProvider>());

	/** The template objects for this context. */
	private final Map<String, Object> objects = Collections.synchronizedMap(new HashMap<String, Object>());

	/** The parent context. */
	private final TemplateContext parentContext;

	/** Whether this context is temporary. */
	private final boolean temporary;

	/** Merged contexts. */
	private final List<TemplateContext> mergedContexts = new ArrayList<TemplateContext>();

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
		this(parentContext, false);
	}

	/**
	 * Creates a new template context with the given parent context.
	 *
	 * @param parentContext
	 *            The parent context
	 */
	public TemplateContext(TemplateContext parentContext, boolean temporary) {
		this.parentContext = parentContext;
		this.temporary = temporary;
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
	 * Adds a template provider to this context.
	 *
	 * @param templateProvider
	 *            The template provider to add
	 * @return This template context
	 * @deprecated Use {@link #addTemplateProvider(TemplateProvider)} instead
	 */
	@Deprecated
	public TemplateContext addProvider(TemplateProvider templateProvider) {
		return addTemplateProvider(templateProvider);
	}

	/**
	 * Adds a template provider to this context.
	 *
	 * @param templateProvider
	 *            The template provider to add
	 * @return This template context
	 */
	public TemplateContext addTemplateProvider(TemplateProvider templateProvider) {
		templateProviders.add(templateProvider);
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
		for (TemplateContext context : getAllTemplateContexts()) {
			Class<?> classToCheck = clazz;
			while ((classToCheck != null) && (accessor == null)) {
				if (context.accessors.containsKey(classToCheck)) {
					accessor = context.accessors.get(classToCheck);
					break;
				}
				List<Class<?>> interfaceClasses = new ArrayList<Class<?>>();
				interfaceClasses.addAll(Arrays.asList(classToCheck.getInterfaces()));
				/*
				 * deliberately using an index, we’re adding to it during the
				 * loop.
				 */
				for (int interfaceIndex = 0; interfaceIndex < interfaceClasses.size(); ++interfaceIndex) {
					if (context.accessors.containsKey(interfaceClasses.get(interfaceIndex))) {
						accessor = context.accessors.get(interfaceClasses.get(interfaceIndex));
						break;
					}
					interfaceClasses.addAll(Arrays.asList(interfaceClasses.get(interfaceIndex).getInterfaces()));
				}
				classToCheck = classToCheck.getSuperclass();
			}
			if (accessor != null) {
				break;
			}
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
		for (TemplateContext context : getAllTemplateContexts()) {
			filter = context.filters.get(name);
			if (filter != null) {
				break;
			}
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
		for (TemplateContext context : getAllTemplateContexts()) {
			plugin = context.plugins.get(name);
			if (plugin != null) {
				break;
			}
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
		if (name == null) {
			return null;
		}
		StringTokenizer nameTokens = new StringTokenizer(name, ".");
		Object object = null;
		while (nameTokens.hasMoreTokens()) {
			String nameToken = nameTokens.nextToken();
			if (object == null) {
				for (TemplateContext context : getAllTemplateContexts()) {
					object = context.objects.get(nameToken);
					if (object != null) {
						break;
					}
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
	 * Returns the object with the given name, if its class is assignable to the
	 * given class. Properties of an object can be accessed by a “.” in the
	 * name. If this context does not have an object with the given name, the
	 * parent context is asked.
	 *
	 * @param <T>
	 *            The requested type of the object
	 * @param name
	 *            The name of the object to get
	 * @param requestedClass
	 *            The requested class
	 * @return The object, or {@code null} if no object could be found, or if
	 *         the real class of the object can not be assigned to the requested
	 *         class
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(String name, Class<?> requestedClass) {
		Object value = get(name);
		if (value == null) {
			return null;
		}
		if (requestedClass.isAssignableFrom(value.getClass())) {
			return (T) value;
		}
		return null;
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
		if (temporary && (parentContext != null)) {
			parentContext.set(name, value);
		}
	}

	/**
	 * Sets the object with the given name to the given value. If {@code
	 * setInParent} is {@code true}, the object is also set in this context’s
	 * parent context, if there is one.
	 *
	 * @param name
	 *            The name of the object
	 * @param value
	 *            The value of the object
	 * @param setInParent
	 *            {@code true} to set the object in the parent context, {@code
	 *            false} otherwise
	 */
	public void set(String name, Object value, boolean setInParent) {
		objects.put(name, value);
		if (temporary && (parentContext != null)) {
			parentContext.set(name, value, setInParent);
		} else if (setInParent && (parentContext != null)) {
			parentContext.set(name, value);
		}
	}

	/**
	 * Returns the template with the given name. If the template providers of
	 * this context can not find a template with the given name, the parent
	 * context is asked.
	 *
	 * @param name
	 *            The name of the template
	 * @return The template, or {@code null} if no template could be found
	 */
	public Template getTemplate(String name) {
		Template template = null;
		for (TemplateContext context : getAllTemplateContexts()) {
			for (TemplateProvider templateProvider : context.templateProviders) {
				template = templateProvider.getTemplate(this, name);
				if (template != null) {
					break;
				}
			}
			if (template != null) {
				break;
			}
		}
		return template;
	}

	/**
	 * Merges the accessors, filters, plugins, template providers, and template
	 * objects into this context.
	 *
	 * @param templateContext
	 *            The context to merge
	 * @return This template context
	 */
	public TemplateContext mergeContext(TemplateContext templateContext) {
		mergedContexts.add(0, templateContext);
		return this;
	}

	//
	// PRIVATE METHODS
	//

	/**
	 * Returns all template context that a request for data (variables,
	 * accessors, plugins, filters) has to visit. This is a combined list of all
	 * merged contexts, their merged contexts (and recursively their merged
	 * contexts), this context, and the parents of this context (and their
	 * merged and parent contexts, and so on).
	 *
	 * @return All template contexts
	 */
	private List<TemplateContext> getAllTemplateContexts() {
		List<TemplateContext> allTemplateContexts = new ArrayList<TemplateContext>();
		for (TemplateContext mergedContext : mergedContexts) {
			allTemplateContexts.addAll(mergedContext.getAllTemplateContexts());
		}
		allTemplateContexts.add(this);
		TemplateContext currentTemplateContext = parentContext;
		while (currentTemplateContext != null) {
			allTemplateContexts.addAll(currentTemplateContext.getAllTemplateContexts());
			currentTemplateContext = currentTemplateContext.parentContext;
		}
		return allTemplateContexts;
	}

}
