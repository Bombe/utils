/*
 * utils - AbstractValue.java - Copyright © 2007-2009 David Roden
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

package net.pterodactylus.util.config;

/**
 * An abstract implementation of a {@link Value}.
 *
 * @param <T>
 *            The type of the wrapped value
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public abstract class AbstractValue<T> implements Value<T> {

	/** The configuration that created this value. */
	protected final Configuration configuration;

	/** The name of the attribute this is the value for. */
	protected final String attribute;

	/**
	 * Creates a new value that reads its values from the given configuration
	 * backend.
	 *
	 * @param configuration
	 *            The configuration that created this value
	 * @param attribute
	 *            The name of this value’s attribute
	 */
	public AbstractValue(Configuration configuration, String attribute) {
		this.configuration = configuration;
		this.attribute = attribute;
	}

}
