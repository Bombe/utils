/*
 * utils - MatchFilter.java - Copyright © 2010 David Roden
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
 * {@link Filter} implementation that compares (for
 * {@link Object#equals(Object) equality}) the data either with a {@link String}
 * (given as parameter “value”) or an object from the {@link DataProvider}
 * (whose name is given as parameter “key”).
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class MatchFilter implements Filter {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object format(DataProvider dataProvider, Object data, Map<String, String> parameters) {
		String key = parameters.get("key");
		Object value = parameters.get("value");
		if (value == null) {
			value = dataProvider.getData(key);
		}
		if (value instanceof String) {
			return value.equals(String.valueOf(data));
		}
		return (value != null) ? value.equals(data) : (data == null);
	}

}
