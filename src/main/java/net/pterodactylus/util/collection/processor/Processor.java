/*
 * utils - Processor.java - Copyright © 2011–2019 David Roden
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
package net.pterodactylus.util.collection.processor;

import net.pterodactylus.util.collection.Container;

/**
 * A processor is used to process all elements of a {@link Container}.
 *
 * @see Container#process(Processor)
 * @param <T>
 *            The type of the elements to process
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public interface Processor<T> {

	/**
	 * Processes the given object.
	 *
	 * @param object
	 *            The object to process
	 */
	public void process(T object);

}
