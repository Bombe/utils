/*
 * utils - DataProviderPart.java - Copyright © 2010 David Roden
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

/**
 * A {@link Part} whose content is dynamically fetched from a
 * {@link TemplateContext}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
class DataPart extends AbstractPart {

	/** The name of the object to get. */
	private final String name;

	/**
	 * Creates a new data provider part.
	 *
	 * @param line
	 *            The line number of the tag
	 * @param column
	 *            The column number of the tag
	 * @param name
	 *            The name of the object
	 */
	public DataPart(int line, int column, String name) {
		super(line, column);
		this.name = name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void render(TemplateContext templateContext, Writer writer) throws TemplateException {
		Object output = templateContext.get(name);
		try {
			if (output instanceof Renderable) {
				((Renderable) output).render(writer);
			} else {
				writer.write((output != null) ? String.valueOf(output) : "");
			}
		} catch (IOException ioe1) {
			throw new TemplateException(getLine(), getColumn(), "Can not render part.", ioe1);
		}
	}

}