/*
 * utils - PluginPart.java - Copyright © 2010–2016 David Roden
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
class PluginPart extends AbstractPart {

	/** The name of the plugin to execute. */
	private final String pluginName;

	/** The plugin parameters. */
	private final Map<String, String> pluginParameters;

	/**
	 * Creates a new plugin part.
	 *
	 * @param line
	 *            The line number of the tag
	 * @param column
	 *            The column number of the tag
	 * @param pluginName
	 *            The name of the plugin to execute
	 * @param pluginParameters
	 *            The plugin parameters
	 */
	public PluginPart(int line, int column, String pluginName, Map<String, String> pluginParameters) {
		super(line, column);
		this.pluginName = pluginName;
		this.pluginParameters = pluginParameters;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void render(TemplateContext templateContext, Writer writer) throws TemplateException {
		Plugin plugin = templateContext.getPlugin(pluginName);
		if (plugin == null) {
			throw new TemplateException(getLine(), getColumn(), "Plugin “" + pluginName + "” not found.");
		}
		plugin.execute(templateContext, pluginParameters);
	}

}
