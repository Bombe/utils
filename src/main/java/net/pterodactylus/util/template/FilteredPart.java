/*
 * utils - FilteredPart.java - Copyright © 2010 David Roden
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
 * {@link Part} implementation that runs the output of another part through one
 * or more {@link Filter}s.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
class FilteredPart extends Part {

	/** The name of the data object to filter. */
	private final String name;

	/** The filters to apply. */
	private final Collection<Filter> filters;

	/** Parameters for all filters. */
	private final Map<Filter, Map<String, String>> allFilterParameters;

	/**
	 * Creates a new filtered part.
	 *
	 * @param name
	 *            The name of the data object
	 * @param filters
	 *            The filters to apply
	 * @param allFilterParameters
	 *            All filters’ parameters
	 */
	public FilteredPart(String name, Collection<Filter> filters, Map<Filter, Map<String, String>> allFilterParameters) {
		this.name = name;
		this.filters = filters;
		this.allFilterParameters = allFilterParameters;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void render(DataProvider dataProvider, Writer writer) throws TemplateException {
		Object data = dataProvider.getData(name);
		Object output = data;
		for (Filter filter : filters) {
			data = output = filter.format(dataProvider, data, allFilterParameters.get(filter));
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
