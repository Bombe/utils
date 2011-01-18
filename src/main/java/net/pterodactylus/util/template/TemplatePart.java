/*
 * utils - TemplatePart.java - Copyright © 2010 David Roden
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

import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A {@link Part} that includes a complete {@link Template}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class TemplatePart implements Part {

	/** The template to include. */
	private final String templateName;

	/** The parameters. */
	private final Map<String, String> parameters;

	/**
	 * Creates a new template part.
	 *
	 * @param templateName
	 *            The name of the template to render
	 * @param parameters
	 *            The parameters for the included template
	 */
	public TemplatePart(String templateName, Map<String, String> parameters) {
		this.templateName = templateName;
		this.parameters = parameters;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void render(DataProvider dataProvider, Writer writer) throws TemplateException {
		Part template = dataProvider.getTemplate(templateName);
		if (template != null) {
			Map<String, Object> overrideObjects = new HashMap<String, Object>();
			for (Entry<String, String> parameter : parameters.entrySet()) {
				if (parameter.getValue().startsWith("=")) {
					overrideObjects.put(parameter.getKey(), parameter.getValue().substring(1));
				} else {
					overrideObjects.put(parameter.getKey(), dataProvider.get(parameter.getValue()));
				}
			}
			OverrideDataProvider overrideDataProvider = new OverrideDataProvider((template instanceof Template) ? ((Template) template).getDataProvider() : dataProvider, overrideObjects);
			template.render(new MultipleDataProvider(overrideDataProvider, new UnmodifiableDataProvider(dataProvider)), writer);
		}
	}

}
