/*
 * utils - ClassPathTemplateProvider.java - Copyright © 2010–2011 David Roden
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

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.pterodactylus.util.cache.Cache;
import net.pterodactylus.util.cache.CacheException;
import net.pterodactylus.util.cache.CacheItem;
import net.pterodactylus.util.cache.DefaultCacheItem;
import net.pterodactylus.util.cache.MemoryCache;
import net.pterodactylus.util.cache.ValueRetriever;
import net.pterodactylus.util.logging.Logging;

/**
 * Template provider implementation that uses
 * {@link Class#getResourceAsStream(String)} to load templates for inclusion.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ClassPathTemplateProvider implements Provider {

	/** The logger. */
	private static final Logger logger = Logging.getLogger(ClassPathTemplateProvider.class);

	/** The class used to locate resources. */
	private final Class<?> resourceClass;

	/** The path to prepend to all requested resources. */
	private final String resourcePath;

	/** Cache for templates. */
	private final Cache<String, Template> templateCache = new MemoryCache<String, Template>(new ValueRetriever<String, Template>() {

		@Override
		@SuppressWarnings("synthetic-access")
		public CacheItem<Template> retrieve(String key) throws CacheException {
			Template template = findTemplate(key);
			if (template != null) {
				return new DefaultCacheItem<Template>(template);
			}
			return null;
		}
	});

	/**
	 * Creates a new class path template provider.
	 *
	 * @param resourceClass
	 *            The class used to locate resources
	 */
	public ClassPathTemplateProvider(Class<?> resourceClass) {
		this(resourceClass, "/");
	}

	/**
	 * Creates a new class path template provider.
	 *
	 * @param resourceClass
	 *            The class used to locate resources
	 * @param resourcePath
	 *            The path to prepend to all requested names
	 */
	public ClassPathTemplateProvider(Class<?> resourceClass, String resourcePath) {
		this.resourceClass = resourceClass;
		this.resourcePath = resourcePath;
	}

	//
	// PROVIDER METHODS
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Template getTemplate(TemplateContext templateContext, String templateName) {
		try {
			return templateCache.get(templateName);
		} catch (CacheException ce1) {
			logger.log(Level.WARNING, "Could not get template for " + templateName + "!", ce1);
			return null;
		}
	}

	//
	// PRIVATE METHODS
	//

	/**
	 * Locates a template in the class path.
	 *
	 * @param templateName
	 *            The name of the template to load
	 * @return The loaded template, or {@code null} if no template could be
	 *         found
	 */
	private Template findTemplate(String templateName) {
		Reader templateReader = createReader(resourcePath + templateName);
		if (templateReader == null) {
			return null;
		}
		Template template = null;
		try {
			template = TemplateParser.parse(templateReader);
		} catch (TemplateException te1) {
			logger.log(Level.WARNING, "Could not parse template “" + templateName + "” for inclusion!", te1);
		}
		return template;
	}

	/**
	 * Creates a {@link Reader} from the {@link InputStream} for the resource
	 * with the given name.
	 *
	 * @param resourceName
	 *            The name of the resource
	 * @return A {@link Reader} for the resource
	 */
	private Reader createReader(String resourceName) {
		try {
			return new InputStreamReader(resourceClass.getResourceAsStream(resourceName), "UTF-8");
		} catch (UnsupportedEncodingException uee1) {
			return null;
		}
	}

}
