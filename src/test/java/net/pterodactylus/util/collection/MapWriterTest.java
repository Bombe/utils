/*
 * utils - MapWriterTest.java - Copyright © 2008-2010 David Roden
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
 * Test case for {@link MapWriter}.
 *
 * @author David Roden &lt;droden@gmail.com&gt;
 */
public class MapWriterTest extends TestCase {

	/**
	 * Tests encoding routines.
	 */
	public void testEncode() {
		assertEquals("a", MapWriter.encode("a"));
		assertEquals("ab", MapWriter.encode("ab"));
		assertEquals("\\n", MapWriter.encode("\n"));
		assertEquals("\\u003d", MapWriter.encode("="));
	}

	/**
	 * Tests decoding routines.
	 */
	public void testDecode() {
		assertEquals("a", MapWriter.decode("a"));
		assertEquals("ab", MapWriter.decode("ab"));
		assertEquals("\n", MapWriter.decode("\\n"));
		assertEquals("=", MapWriter.decode("\\u003d"));
	}

}
