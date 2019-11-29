/*
 * utils - DeletePlugin.java - Copyright © 2011–2019 David Roden
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

import net.pterodactylus.util.collection.TypeSafe;

/**
 * Plugin that will remove a value from the current {@link TemplateContext} by
 * setting it to {@code null}. The name of the value to delete is given as the
 * “key” parameter.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class DeletePlugin implements Plugin {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(TemplateContext templateContext, Map<String, String> parameters) {
		String key = TypeSafe.ensureType(parameters.get("key"), String.class);
		if (key == null) {
			return;
		}
		templateContext.set(key, null);
	}

}
