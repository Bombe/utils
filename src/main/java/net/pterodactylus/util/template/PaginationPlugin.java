/*
 * utils - PaginationPlugin.java - Copyright © 2010 David Roden
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

import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.pterodactylus.util.collection.Pagination;

/**
 * {@link Plugin} implementation that takes care of paginating a {@link List} of
 * items (parameter “list”).
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class PaginationPlugin implements Plugin {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(DataProvider dataProvider, Map<String, String> parameters) {
		String listKey = parameters.get("list");
		String pageSizeString = parameters.get("pagesize");
		String pageKey = parameters.get("page");
		String paginationKey = parameters.get("key");
		if (pageKey == null) {
			pageKey = "page";
		}
		if (paginationKey == null) {
			paginationKey = "pagination";
		}
		String pageString = String.valueOf(dataProvider.getData(pageKey));
		int page = 0;
		try {
			page = Integer.parseInt(pageString);
		} catch (NumberFormatException nfe1) {
			/* ignore. */
		}
		int pageSize = 25;
		try {
			pageSize = Integer.parseInt(pageSizeString);
		} catch (NumberFormatException nfe1) {
			/* ignore. */
		}
		List<?> list = (List<?>) dataProvider.getData(listKey);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Pagination<?> pagination = new Pagination((list == null) ? Collections.emptyList() : list, pageSize).setPage(page);
		dataProvider.setData(paginationKey, pagination);
	}
}
