/*
 * utils - ComboBoxModelList.java - Copyright © 2010–2019 David Roden
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

package net.pterodactylus.util.swing;

import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;

/**
 * Implementation of a {@link List} that doubles as a {@link ComboBoxModel},
 * e.g. for use with a {@link JComboBox}.
 *
 * @param <E>
 *            The type of the elements
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ComboBoxModelList<E> extends ListModelList<E> implements ComboBoxModel {

	/** The selected item. */
	private Object selectedItem;

	/**
	 * Creates a new combo box model list that wraps the given list.
	 *
	 * @param originalList
	 *            The original list to wrap
	 */
	public ComboBoxModelList(List<E> originalList) {
		super(originalList);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getSelectedItem() {
		return selectedItem;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSelectedItem(Object anItem) {
		selectedItem = anItem;
	}

}
