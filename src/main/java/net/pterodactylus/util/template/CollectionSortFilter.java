/*
 * utils - CollectionSortFilter.java - Copyright © 2011 David Roden
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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * {@link Filter} that can sort a {@link Collection} or {@link Map}. A
 * {@code Collection} is sorted using {@link Collections#sort(List)}, and
 * {@link Map}s are sorted by inserting all items into a {@link TreeMap}. The
 * sorted items have to implement the {@link Comparable} interface or using this
 * will filter will result in a {@link ClassCastException}!
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class CollectionSortFilter implements Filter {

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object format(TemplateContext templateContext, Object data, Map<String, String> parameters) {
		if (data instanceof Collection<?>) {
			List list = new ArrayList((Collection<?>) data);
			Collections.sort(list);
			return list;
		}
		if (data instanceof Map<?, ?>) {
			return new TreeMap((Map) data);
		}
		return data;
	}

}
