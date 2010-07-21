/*
 * utils - Node.java - Copyright © 2009-2010 David Roden
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

import java.util.Comparator;
import java.util.Iterator;

/**
 * A node that can be stored in a {@link Tree}. A node has exactly one parent
 * (which is <code>null</code> if the node is the {@link Tree#getRootNode()} of
 * the tree) and an arbitrary amount of child nodes.
 *
 * @param <E>
 *            The type of the element to store
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public interface Node<E extends Comparable<E>> extends Iterable<Node<E>>, Comparable<Node<E>> {

	/**
	 * Returns the parent node of the node.
	 *
	 * @return The parent node
	 */
	public Node<E> getParent();

	/**
	 * Returns the element that is stored in the node.
	 *
	 * @return The node’s element
	 */
	public E getElement();

	/**
	 * Adds an element as a child to this node and returns the created node.
	 *
	 * @param child
	 *            The child node’s element
	 * @return The created child node
	 */
	public Node<E> addChild(E child);

	/**
	 * Returns the number of children this node has.
	 *
	 * @return The number of children
	 */
	public int size();

	/**
	 * Returns the child at the given index.
	 *
	 * @param index
	 *            The index of the child
	 * @return The child at the given index
	 */
	public Node<E> getChild(int index);

	/**
	 * Returns the direct child node that contains the given element.
	 *
	 * @param element
	 *            The element
	 * @return The direct child node containing the given element, or
	 *         <code>null</code> if this node does not have a child node
	 *         containing the given element
	 */
	public Node<E> getChild(E element);

	/**
	 * Returns whether the given node is a direct child of this node.
	 *
	 * @param childNode
	 *            The child node
	 * @return <code>true</code> if the given node is a direct child of this
	 *         node, <code>false</code> otherwise
	 */
	public boolean hasChild(Node<E> childNode);

	/**
	 * Returns whether this node contains a child node containing the given
	 * element.
	 *
	 * @param element
	 *            The element
	 * @return <code>true</code> if this node contains a direct child node
	 *         containing the given element, <code>false</code> otherwise
	 */
	public boolean hasChild(E element);

	/**
	 * Returns the index of the given child node.
	 *
	 * @param childNode
	 *            The child node
	 * @return The index of the child node, or <code>-1</code> if the child node
	 *         is not a child node of this node
	 */
	public int getIndexOfChild(Node<E> childNode);

	/**
	 * Returns the index of the child node containing the given element.
	 *
	 * @param element
	 *            The element
	 * @return The index of the child node, or <code>-1</code> if the child node
	 *         is not a child node of this node
	 */
	public int getIndexOfChild(E element);

	/**
	 * Remove the given child node from this node. If the given node is not a
	 * child of this node, nothing happens.
	 *
	 * @param childNode
	 *            The child node to remove
	 */
	public void removeChild(Node<E> childNode);

	/**
	 * Removes the child node that contains the given element. The element in
	 * the node is checked using {@link Object#equals(Object)}.
	 *
	 * @param child
	 *            The child element to remove
	 */
	public void removeChild(E child);

	/**
	 * Removes the child at the given index.
	 *
	 * @param childIndex
	 *            The index of the child to remove
	 */
	public void removeChild(int childIndex);

	/**
	 * Removes all children from this node.
	 */
	public void removeAllChildren();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<Node<E>> iterator();

	/**
	 * Searches this node’s children recursively for a node that contains the
	 * given element.
	 *
	 * @param element
	 *            The element to search
	 * @return The node that contains the given element, or <code>null</code> if
	 *         no node could be found
	 */
	public Node<E> findChild(E element);

	/**
	 * Sorts all children according to their natural order.
	 */
	public void sortChildren();

	/**
	 * Sorts all children with the given comparator.
	 *
	 * @param comparator
	 *            The comparator used to sort the children
	 */
	public void sortChildren(Comparator<Node<E>> comparator);

}
