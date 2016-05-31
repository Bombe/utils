/*
 * utils - DefaultTheme.java - Copyright © 2011–2016 David Roden
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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Default {@link Theme} implementation that uses a simple
 * {@link Collections#synchronizedMap(Map) synchronized map} to store themes.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class DefaultTheme implements Theme {

	/** The templates. */
	private final Map<String, Template> templates = Collections.synchronizedMap(new HashMap<String, Template>());

	//
	// ACCESSORS
	//

	/**
	 * Adds a template with the given name.
	 *
	 * @param templateName
	 *            The name of the template
	 * @param template
	 *            The template
	 */
	public void addTemplate(String templateName, Template template) {
		templates.put(templateName, template);
	}

	/**
	 * Returns the template with the given name.
	 *
	 * @param templateName
	 *            The name of the template
	 * @return The template with the given name, or {@code null} if no such
	 *         template exists
	 */
	public Template getTemplate(String templateName) {
		return templates.get(templateName);
	}

	//
	// THEME METHODS
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void render(String templateName, TemplateContext templateContext, Writer writer) throws TemplateException, IOException {
		Template template = getTemplate(templateName);
		if (template == null) {
			throw new TemplateException(String.format("Template “%s” not found.", templateName));
		}
		templateContext.mergeContext(template.getInitialContext());
		template.render(templateContext, writer);
	}

}
