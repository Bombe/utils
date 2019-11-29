/*
 * utils - TemplateContextTest.java - Copyright © 2013–2019 David Roden
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
 * Test case for various functions from the {@link TemplateContext}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class TemplateContextTest extends TestCase {

	/** The template context being tested. */
	private TemplateContext templateContext;

	//
	// TESTS
	//

	/**
	 * Test that the {@link Accessor} configured for {@link A} will be returned
	 * when an accessor for {@link C} is wanted.
	 */
	public void testSuperinterfaceAccessors() {
		templateContext.addAccessor(A.class, new ReflectionAccessor());
		assertEquals("Accessor for A", true, templateContext.getAccessor(C.class) != null);
	}

	//
	// TESTCASE METHODS
	//

	/**
	 * {@inheritDocs}
	 */
	@Override
	protected void setUp() throws Exception {
		templateContext = new TemplateContext();
	}

	/**
	 * Interface for {@link TemplateContextTest#testSuperinterfaceAccessors()}.
	 *
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	private static interface A {

		/* nothing here. */

	}

	/**
	 * Interface for {@link TemplateContextTest#testSuperinterfaceAccessors()}.
	 *
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	private static interface B extends A {

		/* nothing here. */

	}

	/**
	 * Class for {@link TemplateContextTest#testSuperinterfaceAccessors()}.
	 *
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	public static class C implements B {

		/* nothing here. */

	}

}
