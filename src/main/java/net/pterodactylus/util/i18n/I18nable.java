/*
 * utils - I18nable.java - Copyright © 2009 David Roden
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

/**
 * Interface for objects that want to be notified when the language is changed.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public interface I18nable {

	/**
	 * Returns the i18n basename used by the component.
	 *
	 * @return The i18n basename of the component
	 */
	public String getI18nBasename();

	/**
	 * Notifies the object that the language in {@link I18n} was changed.
	 */
	public void updateI18n();

}
