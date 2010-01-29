/*
 * utils - SwingUtils.java - Copyright © 2010 David Roden
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

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;

/**
 * Collection of Swing-related helper methods.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class SwingUtils {

	/**
	 * Centers the given window on the primary screen.
	 *
	 * @param window
	 *            The window to center
	 */
	public static void center(Window window) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension windowSize = window.getSize();
		window.setLocation((screenSize.width - windowSize.width) / 2, (screenSize.height - windowSize.height) / 2);
	}

	/**
	 * Centers the given window over its owner window.
	 *
	 * @param window
	 *            The window to center
	 * @param ownerWindow
	 *            The window to center the other window over
	 */
	public static void center(Window window, Window ownerWindow) {
		Point ownerWindowLocation = ownerWindow.getLocation();
		Dimension ownerWindowDimension = ownerWindow.getSize();
		Dimension windowSize = window.getSize();
		window.setLocation(ownerWindowLocation.x + (ownerWindowDimension.width - windowSize.width) / 2, ownerWindowLocation.y + (ownerWindowDimension.height - windowSize.height) / 2);
	}

}
