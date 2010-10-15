/*
 * utils - XmlFilter.java - Copyright © 2010 David Roden
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

import java.util.HashMap;
import java.util.Map;

/**
 * Filters XML by replacing double quotes characters, apostrophes, the less-than
 * character, the greater-than characters, and the ampersand by their respective
 * XML entities.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class XmlFilter implements Filter {

	/** Map of defined XML entities. */
	private static final Map<Character, String> xmlEntities = new HashMap<Character, String>();

	static {
		xmlEntities.put('&', "amp");
		xmlEntities.put('\'', "apos");
		xmlEntities.put('>', "gt");
		xmlEntities.put('<', "lt");
		xmlEntities.put('"', "quot");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String format(DataProvider dataProvider, Object data, Map<String, String> parameters) {
		StringBuilder xmlOutput = new StringBuilder();
		for (char c : (data != null) ? String.valueOf(data).toCharArray() : new char[0]) {
			if (xmlEntities.containsKey(c)) {
				xmlOutput.append('&').append(xmlEntities.get(c)).append(';');
				continue;
			}
			xmlOutput.append(c);
		}
		return xmlOutput.toString();
	}

}
