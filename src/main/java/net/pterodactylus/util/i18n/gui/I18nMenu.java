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

/**
 * Menu that receives its properties from {@link I18n}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class I18nMenu extends JMenu implements I18nable {

	/** The {@link I18n} basename. */
	private final String i18nBasename;

	/**
	 * Creates a new menu with the given {@link I18n} basename.
	 *
	 * @param i18nBasename
	 *            The basename of the {@link I18n} properties
	 */
	public I18nMenu(String i18nBasename) {
		this.i18nBasename = i18nBasename;
		updateI18n();
	}

	//
	// INTERFACE I18nable
	//

	/**
	 * {@inheritDoc}
	 */
	public void updateI18n() {
		setText(I18n.get(i18nBasename + ".name"));
		setMnemonic(I18n.getKey(i18nBasename + ".mnemonic"));
	}

}
