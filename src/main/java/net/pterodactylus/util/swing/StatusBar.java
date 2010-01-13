/*
 * utils - StatusBar.java - Copyright © 2009 David Roden
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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

/**
 * Status bar component that can be added to the {@link BorderLayout#SOUTH} area
 * of a {@link JFrame}’s {@link JFrame#getContentPane() content pane}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
/* TODO - make it possible to add further components. */
public class StatusBar extends JPanel {

	/** The layout. */
	private GridBagLayout layout = new GridBagLayout();

	/** The label. */
	private JLabel statusLabel = new JLabel(" ");

	/** Addition components. */
	private List<Component> sideComponents = new ArrayList<Component>();

	/**
	 * Creates a new status bar.
	 */
	public StatusBar() {
		setLayout(layout);
		statusLabel.setBorder(new CompoundBorder(new EtchedBorder(EtchedBorder.LOWERED), new EmptyBorder(0, 3, 0, 0)));
		add(statusLabel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.LINE_START, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
	}

	/**
	 * Clears the status bar.
	 */
	public void clear() {
		statusLabel.setText(" ");
	}

	/**
	 * Sets the text of the label.
	 *
	 * @param text
	 *            The text of the label
	 */
	public void setText(String text) {
		statusLabel.setText(text);
	}

	/**
	 * Adds a side component to the right side of the status bar, pushing all
	 * previously added side components to the left.
	 *
	 * @param component
	 *            The component to add
	 */
	public void addSideComponent(Component component) {
		sideComponents.add(component);
		int newIndex = sideComponents.size();
		add(component, new GridBagConstraints(newIndex, 0, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.BOTH, new Insets(0, 2, 0, 0), 0, 0));
		validate();
	}

	/**
	 * Returns the number of side components.
	 *
	 * @return The number of side components
	 */
	public int getSideComponentCount() {
		return sideComponents.size();
	}

	/**
	 * Returns all side components in order.
	 *
	 * @return All side components
	 */
	public List<Component> getSideComponents() {
		return sideComponents;
	}

	/**
	 * Removes the side component with the given index.
	 *
	 * @param sideComponentIndex
	 *            The index of the side component to remove
	 */
	public void removeSideComponent(int sideComponentIndex) {
		Component sideComponent = sideComponents.remove(sideComponentIndex);
		remove(sideComponent);
		validate();
	}

	/**
	 * Removes the given side component.
	 *
	 * @param sideComponent
	 *            The side component to remove
	 */
	public void removeSideComponent(Component sideComponent) {
		sideComponents.remove(sideComponent);
		remove(sideComponent);
		validate();
	}

}
