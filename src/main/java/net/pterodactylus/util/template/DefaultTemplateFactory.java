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

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Default {@link TemplateFactory} implementation that creates {@link Template}s
 * with {@link HtmlFilter}s and {@link ReplaceFilter}s added.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class DefaultTemplateFactory implements TemplateFactory {

	/** The default instance. */
	private static DefaultTemplateFactory instance;

	/** Filters that will be added to all created templates. */
	private final Map<String, Filter> filters = new HashMap<String, Filter>();

	/** Plugins that will be added to all created templates. */
	private final Map<String, Plugin> plugins = new HashMap<String, Plugin>();

	/** Accessors that will be added to all created templates. */
	private final Map<Class<?>, Accessor> accessors = new HashMap<Class<?>, Accessor>();

	/** The template provider for all created templates. */
	private TemplateProvider templateProvider;

	/** Additional objects to set in all templates. */
	private final Map<String, Object> templateObjects = new HashMap<String, Object>();

	/**
	 * Creates a new default template factory that adds both an
	 * {@link HtmlFilter} and a {@link ReplaceFilter} to created templates.
	 */
	public DefaultTemplateFactory() {
		this(true, true);
	}

	/**
	 * Creates a new default template factory.
	 *
	 * @param addHtmlFilter
	 *            {@code true} to add an {@link HtmlFilter} to created
	 *            templates, {@code false} otherwise
	 * @param addReplaceFilter
	 *            {@code true} to add a {@link ReplaceFilter} to created
	 *            templates, {@code false} otherwise
	 */
	public DefaultTemplateFactory(boolean addHtmlFilter, boolean addReplaceFilter) {
		this(addHtmlFilter, addReplaceFilter, true, true);
	}

	/**
	 * Creates a new default template factory.
	 *
	 * @param addHtmlFilter
	 *            {@code true} to add an {@link HtmlFilter} to created
	 *            templates, {@code false} otherwise
	 * @param addReplaceFilter
	 *            {@code true} to add a {@link ReplaceFilter} to created
	 *            templates, {@code false} otherwise
	 * @param addStoreFilter
	 *            {@code true} to add a {@link StoreFilter} to created
	 *            templates, {@code false} otherwise
	 * @param addInsertFilter
	 *            {@code true} to add a {@link InsertFilter} to created
	 *            templates, {@code false} otherwise
	 */
	public DefaultTemplateFactory(boolean addHtmlFilter, boolean addReplaceFilter, boolean addStoreFilter, boolean addInsertFilter) {
		this(addHtmlFilter, addReplaceFilter, addStoreFilter, addInsertFilter, true);
	}

	/**
	 * Creates a new default template factory.
	 *
	 * @param addHtmlFilter
	 *            {@code true} to add an {@link HtmlFilter} to created
	 *            templates, {@code false} otherwise
	 * @param addReplaceFilter
	 *            {@code true} to add a {@link ReplaceFilter} to created
	 *            templates, {@code false} otherwise
	 * @param addStoreFilter
	 *            {@code true} to add a {@link StoreFilter} to created
	 *            templates, {@code false} otherwise
	 * @param addInsertFilter
	 *            {@code true} to add a {@link InsertFilter} to created
	 *            templates, {@code false} otherwise
	 * @param addDefaultFilter
	 *            {@code true} to add a {@link DefaultFilter} to created
	 *            templates, {@code false} otherwise
	 */
	public DefaultTemplateFactory(boolean addHtmlFilter, boolean addReplaceFilter, boolean addStoreFilter, boolean addInsertFilter, boolean addDefaultFilter) {
		if (addHtmlFilter) {
			filters.put("html", new HtmlFilter());
		}
		if (addReplaceFilter) {
			filters.put("replace", new ReplaceFilter());
		}
		if (addStoreFilter) {
			filters.put("store", new StoreFilter());
		}
		if (addInsertFilter) {
			filters.put("insert", new InsertFilter());
		}
		if (addDefaultFilter) {
			filters.put("default", new DefaultFilter());
		}
	}

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
	 * Sets the template provider that is set on all created templates.
	 *
	 * @param templateProvider
	 *            The template provider to set
	 */
	public void setTemplateProvider(TemplateProvider templateProvider) {
		this.templateProvider = templateProvider;
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
	public synchronized static TemplateFactory getInstance() {
		if (instance == null) {
			instance = new DefaultTemplateFactory();
		}
		return instance;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Template createTemplate(Reader templateSource) {
		Template template = new Template(templateSource);
		for (Entry<String, Filter> filterEntry : filters.entrySet()) {
			template.addFilter(filterEntry.getKey(), filterEntry.getValue());
		}
		for (Entry<String, Plugin> pluginEntry : plugins.entrySet()) {
			template.addPlugin(pluginEntry.getKey(), pluginEntry.getValue());
		}
		for (Entry<Class<?>, Accessor> accessorEntry : accessors.entrySet()) {
			template.addAccessor(accessorEntry.getKey(), accessorEntry.getValue());
		}
		if (templateProvider != null) {
			template.setTemplateProvider(templateProvider);
		}
		for (Entry<String, Object> objectEntry : templateObjects.entrySet()) {
			template.set(objectEntry.getKey(), objectEntry.getValue());
		}
		return template;
	}

}
