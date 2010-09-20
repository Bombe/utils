/*
 * utils - SimpleXML.java - Copyright © 2006-2009 David Roden
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.pterodactylus.util.logging.Logging;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 * SimpleXML is a helper class to construct XML trees in a fast and simple way.
 * Construct a new XML tree by calling {@link #SimpleXML(String)} and append new
 * nodes by calling {@link #append(String)}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class SimpleXML {

	/** Logger. */
	private static final Logger logger = Logging.getLogger(SimpleXML.class.getName());

	/**
	 * A {@link List} containing all child nodes of this node.
	 */
	private List<SimpleXML> children = new ArrayList<SimpleXML>();

	/**
	 * The name of this node.
	 */
	private String name = null;

	/**
	 * The value of this node.
	 */
	private String value = null;

	/** Attributes of the element. */
	private Map<String, String> attributes = null;

	/**
	 * Constructs a new XML node without a name.
	 */
	public SimpleXML() {
		super();
	}

	/**
	 * Constructs a new XML node with the specified name.
	 *
	 * @param name
	 *            The name of the new node
	 */
	public SimpleXML(String name) {
		this(name, (String[]) null, (String[]) null);
	}

	/**
	 * Constructs a new XML node with the specified name and a single attribute.
	 *
	 * @param name
	 *            The name of the node
	 * @param attributeName
	 *            The name of the attribute
	 * @param attributeValue
	 *            The value of the attribute
	 */
	public SimpleXML(String name, String attributeName, String attributeValue) {
		this(name, new String[] { attributeName }, new String[] { attributeValue });
	}

	/**
	 * Constructs a new XML node with the specified name and attributes.
	 *
	 * @param name
	 *            The name of the node
	 * @param attributeNames
	 *            The names of the attribute
	 * @param attributeValues
	 *            The values of the attribute
	 */
	public SimpleXML(String name, String[] attributeNames, String[] attributeValues) {
		this.name = name;
		attributes = new HashMap<String, String>();
		if ((attributeNames != null) && (attributeValues != null) && (attributeNames.length == attributeValues.length)) {
			for (int index = 0, size = attributeNames.length; index < size; index++) {
				attributes.put(attributeNames[index], attributeValues[index]);
			}
		}
	}

	/**
	 * Returns all attributes’ names. The array is not sorted.
	 *
	 * @return The names of all attributes
	 */
	public String[] getAttributeNames() {
		return attributes.keySet().toArray(new String[attributes.size()]);
	}

	/**
	 * Returns the value of the attribute with the given name.
	 *
	 * @param attributeName
	 *            The name of the attribute to look up
	 * @return The value of the attribute
	 */
	public String getAttribute(String attributeName) {
		return getAttribute(attributeName, null);
	}

	/**
	 * Returns the value of the attribute with the given name.
	 *
	 * @param attributeName
	 *            The name of the attribute to look up
	 * @param defaultValue
	 *            The value to return if there is no attribute with the given
	 *            name
	 * @return The value of the attribute
	 */
	public String getAttribute(String attributeName, String defaultValue) {
		if (!attributes.containsKey(attributeName)) {
			return defaultValue;
		}
		return attributes.get(attributeName);
	}

	/**
	 * Sets the value of an attribute.
	 *
	 * @param attributeName
	 *            The name of the attribute to set
	 * @param attributeValue
	 *            The value of the attribute
	 */
	public void setAttribute(String attributeName, String attributeValue) {
		attributes.put(attributeName, attributeValue);
	}

	/**
	 * Removes the attribute with the given name, returning its previous value.
	 *
	 * @param attributeName
	 *            The name of the attribute to remove
	 * @return The value of the attribute before removing it
	 */
	public String removeAttribute(String attributeName) {
		return attributes.remove(attributeName);
	}

	/**
	 * Checks whether this node contains the attribute with the given name.
	 *
	 * @param attributeName
	 *            The name of the attribute
	 * @return <code>true</code> if this node has an attribute with the given
	 *         name, <code>false</code> otherwise
	 */
	public boolean hasAttribute(String attributeName) {
		return attributes.containsKey(attributeName);
	}

	/**
	 * Returns whether this node has any child nodes.
	 *
	 * @return {@code true} if this node has child nodes, {@code false}
	 *         otherwise
	 */
	public boolean hasNodes() {
		return !children.isEmpty();
	}

	/**
	 * Checks if this object has a child with the specified name.
	 *
	 * @param nodeName
	 *            The name of the child node to check for
	 * @return <code>true</code> if this node has at least one child with the
	 *         specified name, <code>false</code> otherwise
	 */
	public boolean hasNode(String nodeName) {
		return getNode(nodeName) != null;
	}

	/**
	 * Returns the child node of this node with the specified name. If there are
	 * several child nodes with the specified name only the first node is
	 * returned.
	 *
	 * @param nodeName
	 *            The name of the child node
	 * @return The child node, or <code>null</code> if there is no child node
	 *         with the specified name
	 */
	public SimpleXML getNode(String nodeName) {
		for (int index = 0, count = children.size(); index < count; index++) {
			if (children.get(index).name.equals(nodeName)) {
				return children.get(index);
			}
		}
		return null;
	}

	/**
	 * Returns the child node that is specified by the names. The first element
	 * of <code>nodeNames</code> is the name of the child node of this node, the
	 * second element of <code>nodeNames</code> is the name of a child node's
	 * child node, and so on. By using this method you can descend into an XML
	 * tree pretty fast.
	 *
	 * <pre>
	 * <code>
	 * SimpleXML deepNode = topNode.getNodes(new String[] { &quot;person&quot;, &quot;address&quot;, &quot;number&quot; });
	 * </code>
	 * </pre>
	 *
	 * @param nodeNames
	 *            The names of the nodes
	 * @return A node that is a deep child of this node, or <code>null</code> if
	 *         the specified node does not eixst
	 */
	public SimpleXML getNode(String[] nodeNames) {
		SimpleXML node = this;
		for (String nodeName : nodeNames) {
			node = node.getNode(nodeName);
		}
		return node;
	}

	/**
	 * Returns all child nodes of this node.
	 *
	 * @return All child nodes of this node
	 */
	public SimpleXML[] getNodes() {
		return getNodes(null);
	}

	/**
	 * Returns all child nodes of this node with the specified name. If there
	 * are no child nodes with the specified name an empty array is returned.
	 *
	 * @param nodeName
	 *            The name of the nodes to retrieve, or <code>null</code> to
	 *            retrieve all nodes
	 * @return All child nodes with the specified name
	 */
	public SimpleXML[] getNodes(String nodeName) {
		List<SimpleXML> resultList = new ArrayList<SimpleXML>();
		for (SimpleXML child : children) {
			if ((nodeName == null) || child.name.equals(nodeName)) {
				resultList.add(child);
			}
		}
		return resultList.toArray(new SimpleXML[resultList.size()]);
	}

	/**
	 * Appends a new XML node with the specified name and returns the new node.
	 * With this method you can create deep structures very fast.
	 *
	 * <pre>
	 * <code>
	 * SimpleXML mouseNode = topNode.append(&quot;computer&quot;).append(&quot;bus&quot;).append(&quot;usb&quot;).append(&quot;mouse&quot;);
	 * </code>
	 * </pre>
	 *
	 * @param nodeName
	 *            The name of the node to append as a child to this node
	 * @return The new node
	 */
	public SimpleXML append(String nodeName) {
		return append(new SimpleXML(nodeName));
	}

	/**
	 * Appends a new XML node with the specified name and value and returns the
	 * new node.
	 *
	 * @param nodeName
	 *            The name of the node to append
	 * @param nodeValue
	 *            The value of the node to append
	 * @return The newly appended node
	 */
	public SimpleXML append(String nodeName, String nodeValue) {
		return append(nodeName).setValue(nodeValue);
	}

	/**
	 * Appends the node with all its child nodes to this node and returns the
	 * child node.
	 *
	 * @param newChild
	 *            The node to append as a child
	 * @return The child node that was appended
	 */
	public SimpleXML append(SimpleXML newChild) {
		children.add(newChild);
		return newChild;
	}

	/**
	 * Removes the specified child from this node.
	 *
	 * @param child
	 *            The child to remove
	 */
	public void remove(SimpleXML child) {
		children.remove(child);
	}

	/**
	 * Removes the child with the specified name from this node. If more than
	 * one children have the same name only the first is removed.
	 *
	 * @param childName
	 *            The name of the child node to remove
	 */
	public void remove(String childName) {
		SimpleXML child = getNode(childName);
		if (child != null) {
			remove(child);
		}
	}

	/**
	 * Replace the child node with the specified name by a new node with the
	 * specified content.
	 *
	 * @param childName
	 *            The name of the child to replace
	 * @param value
	 *            The node child's value
	 */
	public void replace(String childName, String value) {
		remove(childName);
		append(childName, value);
	}

	/**
	 * Replaces the child node that has the same name as the given node by the
	 * given node.
	 *
	 * @param childNode
	 *            The node to replace the previous child node with the same name
	 */
	public void replace(SimpleXML childNode) {
		remove(childNode.getName());
		append(childNode);
	}

	/**
	 * Removes all children from this node.
	 */
	public void removeAll() {
		children.clear();
	}

	/**
	 * Sets the value of this node.
	 *
	 * @param nodeValue
	 *            The new value of this node
	 * @return This node
	 */
	public SimpleXML setValue(String nodeValue) {
		value = nodeValue;
		return this;
	}

	/**
	 * Returns the name of this node.
	 *
	 * @return The name of this node
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the value of this node.
	 *
	 * @return The value of this node
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Returns the value of the first child node with the specified name.
	 *
	 * @param childName
	 *            The name of the child node
	 * @return The value of the child node
	 * @throws NullPointerException
	 *             if the child node does not exist
	 */
	public String getValue(String childName) {
		return getNode(childName).getValue();
	}

	/**
	 * Returns the value of the first child node with the specified name, or the
	 * default value if there is no child node with the given name.
	 *
	 * @param childName
	 *            The name of the child node
	 * @param defaultValue
	 *            The default value to return if there is no child node with the
	 *            given name
	 * @return The value of the child node
	 * @throws NullPointerException
	 *             if the child node does not exist
	 */
	public String getValue(String childName, String defaultValue) {
		SimpleXML childNode = getNode(childName);
		if (childNode == null) {
			return defaultValue;
		}
		return childNode.getValue();
	}

	/**
	 * Creates a {@link Document} from this node and all its child nodes.
	 *
	 * @return The {@link Document} created from this node
	 */
	public Document getDocument() {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.newDocument();
			Element rootElement = document.createElement(name);
			for (Entry<String, String> attributeEntry : attributes.entrySet()) {
				rootElement.setAttribute(attributeEntry.getKey(), attributeEntry.getValue());
			}
			document.appendChild(rootElement);
			addChildren(rootElement);
			return document;
		} catch (ParserConfigurationException e) {
			/* ignore. */
		}
		return null;
	}

	/**
	 * Appends all children of this node to the specified {@link Element}. If a
	 * node has a value that is not <code>null</code> the value is appended as a
	 * text node.
	 *
	 * @param rootElement
	 *            The element to attach this node's children to
	 */
	private void addChildren(Element rootElement) {
		for (SimpleXML child : children) {
			Element childElement = rootElement.getOwnerDocument().createElement(child.name);
			for (Entry<String, String> attributeEntry : child.attributes.entrySet()) {
				childElement.setAttribute(attributeEntry.getKey(), attributeEntry.getValue());
			}
			rootElement.appendChild(childElement);
			if (child.value != null) {
				Text childText = rootElement.getOwnerDocument().createTextNode(child.value);
				childElement.appendChild(childText);
			} else {
				child.addChildren(childElement);
			}
		}
	}

	/**
	 * Creates a SimpleXML node from the specified {@link Document}. The
	 * SimpleXML node of the document's top-level node is returned.
	 *
	 * @param document
	 *            The {@link Document} to create a SimpleXML node from
	 * @return The SimpleXML node created from the document's top-level node
	 */
	public static SimpleXML fromDocument(Document document) {
		SimpleXML xmlDocument = new SimpleXML(document.getFirstChild().getNodeName());
		NamedNodeMap attributes = document.getFirstChild().getAttributes();
		for (int attributeIndex = 0, attributeCount = attributes.getLength(); attributeIndex < attributeCount; attributeIndex++) {
			Node attribute = attributes.item(attributeIndex);
			logger.log(Level.FINER, "adding attribute: " + attribute.getNodeName() + " = " + attribute.getNodeValue());
			xmlDocument.setAttribute(attribute.getNodeName(), attribute.getNodeValue());
		}
		document.normalizeDocument();
		/* look for first non-comment node */
		Node firstChild = null;
		NodeList children = document.getChildNodes();
		for (int index = 0, count = children.getLength(); index < count; index++) {
			Node child = children.item(index);
			if ((child.getNodeType() != Node.COMMENT_NODE) && (child.getNodeType() != Node.PROCESSING_INSTRUCTION_NODE)) {
				firstChild = child;
				break;
			}
		}
		return addDocumentChildren(xmlDocument, firstChild);
	}

	/**
	 * Appends the child nodes of the specified {@link Document} to this node.
	 * Text nodes are converted into a node's value.
	 *
	 * @param xmlDocument
	 *            The SimpleXML node to append the child nodes to
	 * @param document
	 *            The document whose child nodes to append
	 * @return The SimpleXML node the child nodes were appended to
	 */
	private static SimpleXML addDocumentChildren(SimpleXML xmlDocument, Node document) {
		NodeList childNodes = document.getChildNodes();
		for (int childIndex = 0, childCount = childNodes.getLength(); childIndex < childCount; childIndex++) {
			Node childNode = childNodes.item(childIndex);
			if ((childNode.getChildNodes().getLength() == 1) && (childNode.getFirstChild().getNodeName().equals("#text"))) {
				SimpleXML newXML = xmlDocument.append(childNode.getNodeName(), childNode.getFirstChild().getNodeValue());
				NamedNodeMap childNodeAttributes = childNode.getAttributes();
				for (int attributeIndex = 0, attributeCount = childNodeAttributes.getLength(); attributeIndex < attributeCount; attributeIndex++) {
					Node attribute = childNodeAttributes.item(attributeIndex);
					logger.log(Level.FINER, "adding attribute: " + attribute.getNodeName() + " = " + attribute.getNodeValue());
					newXML.setAttribute(attribute.getNodeName(), attribute.getNodeValue());
				}
			} else {
				if ((childNode.getNodeType() == Node.ELEMENT_NODE) || (childNode.getChildNodes().getLength() != 0)) {
					SimpleXML newXML = xmlDocument.append(childNode.getNodeName());
					NamedNodeMap childNodeAttributes = childNode.getAttributes();
					for (int attributeIndex = 0, attributeCount = childNodeAttributes.getLength(); attributeIndex < attributeCount; attributeIndex++) {
						Node attribute = childNodeAttributes.item(attributeIndex);
						logger.log(Level.FINER, "adding attribute: " + attribute.getNodeName() + " = " + attribute.getNodeValue());
						newXML.setAttribute(attribute.getNodeName(), attribute.getNodeValue());
					}
					addDocumentChildren(newXML, childNode);
				}
			}
		}
		return xmlDocument;
	}
}
