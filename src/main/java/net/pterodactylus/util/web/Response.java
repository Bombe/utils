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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Container for the HTTP response of a {@link Page}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Response {

	/** The HTTP status code of the response. */
	private final int statusCode;

	/** The HTTP status text of the response. */
	private final String statusText;

	/** The content type of the response. */
	private final String contentType;

	/** The headers of the response. */
	private final Map<String, String> headers;

	/** The content of the response body. */
	private final InputStream content;

	/**
	 * Creates a new response.
	 *
	 * @param statusCode
	 *            The HTTP status code of the response
	 * @param statusText
	 *            The HTTP status text of the response
	 * @param contentType
	 *            The content type of the response
	 * @param text
	 *            The text in the response body
	 */
	public Response(int statusCode, String statusText, String contentType, String text) {
		this(statusCode, statusText, contentType, getBytes(text));
	}

	/**
	 * Creates a new response.
	 *
	 * @param statusCode
	 *            The HTTP status code of the response
	 * @param statusText
	 *            The HTTP status text of the response
	 * @param contentType
	 *            The content type of the response
	 * @param content
	 *            The content of the reponse body
	 */
	public Response(int statusCode, String statusText, String contentType, byte[] content) {
		this(statusCode, statusText, contentType, new HashMap<String, String>(), content);
	}

	/**
	 * Creates a new response.
	 *
	 * @param statusCode
	 *            The HTTP status code of the response
	 * @param statusText
	 *            The HTTP status text of the response
	 * @param contentType
	 *            The content type of the response
	 * @param headers
	 *            The headers of the response
	 */
	public Response(int statusCode, String statusText, String contentType, Map<String, String> headers) {
		this(statusCode, statusText, contentType, headers, (InputStream) null);
	}

	/**
	 * Creates a new response.
	 *
	 * @param statusCode
	 *            The HTTP status code of the response
	 * @param statusText
	 *            The HTTP status text of the response
	 * @param contentType
	 *            The content type of the response
	 * @param headers
	 *            The headers of the response
	 * @param content
	 *            The content of the reponse body
	 */
	public Response(int statusCode, String statusText, String contentType, Map<String, String> headers, byte[] content) {
		this(statusCode, statusText, contentType, headers, new ByteArrayInputStream(content));
	}

	/**
	 * Creates a new response.
	 *
	 * @param statusCode
	 *            The HTTP status code of the response
	 * @param statusText
	 *            The HTTP status text of the response
	 * @param contentType
	 *            The content type of the response
	 * @param headers
	 *            The headers of the response
	 * @param content
	 *            The content of the reponse body
	 */
	public Response(int statusCode, String statusText, String contentType, Map<String, String> headers, InputStream content) {
		this.statusCode = statusCode;
		this.statusText = statusText;
		this.contentType = contentType;
		this.headers = headers;
		this.content = content;
	}

	/**
	 * Returns the HTTP status code of the response.
	 *
	 * @return The HTTP status code
	 */
	public int getStatusCode() {
		return statusCode;
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
	 * Returns the content type of the response.
	 *
	 * @return The content type of the reponse
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * Returns HTTP headers of the response. May be {@code null} if no headers
	 * are returned.
	 *
	 * @return The response headers, or {@code null} if there are no response
	 *         headers
	 */
	public Map<String, String> getHeaders() {
		return headers;
	}

	/**
	 * Sets the HTTP header with the given name to the given value. Multiple
	 * headers with the same name are not implemented so that latest call to
	 * {@link #setHeader(String, String)} determines what is sent to the
	 * browser.
	 *
	 * @param name
	 *            The name of the header
	 * @param value
	 *            The value of the header
	 */
	public void setHeader(String name, String value) {
		headers.put(name, value);
	}

	/**
	 * Returns the content of the response body. May be {@code null} if the
	 * response does not have a body.
	 *
	 * @return The content of the response body
	 */
	public InputStream getContent() {
		return content;
	}

	//
	// PRIVATE METHODS
	//

	/**
	 * Returns the UTF-8 representation of the given text.
	 *
	 * @param text
	 *            The text to encode
	 * @return The encoded text
	 */
	private static byte[] getBytes(String text) {
		try {
			return text.getBytes("UTF-8");
		} catch (UnsupportedEncodingException uee1) {
			/* every JVM needs to support UTF-8. */
		}
		return null;
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
