/*
 * utils - ModFilterTest.java - Copyright © 2011 David Roden
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

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import net.pterodactylus.util.collection.MapBuilder;

/**
 * Test case for the {@link ModFilter}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ModFilterTest extends TestCase {

	/**
	 * Tests {@link ModFilter#format(TemplateContext, Object, Map)}.
	 */
	public void test() {
		ModFilter modFilter = new ModFilter();
		Map<String, String> parameters;

		parameters = new HashMap<String, String>();
		assertBoolean(modFilter.format(null, "14", parameters), false);
		assertBoolean(modFilter.format(null, "a", parameters), false);

		parameters = new MapBuilder<String, String>().put("divisor", "3").get();
		assertBoolean(modFilter.format(null, "14", parameters), false);
		assertBoolean(modFilter.format(null, "15", parameters), true);

		parameters = new MapBuilder<String, String>().put("divisor", "3").put("offset", "2").get();
		assertBoolean(modFilter.format(null, "14", parameters), false);
		assertBoolean(modFilter.format(null, "15", parameters), false);
		assertBoolean(modFilter.format(null, "16", parameters), true);

		parameters = new MapBuilder<String, String>().put("divisor", "3").put("offset", "a").get();
		assertBoolean(modFilter.format(null, "14", parameters), false);
		assertBoolean(modFilter.format(null, "15", parameters), true);
		assertBoolean(modFilter.format(null, "16", parameters), false);
	}

	//
	// PRIVATE METHODS
	//

	/**
	 * Asserts that the given value is a {@link Boolean} object and has the
	 * expected value.
	 *
	 * @param value
	 *            The actual value
	 * @param expectedValue
	 *            The expected boolean value
	 */
	private void assertBoolean(Object value, boolean expectedValue) {
		if (value.getClass() != Boolean.class) {
			fail("Value “" + value + "” is not a boolean.");
		}
		assertEquals(((Boolean) value).booleanValue(), expectedValue);
	}

}
