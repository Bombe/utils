/*
 * utils - DataTemplateProvider.java - Copyright © 2010 David Roden
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
 * {@link TemplateProvider} implementation that retrieves a {@link Template}
 * from a {@link DataProvider}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class DataTemplateProvider implements TemplateProvider {

	/** The data provider. */
	private final DataProvider dataProvider;

	/**
	 * Creates a new {@link DataProvider}-based {@link TemplateProvider}.
	 *
	 * @param dataProvider
	 *            The underlying data provider
	 */
	public DataTemplateProvider(DataProvider dataProvider) {
		this.dataProvider = dataProvider;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Template getTemplate(String templateName) {
		Object templateObject = dataProvider.getData(templateName);
		return (templateObject instanceof Template) ? (Template) templateObject : null;
	}

}
