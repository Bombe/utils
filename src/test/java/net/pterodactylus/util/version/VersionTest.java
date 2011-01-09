/*
 * utils - VersionTest.java - Copyright © 2011 David Roden
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

package net.pterodactylus.util.version;

import junit.framework.TestCase;

/**
 * JUnit test case for {@link Version}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class VersionTest extends TestCase {

	/**
	 * Tests {@link Version#compareTo(Version)}.
	 */
	public void testCompareTo() {
		Version version1;
		Version version2;

		version1 = new Version(0, 3, 6, 4);
		version2 = new Version(0, 3, 6, 5);
		assertTrue(version2.compareTo(version1) > 0);
	}

}
