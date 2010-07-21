/*
 * utils - ReadOnlyValue.java - Copyright © 2007-2009 David Roden
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
 * Implementation of a read-only value that never changes.
 *
 * @param <T>
 *            The real type of the value
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ReadOnlyValue<T> implements Value<T> {

	/** The wrapped value. */
	private final T value;

	/**
	 * Creates a new read-only value.
	 *
	 * @param value
	 *            The value to wrap
	 */
	public ReadOnlyValue(T value) {
		this.value = value;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see net.pterodactylus.util.config.Value#getValue()
	 */
	@Override
	public T getValue() throws ConfigurationException {
		return value;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see net.pterodactylus.util.config.Value#getValue(java.lang.Object)
	 */
	@Override
	public T getValue(T defaultValue) {
		return value;
	}

	/**
	 * {@inheritDoc} This method does nothing.
	 *
	 * @see net.pterodactylus.util.config.Value#setValue(java.lang.Object)
	 */
	@Override
	public void setValue(T newValue) throws ConfigurationException {
		/* ignore. */
	}

}
