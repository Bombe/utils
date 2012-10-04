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
	 * Tests {@link Validation#is(String, boolean)}.
	 */
	public void testIs() {
		Validation.begin().is("true", true).check();
		try {
			Validation.begin().is("false", false).check();
			fail();
		} catch (IllegalArgumentException iae1) {
			/* expected. */
		}
	}

	/**
	 * Tests {@link Validation#is(String, boolean)}.
	 */
	public void testIsNot() {
		Validation.begin().isNot("false", false).check();
		try {
			Validation.begin().isNot("true", true).check();
			fail();
		} catch (IllegalArgumentException iae1) {
			/* expected. */
		}
	}

	/**
	 * Tests {@link Validation#isNotNull(String, Object)}.
	 */
	public void testIsNotNull() {
		try {
			Validation.begin().isNotNull("Test Object", new Object()).check();
		} catch (IllegalArgumentException iae1) {
			fail();
		}
		try {
			Validation.begin().isNotNull("Test Object", null).check();
			fail();
		} catch (IllegalArgumentException iae1) {
			/* expected. */
		}
	}

	/**
	 * Tests {@link Validation#isNull(String, Object)}.
	 */
	public void testIsNull() {
		try {
			Validation.begin().isNull("Test Object", null).check();
		} catch (IllegalArgumentException iae1) {
			fail();
		}
		try {
			Validation.begin().isNull("Test Object", new Object()).check();
			fail();
		} catch (IllegalArgumentException iae1) {
			/* expected. */
		}
	}

	/**
	 * Tests {@link Validation#isLess(String, long, long)}.
	 */
	public void testIsLess_Long() {
		Validation.begin().isLess("Test Object", 17, 100).check();
		Validation.begin().isLess("Test Object", new Integer(17), 100).check();
		try {
			Validation.begin().isLess("Test Object", 100, 100).check();
			fail();
		} catch (IllegalArgumentException iae1) {
			/* expected. */
		}
		try {
			Validation.begin().isLess("Test Object", 117, 100).check();
			fail();
		} catch (IllegalArgumentException iae1) {
			/* expected. */
		}
	}

	/**
	 * Tests {@link Validation#isLess(String, double, double)}.
	 */
	public void testIsLess_Double() {
		Validation.begin().isLess("Test Object", 16.5, 100).check();
		Validation.begin().isLess("Test Object", new Float(16.5), 100).check();
		try {
			Validation.begin().isLess("Test Object", 100d, 100).check();
			fail();
		} catch (IllegalArgumentException iae1) {
			/* expected. */
		}
		try {
			Validation.begin().isLess("Test Object", 117d, 100).check();
			fail();
		} catch (IllegalArgumentException iae1) {
			/* expected. */
		}
	}

	/**
	 * Tests {@link Validation#isLessOrEqual(String, long, long)}.
	 */
	public void testIsLessOrEqual_Long() {
		Validation.begin().isLessOrEqual("Test Object", 17, 100).check();
		Validation.begin().isLessOrEqual("Test Object", new Integer(17), 100).check();
		Validation.begin().isLessOrEqual("Test Object", 100, 100).check();
		try {
			Validation.begin().isLessOrEqual("Test Object", 117, 100).check();
			fail();
		} catch (IllegalArgumentException iae1) {
			/* expected. */
		}
	}

	/**
	 * Tests {@link Validation#isLessOrEqual(String, double, double)}.
	 */
	public void testIsLessOrEqual_Double() {
		Validation.begin().isLessOrEqual("Test Object", 16.5, 100).check();
		Validation.begin().isLessOrEqual("Test Object", new Float(16.5), 100).check();
		Validation.begin().isLessOrEqual("Test Object", 100d, 100).check();
		try {
			Validation.begin().isLessOrEqual("Test Object", 117d, 100).check();
			fail();
		} catch (IllegalArgumentException iae1) {
			/* expected. */
		}
	}

	/**
	 * Tests {@link Validation#isEqual(String, Object, Object)}.
	 */
	public void testIsEqual_Object() {
		Validation.begin().isEqual("Test Object", new Integer(5), new Integer(5)).check();
		try {
			Validation.begin().isEqual("Test Object", new Integer(5), new Integer(4)).check();
			fail();
		} catch (IllegalArgumentException iae1) {
			/* expected. */
		}
		try {
			Validation.begin().isEqual("Test Object", 17, 17d).check();
			fail();
		} catch (IllegalArgumentException iae1) {
			/* expected. */
		}
		try {
			Validation.begin().isEqual("Test Object", 18, 17).check();
			fail();
		} catch (IllegalArgumentException iae1) {
			/* expected. */
		}
		Validation.begin().isEqual("Test Object", 16.5, 16.5d).check();
		try {
			Validation.begin().isEqual("Test Object", 18f, 17d).check();
			fail();
		} catch (IllegalArgumentException iae1) {
			/* expected. */
		}
		Validation.begin().isEqual("Test Object", true, true).check();
		Validation.begin().isEqual("Test Object", false, false).check();
		try {
			Validation.begin().isEqual("Test Object", true, false).check();
			fail();
		} catch (IllegalArgumentException iae1) {
			/* expected. */
		}
		try {
			Validation.begin().isEqual("Test Object", false, true).check();
			fail();
		} catch (IllegalArgumentException iae1) {
			/* expected. */
		}
	}

	/**
	 * Tests {@link Validation#isSame(String, Object, Object)}.
	 */
	public void testIsSame() {
		Object testObject = new Object();
		Validation.begin().isSame("Test Object", testObject, testObject).check();
		try {
			Validation.begin().isSame("Test Object", new Object(), testObject).check();
			fail();
		} catch (IllegalArgumentException iae1) {
			/* expected. */
		}
		try {
			Validation.begin().isSame("Test Object", new Object(), new Object()).check();
			fail();
		} catch (IllegalArgumentException iae1) {
			/* expected. */
		}
	}

	/**
	 * Tests {@link Validation#isNotEqual(String, Object, Object)}.
	 */
	public void testIsNotEqual() {
		Validation.begin().isNotEqual("Test Object", 18, 17).check();
		Validation.begin().isNotEqual("Test Object", 17, 17d).check();
		Validation.begin().isNotEqual("Test Object", 18f, 17d).check();
		try {
			Validation.begin().isNotEqual("Test Object", 16.5, 16.5d).check();
			fail();
		} catch (IllegalArgumentException iae1) {
			/* expected. */
		}
		Validation.begin().isNotEqual("Test Object", true, false).check();
		Validation.begin().isNotEqual("Test Object", false, true).check();
		try {
			Validation.begin().isNotEqual("Test Object", true, true).check();
			fail();
		} catch (IllegalArgumentException iae1) {
			/* expected. */
		}
		try {
			Validation.begin().isNotEqual("Test Object", false, false).check();
			fail();
		} catch (IllegalArgumentException iae1) {
			/* expected. */
		}
	}

	/**
	 * Tests {@link Validation#isGreater(String, long, long)}.
	 */
	public void testIsGreater_Long() {
		Validation.begin().isGreater("Test Object", 100, 17).check();
		Validation.begin().isGreater("Test Object", 100, new Integer(17)).check();
		try {
			Validation.begin().isGreater("Test Object", 100, 100).check();
			fail();
		} catch (IllegalArgumentException iae1) {
			/* expected. */
		}
		try {
			Validation.begin().isGreater("Test Object", 100, 117).check();
			fail();
		} catch (IllegalArgumentException iae1) {
			/* expected. */
		}
	}

	/**
	 * Tests {@link Validation#isGreater(String, double, double)}.
	 */
	public void testIsGreater_Double() {
		Validation.begin().isGreater("Test Object", 100, 16.5).check();
		Validation.begin().isGreater("Test Object", 100, new Float(16.5)).check();
		try {
			Validation.begin().isGreater("Test Object", 100d, 100).check();
			fail();
		} catch (IllegalArgumentException iae1) {
			/* expected. */
		}
		try {
			Validation.begin().isGreater("Test Object", 100d, 117d).check();
			fail();
		} catch (IllegalArgumentException iae1) {
			/* expected. */
		}
	}

	/**
	 * Tests {@link Validation#isGreaterOrEqual(String, long, long)}.
	 */
	public void testIsGreaterOrEqual_Long() {
		Validation.begin().isGreaterOrEqual("Test Object", 100, 17).check();
		Validation.begin().isGreaterOrEqual("Test Object", 100, new Integer(17)).check();
		Validation.begin().isGreaterOrEqual("Test Object", 100, 100).check();
		try {
			Validation.begin().isGreaterOrEqual("Test Object", 100, 117).check();
			fail();
		} catch (IllegalArgumentException iae1) {
			/* expected. */
		}
	}

	/**
	 * Tests {@link Validation#isGreaterOrEqual(String, double, double)}.
	 */
	public void testIsGreaterOrEqual_Double() {
		Validation.begin().isGreaterOrEqual("Test Object", 100, 16.5).check();
		Validation.begin().isGreaterOrEqual("Test Object", 100, new Float(16.5)).check();
		Validation.begin().isGreaterOrEqual("Test Object", 100d, 100).check();
		try {
			Validation.begin().isGreaterOrEqual("Test Object", 100d, 117d).check();
			fail();
		} catch (IllegalArgumentException iae1) {
			/* expected. */
		}
	}

	/**
	 * Tests {@link Validation#isPositive(String, long)}.
	 */
	public void testIsPositive_Long() {
		Validation.begin().isPositive("Test Object", 0).check();
		Validation.begin().isPositive("Test Object", 17).check();
		try {
			Validation.begin().isPositive("Test Object", -17).check();
			fail();
		} catch (IllegalArgumentException iae1) {
			/* expected. */
		}
	}

	/**
	 * Tests {@link Validation#isPositive(String, double)}.
	 */
	public void testIsPositive_Double() {
		Validation.begin().isPositive("Test Object", 0d).check();
		Validation.begin().isPositive("Test Object", 16.5d).check();
		try {
			Validation.begin().isPositive("Test Object", -16.5d).check();
			fail();
		} catch (IllegalArgumentException iae1) {
			/* expected. */
		}
	}

	/**
	 * Tests {@link Validation#isNegative(String, long)}.
	 */
	public void testIsNegative_Long() {
		Validation.begin().isNegative("Test Object", -17).check();
		try {
			Validation.begin().isNegative("Test Object", 0).check();
			fail();
		} catch (IllegalArgumentException iae1) {
			/* expected. */
		}
		try {
			Validation.begin().isNegative("Test Object", 17).check();
			fail();
		} catch (IllegalArgumentException iae1) {
			/* expected. */
		}
	}

	/**
	 * Tests {@link Validation#isNegative(String, double)}.
	 */
	public void testIsNegative_Double() {
		Validation.begin().isNegative("Test Object", -16.5d).check();
		try {
			Validation.begin().isNegative("Test Object", 0d).check();
			fail();
		} catch (IllegalArgumentException iae1) {
			/* expected. */
		}
		try {
			Validation.begin().isNegative("Test Object", 16.5d).check();
			fail();
		} catch (IllegalArgumentException iae1) {
			/* expected. */
		}
	}

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

	/**
	 * Tests {@link Validation#isEither(String, Object, Object...)}.
	 */
	public void testIsEither() {
		Validation.begin().isEither("Test Object", "a", null, "a", "b", "c").check();
		Validation.begin().isEither("Test Object", null, "a", "b", "c", null).check();
		try {
			Validation.begin().isEither("Test Object", "a", "b", "c", "d").check();
			fail();
		} catch (IllegalArgumentException iae1) {
			/* expected. */
		}
	}

	/**
	 * Tests {@link Validation#isAll(String, Object, Object...)}.
	 */
	public void testIsAll() {
		Validation.begin().isAll("Test Object", "a", "a").check();
		Validation.begin().isAll("Test Object", "a", "a", "a").check();
		try {
			Validation.begin().isAll("Test Object", "a", "b").check();
		} catch (IllegalArgumentException iae1) {
			/* expected. */
		}
		try {
			Validation.begin().isAll("Test Object", "a", "b", "c").check();
		} catch (IllegalArgumentException iae1) {
			/* expected. */
		}
	}

}
