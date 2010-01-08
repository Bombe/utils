/*
 * utils - DOMUtil.java - Copyright © 2006-2009 David Roden
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

package net.pterodactylus.util.xml;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Contains various helper methods that let you more easily work with
 * {@code org.w3c.dom} objects.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class DOMUtil {

	/**
	 * Returns the first child node of the given root node that has the given
	 * name.
	 *
	 * @param rootNode
	 *            The root node
	 * @param childName
	 *            The name of the child node
	 * @return The child node, or {@code null} if the root node does not have a
	 *         child node with the given name
	 */
	public static Node getChildNode(Node rootNode, String childName) {
		NodeList nodeList = rootNode.getChildNodes();
		for (int nodeIndex = 0, nodeSize = nodeList.getLength(); nodeIndex < nodeSize; nodeIndex++) {
			Node childNode = nodeList.item(nodeIndex);
			if (childNode.getNodeName().equals(childName)) {
				return childNode;
			}
		}
		return null;
	}

	/**
	 * Returns all child nodes of the given root node with the given name.
	 *
	 * @param rootNode
	 *            The root node
	 * @param childName
	 *            The name of child nodes
	 * @return All child nodes with the given name, or an empty array if there
	 *         are no child nodes with the given name
	 */
	public static Node[] getChildNodes(Node rootNode, String childName) {
		List<Node> nodes = new ArrayList<Node>();
		NodeList nodeList = rootNode.getChildNodes();
		for (int nodeIndex = 0, nodeSize = nodeList.getLength(); nodeIndex < nodeSize; nodeIndex++) {
			Node childNode = nodeList.item(nodeIndex);
			if (childNode.getNodeName().equals(childName)) {
				nodes.add(childNode);
			}
		}
		return nodes.toArray(new Node[nodes.size()]);
	}

}
