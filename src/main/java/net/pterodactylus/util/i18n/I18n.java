/*
 * utils - I18n.java - Copyright © 2006-2010 David Roden
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

package net.pterodactylus.util.i18n;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.KeyStroke;

import net.pterodactylus.util.logging.Logging;

/**
 * Handles internationalization.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class I18n {

	/** The logger. */
	private static final Logger logger = Logging.getLogger(I18n.class);

	/** Whether to log access to unknown keys. */
	public static boolean LOG_UNKNOWN_KEYS = true;

	/** The main source for l10n. */
	private final Source mainSource;

	/** Additional l10n sources. */
	private final List<Source> additionalSources = Collections.synchronizedList(new ArrayList<Source>());

	/** List of I18nable listeners. */
	private final List<I18nable> i18nables = new ArrayList<I18nable>();

	/** Mapping from remove reference to list of I18nables. */
	private final Map<RemovalReference, List<I18nable>> removalReferenceI18nables = new HashMap<RemovalReference, List<I18nable>>();

	/** The current locale. */
	private Locale locale;

	/** The current translation values. */
	private Map<String, String> values = new HashMap<String, String>();

	/** Whether to use {@link MessageFormat} for formatting. */
	private boolean useMessageFormat = false;

	/**
	 * Creates a new i18n handler.
	 *
	 * @param name
	 *            The name of the application
	 * @param propertiesPath
	 *            The path of the properties
	 * @param defaultLocale
	 *            The default locale of the application
	 */
	public I18n(String name, String propertiesPath, Locale defaultLocale) {
		this(name, propertiesPath, defaultLocale, Thread.currentThread().getContextClassLoader(), Locale.getDefault());
	}

	/**
	 * Creates a new i18n handler.
	 *
	 * @param name
	 *            The name of the application
	 * @param propertiesPath
	 *            The path of the properties
	 * @param defaultLocale
	 *            The default locale of the application
	 * @param classLoader
	 *            The class loader used to load the properties
	 * @param currentLocale
	 *            The current locale
	 */
	public I18n(String name, String propertiesPath, Locale defaultLocale, ClassLoader classLoader, Locale currentLocale) {
		this(new Source(name, propertiesPath, defaultLocale, classLoader), currentLocale);
	}

	/**
	 * Creates a new i18n handler.
	 *
	 * @param source
	 *            The l10n source
	 */
	public I18n(Source source) {
		this(source, Locale.getDefault());
	}

	/**
	 * Creates a new i18n handler.
	 *
	 * @param source
	 *            The l10n source
	 * @param currentLocale
	 *            The current locale
	 */
	public I18n(Source source, Locale currentLocale) {
		mainSource = source;
		locale = currentLocale;
		reload();
	}

	//
	// LISTENER MANAGEMENT
	//

	/**
	 * Adds an i18n listener that is notified when the language is changed or
	 * additional sources add added or removed.
	 *
	 * @param i18nable
	 *            The i18n listener to add
	 */
	public void addI18nable(I18nable i18nable) {
		addI18nable(i18nable, null);
	}

	/**
	 * Adds an i18n listener that is notified when the language is changed or
	 * additional sources add added or removed.
	 *
	 * @param i18nable
	 *            The i18n listener to add
	 * @param removalReference
	 *            Removal reference (optional)
	 */
	public void addI18nable(I18nable i18nable, RemovalReference removalReference) {
		i18nables.add(i18nable);
		if (removalReference != null) {
			List<I18nable> i18nableList = removalReferenceI18nables.get(removalReference);
			if (i18nableList == null) {
				i18nableList = new ArrayList<I18nable>();
				removalReferenceI18nables.put(removalReference, i18nableList);
			}
			i18nableList.add(i18nable);
		}
	}

	/**
	 * Removes an i18n listener.
	 *
	 * @param i18nable
	 *            The i18n listener to remove
	 */
	public void removeI18nable(I18nable i18nable) {
		i18nables.remove(i18nable);
	}

	/**
	 * Removes all i18n listeners that have been
	 * {@link #addI18nable(I18nable, RemovalReference)} using the given object
	 * as removal reference.
	 *
	 * @param removalReference
	 *            The removal reference
	 */
	public void removeI18nables(RemovalReference removalReference) {
		List<I18nable> i18nableList = removalReferenceI18nables.remove(removalReference);
		if (i18nableList != null) {
			for (I18nable i18nable : i18nableList) {
				i18nables.remove(i18nable);
			}
		}
	}

	//
	// ACCESSORS
	//

	/**
	 * Sets whether to use {@link MessageFormat} for formatting values with
	 * parameters.
	 *
	 * @param useMessageFormat
	 *            {@code true} to use {@link MessageFormat}, {@code false} to
	 *            use {@link String#format(String, Object...)}
	 */
	public void useMessageFormat(boolean useMessageFormat) {
		this.useMessageFormat = useMessageFormat;
	}

	/**
	 * Returns the current locale.
	 *
	 * @return The current locale
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * Sets the current locale.
	 *
	 * @param locale
	 *            The new locale
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
		reload();
	}

	/**
	 * Adds an additional l10n source.
	 *
	 * @param source
	 *            The l10n source to add
	 */
	public void addSource(Source source) {
		additionalSources.add(source);
		reload();
	}

	/**
	 * Removes an additional l10n source.
	 *
	 * @param source
	 *            The l10n source to remove
	 */
	public void removeSource(Source source) {
		if (additionalSources.remove(source)) {
			reload();
		}
	}

	/**
	 * Returns whether the current translation contains the given key.
	 *
	 * @param key
	 *            The key to check for
	 * @return {@code true} if there is a translation for the given key, {@code
	 *         false} otherwise
	 */
	public boolean has(String key) {
		synchronized (values) {
			return values.containsKey(key);
		}
	}

	/**
	 * Returns the translated value for the given key. If no translation is
	 * found, the name of the key is returned.
	 *
	 * @see Formatter
	 * @param key
	 *            The key to get the translation for
	 * @param parameters
	 *            Parameters to substitute in the value of the key
	 * @return The translated value, or the key
	 */
	public String get(String key, Object... parameters) {
		String value;
		synchronized (values) {
			value = values.get(key);
		}
		if (value == null) {
			if (LOG_UNKNOWN_KEYS) {
				logger.log(Level.WARNING, String.format("Please supply a value for “%1$s”!", key), new Exception());
			}
			return key;
		}
		if ((parameters != null) && (parameters.length > 0)) {
			return useMessageFormat ? MessageFormat.format(value, parameters) : String.format(value, parameters);
		}
		return value;
	}

	/**
	 * Returns the keycode from the value of the given key. You can specify the
	 * constants in {@link KeyEvent} in the properties file, e.g. VK_S for the
	 * keycode ‘s’ when used for mnemonics.
	 *
	 * @param key
	 *            The key under which the keycode is stored
	 * @return The keycode
	 */
	public int getKey(String key) {
		String value;
		synchronized (values) {
			value = values.get(key);
		}
		if ((value != null) && value.startsWith("VK_")) {
			try {
				Field field = KeyEvent.class.getField(value);
				return field.getInt(null);
			} catch (SecurityException e) {
				/* ignore. */
			} catch (NoSuchFieldException e) {
				/* ignore. */
			} catch (IllegalArgumentException e) {
				/* ignore. */
			} catch (IllegalAccessException e) {
				/* ignore. */
			}
		}
		if (LOG_UNKNOWN_KEYS) {
			logger.log(Level.WARNING, "please fix “" + key + "”!", new Throwable());
		}
		return KeyEvent.VK_UNDEFINED;
	}

	/**
	 * Returns a key stroke for use with swing accelerators.
	 *
	 * @param key
	 *            The key of the key stroke
	 * @return The key stroke, or <code>null</code> if no key stroke could be
	 *         created from the translated value
	 */
	public KeyStroke getKeyStroke(String key) {
		String value;
		synchronized (values) {
			value = values.get(key);
		}
		if (value == null) {
			return null;
		}
		StringTokenizer keyTokens = new StringTokenizer(value, "+- ");
		int modifierMask = 0;
		while (keyTokens.hasMoreTokens()) {
			String keyToken = keyTokens.nextToken();
			if ("ctrl".equalsIgnoreCase(keyToken)) {
				modifierMask |= InputEvent.CTRL_DOWN_MASK;
			} else if ("alt".equalsIgnoreCase(keyToken)) {
				modifierMask |= InputEvent.ALT_DOWN_MASK;
			} else if ("shift".equalsIgnoreCase(keyToken)) {
				modifierMask |= InputEvent.SHIFT_DOWN_MASK;
			} else {
				if (keyToken.startsWith("VK_")) {
					if (keyToken.equals("VK_UNDEFINED")) {
						return null;
					}
					try {
						Field field = KeyEvent.class.getField(keyToken);
						return KeyStroke.getKeyStroke(field.getInt(null), modifierMask);
					} catch (SecurityException e) {
						/* ignore. */
					} catch (NoSuchFieldException e) {
						/* ignore. */
					} catch (IllegalArgumentException e) {
						/* ignore. */
					} catch (IllegalAccessException e) {
						/* ignore. */
					}
				}
				return KeyStroke.getKeyStroke(keyToken.charAt(0), modifierMask);
			}
		}
		return null;
	}

	//
	// PRIVATE METHODS
	//

	/**
	 * Reloads translation values for the current locale and l10n sources.
	 */
	private void reload() {
		Properties currentValues = new Properties();
		loadSource(currentValues, mainSource, mainSource.getDefaultLocale());
		loadSource(currentValues, mainSource, locale);
		for (Source additionalSource : additionalSources) {
			loadSource(currentValues, additionalSource, additionalSource.getDefaultLocale());
			loadSource(currentValues, additionalSource, locale);
		}
		synchronized (values) {
			values.clear();
			for (Entry<Object, Object> valueEntry : currentValues.entrySet()) {
				values.put((String) valueEntry.getKey(), (String) valueEntry.getValue());
			}
		}
		for (I18nable i18nable : i18nables) {
			i18nable.updateI18n();
		}
	}

	/**
	 * Loads the translation values from a given source.
	 *
	 * @param currentValues
	 *            The current translation values
	 * @param source
	 *            The l10n source to load
	 * @param locale
	 *            The locale to load from the source
	 */
	private void loadSource(Properties currentValues, Source source, Locale locale) {
		for (String variant : buildResourceNames(locale)) {
			loadResource(currentValues, source.getClassLoader(), source.getPropertiesPath() + "/" + source.getName() + "_" + variant + ".properties");
		}
	}

	/**
	 * Builds up to three resource names. The first resource name is always the
	 * language (“en”), the (optional) second one consists of the language and
	 * the country (“en_GB”) and the (optional) third one includes a variant
	 * (“en_GB_MAC”).
	 *
	 * @param locale
	 *            The locale to build variant names from
	 * @return The variant names
	 */
	private String[] buildResourceNames(Locale locale) {
		List<String> variants = new ArrayList<String>();
		String currentVariant = locale.getLanguage();
		variants.add(currentVariant);
		if (!locale.getCountry().equals("")) {
			currentVariant += "_" + locale.getCountry();
			variants.add(currentVariant);
		}
		if ((locale.getVariant() != null) && (!locale.getVariant().equals(""))) {
			if (locale.getCountry().equals("")) {
				currentVariant += "_";
			}
			currentVariant += "_" + locale.getVariant();
			variants.add(locale.getVariant());
		}
		return variants.toArray(new String[variants.size()]);
	}

	/**
	 * Loads a resource from the given class loader.
	 *
	 * @param currentValues
	 *            The current translation values to load the resource into
	 * @param classLoader
	 *            The class loader used to load the resource
	 * @param resourceName
	 *            The name of the resource
	 */
	private void loadResource(Properties currentValues, ClassLoader classLoader, String resourceName) {
		logger.log(Level.FINEST, "Trying to load resources from " + resourceName + "…");
		InputStream inputStream = classLoader.getResourceAsStream(resourceName);
		if (inputStream != null) {
			try {
				logger.log(Level.FINEST, "Loading resources from " + resourceName + "…");
				currentValues.load(inputStream);
				logger.log(Level.FINEST, "Resources successfully loaded.");
			} catch (IOException ioe1) {
				logger.log(Level.WARNING, String.format("Could not read properties from “%1$s”.", resourceName), ioe1);
			} catch (IllegalArgumentException iae1) {
				logger.log(Level.WARNING, String.format("Could not parse properties from “%1$s”.", resourceName), iae1);
			}
		}
	}

	/**
	 * A localization (l10n) source.
	 *
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	public static class Source {

		/** The name of the application. */
		private final String name;

		/** The path of the properties. */
		private final String propertiesPath;

		/** The default locale of the application. */
		private final Locale defaultLocale;

		/** The class loader for loading resources. */
		private final ClassLoader classLoader;

		/**
		 * Creates a new l10n source.
		 *
		 * @param name
		 *            The name of the application
		 * @param propertiesPath
		 *            The path of the properties
		 * @param defaultLocale
		 *            The default locale of the source
		 * @param classLoader
		 *            The class loader for the source’s resources
		 */
		public Source(String name, String propertiesPath, Locale defaultLocale, ClassLoader classLoader) {
			this.name = name;
			this.propertiesPath = propertiesPath;
			this.defaultLocale = defaultLocale;
			this.classLoader = classLoader;
		}

		/**
		 * Returns the name of the application.
		 *
		 * @return The name of the application
		 */
		public String getName() {
			return name;
		}

		/**
		 * Returns the path of the properties.
		 *
		 * @return The path of the properties
		 */
		public String getPropertiesPath() {
			return propertiesPath;
		}

		/**
		 * Returns the default locale of the source.
		 *
		 * @return The default locale of the source
		 */
		public Locale getDefaultLocale() {
			return defaultLocale;
		}

		/**
		 * Returns the source’s class loader.
		 *
		 * @return The class loader of the source
		 */
		public ClassLoader getClassLoader() {
			return classLoader;
		}

	}

	/**
	 * Identifying container for a removal reference.
	 *
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	public static class RemovalReference {

		/* nothing here. */

	}

}
