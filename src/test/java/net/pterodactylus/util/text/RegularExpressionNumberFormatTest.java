/*
 * utils - RegularExpressionNumberFormat.java - Copyright © 2012 David Roden
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

package net.pterodactylus.util.text;

import junit.framework.TestCase;

/**
 * TODO
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class RegularExpressionNumberFormatTest extends TestCase {

	public void test() {
		RegularExpressionNumberFormat format;

		format = new RegularExpressionNumberFormat("^1$/eins;.*1[234]$//alles andere;.*[234]$/zwei");
		assertEquals("1", "eins", format.format(1));
		assertEquals("2", "zwei", format.format(2));
		assertEquals("5", "alles andere", format.format(5));
		assertEquals("12", "alles andere", format.format(12));
		assertEquals("22", "zwei", format.format(22));
		assertEquals("32", "zwei", format.format(32));
		assertEquals("213", "alles andere", format.format(213));
		assertEquals("204", "zwei", format.format(204));
		assertEquals("252", "zwei", format.format(252));
		assertEquals("1204", "zwei", format.format(1204));
		assertEquals("1213", "alles andere", format.format(1213));

		format = new RegularExpressionNumberFormat("^0$/null;87$/eins;^487$/drei;.*7$/zwei;.*5$/vier;fünf");
		assertEquals("0", "null", format.format(0));
		assertEquals("1", "fünf", format.format(1));
		assertEquals("5", "vier", format.format(5));
		assertEquals("7", "zwei", format.format(7));
		assertEquals("15", "vier", format.format(15));
		assertEquals("77", "zwei", format.format(77));
		assertEquals("87", "eins", format.format(87));
		assertEquals("387", "zwei", format.format(387));
		assertEquals("487", "drei", format.format(487));
		assertEquals("587", "zwei", format.format(587));
	}

}
