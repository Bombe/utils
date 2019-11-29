/*
 * utils - TimedMapTest.java - Copyright © 2011–2019 David Roden
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
 * Testcase for {@link TimedMap}. This test is pretty evil because it is
 * timing-dependent, obviously.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class TimedMapTest extends TestCase {

	/**
	 * Tests basic functionality of the {@link TimedMap}.
	 *
	 * @throws InterruptedException
	 *             if {@link Thread#sleep(long)} is interrupted
	 */
	public void test() throws InterruptedException {
		TimedMap<String, String> timedMap = new TimedMap<String, String>(100);

		timedMap.clear();
		timedMap.put("a", "1");
		timedMap.put("b", "2");

		Thread.sleep(150);
		assertEquals(0, timedMap.size());

		timedMap.put("a", "1");
		Thread.sleep(50);
		timedMap.put("b", "2");

		Thread.sleep(75);
		assertEquals(1, timedMap.size());
		assertNull(timedMap.get("a"));
		assertEquals("2", timedMap.get("b"));

		Thread.sleep(50);
		assertEquals(0, timedMap.size());
	}

}
