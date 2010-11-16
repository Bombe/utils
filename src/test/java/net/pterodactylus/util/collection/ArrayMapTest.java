/*
 * utils - ArrayMapTest.java - Copyright © 2010 David Roden
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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import junit.framework.TestCase;

/**
 * JUnit testcase for {@link ArrayMap}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ArrayMapTest extends TestCase {

	/**
	 * Tests simple storage cases.
	 */
	public void testStorage() {
		ArrayMap<String, String> map;
		int count = 2000;

		map = new ArrayMap<String, String>();
		for (int i = 0; i < count; ++i) {
			map.put("object" + i, "value" + i);
		}

		for (int i = 0; i < count; ++i) {
			assertEquals("value" + i, map.get("object" + i));
		}

		for (int i = 0; i < count; i += 2) {
			map.remove("object" + i);
		}

		for (int i = 1; i < count; i += 2) {
			assertEquals("value" + i, map.get("object" + i));
		}

		for (Entry<String, String> entry : map.entrySet()) {
			assertEquals(entry.getValue(), map.get(entry.getKey()));
			assertEquals(entry.getKey().substring(6), entry.getValue().substring(5));
		}

		assertEquals(count / 2, map.size());
		assertEquals(count / 2, map.keySet().size());
		assertEquals(count / 2, map.values().size());

		map.clear();
		assertEquals(0, map.size());
		assertEquals(true, map.isEmpty());
	}

	/**
	 * Runs benchmarks.
	 *
	 * @param arguments
	 *            The command-line arguments (ignored)
	 */
	public static void main(String... arguments) {

		Map<String, String> arrayMap = new ArrayMap<String, String>();
		Map<String, String> hashMap = new HashMap<String, String>();
		Object object = null;

		System.out.println("Testing get() speed…");

		for (int elements : new int[] { 10, 100, 1000, 10000, 100000 }) {
			System.out.println("· " + elements + " elements");
			fillMap(hashMap, elements);
			fillMap(arrayMap, elements);

			for (int gets : new int[] { 10, 100, 1000, 10000, 100000, 1000000 }) {
				System.out.println("  · " + gets + " gets");

				long start = System.nanoTime();
				for (int i = 0; i < gets; ++i) {
					object = arrayMap.get("object" + (i % elements));
				}
				long end = System.nanoTime();
				System.out.println("    · arrayMap: " + (end - start) / 1000000.0 + " ms");

				start = System.nanoTime();
				for (int i = 0; i < gets; ++i) {
					object = hashMap.get("object" + (i % elements));
				}
				end = System.nanoTime();
				System.out.println("    · hashMap: " + (end - start) / 1000000.0 + " ms");
			}
		}

		/* try to not let the compiler ignore everything. */
		if (object != null) {
			object = null;
		}
	}

	/**
	 * Clears the map and fills it with elements.
	 *
	 * @param map
	 *            The map to fill
	 * @param elements
	 *            The number of elements
	 */
	private static void fillMap(Map<String, String> map, int elements) {
		map.clear();
		for (int i = 0; i < elements; ++i) {
			map.put("object" + i, "value" + i);
		}
	}

}
