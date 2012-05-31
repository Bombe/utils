/*
 * utils - PaginationFilter.java - Copyright © 2012 David Roden
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.pterodactylus.util.collection.Pagination;
import net.pterodactylus.util.number.Numbers;

/**
 * {@link Filter} implementation that paginates an {@link Iterable}. If the
 * object to filter is not an {@link Iterable}, it is returned directly.
 * Otherwise it will be turned into a {@link List} unless it already is one. The
 * filter understands the parameters “pageSize” (defaulting to 25), “page” (the
 * current page), and “pagination” (the name under which to store a
 * {@link Pagination} object in the {@link TemplateContext}, defaulting to
 * “pagination”).
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class PaginationFilter implements Filter {

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Object format(TemplateContext templateContext, Object data, Map<String, Object> parameters) {
		List<Object> list = null;
		if (data instanceof List<?>) {
			list = (List<Object>) data;
		} else if (data instanceof Iterable<?>) {
			Iterator<Object> iterator = ((Iterable<Object>) data).iterator();
			list = new ArrayList<Object>();
			while (iterator.hasNext()) {
				list.add(iterator.next());
			}
		} else {
			return data;
		}
		int pageSize = Numbers.safeParseInteger(parameters.get("pageSize"), 25);
		int page = Numbers.safeParseInteger(parameters.get("page"), 0);
		String paginationName = String.valueOf(parameters.get("pagination"));
		if (paginationName == null) {
			paginationName = "pagination";
		}
		Pagination<Object> pagination = new Pagination<Object>(list, pageSize).setPage(page);
		templateContext.set(paginationName, pagination);
		return pagination.getItems();
	}

}
