/*
 * utils - TextPart.java - Copyright © 2010–2016 David Roden
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

/**
 * A {@link Part} that contains only text.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
class TextPart extends AbstractPart {

	/** The text of the part. */
	private final String text;

	/**
	 * @param line
	 *            The line number of the tag, if any
	 * @param column
	 *            The column number of the tag, if any Creates a new text part.
	 * @param text
	 *            The text of the part
	 */
	public TextPart(int line, int column, String text) {
		super(line, column);
		this.text = text;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void render(TemplateContext templateContext, Writer writer) throws TemplateException {
		try {
			writer.write(text);
		} catch (IOException ioe1) {
			throw new TemplateException(getLine(), getColumn(), "Can not render part.", ioe1);
		}
	}

}
