/*
 * utils - TimedMap.java - Copyright © 2011–2016 David Roden
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

package net.pterodactylus.util.collection;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * {@link Map} implementation that forgets its entries after a specified time.
 *
 * @param <K>
 *            The type of the key
 * @param <V>
 *            The type of the value
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class TimedMap<K, V> implements Map<K, V> {

	/** The backing storage. */
	private final Map<K, TimedItem<V>> storage = new HashMap<K, TimedItem<V>>();

	/** The maximum ago for items in the map. */
	private final long maximumAge;

	/**
	 * Creates a new timed map with a default maximum age of 60 seconds.
	 */
	public TimedMap() {
		this(60000);
	}

	/**
	 * Creates a new timed map.
	 *
	 * @param maximumAge
	 *            The maximum age for items (in milliseconds)
	 */
	public TimedMap(long maximumAge) {
		this.maximumAge = maximumAge;
	}

	/**
	 * Creates a new timed map with a default maximum age of 60 seconds, copying
	 * the given map.
	 *
	 * @param original
	 *            The map to copy
	 */
	public TimedMap(Map<K, V> original) {
		this(60000, original);
	}

	/**
	 * Creates a new timed map, copying the given map.
	 *
	 * @param maximumAge
	 *            The maximum age for items (in milliseconds)
	 * @param original
	 *            The map to copy
	 */
	public TimedMap(long maximumAge, Map<K, V> original) {
		this(maximumAge);
		for (Entry<K, V> originalEntry : original.entrySet()) {
			storage.put(originalEntry.getKey(), new TimedItem<V>(originalEntry.getValue()));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		checkForExpiredValues();
		return storage.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		checkForExpiredValues();
		return storage.isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsKey(Object key) {
		checkForExpiredValues();
		return storage.containsKey(key);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsValue(Object value) {
		checkForExpiredValues();
		for (Entry<K, TimedItem<V>> entry : storage.entrySet()) {
			if (((value == null) && (entry.getValue().getItem() == null)) || ((value != null) && (value.equals(entry.getValue().getItem())))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public V get(Object key) {
		checkForExpiredValues();
		TimedItem<V> item = storage.get(key);
		return (item != null) ? item.getItem() : null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public V put(K key, V value) {
		checkForExpiredValues();
		TimedItem<V> oldItem = storage.get(key);
		storage.put(key, new TimedItem<V>(value));
		return (oldItem != null) ? oldItem.getItem() : null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public V remove(Object key) {
		checkForExpiredValues();
		TimedItem<V> removedItem = storage.remove(key);
		return (removedItem != null) ? removedItem.getItem() : null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void putAll(Map<? extends K, ? extends V> map) {
		checkForExpiredValues();
		for (Entry<? extends K, ? extends V> entry : map.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear() {
		storage.clear();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<K> keySet() {
		checkForExpiredValues();
		return storage.keySet();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<V> values() {
		checkForExpiredValues();
		List<V> values = new ArrayList<V>();
		for (TimedItem<V> value : storage.values()) {
			values.add(value.getItem());
		}
		return values;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Entry<K, V>> entrySet() {
		checkForExpiredValues();
		Set<Entry<K, V>> resultSet = new HashSet<Entry<K, V>>();
		for (Entry<K, TimedItem<V>> entry : storage.entrySet()) {
			resultSet.add(new AbstractMap.SimpleEntry<K, V>(entry.getKey(), entry.getValue().getItem()));
		}
		return resultSet;
	}

	//
	// PRIVATE METHODS
	//

	/**
	 * Checks the map for expired values and removes them.
	 */
	private void checkForExpiredValues() {
		long maximumTime = System.currentTimeMillis() - maximumAge;
		for (Iterator<Entry<K, TimedItem<V>>> entryIterator = storage.entrySet().iterator(); entryIterator.hasNext();) {
			Entry<K, TimedItem<V>> entry = entryIterator.next();
			if (entry.getValue().getTimestamp() < maximumTime) {
				entryIterator.remove();
			}
		}
	}

	/**
	 * Container that stores an item and a timestamp.
	 *
	 * @param <V>
	 *            The type of the item
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	private static class TimedItem<V> {

		/** The timestamp of the item. */
		private final long timestamp;

		/** The item. */
		private final V item;

		/**
		 * Creates a new timed item with a timestamp of “now.”
		 *
		 * @param item
		 *            The item
		 */
		public TimedItem(V item) {
			this(System.currentTimeMillis(), item);
		}

		/**
		 * Creates a new timed item.
		 *
		 * @param timestamp
		 *            The timestamp of the item
		 * @param item
		 *            The item
		 */
		public TimedItem(long timestamp, V item) {
			this.timestamp = timestamp;
			this.item = item;
		}

		/**
		 * Returns the timestamp of the item.
		 *
		 * @return The timestamp of the item
		 */
		public long getTimestamp() {
			return timestamp;
		}

		/**
		 * Returns the item.
		 *
		 * @return The item
		 */
		public V getItem() {
			return item;
		}

	}

}
