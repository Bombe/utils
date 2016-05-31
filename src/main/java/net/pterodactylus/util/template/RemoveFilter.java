/*
 * utils - RemoveFilter.java - Copyright © 2012–2016 David Roden
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

import net.pterodactylus.util.collection.filter.Filters;

/**
 * {@link Filter} implementation that can remove elements from a
 * {@link Collection}. The elements are given as parameter “value” and can
 * either be a single object or a {@link Collection}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class RemoveFilter implements Filter {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object format(TemplateContext templateContext, Object data, Map<String, Object> parameters) {
		final Object value = parameters.get("value");

		if (value instanceof Collection<?>) {
			return Filters.filteredCollection((Collection<?>) data, Filters.reverseFilter(new net.pterodactylus.util.collection.filter.Filter<Object>() {

				@Override
				public boolean filterObject(Object object) {
					return ((Collection<?>) value).contains(object);
				}
			}));
		}

		return Filters.filteredCollection((Collection<?>) data, Filters.reverseFilter(new net.pterodactylus.util.collection.filter.Filter<Object>() {

			@Override
			public boolean filterObject(Object object) {
				return ((object != null) && object.equals(value)) || ((object == null) && (value == null));
			}
		}));
	}

}
