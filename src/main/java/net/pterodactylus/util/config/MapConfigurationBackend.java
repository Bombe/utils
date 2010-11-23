/*
 * utils - MapConfigurationBackend.java - Copyright © 2007-2009 David Roden
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

package net.pterodactylus.util.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.pterodactylus.util.io.Closer;
import net.pterodactylus.util.logging.Logging;
import net.pterodactylus.util.text.StringEscaper;
import net.pterodactylus.util.text.TextException;

/**
 * Configuration backend that is backed by a {@link Map}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class MapConfigurationBackend implements ConfigurationBackend {

	/** The logger. */
	private static final Logger logger = Logging.getLogger(MapConfigurationBackend.class);

	/** The backing file, if any. */
	private final File configurationFile;

	/** The backing map. */
	private final Map<String, String> values = new HashMap<String, String>();

	/**
	 * Creates a new configuration backend.
	 */
	public MapConfigurationBackend() {
		this(Collections.<String, String> emptyMap());
	}

	/**
	 * Creates a new configuration backend that contains the values from the
	 * given map.
	 *
	 * @param values
	 *            The initial values
	 */
	public MapConfigurationBackend(Map<String, String> values) {
		configurationFile = null;
		this.values.putAll(values);
	}

	/**
	 * Creates a new configuration backend that loads and stores its
	 * configuration in the given file.
	 *
	 * @param configurationFile
	 *            The file to store the configuration in
	 * @throws ConfigurationException
	 *             if the configuration can not be loaded from the file
	 */
	public MapConfigurationBackend(File configurationFile) throws ConfigurationException {
		this(configurationFile, false);
	}

	/**
	 * Creates a new configuration backend from the given file that also
	 * contains the given values.
	 *
	 * @param configurationFile
	 *            The file to store the configuration in
	 * @param values
	 *            Additional initial values
	 * @throws ConfigurationException
	 *             if the configuration can not be loaded from the file
	 */
	public MapConfigurationBackend(File configurationFile, Map<String, String> values) throws ConfigurationException {
		this(configurationFile, false, values);
	}

	/**
	 * Creates a new configuration backend from the given file.
	 *
	 * @param configurationFile
	 *            The file to store the configuration in
	 * @param ignoreMissing
	 *            {@code true} to ignore a missing configuration file when
	 *            loading the configuration
	 * @throws ConfigurationException
	 *             if the configuration can not be loaded from the file
	 */
	public MapConfigurationBackend(File configurationFile, boolean ignoreMissing) throws ConfigurationException {
		this(configurationFile, ignoreMissing, null);
	}

	/**
	 * Creates a new configuration backend from the given file that also
	 * contains the given values.
	 *
	 * @param configurationFile
	 *            The file to store the configuration in
	 * @param ignoreMissing
	 *            {@code true} to ignore a missing configuration file when
	 *            loading the configuration
	 * @param values
	 *            Additional initial values
	 * @throws ConfigurationException
	 *             if the configuration can not be loaded from the file
	 */
	public MapConfigurationBackend(File configurationFile, boolean ignoreMissing, Map<String, String> values) throws ConfigurationException {
		this.configurationFile = configurationFile;
		if (configurationFile != null) {
			loadValues(ignoreMissing);
		}
		if (values != null) {
			this.values.putAll(values);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see net.pterodactylus.util.config.ConfigurationBackend#getValue(java.lang.String)
	 */
	@Override
	public String getValue(String attribute) {
		synchronized (values) {
			return values.get(attribute);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see net.pterodactylus.util.config.ConfigurationBackend#putValue(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public void putValue(String attribute, String value) throws ConfigurationException {
		synchronized (values) {
			values.put(attribute, value);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void save() throws ConfigurationException {
		synchronized (values) {
			saveValues();
		}
	}

	//
	// PRIVATE METHODS
	//

	/**
	 * Loads the configuration from the {@link #configurationFile}. If
	 * {@code ignoreMissing} is {@code false} a {@link ConfigurationException}
	 * will be thrown if the file does not exist.
	 *
	 * @param ignoreMissing
	 *            {@code true} to ignore a missing configuration file,
	 *            {@code false} to throw a {@link ConfigurationException}
	 * @throws ConfigurationException
	 *             if the file can not be found or read, or the values can not
	 *             be parsed
	 */
	private void loadValues(boolean ignoreMissing) throws ConfigurationException {
		if (!configurationFile.exists()) {
			if (!ignoreMissing) {
				throw new ConfigurationException("Configuration file “" + configurationFile.getName() + "” is missing!");
			}
			return;
		}
		FileInputStream configurationInputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		try {
			configurationInputStream = new FileInputStream(configurationFile);
			inputStreamReader = new InputStreamReader(configurationInputStream, "UTF-8");
			bufferedReader = new BufferedReader(inputStreamReader);
			Map<String, String> values = new HashMap<String, String>();
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				if (line.startsWith("#") || line.startsWith(";") || (line.trim().length() == 0)) {
					continue;
				}
				int colon = line.indexOf(':');
				int equals = line.indexOf('=');
				if ((colon == -1) && (equals == -1)) {
					throw new ConfigurationException("Line without “:” or “=” found: " + line);
				}
				String key;
				if (colon != -1) {
					if (equals != -1) {
						key = line.substring(0, Math.min(colon, equals));
					} else {
						key = line.substring(0, colon);
					}
				} else {
					key = line.substring(0, equals);
				}
				if (line.substring(key.length() + 1).trim().length() == 0) {
					values.put(key, null);
				} else {
					key = StringEscaper.parseLine(key).get(0);
					List<String> words = StringEscaper.parseLine(line.substring(key.length() + 1).trim());
					StringBuilder value = new StringBuilder();
					for (String word : words) {
						if (value.length() > 0) {
							value.append(' ');
						}
						value.append(word);
					}
					values.put(key, value.toString());
				}
			}
			this.values.putAll(values);
		} catch (FileNotFoundException fnfe1) {
			if (!ignoreMissing) {
				throw new ConfigurationException("Could not find configuration file “" + configurationFile.getName() + "”!", fnfe1);
			}
		} catch (UnsupportedEncodingException uee1) {
			/* impossible, I’d say. */
			logger.log(Level.SEVERE, "JVM does not support UTF-8!");
		} catch (IOException ioe1) {
			throw new ConfigurationException("Could not read configuration from “" + configurationFile.getName() + "”!", ioe1);
		} catch (TextException te1) {
			throw new ConfigurationException("Could not parse configuration value!", te1);
		} finally {
			Closer.close(bufferedReader);
			Closer.close(inputStreamReader);
			Closer.close(configurationInputStream);
		}
	}

	/**
	 * Saves the configuration to the configuration file, if it is not
	 * {@code null}. If no configuration file has been set, this method simply
	 * returns.
	 *
	 * @throws ConfigurationException
	 *             if there was an error when writing the configuration
	 */
	public void saveValues() throws ConfigurationException {
		if (configurationFile == null) {
			return;
		}
		FileOutputStream configurationOutputStream = null;
		OutputStreamWriter outputStreamWriter = null;
		BufferedWriter bufferedWriter = null;
		try {
			configurationOutputStream = new FileOutputStream(configurationFile);
			outputStreamWriter = new OutputStreamWriter(configurationOutputStream, "UTF-8");
			bufferedWriter = new BufferedWriter(outputStreamWriter);
			for (Entry<String, String> value : values.entrySet()) {
				bufferedWriter.write(StringEscaper.escapeWord(value.getKey()));
				bufferedWriter.write(": ");
				bufferedWriter.write(StringEscaper.escapeWord(value.getValue()));
				bufferedWriter.newLine();
			}
		} catch (FileNotFoundException fnfe1) {
			throw new ConfigurationException("Could not create configuration file “" + configurationFile.getName() + "”!", fnfe1);
		} catch (UnsupportedEncodingException uee1) {
			/* impossible, I’d say. */
			logger.log(Level.SEVERE, "JVM does not support UTF-8!");
		} catch (IOException ioe1) {
			throw new ConfigurationException("Could not write to configuration file!", ioe1);
		} finally {
			Closer.close(bufferedWriter);
			Closer.close(outputStreamWriter);
			Closer.close(configurationOutputStream);
		}
	}

}
