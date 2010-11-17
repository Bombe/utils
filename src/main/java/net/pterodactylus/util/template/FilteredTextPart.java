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
import java.util.Collection;
import java.util.Map;

import net.pterodactylus.util.io.Renderable;

/**
 * {@link Part} implementation that runs a predefined text through one or more
 * {@link Filter}s.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
class FilteredTextPart extends Part {

	/** The text to filter. */
	private final String text;

	/** The filters to apply. */
	private final Collection<Filter> filters;

	/** Parameters for all filters. */
	private final Map<Filter, Map<String, String>> allFilterParameters;

	/**
	 * Creates a new filtered part.
	 *
	 * @param text
	 *            The text to filter
	 * @param filters
	 *            The filters to apply
	 * @param allFilterParameters
	 *            Parameters for all filters
	 */
	public FilteredTextPart(String text, Collection<Filter> filters, Map<Filter, Map<String, String>> allFilterParameters) {
		this.text = text;
		this.filters = filters;
		this.allFilterParameters = allFilterParameters;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void render(DataProvider dataProvider, Writer writer) throws TemplateException {
		Object output = text;
		for (Filter filter : filters) {
			output = filter.format(dataProvider, output, allFilterParameters.get(filter));
		}
		try {
			if (output instanceof Renderable) {
				((Renderable) output).render(writer);
			} else {
				writer.write((output != null) ? String.valueOf(output) : "");
			}
		} catch (IOException ioe1) {
			throw new TemplateException("Can not render part.", ioe1);
		}
	}

}
