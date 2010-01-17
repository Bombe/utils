/*
 * utils - ListListModel.java - Copyright © 2010 David Roden
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

package net.pterodactylus.util.swing;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * Wrapper around a {@link List} that doubles as a {@link ListModel}, e.g. for a
 * {@link JComboBox} or a {@link JList}.
 *
 * @param <E>
 *            The type of the elements
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ListModelList<E> implements ListModel, List<E> {

	/** The wrapped list. */
	private final List<E> wrappedList;

	/** The list data listeners. */
	private final List<ListDataListener> listDataListeners = new CopyOnWriteArrayList<ListDataListener>();

	/**
	 * Creates a new list model list by wrapping the given list.
	 *
	 * @param originalList
	 *            The list to wrap
	 */
	public ListModelList(List<E> originalList) {
		this.wrappedList = originalList;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addListDataListener(ListDataListener listDataListener) {
		listDataListeners.add(listDataListener);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public E getElementAt(int index) {
		return wrappedList.get(index);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getSize() {
		return wrappedList.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeListDataListener(ListDataListener listDataListener) {
		listDataListeners.remove(listDataListener);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean add(E element) {
		boolean added = wrappedList.add(element);
		int position = wrappedList.size() - 1;
		ListDataEvent listDataEvent = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, position, position);
		for (ListDataListener listDataListener : listDataListeners) {
			listDataListener.intervalAdded(listDataEvent);
		}
		return added;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void add(int index, E element) {
		wrappedList.add(element);
		ListDataEvent listDataEvent = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, index, index);
		for (ListDataListener listDataListener : listDataListeners) {
			listDataListener.intervalAdded(listDataEvent);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addAll(Collection<? extends E> collection) {
		int firstPosition = wrappedList.size();
		boolean changed = wrappedList.addAll(collection);
		if (changed) {
			ListDataEvent listDataEvent = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, firstPosition, wrappedList.size() - 1);
			for (ListDataListener listDataListener : listDataListeners) {
				listDataListener.intervalAdded(listDataEvent);
			}
		}
		return changed;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addAll(int index, Collection<? extends E> collection) {
		boolean changed = wrappedList.addAll(collection);
		if (changed) {
			ListDataEvent listDataEvent = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, index, index + collection.size() - 1);
			for (ListDataListener listDataListener : listDataListeners) {
				listDataListener.intervalAdded(listDataEvent);
			}
		}
		return changed;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear() {
		if (!wrappedList.isEmpty()) {
			int size = wrappedList.size();
			wrappedList.clear();
			ListDataEvent listDataEvent = new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, 0, size - 1);
			for (ListDataListener listDataListener : listDataListeners) {
				listDataListener.intervalRemoved(listDataEvent);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean contains(Object object) {
		return wrappedList.contains(object);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsAll(Collection<?> collection) {
		return wrappedList.containsAll(collection);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public E get(int index) {
		return wrappedList.get(index);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int indexOf(Object object) {
		return wrappedList.indexOf(object);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		return wrappedList.isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<E> iterator() {
		return wrappedList.iterator();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int lastIndexOf(Object object) {
		return wrappedList.lastIndexOf(object);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ListIterator<E> listIterator() {
		return wrappedList.listIterator();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ListIterator<E> listIterator(int index) {
		return wrappedList.listIterator(index);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean remove(Object object) {
		int index = wrappedList.indexOf(object);
		if (index != -1) {
			wrappedList.remove(object);
			ListDataEvent listDataEvent = new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, index, index);
			for (ListDataListener listDataListener : listDataListeners) {
				listDataListener.intervalRemoved(listDataEvent);
			}
			return true;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public E remove(int index) {
		E removedElement = wrappedList.remove(index);
		ListDataEvent listDataEvent = new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, index, index);
		for (ListDataListener listDataListener : listDataListeners) {
			listDataListener.intervalRemoved(listDataEvent);
		}
		return removedElement;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeAll(Collection<?> collection) {
		int lowestPosition = Integer.MAX_VALUE;
		int highestPosition = Integer.MIN_VALUE;
		for (Object element : collection) {
			int position = wrappedList.indexOf(element);
			if (position == -1) {
				continue;
			}
			if (position < lowestPosition) {
				lowestPosition = position;
			}
			if (position > highestPosition) {
				highestPosition = position;
			}
		}
		if (lowestPosition < Integer.MAX_VALUE) {
			for (Object element : collection) {
				wrappedList.remove(element);
			}
			ListDataEvent listDataEvent = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, lowestPosition, highestPosition);
			for (ListDataListener listDataListener : listDataListeners) {
				listDataListener.contentsChanged(listDataEvent);
			}
		}
		return (lowestPosition < Integer.MAX_VALUE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean retainAll(Collection<?> collection) {
		int size = wrappedList.size();
		boolean changed = wrappedList.retainAll(collection);
		ListDataEvent listDataEvent = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, size - 1);
		for (ListDataListener listDataListener : listDataListeners) {
			listDataListener.contentsChanged(listDataEvent);
		}
		return changed;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public E set(int index, E element) {
		E oldElement = wrappedList.set(index, element);
		ListDataEvent listDataEvent = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, index, index);
		for (ListDataListener listDataListener : listDataListeners) {
			listDataListener.contentsChanged(listDataEvent);
		}
		return oldElement;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		return wrappedList.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		return wrappedList.subList(fromIndex, toIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] toArray() {
		return wrappedList.toArray();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T[] toArray(T[] a) {
		return wrappedList.toArray(a);
	}

}
