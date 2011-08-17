/*
 * utils - QueryTest.java - Copyright © 2011 David Roden
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

package net.pterodactylus.util.database;

import java.io.IOException;
import java.io.StringWriter;

import junit.framework.TestCase;
import net.pterodactylus.util.database.Query.Type;
import net.pterodactylus.util.io.Closer;

/**
 * Test case for {@link Query} and related classes.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class QueryTest extends TestCase {

	/**
	 * Tests creation of simple SELECT queries.
	 */
	public void testSimpleSelectQueries() {
		Query query;

		query = new Query(Type.SELECT, "TEST");
		assertEquals("SELECT * FROM TEST", query);

		query.addField(new Field("ID"));
		assertEquals("SELECT ID FROM TEST", query);

		query.addField(new Field("TYPE"));
		assertEquals("SELECT ID, TYPE FROM TEST", query);
	}

	//
	// PRIVATE METHODS
	//

	/**
	 * Asserts that the generated query matches the expected query.
	 *
	 * @param expected
	 *            The expected query
	 * @param query
	 *            The generated query
	 */
	private void assertEquals(String expected, Query query) {
		StringWriter writer = new StringWriter();
		try {
			query.render(writer);
		} catch (IOException ioe1) {
			/* won’t occur but throw anyway. */
			throw new RuntimeException("Could not render query.", ioe1);
		} finally {
			Closer.close(writer);
		}
		assertEquals(expected, writer.toString());
	}

}
