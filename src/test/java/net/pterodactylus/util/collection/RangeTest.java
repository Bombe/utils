/*
 * utils - RangeTest.java - Copyright © 2011 David Roden
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

package net.pterodactylus.util.collection;

import java.util.Iterator;

import junit.framework.TestCase;

/**
 * TODO
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class RangeTest extends TestCase {

	/**
	 * Tests {@link Range#create(int)}.
	 */
	public void testSimpleIntegerRange() {
		Iterable<Integer> iterable;
		Iterator<Integer> iterator;

		iterable = Range.create(10);
		assertNotNull("Iterable<Integer>", iterable);
		iterator = iterable.iterator();
		assertNotNull("Iterator<Integer>", iterator);
		for (int index = 0; index < 10; ++index) {
			assertEquals("Iterator has next", true, iterator.hasNext());
			Integer next = iterator.next();
			assertNotNull("Iterator next", next);
			assertEquals("Index", index, next.intValue());
		}
		assertEquals("Iterator finished", false, iterator.hasNext());

		/* test for a second iterator from the same iterable. */
		iterator = iterable.iterator();
		assertNotNull("Second Iterator<Integer>", iterator);
		for (int index = 0; index < 10; ++index) {
			assertEquals("Second Iterator has next", true, iterator.hasNext());
			Integer next = iterator.next();
			assertNotNull("Second Iterator next", next);
			assertEquals("Second Index", index, next.intValue());
		}
		assertEquals("Second Iterator finished", false, iterator.hasNext());

		/* negative range, empty iterator. */
		iterable = Range.create(-10);
		assertNotNull("Iterable<Integer>", iterable);
		iterator = iterable.iterator();
		assertNotNull("Iterator<Integer>", iterator);
		assertEquals("Iterator finished", true, !iterator.hasNext());
	}

	/**
	 * Tests {@link Range#create(int, int)}.
	 */
	public void testFullIntegerRange() {
		Iterable<Integer> iterable;
		Iterator<Integer> iterator;

		iterable = Range.create(0, 10);
		assertNotNull("Iterable<Integer>", iterable);
		iterator = iterable.iterator();
		assertNotNull("Iterator<Integer>", iterator);
		for (int index = 0; index < 10; ++index) {
			assertEquals("Iterator has next", true, iterator.hasNext());
			Integer next = iterator.next();
			assertNotNull("Iterator next", next);
			assertEquals("Index", index, next.intValue());
		}
		assertEquals("Iterator finished", false, iterator.hasNext());

		/* test for a second iterator from the same iterable. */
		iterator = iterable.iterator();
		assertNotNull("Second Iterator<Integer>", iterator);
		for (int index = 0; index < 10; ++index) {
			assertEquals("Second Iterator has next", true, iterator.hasNext());
			Integer next = iterator.next();
			assertNotNull("Second Iterator next", next);
			assertEquals("Second Index", index, next.intValue());
		}
		assertEquals("Second Iterator finished", false, iterator.hasNext());

		/* larger range with negative numbers. */
		iterable = Range.create(-10, 10);
		assertNotNull("Iterable<Integer>", iterable);
		iterator = iterable.iterator();
		assertNotNull("Iterator<Integer>", iterator);
		for (int index = -10; index < 10; ++index) {
			assertEquals("Iterator has next", true, iterator.hasNext());
			Integer next = iterator.next();
			assertNotNull("Iterator next", next);
			assertEquals("Index", index, next.intValue());
		}
		assertEquals("Iterator finished", false, iterator.hasNext());

		/* negative range, empty iterator. */
		iterable = Range.create(10, -10);
		assertNotNull("Iterable<Integer>", iterable);
		iterator = iterable.iterator();
		assertNotNull("Iterator<Integer>", iterator);
		assertEquals("Iterator finished", true, !iterator.hasNext());
	}

	/**
	 * Tests {@link Range#create(long)}.
	 */
	public void testSimpleLongRange() {
		Iterable<Long> iterable;
		Iterator<Long> iterator;

		iterable = Range.create(10L);
		assertNotNull("Iterable<Integer>", iterable);
		iterator = iterable.iterator();
		assertNotNull("Iterator<Integer>", iterator);
		for (int index = 0; index < 10; ++index) {
			assertEquals("Iterator has next", true, iterator.hasNext());
			Long next = iterator.next();
			assertNotNull("Iterator next", next);
			assertEquals("Index", index, next.longValue());
		}
		assertEquals("Iterator finished", false, iterator.hasNext());

		/* test for a second iterator from the same iterable. */
		iterator = iterable.iterator();
		assertNotNull("Second Iterator<Integer>", iterator);
		for (int index = 0; index < 10; ++index) {
			assertEquals("Second Iterator has next", true, iterator.hasNext());
			Long next = iterator.next();
			assertNotNull("Second Iterator next", next);
			assertEquals("Second Index", index, next.longValue());
		}
		assertEquals("Second Iterator finished", false, iterator.hasNext());

		/* negative range, empty iterator. */
		iterable = Range.create(-10L);
		assertNotNull("Iterable<Integer>", iterable);
		iterator = iterable.iterator();
		assertNotNull("Iterator<Integer>", iterator);
		assertEquals("Iterator finished", true, !iterator.hasNext());
	}

	/**
	 * Tests {@link Range#create(long, long)}.
	 */
	public void testFullLongRange() {
		Iterable<Long> iterable;
		Iterator<Long> iterator;

		iterable = Range.create(0L, 10L);
		assertNotNull("Iterable<Integer>", iterable);
		iterator = iterable.iterator();
		assertNotNull("Iterator<Integer>", iterator);
		for (int index = 0; index < 10; ++index) {
			assertEquals("Iterator has next", true, iterator.hasNext());
			Long next = iterator.next();
			assertNotNull("Iterator next", next);
			assertEquals("Index", index, next.longValue());
		}
		assertEquals("Iterator finished", false, iterator.hasNext());

		/* test for a second iterator from the same iterable. */
		iterator = iterable.iterator();
		assertNotNull("Second Iterator<Integer>", iterator);
		for (int index = 0; index < 10; ++index) {
			assertEquals("Second Iterator has next", true, iterator.hasNext());
			Long next = iterator.next();
			assertNotNull("Second Iterator next", next);
			assertEquals("Second Index", index, next.longValue());
		}
		assertEquals("Second Iterator finished", false, iterator.hasNext());

		/* larger range with negative numbers. */
		iterable = Range.create(-10L, 10L);
		assertNotNull("Iterable<Integer>", iterable);
		iterator = iterable.iterator();
		assertNotNull("Iterator<Integer>", iterator);
		for (int index = -10; index < 10; ++index) {
			assertEquals("Iterator has next", true, iterator.hasNext());
			Long next = iterator.next();
			assertNotNull("Iterator next", next);
			assertEquals("Index", index, next.longValue());
		}
		assertEquals("Iterator finished", false, iterator.hasNext());

		/* negative range, empty iterator. */
		iterable = Range.create(10L, -10L);
		assertNotNull("Iterable<Integer>", iterable);
		iterator = iterable.iterator();
		assertNotNull("Iterator<Integer>", iterator);
		assertEquals("Iterator finished", true, !iterator.hasNext());
	}

}
