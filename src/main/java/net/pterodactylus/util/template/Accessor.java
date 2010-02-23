/*
 * utils - Accessor.java - Copyright © 2010 David Roden
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

import java.util.Map;

/**
 * An accessor can access member variables of objects of a given type.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public interface Accessor {

	/** Accessor for {@link Map}s. */
	public static final Accessor MAP_ACCESSOR = new MapAccessor();

	/**
	 * Returns the member with the given name.
	 *
	 * @param dataProvider
	 *            The current data provider
	 * @param object
	 *            The object to access
	 * @param member
	 *            The name of the member
	 * @return The member, or {@code null} if the member does not exist
	 */
	public Object get(DataProvider dataProvider, Object object, String member);

}

/**
 * {@link Accessor} implementation that can access values in a {@link Map}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
class MapAccessor implements Accessor {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object get(DataProvider dataProvider, Object object, String member) {
		return ((Map<?, ?>) object).get(member);
	}

}
