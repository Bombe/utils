/*
 * utils - I18nTest.java - Copyright © 2010 David Roden
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

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;

import junit.framework.TestCase;

/**
 * Rudimentary test case for {@link I18n}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class I18nTest extends TestCase {

	/**
	 * Tests basic {@link I18n} functionality by using three different
	 * properties files.
	 *
	 * @throws Throwable
	 *             if an exception occurs
	 */
	public void testBasicFunctionality() throws Throwable {
		URLClassLoader urlClassLoader = new URLClassLoader(new URL[] { new File("/home/bombe/workspace/utils/target/test-classes").toURI().toURL() }, Thread.currentThread().getContextClassLoader());
		I18n i18n;

		i18n = new I18n(new I18n.Source("I18n", "net/pterodactylus/util/i18n", new Locale("en"), urlClassLoader), Locale.ENGLISH);
		assertEquals("Color", i18n.get("Text.Color"));

		i18n = new I18n(new I18n.Source("I18n", "net/pterodactylus/util/i18n", new Locale("en"), urlClassLoader), new Locale("en", "GB"));
		assertEquals("Colour", i18n.get("Text.Color"));

		i18n = new I18n(new I18n.Source("I18n", "net/pterodactylus/util/i18n", new Locale("en"), urlClassLoader), Locale.GERMAN);
		assertEquals("Farbe", i18n.get("Text.Color"));
	}

	/**
	 * Tests whether fallbacks to the default language of a source work.
	 *
	 * @throws Throwable
	 *             if an exception occurs
	 */
	public void testDefaultLanguage() throws Throwable {
		URLClassLoader urlClassLoader = new URLClassLoader(new URL[] { new File("/home/bombe/workspace/utils/target/test-classes").toURI().toURL() }, Thread.currentThread().getContextClassLoader());
		I18n i18n;

		i18n = new I18n(new I18n.Source("I18n", "net/pterodactylus/util/i18n", new Locale("en"), urlClassLoader), Locale.GERMAN);
		assertEquals("Backup", i18n.get("Text.Backup"));

		i18n = new I18n(new I18n.Source("I18n", "net/pterodactylus/util/i18n", new Locale("de"), urlClassLoader), Locale.GERMAN);
		I18n.LOG_UNKNOWN_KEYS = false;
		assertEquals("Text.Backup", i18n.get("Text.Backup"));

		i18n = new I18n(new I18n.Source("I18n", "net/pterodactylus/util/i18n", new Locale("en"), urlClassLoader), new Locale("en", "GB"));
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
		URLClassLoader urlClassLoader = new URLClassLoader(new URL[] { new File("/home/bombe/workspace/utils/target/test-classes").toURI().toURL() }, Thread.currentThread().getContextClassLoader());
		I18n i18n;

		i18n = new I18n(new I18n.Source("I18n", "net/pterodactylus/util/i18n", Locale.GERMAN, urlClassLoader), Locale.GERMAN);
		assertEquals("Text.Backup", i18n.get("Text.Backup"));
		i18n.setLocale(Locale.ENGLISH);
		assertEquals("Backup", i18n.get("Text.Backup"));
		i18n.setLocale(new Locale("en", "GB"));
		assertEquals("Backup", i18n.get("Text.Backup"));
		i18n.setLocale(Locale.GERMAN);
		assertEquals("Text.Backup", i18n.get("Text.Backup"));
	}

}
