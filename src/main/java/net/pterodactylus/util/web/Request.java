/*
 * utils - Request.java - Copyright © 2010–2011 David Roden
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

import java.net.URI;

/**
 * Container for request data.
 *
 * @param <REQ>
 *            The type of the original requests
 * @param <RES>
 *            The type of the original responses
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public abstract class Request<REQ, RES> {

	/** The URI that was accessed. */
	private final URI uri;

	/** The HTTP method that was used. */
	private final Method method;

	/** The original request. */
	private final REQ request;

	/** The original response. */
	private final RES response;

	/**
	 * Creates a new request that holds the given data.
	 *
	 * @param uri
	 *            The URI of the request
	 * @param method
	 *            The HTTP method of the request
	 * @param request
	 *            The original request
	 * @param response
	 *            The original response
	 */
	public Request(URI uri, Method method, REQ request, RES response) {
		this.uri = uri;
		this.method = method;
		this.request = request;
		this.response = response;
	}

	/**
	 * Returns the URI that was accessed.
	 *
	 * @return The accessed URI
	 */
	public URI getUri() {
		return uri;
	}

	/**
	 * Returns the HTTP method that was used to access the page.
	 *
	 * @return The HTTP method
	 */
	public Method getMethod() {
		return method;
	}

	/**
	 * Returns the original request.
	 *
	 * @return The original reqest
	 */
	public REQ getRequest() {
		return request;
	}

	/**
	 * Returns the original response
	 *
	 * @return The original response
	 */
	public RES getResponse() {
		return response;
	}

}
