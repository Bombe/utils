/*
 * utils - XMLConfigurationBackend.java - Copyright © 2007-2009 David Roden
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.pterodactylus.util.io.Closer;
import net.pterodactylus.util.logging.Logging;
import net.pterodactylus.util.xml.SimpleXML;
import net.pterodactylus.util.xml.XML;

import org.w3c.dom.Document;

/**
 * Configuration backend that reads and writes its configuration from/to an XML
 * file.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class XMLConfigurationBackend implements ConfigurationBackend {

	/** The logger. */
	private static final Logger logger = Logging.getLogger(XMLConfigurationBackend.class.getName());

	/** The node cache. */
	private final Map<String, SimpleXML> nodeCache = new HashMap<String, SimpleXML>();

	/** The configuration file. */
	private final File configurationFile;

	/** The last modification time of the configuration file. */
	private long lastModified;

	/** The root node of the document. */
	private final SimpleXML rootNode;

	/**
	 * Creates a new backend backed by the given file.
	 *
	 * @param configurationFile
	 *            The XML file to read the configuration from
	 * @throws ConfigurationException
	 *             if the XML can not be read or parsed
	 */
	public XMLConfigurationBackend(File configurationFile) throws ConfigurationException {
		this(configurationFile, false);
	}

	/**
	 * Creates a new backend backed by the given file.
	 *
	 * @param configurationFile
	 *            The XML file to read the configuration from
	 * @param create
	 *            {@code true} to create a new configuration when loading the
	 *            configuration from the given file fails, {@code false} to
	 *            throw a {@link ConfigurationException}
	 * @throws ConfigurationException
	 *             if the XML can not be read or parsed
	 */
	public XMLConfigurationBackend(File configurationFile, boolean create) throws ConfigurationException {
		this.configurationFile = configurationFile;
		rootNode = readConfigurationFile(create);
	}

	/**
	 * Reads and parses the configuration file.
	 *
	 * @param create
	 *            {@code true} to create a new configuration if loading the
	 *            configuration file fails, {@code false} to throw a
	 *            {@link ConfigurationException}
	 * @return The created root node
	 * @throws ConfigurationException
	 *             if the file can not be read or parsed
	 */
	private synchronized SimpleXML readConfigurationFile(boolean create) throws ConfigurationException {
		FileInputStream configFileInputStream = null;
		try {
			configFileInputStream = new FileInputStream(configurationFile);
			Document configurationDocument = XML.transformToDocument(configFileInputStream);
			if (configurationDocument == null) {
				if (!create) {
					throw new ConfigurationException("can not parse XML document");
				}
				configurationDocument = new SimpleXML("config").getDocument();
			}
			nodeCache.clear();
			return SimpleXML.fromDocument(configurationDocument);
		} catch (FileNotFoundException fnfe1) {
			if (!create) {
				throw new ConfigurationException(fnfe1);
			}
			return new SimpleXML("config");
		} finally {
			Closer.close(configFileInputStream);
		}
	}

	/**
	 * Writes the current document (including changes) back to the
	 * {@link #configurationFile}.
	 *
	 * @throws ConfigurationException
	 *             if the document could not be written
	 */
	private synchronized void writeConfigurationFile() throws ConfigurationException {
		FileOutputStream configurationFileOutputStream = null;
		OutputStreamWriter configurationOutputStreamWriter = null;
		try {
			configurationFileOutputStream = new FileOutputStream(configurationFile);
			configurationOutputStreamWriter = new OutputStreamWriter(configurationFileOutputStream, "UTF-8");
			XML.writeToOutputStream(rootNode.getDocument(), configurationOutputStreamWriter);
		} catch (IOException ioe1) {
			throw new ConfigurationException(ioe1.getMessage(), ioe1);
		} finally {
			Closer.close(configurationOutputStreamWriter);
			Closer.close(configurationFileOutputStream);
		}
	}

	//
	// INTERFACE ConfigurationBackend
	//

	/**
	 * {@inheritDoc}
	 *
	 * @see net.pterodactylus.util.config.ConfigurationBackend#getValue(java.lang.String)
	 */
	@Override
	public String getValue(String attribute) throws ConfigurationException {
		if (configurationFile.lastModified() > lastModified) {
			logger.info("reloading configuration file " + configurationFile.getAbsolutePath());
			readConfigurationFile(false);
			lastModified = configurationFile.lastModified();
		}
		SimpleXML node = getNode(attribute);
		String value = node.getValue();
		logger.log(Level.FINEST, "attribute: “%1$s”, value: “%2$s”", new Object[] { attribute, value });
		return value;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see net.pterodactylus.util.config.ConfigurationBackend#putValue(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public void putValue(String attribute, String value) throws ConfigurationException {
		SimpleXML node = getNode(attribute, true);
		node.setValue(value);
		writeConfigurationFile();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void save() throws ConfigurationException {
		writeConfigurationFile();
	}

	//
	// PRIVATE METHODS
	//

	/**
	 * Searches for the node with the given name and returns it. The given
	 * attribute may contain several nodes, separated by a pipe character (“|”),
	 * that describe where in the document hierarchy the node can be found.
	 *
	 * @param attribute
	 *            The complete name of the node
	 * @return The node, if found
	 * @throws ConfigurationException
	 *             if the node could not be found
	 */
	private SimpleXML getNode(String attribute) throws ConfigurationException {
		return getNode(attribute, false);
	}

	/**
	 * Searches for the node with the given name and returns it. The given
	 * attribute may contain several nodes, separated by a pipe character (“|”),
	 * that describe where in the document hierarchy the node can be found.
	 *
	 * @param attribute
	 *            The complete name of the node
	 * @param create
	 *            {@code true} to create the node if it doesn’t exist,
	 *            {@code false} to throw a {@link ConfigurationException} if it
	 *            doesn’t exist
	 * @return The node, if found
	 * @throws ConfigurationException
	 *             if the node could not be found
	 */
	private SimpleXML getNode(String attribute, boolean create) throws ConfigurationException {
		if (nodeCache.containsKey(attribute)) {
			return nodeCache.get(attribute);
		}
		StringTokenizer attributes = new StringTokenizer(attribute, "|/");
		SimpleXML node = rootNode;
		while (attributes.hasMoreTokens()) {
			String nodeName = attributes.nextToken();
			if (node.hasNode(nodeName)) {
				node = node.getNode(nodeName);
			} else {
				if (!create) {
					throw new AttributeNotFoundException(attribute);
				}
				node = node.append(nodeName);
			}
		}
		nodeCache.put(attribute, node);
		return node;
	}

}
