/*
 * utils - Plugin.java - Copyright © 2010 David Roden
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
 * Defines a template plugin. A plugin can be called just like the built-in
 * functions, e.g. “<%plugin>”. It also can have parameters just like a filter,
 * e.g. “<%plugin parameter=value>”.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public interface Plugin {

	/**
	 * Executes the plugin.
	 *
	 * @param dataProvider
	 *            The data provider
	 * @param parameters
	 *            The plugin parameters
	 */
	public void execute(DataProvider dataProvider, Map<String, String> parameters);

}
