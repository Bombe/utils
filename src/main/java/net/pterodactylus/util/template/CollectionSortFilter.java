/*
 * utils - CollectionSortFilter.java - Copyright © 2011–2019 David Roden
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
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.pterodactylus.util.object.Classes;

/**
 * {@link Filter} that can sort a {@link Collection} or {@link Map}. A {@code
 * Collection} is sorted using {@link Collections#sort(List)}, and {@link Map}s
 * are sorted by inserting all items into a {@link TreeMap}. It is possible to
 * use comparators for certain classes; in order for those to be used, the
 * collection must return an object of a matching type as the first item of its
 * iterator. If comparators are not used, the sorted items have to implement the
 * {@link Comparable} interface or using this will filter will result in a
 * {@link ClassCastException}!
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class CollectionSortFilter implements Filter {

	/** Comparators. */
	private final Map<Class<?>, Comparator<?>> comparators = new HashMap<Class<?>, Comparator<?>>();

	//
	// ACCESSORS
	//

	/**
	 * Adds a comparator to use when an object of the given class is found as
	 * first item in a collection’s iterator.
	 *
	 * @param clazz
	 *            The class to use the comparator for
	 * @param comparator
	 *            The comparator to use
	 * @param <T>
	 *            The type of the object to compare
	 */
	public <T> void addComparator(Class<T> clazz, Comparator<? super T> comparator) {
		comparators.put(clazz, comparator);
	}

	//
	// FILTER METHODS
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Object format(TemplateContext templateContext, Object data, Map<String, Object> parameters) {
		if (data instanceof Collection<?>) {
			List list = new ArrayList((Collection<?>) data);
			if (!list.isEmpty()) {
				Class<?> firstElementClass = list.get(0).getClass();
				Class<?> comparatorClass = Classes.closest(firstElementClass, comparators.keySet());
				if (comparatorClass != null) {
					Comparator comparator = comparators.get(comparatorClass);
					Collections.sort(list, comparator);
				} else {
					Collections.sort(list);
				}
			}
			return list;
		}
		if (data instanceof Map<?, ?>) {
			return new TreeMap((Map<?, ?>) data);
		}
		return data;
	}

}
