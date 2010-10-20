/*
 * utils - MultipleDataProvider.java - Copyright © 2010 David Roden
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

/**
 * {@link DataProvider} implementation that can get its data from multiple other
 * {@link DataProvider}s.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class MultipleDataProvider extends DataProvider {

	/** The source data providers. */
	private final List<DataProvider> dataProviders = new ArrayList<DataProvider>();

	/** The data stores. */
	private final MultipleDataStore dataStore;

	/**
	 * Creates a new multiple data provider.
	 *
	 * @param dataProviders
	 *            The source data providers
	 */
	public MultipleDataProvider(DataProvider... dataProviders) {
		this.dataProviders.addAll(Arrays.asList(dataProviders));
		List<DataStore> dataStores = new ArrayList<DataStore>();
		for (DataProvider dataProvider : dataProviders) {
			dataStores.add(dataProvider.getDataStore());
		}
		this.dataStore = new MultipleDataStore(dataStores);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getData(String name) throws TemplateException {
		if (name.indexOf('.') == -1) {
			for (DataProvider dataProvider : dataProviders) {
				Object data = dataProvider.getDataStore().get(name);
				if (data != null) {
					return data;
				}
			}
			return null;
		}
		StringTokenizer nameTokens = new StringTokenizer(name, ".");
		Object object = null;
		while (nameTokens.hasMoreTokens()) {
			String nameToken = nameTokens.nextToken();
			if (object == null) {
				for (DataProvider dataProvider : dataProviders) {
					object = dataProvider.getDataStore().get(nameToken);
					if (object != null) {
						break;
					}
				}
			} else {
				Accessor accessor = null;
				for (DataProvider dataProvider : dataProviders) {
					accessor = dataProvider.findAccessor(object.getClass());
					if (accessor != null) {
						break;
					}
				}
				if (accessor != null) {
					object = accessor.get(this, object, nameToken);
				} else {
					throw new TemplateException("no accessor found for " + object.getClass());
				}
			}
			if (object == null) {
				return null;
			}
		}
		return object;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DataStore getDataStore() {
		return dataStore;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setData(String name, Object data) {
		for (DataProvider dataProvider : dataProviders) {
			dataProvider.setData(name, data);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Accessor findAccessor(Class<?> clazz) {
		for (DataProvider dataProvider : dataProviders) {
			Accessor accessor = dataProvider.findAccessor(clazz);
			if (accessor != null) {
				return accessor;
			}
		}
		return null;
	}

	/**
	 * A {@link DataStore} implementation that is backed by multiple other
	 * {@link DataStore}s.
	 *
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	private static class MultipleDataStore implements DataStore {

		/** The backing data stores. */
		private List<DataStore> dataStores = new ArrayList<DataStore>();

		/**
		 * Creates a new multiple data store.
		 *
		 * @param dataStores
		 *            The backing data stores
		 */
		public MultipleDataStore(List<DataStore> dataStores) {
			this.dataStores.addAll(dataStores);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Object get(String name) {
			for (DataStore dataStore : dataStores) {
				Object data = dataStore.get(name);
				if (data != null) {
					return data;
				}
			}
			return null;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void set(String name, Object data) {
			for (DataStore dataStore : dataStores) {
				dataStore.set(name, data);
			}
		}

	}

}
