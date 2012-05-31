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
import java.util.Map;
import java.util.Map.Entry;

/**
 * A {@link Part} that includes a complete {@link Template}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class TemplatePart extends AbstractPart {

	/** The template to include. */
	private final String templateName;

	/** The parameters. */
	private final Map<String, String> parameters;

	/**
	 * Creates a new template part.
	 *
	 * @param line
	 *            The line number of the tag, if any
	 * @param column
	 *            The column number of the tag, if any
	 * @param templateName
	 *            The name of the template to render
	 * @param parameters
	 *            The parameters for the included template
	 */
	public TemplatePart(int line, int column, String templateName, Map<String, String> parameters) {
		super(line, column);
		this.templateName = templateName;
		this.parameters = parameters;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void render(TemplateContext templateContext, Writer writer) throws TemplateException {
		Template template = templateContext.getTemplate(templateName);
		if (template == null) {
			throw new TemplateException(getLine(), getColumn(), "Template “" + templateName + "” not found.");
		}
		TemplateContext includedContext = new TemplateContext(templateContext);
		for (Entry<String, String> parameter : parameters.entrySet()) {
			if (parameter.getValue().startsWith("=")) {
				includedContext.set(parameter.getKey(), parameter.getValue().substring(1));
			} else {
				includedContext.set(parameter.getKey(), templateContext.get(parameter.getValue()));
			}
		}
		template.render(includedContext, writer);
	}

}
