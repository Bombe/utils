/*
 * utils - XML.java - Copyright © 2006-2009 David Roden
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

package net.pterodactylus.util.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import net.pterodactylus.util.io.Closer;
import net.pterodactylus.util.logging.Logging;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Contains method to transform DOM XML trees to byte arrays and vice versa.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class XML {

	/** The logger. */
	private static final Logger logger = Logging.getLogger(XML.class.getName());

	/** Cached document builder factory. */
	private static DocumentBuilderFactory documentBuilderFactory = null;

	/** Cached document builder. */
	private static DocumentBuilder documentBuilder = null;

	/** Cached transformer factory. */
	private static TransformerFactory transformerFactory = null;

	/**
	 * Returns a document builder factory. If possible the cached instance will
	 * be returned.
	 *
	 * @return A document builder factory
	 */
	private static DocumentBuilderFactory getDocumentBuilderFactory() {
		if (documentBuilderFactory != null) {
			return documentBuilderFactory;
		}
		documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setXIncludeAware(true);
		documentBuilderFactory.setNamespaceAware(true);
		return documentBuilderFactory;
	}

	/**
	 * Returns a document builder. If possible the cached instance will be
	 * returned.
	 *
	 * @return A document builder
	 */
	private static DocumentBuilder getDocumentBuilder() {
		if (documentBuilder != null) {
			return documentBuilder;
		}
		try {
			documentBuilder = getDocumentBuilderFactory().newDocumentBuilder();
		} catch (ParserConfigurationException pce1) {
			logger.log(Level.WARNING, "Could not create DocumentBuilder.", pce1);
		}
		return documentBuilder;
	}

	/**
	 * Returns a transformer factory. If possible the cached instance will be
	 * returned.
	 *
	 * @return A transformer factory
	 */
	private static TransformerFactory getTransformerFactory() {
		if (transformerFactory != null) {
			return transformerFactory;
		}
		transformerFactory = TransformerFactory.newInstance();
		return transformerFactory;
	}

	/**
	 * Creates a new XML document.
	 *
	 * @return A new XML document
	 */
	public static Document createDocument() {
		return getDocumentBuilder().newDocument();
	}

	/**
	 * Transforms the DOM XML document into a byte array.
	 *
	 * @param document
	 *            The document to transform
	 * @return The byte array containing the XML representation
	 */
	public static byte[] transformToByteArray(Document document) {
		ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
		OutputStreamWriter converter = new OutputStreamWriter(byteOutput, Charset.forName("UTF-8"));
		writeToOutputStream(document, converter);
		try {
			converter.flush();
			byteOutput.flush();
			byte[] result = byteOutput.toByteArray();
			return result;
		} catch (IOException ioe1) {
			return null;
		} finally {
			Closer.close(converter);
			Closer.close(byteOutput);
		}
	}

	/**
	 * Writes the given document to the given writer.
	 *
	 * @param document
	 *            The document to write
	 * @param writer
	 *            The writer to write the document to
	 */
	public static void writeToOutputStream(Document document, Writer writer) {
		writeToOutputStream(document, writer, true);
	}

	/**
	 * Writes the given document to the given writer.
	 *
	 * @param document
	 *            The document to write
	 * @param writer
	 *            The writer to write the document to
	 * @param preamble
	 *            <code>true</code> to include the XML header,
	 *            <code>false</code> to not include it
	 */
	public static void writeToOutputStream(Document document, Writer writer, boolean preamble) {
		Result transformResult = new StreamResult(writer);
		Source documentSource = new DOMSource(document);
		try {
			Transformer transformer = getTransformerFactory().newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, preamble ? "no" : "yes");
			transformer.transform(documentSource, transformResult);
		} catch (TransformerConfigurationException tce1) {
			logger.log(Level.WARNING, "Could create Transformer.", tce1);
		} catch (TransformerException te1) {
			logger.log(Level.WARNING, "Could not transform Document.", te1);
		}
	}

	/**
	 * Transforms the byte array into a DOM XML document.
	 *
	 * @param data
	 *            The byte array to parse
	 * @return The DOM XML document
	 */
	public static Document transformToDocument(byte[] data) {
		return transformToDocument(new ByteArrayInputStream(data));
	}

	/**
	 * Transforms the input stream into a DOM XML document.
	 *
	 * @param inputStream
	 *            The input stream to parse
	 * @return The DOM XML document
	 */
	public static Document transformToDocument(InputStream inputStream) {
		return transformToDocument(new InputSource(inputStream));
	}

	/**
	 * Transforms the reader into a DOM XML document.
	 *
	 * @param inputReader
	 *            The reader to read the XML from
	 * @return The DOM XML document
	 */
	public static Document transformToDocument(Reader inputReader) {
		return transformToDocument(new InputSource(inputReader));
	}

	/**
	 * Transforms the inout source into a DOM XML document.
	 *
	 * @param inputSource
	 *            The source to read the XML from
	 * @return The DOM XML document
	 */
	public static Document transformToDocument(InputSource inputSource) {
		try {
			DocumentBuilder documentBuilder = getDocumentBuilder();
			return documentBuilder.parse(inputSource);
		} catch (SAXException saxe1) {
			logger.log(Level.WARNING, "Could not parse InputSource.", saxe1);
		} catch (IOException ioe1) {
			logger.log(Level.WARNING, "Could not read InputSource.", ioe1);
		}
		return null;
	}

}
