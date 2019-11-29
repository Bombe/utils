/*
 * utils - I18nTest.java - Copyright © 2010–2019 David Roden
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

package net.pterodactylus.util.i18n;

import java.util.Locale;

import junit.framework.TestCase;

/**
 * Rudimentary test case for {@link I18n}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class I18nTest extends TestCase {

	static {
		I18n.LOG_UNKNOWN_KEYS = false;
	}

	/**
	 * Tests basic {@link I18n} functionality by using three different
	 * properties files.
	 *
	 * @throws Throwable
	 *             if an exception occurs
	 */
	public void testBasicFunctionality() throws Throwable {
		ClassLoader classLoader = I18nTest.class.getClassLoader();
		I18n i18n;

		i18n = new I18n(new I18n.Source("I18n", "net/pterodactylus/util/i18n", new Locale("en"), classLoader), Locale.ENGLISH);
		assertEquals("Color", i18n.get("Text.Color"));

		i18n = new I18n(new I18n.Source("I18n", "net/pterodactylus/util/i18n", new Locale("en"), classLoader), new Locale("en", "GB"));
		assertEquals("Colour", i18n.get("Text.Color"));

		i18n = new I18n(new I18n.Source("I18n", "net/pterodactylus/util/i18n", new Locale("en"), classLoader), Locale.GERMAN);
		assertEquals("Farbe", i18n.get("Text.Color"));
	}

	/**
	 * Tests whether fallbacks to the default language of a source work.
	 *
	 * @throws Throwable
	 *             if an exception occurs
	 */
	public void testDefaultLanguage() throws Throwable {
		ClassLoader classLoader = I18n.class.getClassLoader();
		I18n i18n;

		i18n = new I18n(new I18n.Source("I18n", "net/pterodactylus/util/i18n", new Locale("en"), classLoader), Locale.GERMAN);
		assertEquals("Backup", i18n.get("Text.Backup"));

		i18n = new I18n(new I18n.Source("I18n", "net/pterodactylus/util/i18n", new Locale("de"), classLoader), Locale.GERMAN);
		assertEquals("Text.Backup", i18n.get("Text.Backup"));

		i18n = new I18n(new I18n.Source("I18n", "net/pterodactylus/util/i18n", new Locale("en"), classLoader), new Locale("en", "GB"));
		assertEquals("Backup", i18n.get("Text.Backup"));
	}

	/**
	 * Tests whether {@link I18n#setLocale(Locale) switching languages}
	 * works.
	 *
	 * @throws Throwable
	 *             if an exception occurs
	 */
	public void testSwitchLanguage() throws Throwable {
		ClassLoader classLoader = I18n.class.getClassLoader();
		I18n i18n;

		i18n = new I18n(new I18n.Source("I18n", "net/pterodactylus/util/i18n", Locale.GERMAN, classLoader), Locale.GERMAN);
		assertEquals("Text.Backup", i18n.get("Text.Backup"));
		i18n.setLocale(Locale.ENGLISH);
		assertEquals("Backup", i18n.get("Text.Backup"));
		i18n.setLocale(new Locale("en", "GB"));
		assertEquals("Backup", i18n.get("Text.Backup"));
		i18n.setLocale(Locale.GERMAN);
		assertEquals("Text.Backup", i18n.get("Text.Backup"));
	}

}
