/*
 * utils - BasicWhitespaceRemoverTest.java - Copyright © 2011 David Roden
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

import junit.framework.TestCase;

/**
 * JUnit test case for {@link BasicWhitespaceRemover}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class BasicWhitespaceRemoverTest extends TestCase {

	/**
	 * Tests {@link BasicWhitespaceRemover#removeWhitespace(String)}.
	 */
	public void test() {
		final WhitespaceRemover whitespaceRemover = new BasicWhitespaceRemover();
		String source;

		source = "  Test.";
		assertEquals("Test.", whitespaceRemover.removeWhitespace(source));

		source = "\rTest.";
		assertEquals("Test.", whitespaceRemover.removeWhitespace(source));

		source = "\nTest.";
		assertEquals("Test.", whitespaceRemover.removeWhitespace(source));

		source = "\tTest.";
		assertEquals("Test.", whitespaceRemover.removeWhitespace(source));

		source = "\n		Test.";
		assertEquals("Test.", whitespaceRemover.removeWhitespace(source));

		source = "\n		Test. ";
		assertEquals("Test. ", whitespaceRemover.removeWhitespace(source));
	}

}
