/*
 * utils - ValidationTest.java - Copyright © 2011 David Roden
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

package net.pterodactylus.util.validation;

import junit.framework.TestCase;

/**
 * Test cases for {@link Validation}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ValidationTest extends TestCase {

	/**
	 * Tests {@link Validation#isInstanceOf(String, Object, Class)}.
	 */
	public void testIsInstanceOf() {
		try {
			Validation.begin().isInstanceOf("Test Object", new Integer(4), Integer.class).check();
		} catch (IllegalArgumentException iae1) {
			fail();
		}
		try {
			Validation.begin().isInstanceOf("Test Object", new Integer(4), Number.class).check();
		} catch (IllegalArgumentException iae1) {
			fail();
		}
		try {
			Validation.begin().isInstanceOf("Test Object", new Integer(4), Object.class).check();
		} catch (IllegalArgumentException iae1) {
			fail();
		}
		try {
			Validation.begin().isInstanceOf("Test Object", new Integer(4), Long.class).check();
			fail();
		} catch (IllegalArgumentException iae1) {
			/* expected. */
		}
	}

}
