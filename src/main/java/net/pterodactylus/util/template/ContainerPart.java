/*
 * utils - ContainerPart.java - Copyright © 2010 David Roden
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A {@link Part} that can contain multiple other {@code Part}s.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
class ContainerPart extends Part implements Iterable<Part> {

	/** The parts this part contains. */
	protected final List<Part> parts = new ArrayList<Part>();

	/**
	 * Creates a new container part that contains the given parts
	 */
	public ContainerPart() {
		/* do nothing. */
	}

	/**
	 * /** Creates a new container part that contains the given parts
	 *
	 * @param parts
	 *            The parts the container part contains
	 */
	public ContainerPart(List<Part> parts) {
		this.parts.addAll(parts);
	}

	/**
	 * Adds the given part.
	 *
	 * @param part
	 *            The part to add
	 */
	public void add(Part part) {
		parts.add(part);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void render(DataProvider dataProvider, Writer writer) throws TemplateException {
		for (Part part : parts) {
			part.render(dataProvider, writer);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<Part> iterator() {
		return parts.iterator();
	}

}
