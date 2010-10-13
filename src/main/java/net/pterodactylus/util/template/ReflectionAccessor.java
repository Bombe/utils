/*
 * utils - ReflectionAccessor.java - Copyright © 2010 David Roden
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * {@link Accessor} implementation that checks the object for a method that
 * looks like a getter for the requested member name. If “object.data” is
 * requested, the methods “getData()” and “isData()” are checked,
 * “object.realName” would search for the methods “getRealName()” and
 * “isRealName()”.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ReflectionAccessor implements Accessor {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object get(DataProvider dataProvider, Object object, String member) {
		Method method = null;
		String methodName = member.substring(0, 1).toUpperCase() + member.substring(1);
		try {
			method = object.getClass().getMethod("get" + methodName);
		} catch (SecurityException se1) {
			/* TODO - logging. */
		} catch (NoSuchMethodException nsme1) {
			/* swallow, method just doesn’t exist. */
		}
		if (method == null) {
			try {
				method = object.getClass().getMethod("is" + methodName);
			} catch (SecurityException e) {
				/* TODO - logging. */
			} catch (NoSuchMethodException e) {
				/* swallow, method just doesn’t exist. */
			}
		}
		if (method == null) {
			try {
				method = object.getClass().getMethod(member);
			} catch (SecurityException e) {
				/* TODO - logging. */
			} catch (NoSuchMethodException e) {
				/* swallow, method just doesn’t exist. */
			}
		}
		if (method != null) {
			try {
				return method.invoke(object);
			} catch (IllegalArgumentException iae1) {
				/* TODO - logging. */
			} catch (IllegalAccessException iae1) {
				/* TODO - logging. */
			} catch (InvocationTargetException ite1) {
				/* TODO - logging. */
			}
		}
		return null;
	}

}
