/*
 * utils - RedirectException.java - Copyright © 2012–2016 David Roden
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

import net.pterodactylus.util.template.TemplateContext;

/**
 * Exception that can be thrown to signal that a subclassed {@link TemplatePage}
 * wants to redirect the user during the
 * {@link TemplatePage#processTemplate(TemplateContext, Request)} method call.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class RedirectException extends Exception {

	/** The target to redirect to. */
	private final String target;

	/**
	 * Creates a new redirect exception.
	 *
	 * @param target
	 *            The target of the redirect
	 */
	public RedirectException(String target) {
		this.target = target;
	}

	/**
	 * Returns the target to redirect to.
	 *
	 * @return The target to redirect to
	 */
	public String getTarget() {
		return target;
	}

}
