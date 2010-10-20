/*
 * utils - UnmodifiableDataProvider.java - Copyright © 2010 David Roden
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
 * Wrapper around a {@link DataProvider} that prevents modifications.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class UnmodifiableDataProvider extends DataProvider {

	/** The wrapped data provider. */
	private final DataProvider dataProvider;

	/**
	 * Creates a new unmodifiable data provider backed by the given data
	 * provider.
	 *
	 * @param dataProvider
	 *            The data provider to wrap
	 */
	public UnmodifiableDataProvider(DataProvider dataProvider) {
		this.dataProvider = dataProvider;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addAccessor(Class<?> clazz, Accessor accessor) {
		/* ignore. */
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Accessor findAccessor(Class<?> clazz) {
		return dataProvider.findAccessor(clazz);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DataStore getDataStore() {
		/* TODO - return an unmodifiable data store here? */
		return dataProvider.getDataStore();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getData(String name) throws TemplateException {
		return dataProvider.getData(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setData(String name, Object data) {
		/* ignore. */
	}

}
