/*
 * utils - I18nMenuItem.java - Copyright © 2009 David Roden
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

import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;

import net.pterodactylus.util.i18n.I18n;
import net.pterodactylus.util.i18n.I18nable;

/**
 * {@link I18nable} wrapper around an AWT {@link MenuItem} that can also use an
 * {@link Action} to be performed when it is selected. This should not be used
 * for “normal” Swing applications!
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class I18nMenuItem extends MenuItem implements I18nable {

	/** The i18n basename of the menu item. */
	private final String i18nBasename;

	/** The action, if this menu item was derived from one. */
	private final Action action;

	/**
	 * Creates a new i18nable menu item.
	 *
	 * @param i18nBasename
	 *            The i18n basename of the menu item
	 */
	public I18nMenuItem(String i18nBasename) {
		this(i18nBasename, null);
	}

	/**
	 * Creates a new i18nable menu item that will perform the given action when
	 * selected.
	 *
	 * @param i18nBasename
	 *            The i18n base name of the menu item
	 * @param action
	 *            The action to perform when selected
	 */
	public I18nMenuItem(String i18nBasename, final Action action) {
		I18n.registerI18nable(this);
		this.i18nBasename = i18nBasename;
		this.action = action;
		if (action != null) {
			addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent actionEvent) {
					action.actionPerformed(actionEvent);
				}
			});
			action.addPropertyChangeListener(new PropertyChangeListener() {

				@Override
				public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
					if ("enabled".equals(propertyChangeEvent.getPropertyName())) {
						updateI18n();
					}
				}
			});
		}
		updateI18n();
	}

	/**
	 * Creates a new i18nable menu item that used the properties of the given
	 * {@link I18nAction}.
	 *
	 * @param i18nAction
	 *            The action to copy
	 */
	public I18nMenuItem(I18nAction i18nAction) {
		this(i18nAction.getI18nBasename(), i18nAction);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getI18nBasename() {
		return i18nBasename;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateI18n() {
		setLabel(I18n.get(i18nBasename + ".name"));
		setShortcut(new MenuShortcut(I18n.getKey(i18nBasename + ".mnemonic"), false));
		if (action != null) {
			setEnabled(action.isEnabled());
		}
	}
}
