/*
 * utils - DataProvider.java - Copyright © 2010 David Roden
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
import java.util.StringTokenizer;

/**
 * Interface for objects that need to supply data to a {@link Template}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public abstract class DataProvider {

	/**
	 * Returns the object stored under the given name. The name can contain
	 * hierarchical structures separated by a dot (“.”), such as “loop.count” in
	 * which case a {@link Map} must be stored under “loop”.
	 *
	 * @param name
	 *            The name of the object to get
	 * @return The object
	 * @throws TemplateException
	 *             if the name or some objects can not be parsed or evaluated
	 */
	@SuppressWarnings("unchecked")
	public Object getData(String name) throws TemplateException {
		if (name.indexOf('.') == -1) {
			return retrieveData(name);
		}
		StringTokenizer nameTokens = new StringTokenizer(name, ".");
		Object object = null;
		while (nameTokens.hasMoreTokens()) {
			String nameToken = nameTokens.nextToken();
			if (object == null) {
				object = retrieveData(nameToken);
			} else {
				if (object instanceof Map) {
					object = ((Map<String, Object>) object).get(nameToken);
				} else {
					throw new TemplateException("object is not a map");
				}
			}
		}
		return object;
	}

	/**
	 * Returns the object with the given name.
	 *
	 * @param name
	 *            The name of the object
	 * @return The object, or {@code null} if an object with the given name does
	 *         not exist
	 */
	protected abstract Object retrieveData(String name);

}
