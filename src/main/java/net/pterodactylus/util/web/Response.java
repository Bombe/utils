/*
 * utils - Response.java - Copyright © 2010–2011 David Roden
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

package net.pterodactylus.util.web;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import net.pterodactylus.util.io.Closer;

/**
 * Container for the HTTP response of a {@link Page}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Response {

	/** The HTTP status code of the response. */
	private int statusCode = 200;

	/** The HTTP status text of the response. */
	private String statusText = "OK";

	/** The content type of the response. */
	private String contentType = "text/plain; charset=utf-8";

	/** The headers of the response. */
	private Map<String, Header> headers = new HashMap<String, Header>();

	/** Output stream the response body is written to. */
	private final OutputStream content;

	/**
	 * Creates a new response.
	 *
	 * @param content
	 *            The output stream the response body will be written to
	 */
	public Response(OutputStream content) {
		this.content = content;
	}

	//
	// ACCESSORS
	//

	/**
	 * Returns the HTTP status code of the response.
	 *
	 * @return The HTTP status code
	 */
	public int getStatusCode() {
		return statusCode;
	}

	/**
	 * Sets the status code of this response.
	 *
	 * @param statusCode
	 *            The status code of this response
	 * @return This response
	 */
	public Response setStatusCode(int statusCode) {
		this.statusCode = statusCode;
		return this;
	}

	/**
	 * Returns the HTTP status text.
	 *
	 * @return The HTTP status text
	 */
	public String getStatusText() {
		return statusText;
	}

	/**
	 * Sets the text of the HTTP status line. The status line is part of the
	 * HTTP protocol and is normally not displayed to the browser.
	 *
	 * @param statusText
	 *            The text of the HTTP status line
	 * @return This response
	 */
	public Response setStatusText(String statusText) {
		this.statusText = statusText;
		return this;
	}

	/**
	 * Returns the content type of the response.
	 *
	 * @return The content type of the reponse
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * Sets the content type of this response.
	 *
	 * @param contentType
	 *            The content type of this response
	 * @return This response
	 */
	public Response setContentType(String contentType) {
		this.contentType = contentType;
		return this;
	}

	/**
	 * Writes the text to this response’s {@link #getContent() content output
	 * stream}.
	 *
	 * @param text
	 *            The text to write
	 * @return This response
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public Response write(String text) throws IOException {
		OutputStreamWriter outputStreamWriter = null;
		try {
			outputStreamWriter = new OutputStreamWriter(content, "UTF-8");
			outputStreamWriter.write(text);
			outputStreamWriter.flush();
		} finally {
			Closer.close(outputStreamWriter);
		}
		return this;
	}

	/**
	 * Writes the given data to this response’s {@link #getContent() content
	 * output stream}.
	 *
	 * @param data
	 *            The data to write
	 * @return This response
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public Response write(byte[] data) throws IOException {
		content.write(data);
		return this;
	}

	/**
	 * Returns HTTP headers of the response. May be {@code null} if no headers
	 * are returned.
	 *
	 * @return The response headers
	 */
	public Iterable<Header> getHeaders() {
		return headers.values();
	}

	/**
	 * Adds a header field with the given name and value to this response.
	 *
	 * @param name
	 *            The name of the header field
	 * @param value
	 *            The value of the header field
	 * @return This response
	 */
	public Response addHeader(String name, String value) {
		getHeader(name).addValue(value);
		return this;
	}

	/**
	 * Returns the output stream the response body has to be written to.
	 *
	 * @return The output stream of the response body
	 */
	public OutputStream getContent() {
		return content;
	}

	//
	// PRIVATE METHODS
	//

	/**
	 * Returns the header field with the given name, creating a new header field
	 * if no header field with the given name already exists.
	 *
	 * @param name
	 *            The name of the header field to return
	 * @return The header field with the given name
	 */
	private Header getHeader(String name) {
		Header header = headers.get(name);
		if (header == null) {
			header = new Header(name);
			headers.put(name, header);
		}
		return header;
	}

	/**
	 * Creates a header map containing a single header.
	 *
	 * @param name
	 *            The name of the header
	 * @param value
	 *            The value of the header
	 * @return The map containing the single header
	 */
	protected static Map<String, String> createHeader(String name, String value) {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put(name, value);
		return headers;
	}

}
