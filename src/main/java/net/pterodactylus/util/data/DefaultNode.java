/*
 * utils - DefaultNode.java - Copyright © 2009-2010 David Roden
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

package net.pterodactylus.util.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Default implementation of the {@link Node} interface.
 *
 * @param <E>
 *            The type of the element to store
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
class DefaultNode<E extends Comparable<E>> implements Node<E> {

	/** The parent node of this node. */
	private final Node<E> parentNode;

	/** The element contained in this node. */
	private final E element;

	/** The child nodes of this node. */
	private final List<Node<E>> children = new ArrayList<Node<E>>();

	/**
	 * Creates a new root node.
	 */
	DefaultNode() {
		this.parentNode = null;
		this.element = null;
	}

	/**
	 * Creates a new node with the given parent and element.
	 *
	 * @param parentNode
	 *            The parent of this node
	 * @param element
	 *            The element of this node
	 */
	DefaultNode(Node<E> parentNode, E element) {
		if ((parentNode == null) || (element == null)) {
			throw new NullPointerException("null is not allowed as parent or element");
		}
		this.parentNode = parentNode;
		this.element = element;
	}

	/**
	 * {@inheritDoc}
	 */
	public Node<E> getParent() {
		return parentNode;
	}

	/**
	 * {@inheritDoc}
	 */
	public E getElement() {
		return element;
	}

	/**
	 * {@inheritDoc}
	 */
	public Node<E> addChild(E child) {
		Node<E> childNode = new DefaultNode<E>(this, child);
		children.add(childNode);
		return childNode;
	}

	/**
	 * {@inheritDoc}
	 */
	public int size() {
		return children.size();
	}

	/**
	 * {@inheritDoc}
	 */
	public Node<E> getChild(int childIndex) {
		return children.get(childIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	public Node<E> getChild(E element) {
		for (Node<E> childNode : children) {
			if (childNode.getElement().equals(element)) {
				return childNode;
			}
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean hasChild(Node<E> childNode) {
		return children.contains(childNode);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean hasChild(E element) {
		for (Node<E> childNode : children) {
			if (childNode.getElement().equals(element)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getIndexOfChild(Node<E> childNode) {
		int childIndex = 0;
		for (Node<E> node : children) {
			if (node.equals(childNode)) {
				return childIndex;
			}
			childIndex++;
		}
		return -1;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getIndexOfChild(E element) {
		int childIndex = 0;
		for (Node<E> node : children) {
			if (node.getElement().equals(element)) {
				return childIndex;
			}
			childIndex++;
		}
		return -1;
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeChild(Node<E> childNode) {
		children.remove(childNode);
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeChild(E child) {
		for (Node<E> childNode : children) {
			if (child.equals(childNode.getElement())) {
				children.remove(childNode);
				break;
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeChild(int childIndex) {
		children.remove(childIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeAllChildren() {
		children.clear();
	}

	/**
	 * {@inheritDoc}
	 */
	public Iterator<Node<E>> iterator() {
		return children.iterator();
	}

	/**
	 * {@inheritDoc}
	 */
	public Node<E> findChild(E element) {
		for (Node<E> childNode : children) {
			Node<E> wantedNode = childNode.findChild(element);
			if (wantedNode != null) {
				return wantedNode;
			}
		}
		if (this.element.equals(element)) {
			return this;
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public void sortChildren() {
		Collections.sort(children);
	}

	/**
	 * {@inheritDoc}
	 */
	public void sortChildren(Comparator<Node<E>> comparator) {
		Collections.sort(children, comparator);
	}

	//
	// INTERFACE Comparable
	//

	/**
	 * {@inheritDoc}
	 */
	public int compareTo(Node<E> otherNode) {
		return element.compareTo(otherNode.getElement());
	}

}
