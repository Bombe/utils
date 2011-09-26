/*
 * utils - Classes.java - Copyright © 2011 David Roden
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

package net.pterodactylus.util.object;

import java.util.Collection;

/**
 * Helper class that simplifies handling of {@link Class}es.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Classes {

	/**
	 * Returns the class from {@code classes} that is closest to the given
	 * target class. The closest class is searched for in this order:
	 * <ul>
	 * <li>First, the class itself is checked.</li>
	 * <li>Then, all interfaces implemented by the class are checked.</li>
	 * <li>Both checks are then repeated with the superclass of the target
	 * class, until the top-most class (which is always {@link Object}) is
	 * reached.</li>
	 *
	 * @param targetClass
	 *            The target class
	 * @param classes
	 *            The possible classes from which a match should be located
	 * @return The closest class, or {@code null} if no given class matches the
	 *         given target class
	 */
	public static Class<?> closest(Class<?> targetClass, Collection<? extends Class<?>> classes) {
		Class<?> currentClass = targetClass;
		while (currentClass != null) {
			if (classes.contains(currentClass)) {
				return currentClass;
			}
			for (Class<?> interfaceClass : currentClass.getInterfaces()) {
				if (classes.contains(interfaceClass)) {
					return interfaceClass;
				}
			}
			currentClass = currentClass.getSuperclass();
		}
		return null;
	}

}
