/*
 * utils - Page.java - Copyright © 2010–2019 David Roden
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

/**
 * A page is responsible for handling HTTP requests and creating appropriate
 * responses.
 *
 * @param <REQ>
 *            The type of the request
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public interface Page<REQ extends Request> {

	/**
	 * Returns the path of this page.
	 *
	 * @return The path of this page
	 */
	public String getPath();

	/**
	 * Returns whether this page handles all paths beginning with
	 * {@link #getPath()} or whether this page only handles a single path.
	 *
	 * @return {@code true} if this page can handle multiple paths,
	 *         {@code false} if it handles only a single path
	 */
	public boolean isPrefixPage();

	/**
	 * Handles a request.
	 *
	 * @param request
	 *            The request to handle
	 * @param response
	 *            The response
	 * @return The response to send to the browser
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public Response handleRequest(REQ request, Response response) throws IOException;

}
