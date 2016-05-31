/*
 * utils - Factory.java - Copyright © 2011–2016 David Roden
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

package net.pterodactylus.util.storage;

/**
 * Interface for factory classes that can create objects from a byte array.
 *
 * @param <T>
 *            The type of the object to create
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public interface Factory<T> {

	/**
	 * Creates an object from the given byte array.
	 *
	 * @param buffer
	 *            The byte array with the object’s contents
	 * @return The object
	 */
	public T restore(byte[] buffer);

}
