/*
 * utils - Header.java - Copyright © 2011–2019 David Roden
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

package net.pterodactylus.util.web;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A single HTTP header field which can have multiple values.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Header implements Iterable<String> {

	/** The name of this header field. */
	private final String name;

	/** The values of this header field. */
	private final List<String> values = new ArrayList<String>();

	/**
	 * Creates a new header field with the given name.
	 *
	 * @param name
	 *            The name of the header field.
	 */
	public Header(String name) {
		this.name = name;
	}

	//
	// ACCESSORS
	//

	/**
	 * Returns the name of this header field.
	 *
	 * @return The name of this header field.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns whether this header field has a value matching the given value.
	 *
	 * @param value
	 *            The value to locate
	 * @return {@code true} if this header field already has a value matching
	 *         the given value, {@code false} otherwise
	 */
	public boolean hasValue(String value) {
		return values.contains(value);
	}

	/**
	 * Adds the given value to this header field.
	 *
	 * @param value
	 *            The value to add
	 */
	public void addValue(String value) {
		values.add(value);
	}

	//
	// ITERABLE METHODS
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<String> iterator() {
		return values.iterator();
	}

}
