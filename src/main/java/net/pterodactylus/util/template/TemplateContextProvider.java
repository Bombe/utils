/*
 * utils - TemplateContextProvider.java - Copyright © 2011 David Roden
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
 * {@link Provider} implementation that returns a {@link Template} from a
 * {@link TemplateContext}. If the object in the context is a {@link Part}, a
 * {@link Template} will be wrapped around it.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class TemplateContextProvider implements Provider {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Template getTemplate(TemplateContext templateContext, String templateName) {
		Object templateObject = templateContext.get(templateName);
		if (templateObject instanceof Template) {
			return (Template) templateObject;
		} else if (templateObject instanceof Part) {
			Part part = (Part) templateObject;
			Template template = new Template();
			template.add(part);
			return template;
		}
		return null;
	}

}
