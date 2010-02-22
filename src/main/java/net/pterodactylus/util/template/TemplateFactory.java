/*
 * utils - TemplateFactory.java - Copyright © 2010 David Roden
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

package net.pterodactylus.util.template;

import java.io.Reader;

/**
 * Interface for factories that can create templates with pre-defined settings,
 * e.g. a template factory that creates templates with a default
 * {@link HtmlFilter} added.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public interface TemplateFactory {

	/**
	 * Creates a template that is read from the given source.
	 *
	 * @param templateSource
	 *            The source of the template
	 * @return A template that is created from the given source
	 */
	public Template createTemplate(Reader templateSource);

}
