/*
 * utils - SortableTreeNode.java - Copyright © 2008-2010 David Roden
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

/**
 * {@link MutableTreeNode} subclass that allows to sort its children.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class SortableTreeNode implements MutableTreeNode {

	/** The parent node. */
	private MutableTreeNode parentNode;

	/** The user-defined object. */
	private Object userObject;

	/** Whether this node allows children. */
	private boolean allowsChildren;

	/** The children of this node. */
	private List<MutableTreeNode> children = new ArrayList<MutableTreeNode>();

	/**
	 * Creates a new sortable tree node.
	 *
	 * @param allowsChildren
	 *            <code>true</code> if this node allows children,
	 *            <code>false</code> otherwise
	 */
	public SortableTreeNode(boolean allowsChildren) {
		this(null, allowsChildren);
	}

	/**
	 * Creates a new sortable tree node that contains the given user-defined
	 * object.
	 *
	 * @param userObject
	 *            The user-defined object
	 */
	public SortableTreeNode(Object userObject) {
		this(userObject, true);
	}

	/**
	 * Creates a new sortable tree node that contains the given user-defined
	 * object.
	 *
	 * @param userObject
	 *            The user-defined object
	 * @param allowsChildren
	 *            <code>true</code> if this node allows children,
	 *            <code>false</code> otherwise
	 */
	public SortableTreeNode(Object userObject, boolean allowsChildren) {
		this.allowsChildren = allowsChildren;
		this.userObject = userObject;
	}

	//
	// ACCESSORS
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean getAllowsChildren() {
		return allowsChildren;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TreeNode getChildAt(int childIndex) {
		return children.get(childIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getChildCount() {
		return children.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getIndex(TreeNode node) {
		return children.indexOf(node);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TreeNode getParent() {
		return parentNode;
	}

	/**
	 * Returns the user-defined object.
	 *
	 * @return The user-defined object
	 */
	public Object getUserObject() {
		return userObject;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isLeaf() {
		return children.isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Enumeration<?> children() {
		return Collections.enumeration(children);
	}

	//
	// ACTIONS
	//

	/**
	 * Adds the given node to this node as a child.
	 *
	 * @param child
	 *            The child node to add
	 */
	public void add(MutableTreeNode child) {
		children.add(child);
		child.setParent(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void insert(MutableTreeNode child, int index) {
		children.add(index, child);
		child.setParent(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove(int index) {
		children.remove(index).setParent(null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove(MutableTreeNode node) {
		children.remove(node);
		node.setParent(null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeFromParent() {
		if (parentNode != null) {
			parentNode.remove(this);
			parentNode = null;
		}
	}

	/**
	 * Removes all children of this node.
	 */
	public void removeAll() {
		for (MutableTreeNode childNode : children) {
			childNode.setParent(null);
		}
		children.clear();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setParent(MutableTreeNode newParent) {
		parentNode = newParent;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUserObject(Object userObject) {
		this.userObject = userObject;
	}

	/**
	 * Sorts the children of this node.
	 */
	public void sort() {
		Collections.sort(children, new Comparator<MutableTreeNode>() {

			/**
			 * {@inheritDoc}
			 */
			@Override
			@SuppressWarnings( { "synthetic-access", "unchecked" })
			public int compare(MutableTreeNode firstNode, MutableTreeNode secondNode) {
				if (!(firstNode instanceof SortableTreeNode) || !(secondNode instanceof SortableTreeNode)) {
					return 0;
				}
				SortableTreeNode firstSortableNode = (SortableTreeNode) firstNode;
				SortableTreeNode secondSortableNode = (SortableTreeNode) secondNode;
				if ((firstSortableNode.userObject == null) && (secondSortableNode.userObject == null)) {
					return 0;
				}
				if ((firstSortableNode.userObject == null) && (secondSortableNode.userObject != null)) {
					return -1;
				}
				if ((firstSortableNode.userObject != null) && (secondSortableNode.userObject == null)) {
					return 1;
				}
				if (!(firstSortableNode.userObject instanceof Comparable) || !(secondSortableNode.userObject instanceof Comparable)) {
					return 0;
				}
				return ((Comparable<Object>) firstSortableNode.userObject).compareTo(secondSortableNode.userObject);
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return (userObject != null) ? userObject.toString() : null;
	}

}
