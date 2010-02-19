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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		for (Part part : extractParts()) {
			part.render(writer);
		}
	}

	//
	// INTERFACE DataProvider
	//

	/**
	 * {@inheritDoc}
	 */
	public String getData(String name) {
		return String.valueOf(templateObjects.get(name));
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
	private List<Part> extractParts() throws IOException {
		BufferedReader bufferedInputReader;
		if (input instanceof BufferedReader) {
			bufferedInputReader = (BufferedReader) input;
		} else {
			bufferedInputReader = new BufferedReader(input);
		}
		List<Part> parts = new ArrayList<Part>();
		StringBuilder currentTextPart = new StringBuilder();
		boolean gotLeftAngleBracket = false;
		boolean inAngleBracket = false;
		while (true) {
			int nextCharacter = bufferedInputReader.read();
			if (nextCharacter == -1) {
				break;
			}
			if (inAngleBracket) {
				if (nextCharacter == '>') {
					inAngleBracket = false;
					parts.add(new ObjectPart(currentTextPart.toString().trim()));
					currentTextPart.setLength(0);
					continue;
				}
			} else {
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
			}
			currentTextPart.append((char) nextCharacter);
		}
		if (currentTextPart.length() > 0) {
			parts.add(new TextPart(currentTextPart.toString()));
		}
		return parts;
	}

	/**
	 * Interface for a part of a template that can be render without further
	 * parsing.
	 *
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	private interface Part {

		/**
		 * Renders this part.
		 *
		 * @param writer
		 *            The writer to render the part to
		 * @throws IOException
		 *             if an I/O error occurs
		 */
		public void render(Writer writer) throws IOException;

	}

	/**
	 * A {@link Part} that contains only text.
	 *
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	private static class TextPart implements Part {

		/** The text of the part. */
		private final String text;

		/**
		 * Creates a new text part.
		 *
		 * @param text
		 *            The text of the part
		 */
		public TextPart(String text) {
			this.text = text;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void render(Writer writer) throws IOException {
			writer.write(text);
		}

	}

	/**
	 * A {@link Part} whose content is dynamically fetched from the
	 * {@link Template}’s {@link DataProvider}.
	 *
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	private class ObjectPart implements Part {

		/** The name of the object to get. */
		private final String name;

		/**
		 * Creates a new object part.
		 *
		 * @param name
		 *            The name of the object
		 */
		public ObjectPart(String name) {
			this.name = name;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		@SuppressWarnings("synthetic-access")
		public void render(Writer writer) throws IOException {
			writer.write(String.valueOf(dataProvider.getData(name)));
		}

	}

}
