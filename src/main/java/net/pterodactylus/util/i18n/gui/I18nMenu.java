/*
 * utils - I18nMenu.java - Copyright © 2009 David Roden
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

package net.pterodactylus.util.i18n.gui;

import javax.swing.JMenu;

import net.pterodactylus.util.i18n.I18n;
import net.pterodactylus.util.i18n.I18nable;
import net.pterodactylus.util.i18n.I18n.RemovalReference;

/**
 * Menu that receives its properties from {@link I18n}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class I18nMenu extends JMenu implements I18nable {

	/** The i18n handler. */
	private final I18n i18n;

	/** The {@link I18n} basename. */
	private final String i18nBasename;

	/**
	 * Creates a new menu with the given {@link I18n} basename.
	 *
	 * @param i18n
	 *            The i18n handler
	 * @param i18nBasename
	 *            The basename of the {@link I18n} properties
	 */
	public I18nMenu(I18n i18n, String i18nBasename) {
		this(i18n, null, i18nBasename);
	}

	/**
	 * Creates a new menu with the given {@link I18n} basename.
	 *
	 * @param i18n
	 *            The i18n handler
	 * @param removalReference
	 *            Removal reference (optional)
	 * @param i18nBasename
	 *            The basename of the {@link I18n} properties
	 */
	public I18nMenu(I18n i18n, RemovalReference removalReference, String i18nBasename) {
		this.i18n = i18n;
		this.i18nBasename = i18nBasename;
		i18n.addI18nable(this, removalReference);
		updateI18n();
	}

	//
	// INTERFACE I18nable
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateI18n() {
		setText(i18n.get(i18nBasename + ".name"));
		setMnemonic(i18n.getKey(i18nBasename + ".mnemonic"));
	}

}
