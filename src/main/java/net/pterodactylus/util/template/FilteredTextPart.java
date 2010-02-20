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
import java.util.Arrays;
import java.util.Collection;

/**
 * {@link Part} implementation that runs a predefined text through one or more
 * {@link Filter}s.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class FilteredTextPart extends Part {

	/** The text to filter. */
	private final String text;

	/** The filters to apply. */
	private final Collection<Filter> filters;

	/**
	 * Creates a new filtered part.
	 *
	 * @param text
	 *            The text to filter
	 * @param filters
	 *            The filters to apply
	 */
	public FilteredTextPart(String text, Filter... filters) {
		this(text, Arrays.asList(filters));
	}

	/**
	 * Creates a new filtered part.
	 *
	 * @param text
	 *            The text to filter
	 * @param filters
	 *            The filters to apply
	 */
	public FilteredTextPart(String text, Collection<Filter> filters) {
		this.text = text;
		this.filters = filters;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void render(DataProvider dataProvider, Writer writer) throws IOException, TemplateException {
		String output = text;
		for (Filter filter : filters) {
			output = filter.format(output);
		}
		writer.write(output);
	}

}
