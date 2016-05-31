/*
 * utils - PairTest.java - Copyright © 2011–2016 David Roden
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

import junit.framework.TestCase;

/**
 * JUnit test case for {@link Pair}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class PairTest extends TestCase {

	/**
	 * Tests {@link Pair#equals(Object)} and {@link Pair#hashCode()}.
	 */
	public void testEquals() {
		Pair<Object, Object> leftPair;
		Pair<Object, Object> rightPair;
		Object object1;
		Object object2;

		leftPair = new Pair<Object, Object>(null, null);
		rightPair = new Pair<Object, Object>(null, null);
		assertEquals(leftPair, rightPair);
		assertEquals(leftPair.hashCode(), rightPair.hashCode());

		object1 = new Object();
		leftPair = new Pair<Object, Object>(object1, null);
		rightPair = new Pair<Object, Object>(object1, null);
		assertEquals(leftPair, rightPair);
		assertEquals(leftPair.hashCode(), rightPair.hashCode());

		rightPair = new Pair<Object, Object>(null, object1);
		assertFalse(leftPair.equals(rightPair));

		object2 = new Object();
		leftPair = new Pair<Object, Object>(object1, object2);
		rightPair = new Pair<Object, Object>(object1, object2);
		assertEquals(leftPair, rightPair);
		assertEquals(leftPair.hashCode(), rightPair.hashCode());

		rightPair = new Pair<Object, Object>(object2, object1);
		assertFalse(leftPair.equals(rightPair));
		assertFalse(leftPair.equals(new Pair<Object, Object>(null, null)));

		assertSame(leftPair.getLeft(), object1);
		assertSame(leftPair.getRight(), object2);
		assertSame(rightPair.getLeft(), object2);
		assertSame(rightPair.getRight(), object1);
	}

}
