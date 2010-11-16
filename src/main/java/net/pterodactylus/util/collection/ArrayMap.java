/*
 * utils - ArrayMap.java - Copyright © 2010 David Roden
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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * An {@code ArrayMap} is a {@link Map} implementation that is backed by arrays.
 * It does not rely on {@link Object#hashCode() object hashes} but solely uses
 * {@link Object#equals(Object)} to compare objects.
 *
 * @param <K>
 *            The type of the keys
 * @param <V>
 *            The type of the values
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ArrayMap<K, V> implements Map<K, V> {

	/** The keys. */
	private Object[] keys;

	/** The values. */
	private Object[] values;

	/** The current size. */
	private int size = 0;

	/**
	 * Creates a new array map with a default size of 10.
	 */
	public ArrayMap() {
		this(10);
	}

	/**
	 * Creates a new array map with the given default size.
	 *
	 * @param initialSize
	 *            The initial size of the array map
	 */
	public ArrayMap(int initialSize) {
		keys = new Object[initialSize];
		values = new Object[initialSize];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		return size;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsKey(Object key) {
		return locateKey(key) != -1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsValue(Object value) {
		return locateValue(value) != -1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public V get(Object key) {
		int index = locateKey(key);
		if (index == -1) {
			return null;
		}
		return (V) values[index];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public V put(K key, V value) {
		int index = locateKey(key);
		if (index == -1) {
			checkResize();
			keys[size] = key;
			values[size] = value;
			++size;
			return null;
		}
		Object oldValue = values[index];
		values[index] = value;
		return (V) oldValue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public V remove(Object key) {
		int index = locateKey(key);
		if (index == -1) {
			return null;
		}
		Object value = values[index];
		if (index < (size - 1)) {
			keys[index] = keys[size - 1];
			values[index] = values[size - 1];
		}
		--size;
		return (V) value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void putAll(Map<? extends K, ? extends V> map) {
		for (Entry<? extends K, ? extends V> entry : map.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear() {
		size = 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Set<K> keySet() {
		if (size < keys.length) {
			Object[] temp = new Object[size];
			System.arraycopy(keys, 0, temp, 0, size);
			return new HashSet<K>(Arrays.asList((K[]) temp));
		}
		return new HashSet<K>(Arrays.asList((K[]) keys));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Collection<V> values() {
		if (size < keys.length) {
			Object[] temp = new Object[size];
			System.arraycopy(values, 0, temp, 0, size);
			return Arrays.asList((V[]) temp);
		}
		return Arrays.asList((V[]) values);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Set<Entry<K, V>> entrySet() {
		Set<Entry<K, V>> entries = new HashSet<Entry<K, V>>();
		for (int index = 0; index < size; ++index) {
			final K key = (K) keys[index];
			final V value = (V) values[index];
			entries.add(new Entry<K, V>() {

				@Override
				public K getKey() {
					return key;
				}

				@Override
				public V getValue() {
					return value;
				}

				@Override
				public V setValue(V value) {
					/* nothing. */
					return value;
				}
			});
		}
		return entries;
	}

	//
	// PRIVATE METHODS
	//

	/**
	 * Locates the given key in the {@link #keys} array.
	 *
	 * @param key
	 *            The key to locate
	 * @return The index of the key, or {@code -1} if the key could not be found
	 */
	private int locateKey(Object key) {
		return locateObject(keys, key);
	}

	/**
	 * Locates the index of the given value.
	 *
	 * @param value
	 *            The value to locate
	 * @return The index of the value, or {@code -1} if the value could not be
	 *         found
	 */
	private int locateValue(Object value) {
		return locateObject(values, value);
	}

	/**
	 * Locates an object in the given array of objects.
	 *
	 * @param data
	 *            The array of objects to search
	 * @param value
	 *            The object to search
	 * @return The index of the object, or {@code -1} if the object could not be
	 *         found
	 */
	private int locateObject(Object[] data, Object value) {
		for (int index = 0; index < size; ++index) {
			if ((value == null) && (data[index] == null) || ((value != null) && value.equals(data[index]))) {
				return index;
			}
		}
		return -1;
	}

	/**
	 * Checks if the map needs to be resized and resizes it if the current
	 * {@link #size} equals the current capacity of the {@link #keys} array.
	 */
	private void checkResize() {
		if (size == (keys.length)) {
			Object[] newKeys = new Object[keys.length * 2];
			Object[] newValues = new Object[keys.length * 2];
			System.arraycopy(keys, 0, newKeys, 0, size);
			System.arraycopy(values, 0, newValues, 0, size);
			keys = newKeys;
			values = newValues;
		}
	}

}
