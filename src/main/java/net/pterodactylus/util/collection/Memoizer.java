/*
 * utils - Memoizer.java - Copyright © 2012–2019 David Roden
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

import java.util.concurrent.Callable;

/**
 * Class that caches a single value. A given {@link Callable} is called once
 * when the value is first requested; the same value is return for all following
 * invocations of {@link #get()}.
 *
 * @param <T>
 *            The type of the cached value
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Memoizer<T> {

	/** The callable to retrieve the value. */
	private final Callable<T> callable;

	/** Whether the callable was already called. */
	private boolean called;

	/** The retrieved value. */
	private T value;

	/**
	 * Creates a new memoizer for the given callable.
	 *
	 * @param callable
	 *            The callable to retrieve the value
	 */
	public Memoizer(Callable<T> callable) {
		this.callable = callable;
	}

	/**
	 * Returns the memoized value. On the first invocation of this method, the
	 * callable used for constructing this memoizer is called; its return value
	 * is returned for all following invocations of this method.
	 * <p>
	 * {@link Callable#call()} is declard to throw arbitrary exceptions. All
	 * exceptions are caught and wrapped into a {@link RuntimeException} which
	 * is then thrown from this method.
	 *
	 * @return The retrieved value
	 * @throws RuntimeException
	 *             if the callable throws an exception
	 */
	public T get() throws RuntimeException {
		if (!called) {
			try {
				value = callable.call();
			} catch (Exception e1) {
				throw new RuntimeException("Could not get value from callable!", e1);
			}
			called = true;
		}
		return value;
	}

}
