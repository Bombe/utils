/*
 * utils - TemplateProvider.java - Copyright © 2010–2019 David Roden
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

/**
 * A provider is used to load templates that are included in other templates
 * when rendering a template.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public interface TemplateProvider {

	/**
	 * TemplateProvider that gets a {@link Template} from the {@link TemplateContext}.
	 */
	public static final TemplateProvider TEMPLATE_CONTEXT_PROVIDER = new TemplateContextProvider();

	/**
	 * Retrieves the template with the given name.
	 *
	 * @param templateContext
	 *            The template context
	 * @param templateName
	 *            The name of the template
	 * @return The template with the given name, or {@code null} if there is no
	 *         template with the requested name
	 */
	public Template getTemplate(TemplateContext templateContext, String templateName);

}
