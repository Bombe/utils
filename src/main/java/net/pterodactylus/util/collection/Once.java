/*
 * utils - Once.java - Copyright © 2011–2016 David Roden
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

/**
 * Wrapper for an object that should once be returned once.
 *
 * @param <T>
 *            The type of the object
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Once<T> {

	/** The object to return on the first call. */
	private final T object;

	/** The object to return on all other calls. */
	private final T emptyObject;

	/** Whether the object has already been retrieved. */
	private boolean retrieved;

	/**
	 * Creates a new one-time wrapper, returning the given object on the first
	 * call to {@link #get()} and {@code null} on all subsequent calls.
	 *
	 * @param object
	 *            The object to return on the first call
	 */
	public Once(T object) {
		this(object, null);
	}

	/**
	 * Creates a new one-time wrapper, returning {@code object} on the first
	 * call to {@link #get()} and {@code emptyObject} on all subsequent calls.
	 *
	 * @param object
	 *            The object to return on the first call
	 * @param emptyObject
	 *            The object to return on all subsequent calls
	 */
	public Once(T object, T emptyObject) {
		this.object = object;
		this.emptyObject = emptyObject;
	}

	/**
	 * Returns the wrapped object, or an “empty object” (might be {@code null})
	 * if the object has already been retrieved by an earlier call to
	 * {@link #get()}.
	 *
	 * @return The wrapped object
	 */
	public T get() {
		if (retrieved) {
			return emptyObject;
		}
		retrieved = true;
		return object;
	}

	/**
	 * Returns whether the wrapped object has already been retrieved.
	 *
	 * @return {@code true} if the object has already been retrieved by a call
	 *         to {@link #get()}, {@code false} otherwise
	 */
	public boolean isRetrieved() {
		return retrieved;
	}

	/**
	 * Resets whether the wrapped object was retrieved by a call to
	 * {@link #get()}. The first call to {@link #get()} after calling this
	 * method will return the wrapped object once again.
	 */
	public void reset() {
		retrieved = false;
	}

}
