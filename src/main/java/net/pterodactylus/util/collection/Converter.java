/*
 * utils - Converter.java - Copyright © 2011 David Roden
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

package net.pterodactylus.util.collection;

import net.pterodactylus.util.collection.mapper.Mapper;

/**
 * Interface for objects that can convert one object into another.
 *
 * @param <I>
 *            The type of the input object
 * @param <O>
 *            The type of the output object
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 * @deprecated Use {@link Mapper} instead
 */
@Deprecated
public interface Converter<I, O> {

	/**
	 * Converts the given input object into another object.
	 *
	 * @param input
	 *            The object to convert
	 * @return The converted object
	 */
	public O convert(I input);

}
