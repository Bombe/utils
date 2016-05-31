/*
 * utils - Template.java - Copyright © 2010–2016 David Roden
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
 * Simple container that holds an initial {@link TemplateContext} that can be
 * used to store filters, accessors, plugins, providers, and template
 * objects that are used for all render passes of this template.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Template extends ContainerPart {

	/** The initial template context. */
	private final TemplateContext templateContext = new TemplateContext();

	/**
	 * Creates a new template.
	 */
	public Template() {
		super(0, 0);
	}

	/**
	 * Returns the initial context of this template.
	 *
	 * @return The initial context of this template
	 */
	public TemplateContext getInitialContext() {
		return templateContext;
	}

}
