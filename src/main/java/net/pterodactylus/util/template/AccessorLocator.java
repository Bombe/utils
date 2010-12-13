/*
 * utils - AccessorLocator.java - Copyright © 2010 David Roden
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
import java.util.HashMap;
import java.util.Map;

/**
 * An accessor locator stores mappings from {@link Class}es to {@link Accessor}s
 * and allows locating an {@link Accessor} for any given class, traversing
 * superclass and interface hierarchy.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class AccessorLocator {

	/** Accessors. */
	private final Map<Class<?>, Accessor> classAccessors = Collections.synchronizedMap(new HashMap<Class<?>, Accessor>());

	/**
	 * Creates a new accessor locator.
	 */
	public AccessorLocator() {
		classAccessors.put(Map.class, Accessor.MAP_ACCESSOR);
	}

	/**
	 * Creates a new accessor locator that contains all accessor mappings from
	 * the given locator.
	 *
	 * @param accessorLocator
	 *            The accessor locator to copy
	 */
	public AccessorLocator(AccessorLocator accessorLocator) {
		classAccessors.putAll(accessorLocator.classAccessors);
	}

	/**
	 * Adds an accessor for objects of the given class.
	 *
	 * @param clazz
	 *            The class of the objects to handle with the accessor
	 * @param accessor
	 *            The accessor to handle the objects with
	 */
	public void addAccessor(Class<?> clazz, Accessor accessor) {
		classAccessors.put(clazz, accessor);
	}

	/**
	 * Finds an accessor that can handle the given class. If
	 * {@link #classAccessors} does not contain a perfect match, a match to a
	 * superclass or superinterface is searched.
	 *
	 * @param clazz
	 *            The class to get an accessor for
	 * @return The accessor for the given class, or {@code null} if no accessor
	 *         could be found
	 */
	public Accessor findAccessor(Class<?> clazz) {
		if (classAccessors.containsKey(clazz)) {
			return classAccessors.get(clazz);
		}
		for (Class<?> interfaceClass : clazz.getInterfaces()) {
			if (classAccessors.containsKey(interfaceClass)) {
				return classAccessors.get(interfaceClass);
			}
		}
		Class<?> classToCheck = clazz.getSuperclass();
		while (classToCheck != null) {
			if (classAccessors.containsKey(classToCheck)) {
				return classAccessors.get(classToCheck);
			}
			for (Class<?> interfaceClass : classToCheck.getInterfaces()) {
				if (classAccessors.containsKey(interfaceClass)) {
					return classAccessors.get(interfaceClass);
				}
			}
			classToCheck = classToCheck.getSuperclass();
		}
		return null;
	}

}
