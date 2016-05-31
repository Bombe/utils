/*
 * utils - FilteredPart.java - Copyright © 2010–2016 David Roden
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

import net.pterodactylus.util.io.Renderable;
import net.pterodactylus.util.template.TemplateParser.Filters;

/**
 * {@link Part} implementation that runs the output of another part through one
 * or more {@link Filter}s.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
class FilteredPart extends AbstractPart {

	/** The name of the data object to filter. */
	private final String name;

	/** The filters to apply. */
	private final Filters filters;

	/**
	 * Creates a new filtered part.
	 *
	 * @param line
	 *            The line number of the tag
	 * @param column
	 *            The column number of the tag
	 * @param name
	 *            The name of the data object
	 * @param filters
	 *            The filters to apply
	 */
	public FilteredPart(int line, int column, String name, Filters filters) {
		super(line, column);
		this.name = name;
		this.filters = filters;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void render(TemplateContext templateContext, Writer writer) throws TemplateException {
		Object data = filters.filter(getLine(), getColumn(), templateContext, templateContext.get(name));
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
