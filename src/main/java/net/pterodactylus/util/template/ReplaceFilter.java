/*
 * utils - ReplaceFilter.java - Copyright © 2010 David Roden
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
 * {@link Filter} implementation that replaces parts of a value.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ReplaceFilter implements Filter {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String format(DataProvider dataProvider, Object data, Map<String, String> parameters) {
		String input = String.valueOf(data);
		String needle = parameters.get("needle");
		String replacementKey = parameters.get("replacementKey");
		String replacement = parameters.get("replacement");
		if (replacement == null) {
			replacement = String.valueOf(dataProvider.getData(replacementKey));
		}
		return input.replace(needle, replacement);
	}

}
