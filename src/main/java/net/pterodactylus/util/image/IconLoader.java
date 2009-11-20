/*
 * utils - IconLoader.java - Copyright © 2008-2009 David Roden
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package net.pterodactylus.util.image;

import java.awt.Image;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Loads an {@link Icon} or an {@link Image} from the class path.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class IconLoader {

	/**
	 * Loads an icon from the class path.
	 *
	 * @param resourceName
	 *            The name of the icon
	 * @return The icon, or <code>null</code> if no icon was found
	 */
	public static Icon loadIcon(String resourceName) {
		try {
			InputStream resourceStream = IconLoader.class.getResourceAsStream(resourceName);
			if (resourceStream == null) {
				return null;
			}
			ByteArrayOutputStream imageOutput = new ByteArrayOutputStream();
			byte[] buffer = new byte[16384];
			int r = 0;
			while ((r = resourceStream.read(buffer)) != -1) {
				imageOutput.write(buffer, 0, r);
			}
			imageOutput.flush();
			return new ImageIcon(imageOutput.toByteArray());
		} catch (IOException e) {
			/* ignore. */
		}
		return null;
	}

	/**
	 * Loads an image from the class path.
	 *
	 * @param resourceName
	 *            The name of the image
	 * @return The image, or <code>null</code> if no image was found
	 */
	public static Image loadImage(String resourceName) {
		ImageIcon imageIcon = (ImageIcon) loadIcon(resourceName);
		if (imageIcon == null) {
			return null;
		}
		return imageIcon.getImage();
	}

}
