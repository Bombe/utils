/*
 * utils - Container.java - Copyright © 2011 David Roden
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

package net.pterodactylus.util.container;

import junit.framework.TestCase;

/**
 * JUnit test case for {@link Container}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ContainerTest extends TestCase {

	/**
	 * Tests the constructors with various arguments.
	 *
	 * @see Container#Container()
	 * @see Container#Container(Object)
	 * @see Container#Container(Object[])
	 */
	@SuppressWarnings("unused")
	public void testConstructor() {
		/* constructor without arguments. */
		new Container<Object>();

		/* constructor with a single argument. */
		new Container<Object>(new Object());
		new Container<Object>((Object) null);

		/* constructor with array argument. */
		new Container<Object>(new Object[] { null, null });
		new Container<Object>(new Object[] { null, new Object() });
		new Container<Object>(new Object[] { new Object(), null });
		new Container<Object>(new Object[] { new Object(), new Object() });
		try {
			new Container<Object>((Object[]) null);
			fail();
		} catch (RuntimeException re1) {
			/* expected. */
		}
	}

	/**
	 * Tests the {@link Container#size()} method return values.
	 */
	public void testSize() {
		Container<Object> container;

		container = new Container<Object>();
		assertEquals("Container Size", 0, container.size());

		container = new Container<Object>(new Object());
		assertEquals("Container Size", 1, container.size());

		container = new Container<Object>(new Object[] { new Object(), new Object() });
		assertEquals("Container Size", 2, container.size());

		container = new Container<Object>(new Object[] { new Object(), new Object(), new Object(), new Object() });
		assertEquals("Container Size", 4, container.size());
	}

	/**
	 * Tests the {@link Container#isEmpty()} method.
	 */
	public void testIsEmpty() {
		Container<Object> container;

		container = new Container<Object>();
		assertEquals("Container is empty", true, container.isEmpty());

		container = new Container<Object>(new Object());
		assertEquals("Container is empty", false, container.isEmpty());

		container = new Container<Object>(new Object[] { new Object(), new Object() });
		assertEquals("Container is empty", false, container.isEmpty());

		container = new Container<Object>(new Object[] { new Object(), new Object(), new Object(), new Object() });
		assertEquals("Container is empty", false, container.isEmpty());
	}

	/**
	 * Tests the {@link Container#get()} method.
	 */
	public void testGet() {
		Container<Object> container;
		Object object1 = new Object();
		Object object2 = new Object();

		container = new Container<Object>();
		assertEquals("Object", null, container.get());

		container = new Container<Object>(object1);
		assertEquals("Object", object1, container.get());

		container = new Container<Object>(new Object[] { object1, object2 });
		assertEquals("Object", object1, container.get());

		container = new Container<Object>(new Object[] { object2, object1 });
		assertEquals("Object", object2, container.get());
	}

	/**
	 * Tests the {@link Container#get(Object)} method.
	 */
	public void testGetWithDefaultValue() {
		Container<Object> container;
		Object object1 = new Object();
		Object object2 = new Object();
		Object defaultObject = new Object();

		container = new Container<Object>();
		assertEquals("Object", defaultObject, container.get(defaultObject));

		container = new Container<Object>(object1);
		assertEquals("Object", object1, container.get(object2));

		container = new Container<Object>(new Object[] { object1, object2 });
		assertEquals("Object", object1, container.get(defaultObject));

		container = new Container<Object>(new Object[] { object2, object1 });
		assertEquals("Object", object2, container.get(object1));
	}

	/**
	 * Tests the {@link Container#last()} method.
	 */
	public void testLast() {
		Container<Object> container;
		Object object1 = new Object();
		Object object2 = new Object();
		Object object3 = new Object();

		container = new Container<Object>();
		assertEquals("last() is empty", true, container.last().isEmpty());

		container = new Container<Object>(object1);
		assertEquals("last() is empty", false, container.last().isEmpty());
		assertEquals("last element", object1, container.last().get());

		container = new Container<Object>(new Object[] { object1, object2 });
		assertEquals("last() is empty", false, container.last().isEmpty());
		assertEquals("last element", object2, container.last().get());

		container = new Container<Object>(new Object[] { object1, object2, object3 });
		assertEquals("last() is empty", false, container.last().isEmpty());
		assertEquals("last element", object3, container.last().get());

		container = new Container<Object>(new Object[] { object1, object3, object2 });
		assertEquals("last() is empty", false, container.last().isEmpty());
		assertEquals("last element", object2, container.last().get());
	}

}
