/*
 * utils - PluginPart.java - Copyright © 2010 David Roden
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

import java.io.Writer;
import java.util.Map;

/**
 * {@link Part} implementation that executes a {@link Plugin}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
class PluginPart extends Part {

	/** The plugin to execute. */
	private final Plugin plugin;

	/** The plugin parameters. */
	private final Map<String, String> pluginParameters;

	/**
	 * Creates a new plugin part.
	 *
	 * @param plugin
	 *            The plugin to execute
	 * @param pluginParameters
	 *            The plugin parameters
	 */
	public PluginPart(Plugin plugin, Map<String, String> pluginParameters) {
		this.plugin = plugin;
		this.pluginParameters = pluginParameters;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void render(DataProvider dataProvider, Writer writer) throws TemplateException {
		plugin.execute(dataProvider, pluginParameters);
	}

}
