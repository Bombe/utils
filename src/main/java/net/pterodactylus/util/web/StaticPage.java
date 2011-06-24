/*
 * utils - StaticPage.java - Copyright © 2010–2011 David Roden
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
import java.io.InputStream;

import net.pterodactylus.util.io.StreamCopier;

/**
 * {@link Page} implementation that delivers static files from the class path.
 *
 * @param <REQ>
 *            The type of the request
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class StaticPage<REQ extends Request> implements Page<REQ> {

	/** The prefix for {@link #getPath()}. */
	private final String pathPrefix;

	/** The path used as prefix when loading resources. */
	private final String resourcePathPrefix;

	/** The MIME type for the files this path contains. */
	private final String mimeType;

	/**
	 * Creates a new CSS page.
	 *
	 * @param pathPrefix
	 *            The prefix for {@link #getPath()}
	 * @param resourcePathPrefix
	 *            The path prefix when loading resources
	 * @param mimeType
	 *            The MIME type of the files this path contains
	 */
	public StaticPage(String pathPrefix, String resourcePathPrefix, String mimeType) {
		this.pathPrefix = pathPrefix;
		this.resourcePathPrefix = resourcePathPrefix;
		this.mimeType = mimeType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPath() {
		return pathPrefix;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Response handleRequest(REQ request, Response response) throws IOException {
		String path = request.getUri().getPath();
		int lastSlash = path.lastIndexOf('/');
		String filename = path.substring(lastSlash + 1);
		InputStream fileInputStream = getClass().getResourceAsStream(resourcePathPrefix + filename);
		if (fileInputStream == null) {
			return response.setStatusCode(404).setStatusText("Not found.");
		}
		StreamCopier.copy(fileInputStream, response.getContent());
		return response.setStatusCode(200).setStatusText("OK").setContentType(mimeType);
	}
}
