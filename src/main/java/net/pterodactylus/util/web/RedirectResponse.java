/*
 * utils - RedirectResponse.java - Copyright © 2010–2011 David Roden
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

/**
 * {@link Response} implementation that performs an HTTP redirect.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class RedirectResponse extends Response {

	/**
	 * Creates a new redirect response to the new location.
	 *
	 * @param newLocation
	 *            The new location
	 */
	public RedirectResponse(String newLocation) {
		this(newLocation, true);
	}

	/**
	 * Creates a new redirect response to the new location.
	 *
	 * @param newLocation
	 *            The new location
	 * @param permanent
	 *            Whether the redirect should be marked as permanent
	 */
	public RedirectResponse(String newLocation, boolean permanent) {
		super(permanent ? 302 : 307, "Redirected", null, createHeader("Location", newLocation));
	}

}
