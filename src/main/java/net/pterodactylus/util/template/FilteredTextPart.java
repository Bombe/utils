/*
 * utils - FilteredTextPart.java - Copyright © 2010 David Roden
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
import java.util.List;

import net.pterodactylus.util.io.Renderable;
import net.pterodactylus.util.template.TemplateParser.FilterDefinition;

/**
 * {@link Part} implementation that runs a predefined text through one or more
 * {@link Filter}s.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
class FilteredTextPart extends AbstractPart {

	/** The text to filter. */
	private final String text;

	/** The filters to apply. */
	private final List<FilterDefinition> filterDefinitions;

	/**
	 * Creates a new filtered part.
	 *
	 * @param line
	 *            The line number of the tag
	 * @param column
	 *            The column number of the tag
	 * @param text
	 *            The text to filter
	 * @param filterDefinitions
	 *            The filters to apply
	 */
	public FilteredTextPart(int line, int column, String text, List<FilterDefinition> filterDefinitions) {
		super(line, column);
		this.text = text;
		this.filterDefinitions = filterDefinitions;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void render(TemplateContext templateContext, Writer writer) throws TemplateException {
		Object data = text;
		for (FilterDefinition filterDefinition : filterDefinitions) {
			Filter filter = templateContext.getFilter(filterDefinition.getName());
			if (filter == null) {
				throw new TemplateException(getLine(), getColumn(), "Filter “" + filterDefinition.getName() + "” not found.");
			}
			data = filter.format(templateContext, data, filterDefinition.getParameters());
		}
		try {
			if (data instanceof Renderable) {
				((Renderable) data).render(writer);
			} else {
				writer.write((data != null) ? String.valueOf(data) : "");
			}
		} catch (IOException ioe1) {
			throw new TemplateException(getLine(), getColumn(), "Can not render part.", ioe1);
		}
	}

}
