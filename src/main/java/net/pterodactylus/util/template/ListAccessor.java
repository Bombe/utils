/*
 * utils - ListAccessor.java - Copyright © 2010 David Roden
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

import java.util.List;

/**
 * {@link Accessor} implementation that allows to access a {@link List} by
 * index. “list.size” will return the size of the list, “list.isEmpty” will
 * return the result of {@link List#isEmpty()}, and “list.0” will return the
 * first element of the list, “list.1” the second, and so on.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ListAccessor implements Accessor {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object get(DataProvider dataProvider, Object object, String member) {
		List<?> list = (List<?>) object;
		if ("size".equals(member)) {
			return list.size();
		} else if ("isEmpty".equals(member)) {
			return list.isEmpty();
		}
		int index = -1;
		try {
			index = Integer.parseInt(member);
		} catch (NumberFormatException nfe1) {
			/* ignore. */
		}
		if ((index > -1) && (index < list.size())) {
			return list.get(index);
		}
		return null;
	}

}
