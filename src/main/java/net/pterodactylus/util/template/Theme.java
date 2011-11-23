/*
 * utils - Theme.java - Copyright © 2011 David Roden
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

import java.io.IOException;
import java.io.Writer;

/**
 * A theme bundles an arbitrary amount of {@link Template}s together and can
 * perform additional processing around it. One example would be embedding the
 * content of the rendered templates into a different templating or generation
 * system.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public interface Theme {

	/**
	 * Renders the template with the given name using the the given template
	 * context to the given writer.
	 *
	 * @param templateName
	 *            The name of the template to render
	 * @param templateContext
	 *            The template context
	 * @param writer
	 *            The writer
	 * @throws TemplateException
	 *             if a template error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public void render(String templateName, TemplateContext templateContext, Writer writer) throws TemplateException, IOException;

}
