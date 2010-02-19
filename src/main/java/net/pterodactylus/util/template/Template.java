/*
 * utils - TemplateImpl.java - Copyright © 2010 David Roden
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.StringTokenizer;

/**
 * Simple template system that is geared towards easy of use and high
 * performance.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Template implements DataProvider {

	/** Objects stored in the template. */
	private final Map<String, Object> templateObjects = new HashMap<String, Object>();

	/** The input of the template. */
	private Reader input;

	/** The data provider of this template. */
	private DataProvider dataProvider = this;

	/**
	 * Sets the template’s input source. This is the text that will be parsed to
	 * create the output when {@link #render(Writer)} is called.
	 *
	 * @param input
	 *            The template’s input source
	 */
	public void setInput(Reader input) {
		this.input = input;
	}

	/**
	 * Sets the template object with the given name.
	 *
	 * @param name
	 *            The name of the template object
	 * @param object
	 *            The object to store in the template
	 */
	public void set(String name, Object object) {
		System.out.println("storing " + object + " as " + name);
		templateObjects.put(name, object);
	}

	/**
	 * Renders the template to the given writer.
	 *
	 * @param writer
	 *            The write to render the template to
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public void render(Writer writer) throws IOException {
		extractParts().render(writer);
	}

	//
	// INTERFACE DataProvider
	//

	/**
	 * {@inheritDoc}
	 */
	public Object getData(String name) {
		return templateObjects.get(name);
	}

	//
	// PRIVATE METHODS
	//

	/**
	 * Parses the template and creates {@link Part}s of the input.
	 *
	 * @return The list of parts created from the template’s
	 *         {@link #setInputSource(TemplateInput) input source}
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	@SuppressWarnings("synthetic-access")
	private Part extractParts() throws IOException {
		BufferedReader bufferedInputReader;
		if (input instanceof BufferedReader) {
			bufferedInputReader = (BufferedReader) input;
		} else {
			bufferedInputReader = new BufferedReader(input);
		}
		Stack<ContainerPart> partsStack = new Stack<ContainerPart>();
		DataProvider dataProvider = new DynamicDataProvider();
		ContainerPart parts = new ContainerPart(dataProvider);
		StringBuilder currentTextPart = new StringBuilder();
		boolean gotLeftAngleBracket = false;
		boolean inAngleBracket = false;
		while (true) {
			int nextCharacter = bufferedInputReader.read();
			if (nextCharacter == -1) {
				break;
			}
			System.out.print("(" + (char) nextCharacter + ")");
			if (inAngleBracket) {
				if (nextCharacter == '>') {
					inAngleBracket = false;
					String objectName = currentTextPart.toString().trim();
					currentTextPart.setLength(0);
					System.out.println("object name: " + objectName);
					StringTokenizer objectNameTokens = new StringTokenizer(objectName);
					if ((objectNameTokens.countTokens() == 1) && !objectName.startsWith("/")) {
						parts.add(new DataProviderPart(dataProvider, objectName));
						currentTextPart.setLength(0);
						continue;
					}
					String function = objectNameTokens.nextToken();
					if (function.equals("foreach")) {
						/* TODO: check if there is a next token */
						String collectionName = objectNameTokens.nextToken();
						String itemName = objectNameTokens.nextToken();
						partsStack.push(parts);
						parts = new LoopPart(dataProvider, collectionName, itemName);
					} else if (function.equals("/foreach")) {
						System.out.println("/foreach");
						ContainerPart innerParts = parts;
						parts = partsStack.pop();
						parts.add(innerParts);
					}
				} else {
					currentTextPart.append((char) nextCharacter);
				}
				continue;
			}
			if (gotLeftAngleBracket) {
				if (nextCharacter == '%') {
					inAngleBracket = true;
					parts.add(new TextPart(currentTextPart.toString()));
					currentTextPart.setLength(0);
				} else {
					currentTextPart.append('<').append((char) nextCharacter);
				}
				gotLeftAngleBracket = false;
				continue;
			}
			if (nextCharacter == '<') {
				gotLeftAngleBracket = true;
				continue;
			}
			currentTextPart.append((char) nextCharacter);
		}
		if (currentTextPart.length() > 0) {
			parts.add(new TextPart(currentTextPart.toString()));
		}
		assert (partsStack.isEmpty());
		return parts;
	}

	/**
	 * {@link DataProvider} implementation that always uses the {@link Template}
	 * ’s current {@link Template#dataProvider} to allow rendering a template
	 * multiple times after changing the data provider.
	 *
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	private class DynamicDataProvider implements DataProvider {

		/**
		 * {@inheritDoc}
		 */
		@Override
		@SuppressWarnings("synthetic-access")
		public Object getData(String name) {
			return dataProvider.getData(name);
		}

	}

}
