/*
 * utils - I18nLabel.java - Copyright © 2009 David Roden
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

import java.awt.Component;

import javax.swing.JLabel;

import net.pterodactylus.util.i18n.I18n;
import net.pterodactylus.util.i18n.I18nable;
import net.pterodactylus.util.i18n.I18n.RemovalReference;

/**
 * Label that can update itself from {@link I18n}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class I18nLabel extends JLabel implements I18nable {

	/** The i18n handler. */
	private final I18n i18n;

	/** The I18n basename of the label. */
	private final String i18nBasename;

	/** Optional arguments for i18n replacement. */
	private final Object[] arguments;

	/**
	 * Creates a new label with the given I18n basename.
	 *
	 * @param i18n
	 *            The i18n handler
	 * @param i18nBasename
	 *            The I18n basename of the label
	 */
	public I18nLabel(I18n i18n, String i18nBasename) {
		this(i18n, null, i18nBasename);
	}

	/**
	 * Creates a new label with the given I18n basename.
	 *
	 * @param i18n
	 *            The i18n handler
	 * @param removalReference
	 *            Removal reference (optional)
	 * @param i18nBasename
	 *            The I18n basename of the label
	 */
	public I18nLabel(I18n i18n, RemovalReference removalReference, String i18nBasename) {
		this(i18n, removalReference, i18nBasename, (Component) null);
	}

	/**
	 * Creates a new label with the given I18n basename that optionally is a
	 * label for the given component.
	 *
	 * @param i18n
	 *            The i18n handler
	 * @param i18nBasename
	 *            The I18n basename of the label
	 * @param component
	 *            The component that is activated by the label, or
	 *            <code>null</code> if this label should not activate a
	 *            component
	 */
	public I18nLabel(I18n i18n, String i18nBasename, Component component) {
		this(i18n, null, i18nBasename, component);
	}

	/**
	 * Creates a new label with the given I18n basename that optionally is a
	 * label for the given component.
	 *
	 * @param i18n
	 *            The i18n handler
	 * @param removalReference
	 *            Removal reference (optional)
	 * @param i18nBasename
	 *            The I18n basename of the label
	 * @param component
	 *            The component that is activated by the label, or
	 *            <code>null</code> if this label should not activate a
	 *            component
	 */
	public I18nLabel(I18n i18n, RemovalReference removalReference, String i18nBasename, Component component) {
		this(i18n, removalReference, i18nBasename, component, (Object[]) null);
	}

	/**
	 * Creates a new label with the given I18n basename that optionally is a
	 * label for the given component.
	 *
	 * @param i18n
	 *            The i18n handler
	 * @param i18nBasename
	 *            The I18n basename of the label
	 * @param arguments
	 *            Optional arguments that are handed in to
	 *            {@link I18n#get(String, Object...)}
	 */
	public I18nLabel(I18n i18n, String i18nBasename, Object... arguments) {
		this(i18n, (RemovalReference) null, i18nBasename, arguments);
	}

	/**
	 * Creates a new label with the given I18n basename that optionally is a
	 * label for the given component.
	 *
	 * @param i18n
	 *            The i18n handler
	 * @param removalReference
	 *            Removal reference (optional)
	 * @param i18nBasename
	 *            The I18n basename of the label
	 * @param arguments
	 *            Optional arguments that are handed in to
	 *            {@link I18n#get(String, Object...)}
	 */
	public I18nLabel(I18n i18n, RemovalReference removalReference, String i18nBasename, Object... arguments) {
		this(i18n, removalReference, i18nBasename, (Component) null, arguments);
	}

	/**
	 * Creates a new label with the given I18n basename that optionally is a
	 * label for the given component.
	 *
	 * @param i18n
	 *            The i18n handler
	 * @param i18nBasename
	 *            The I18n basename of the label
	 * @param component
	 *            The component that is activated by the label, or
	 *            <code>null</code> if this label should not activate a
	 *            component
	 * @param arguments
	 *            Optional arguments that are handed in to
	 *            {@link I18n#get(String, Object...)}
	 */
	public I18nLabel(I18n i18n, String i18nBasename, Component component, Object... arguments) {
		this(i18n, null, i18nBasename, component, arguments);
	}

	/**
	 * Creates a new label with the given I18n basename that optionally is a
	 * label for the given component.
	 *
	 * @param i18n
	 *            The i18n handler
	 * @param removalReference
	 *            Removal reference (optional)
	 * @param i18nBasename
	 *            The I18n basename of the label
	 * @param component
	 *            The component that is activated by the label, or
	 *            <code>null</code> if this label should not activate a
	 *            component
	 * @param arguments
	 *            Optional arguments that are handed in to
	 *            {@link I18n#get(String, Object...)}
	 */
	public I18nLabel(I18n i18n, RemovalReference removalReference, String i18nBasename, Component component, Object... arguments) {
		super();
		this.i18n = i18n;
		this.i18nBasename = i18nBasename;
		i18n.addI18nable(this, removalReference);
		this.arguments = arguments;
		if (component != null) {
			setLabelFor(component);
		}
		updateI18n();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateI18n() {
		setText(i18n.get(i18nBasename + ".name", arguments));
		if (getLabelFor() != null) {
			setDisplayedMnemonic(i18n.getKey(i18nBasename + ".mnemonic"));
		}
	}

}
