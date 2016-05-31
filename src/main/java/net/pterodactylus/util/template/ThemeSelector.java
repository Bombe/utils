/*
 * utils - ThemeSelector.java - Copyright © 2011–2016 David Roden
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

package net.pterodactylus.util.template;

/**
 * Selects themes based on a context.
 *
 * @param <C>
 *            The type of the context
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public interface ThemeSelector<C extends ThemeSelectorContext> {

	/**
	 * Returns a theme for the given context. This method should always return a
	 * theme! If the context does not contain information about a theme which
	 * should be selected a default theme should be returned.
	 *
	 * @param context
	 *            The context to get the theme selection parameters from
	 * @return The selected theme
	 */
	public Theme getTheme(C context);

}
