/*
 * utils - ContainsFilter.java - Copyright © 2011–2019 David Roden
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

import java.util.Collection;
import java.util.Map;

/**
 * {@link Filter} implementation that returns a boolean, depending on whether
 * the object being filtered is contained in a {@link Collection} that is given
 * as parameter to this filter.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ContainsFilter implements Filter {

	//
	// FILTER METHODS
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object format(TemplateContext templateContext, Object data, Map<String, Object> parameters) {
		Object collection = parameters.get("collection");
		if (!(collection instanceof Collection<?>)) {
			return false;
		}
		return ((Collection<?>) collection).contains(data);
	}

}
