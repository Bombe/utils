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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import net.pterodactylus.util.template.ConditionalPart.AndCondition;
import net.pterodactylus.util.template.ConditionalPart.Condition;
import net.pterodactylus.util.template.ConditionalPart.DataCondition;
import net.pterodactylus.util.template.ConditionalPart.NotCondition;
import net.pterodactylus.util.template.ConditionalPart.OrCondition;

/**
 * Simple template system that is geared towards easy of use and high
 * performance.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Template extends DataProvider {

	/** Objects stored in the template. */
	private final Map<String, Object> templateObjects = new HashMap<String, Object>();

	/** The input of the template. */
	private final Reader input;

	/** The parsed template. */
	private Part parsedTemplate;

	/** Filters for the template. */
	private final Map<String, Filter> filters = new HashMap<String, Filter>();

	/**
	 * Creates a new template from the given input.
	 *
	 * @param input
	 *            The template’s input source
	 */
	public Template(Reader input) {
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
	 * Adds a filter with the given name.
	 *
	 * @param name
	 *            The name of the filter
	 * @param filter
	 *            The filter
	 */
	public void addFilter(String name, Filter filter) {
		filters.put(name, filter);
	}

	/**
	 * Parses the input of the template if it wasn’t already parsed.
	 *
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws TemplateException
	 *             if the template can not be parsed
	 */
	public synchronized void parse() throws IOException, TemplateException {
		if (parsedTemplate == null) {
			parsedTemplate = extractParts();
		}
	}

	/**
	 * Renders the template to the given writer.
	 *
	 * @param writer
	 *            The write to render the template to
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws TemplateException
	 *             if the template can not be parsed
	 */
	public synchronized void render(Writer writer) throws IOException, TemplateException {
		parse();
		parsedTemplate.render(this, writer);
	}

	//
	// INTERFACE DataProvider
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object retrieveData(String name) {
		return templateObjects.get(name);
	}

	//
	// PRIVATE METHODS
	//

	/**
	 * Parses the template and creates {@link Part}s of the input.
	 *
	 * @return The list of parts created from the template’s {@link #input}
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws TemplateException
	 *             if the template can not be parsed correctly
	 */
	private Part extractParts() throws IOException, TemplateException {
		BufferedReader bufferedInputReader;
		if (input instanceof BufferedReader) {
			bufferedInputReader = (BufferedReader) input;
		} else {
			bufferedInputReader = new BufferedReader(input);
		}
		Stack<String> commandStack = new Stack<String>();
		Stack<ContainerPart> partsStack = new Stack<ContainerPart>();
		Stack<String> lastCollectionName = new Stack<String>();
		Stack<String> lastLoopName = new Stack<String>();
		Stack<Condition> lastCondition = new Stack<Condition>();
		Stack<List<Condition>> lastConditions = new Stack<List<Condition>>();
		Stack<String> lastIfCommand = new Stack<String>();
		ContainerPart parts = new ContainerPart();
		StringBuilder currentTextPart = new StringBuilder();
		boolean gotLeftAngleBracket = false;
		boolean inAngleBracket = false;
		boolean inSingleQuotes = false;
		boolean inDoubleQuotes = false;
		while (true) {
			int nextCharacter = bufferedInputReader.read();
			if (nextCharacter == -1) {
				break;
			}
			if (inAngleBracket) {
				if (inSingleQuotes) {
					if (nextCharacter == '\'') {
						inSingleQuotes = false;
					}
					currentTextPart.append((char) nextCharacter);
				} else if (inDoubleQuotes) {
					if (nextCharacter == '"') {
						inDoubleQuotes = false;
					}
					currentTextPart.append((char) nextCharacter);
				} else if (nextCharacter == '\'') {
					inSingleQuotes = true;
					currentTextPart.append((char) nextCharacter);
				} else if (nextCharacter == '"') {
					inDoubleQuotes = true;
					currentTextPart.append((char) nextCharacter);
				} else if (nextCharacter == '>') {
					inAngleBracket = false;
					String tagContent = currentTextPart.toString().trim();
					currentTextPart.setLength(0);
					System.out.println("tag content: " + tagContent);
					Iterator<String> tokens = parseTag(tagContent).iterator();
					if (!tokens.hasNext()) {
						throw new TemplateException("empty tag found");
					}
					String function = tokens.next();
					if (function.startsWith("/")) {
						String lastFunction = commandStack.pop();
						if (!("/" + lastFunction).equals(function)) {
							throw new TemplateException("unbalanced template, /" + lastFunction + " expected, " + function + " found");
						}
						if (lastFunction.equals("foreach")) {
							ContainerPart innerParts = parts;
							parts = partsStack.pop();
							lastCollectionName.pop();
							lastLoopName.pop();
							parts.add(innerParts);
						} else if (lastFunction.equals("first") || lastFunction.equals("notfirst") || lastFunction.equals("last") || lastFunction.equals("notlast")) {
							ContainerPart innerParts = parts;
							parts = partsStack.pop();
							parts.add(innerParts);
						} else if (lastFunction.equals("if")) {
							ContainerPart innerParts = parts;
							parts = partsStack.pop();
							lastCondition.pop();
							parts.add(innerParts);
						}
					} else if (function.equals("foreach")) {
						if (!tokens.hasNext()) {
							throw new TemplateException("foreach requires at least one parameter");
						}
						String collectionName = tokens.next();
						String itemName = null;
						if (tokens.hasNext()) {
							itemName = tokens.next();
						}
						String loopName = "loop";
						if (tokens.hasNext()) {
							loopName = tokens.next();
						}
						partsStack.push(parts);
						parts = new LoopPart(collectionName, itemName, loopName);
						commandStack.push("foreach");
						lastCollectionName.push(collectionName);
						lastLoopName.push(loopName);
					} else if (function.equals("foreachelse")) {
						if (!"foreach".equals(commandStack.peek())) {
							throw new TemplateException("foreachelse is only allowed in foreach");
						}
						partsStack.peek().add(parts);
						parts = new EmptyLoopPart(lastCollectionName.peek());
					} else if (function.equals("first")) {
						if (!"foreach".equals(commandStack.peek())) {
							throw new TemplateException("first is only allowed in foreach");
						}
						partsStack.push(parts);
						final String loopName = lastLoopName.peek();
						parts = new ConditionalPart(new ConditionalPart.Condition() {

							@Override
							public boolean isAllowed(DataProvider dataProvider) throws TemplateException {
								return (Boolean) (dataProvider.getData(loopName + ".first"));
							}
						});
						commandStack.push("first");
					} else if (function.equals("notfirst")) {
						if (!"foreach".equals(commandStack.peek())) {
							throw new TemplateException("notfirst is only allowed in foreach");
						}
						partsStack.push(parts);
						final String loopName = lastLoopName.peek();
						parts = new ConditionalPart(new ConditionalPart.Condition() {

							@Override
							public boolean isAllowed(DataProvider dataProvider) throws TemplateException {
								return !(Boolean) (dataProvider.getData(loopName + ".first"));
							}
						});
						commandStack.push("notfirst");
					} else if (function.equals("last")) {
						if (!"foreach".equals(commandStack.peek())) {
							throw new TemplateException("last is only allowed in foreach");
						}
						partsStack.push(parts);
						final String loopName = lastLoopName.peek();
						parts = new ConditionalPart(new ConditionalPart.Condition() {

							@Override
							public boolean isAllowed(DataProvider dataProvider) throws TemplateException {
								return (Boolean) (dataProvider.getData(loopName + ".last"));
							}
						});
						commandStack.push("last");
					} else if (function.equals("notlast")) {
						if (!"foreach".equals(commandStack.peek())) {
							throw new TemplateException("notlast is only allowed in foreach");
						}
						partsStack.push(parts);
						final String loopName = lastLoopName.peek();
						parts = new ConditionalPart(new ConditionalPart.Condition() {

							@Override
							public boolean isAllowed(DataProvider dataProvider) throws TemplateException {
								return !(Boolean) (dataProvider.getData(loopName + ".last"));
							}
						});
						commandStack.push("notlast");
					} else if (function.equals("if")) {
						if (!tokens.hasNext()) {
							throw new TemplateException("if requires one or two parameters");
						}
						String itemName = tokens.next();
						boolean invert = false;
						if (itemName.equals("!")) {
							invert = true;
							if (!tokens.hasNext()) {
								throw new TemplateException("if ! requires one parameter");
							}
							itemName = tokens.next();
						} else {
							if (itemName.startsWith("!")) {
								invert = true;
								itemName = itemName.substring(1);
							}
						}
						partsStack.push(parts);
						Condition condition = new DataCondition(itemName, invert);
						parts = new ConditionalPart(condition);
						commandStack.push("if");
						lastCondition.push(condition);
						lastConditions.push(new ArrayList<Condition>(Arrays.asList(condition)));
						lastIfCommand.push("if");
					} else if (function.equals("else")) {
						if (!"if".equals(commandStack.peek())) {
							throw new TemplateException("else is only allowed in if");
						}
						if (!"if".equals(lastIfCommand.peek()) && !"elseif".equals(lastIfCommand.peek())) {
							throw new TemplateException("else may only follow if or elseif");
						}
						partsStack.peek().add(parts);
						Condition condition = new NotCondition(new OrCondition(lastConditions.pop()));
						parts = new ConditionalPart(condition);
						lastIfCommand.pop();
						lastIfCommand.push("else");
					} else if (function.equals("elseif")) {
						if (!"if".equals(commandStack.peek())) {
							throw new TemplateException("elseif is only allowed in if");
						}
						if (!"if".equals(lastIfCommand.peek()) && !"elseif".equals(lastIfCommand.peek())) {
							throw new TemplateException("elseif is only allowed after if or elseif");
						}
						if (!tokens.hasNext()) {
							throw new TemplateException("elseif requires one or two parameters");
						}
						String itemName = tokens.next();
						boolean invert = false;
						if (itemName.equals("!")) {
							invert = true;
							if (!tokens.hasNext()) {
								throw new TemplateException("if ! requires one parameter");
							}
							itemName = tokens.next();
						} else {
							if (itemName.startsWith("!")) {
								invert = true;
								itemName = itemName.substring(1);
							}
						}
						partsStack.peek().add(parts);
						Condition condition = new AndCondition(new NotCondition(lastCondition.pop()), new DataCondition(itemName, invert));
						parts = new ConditionalPart(condition);
						lastCondition.push(condition);
						lastConditions.peek().add(condition);
						lastIfCommand.pop();
						lastIfCommand.push("elseif");
					} else {
						boolean directText = false;
						String itemName = function;
						if (function.equals("=")) {
							if (!tokens.hasNext()) {
								throw new TemplateException("empty tag found");
							}
							itemName = tokens.next();
							directText = true;
						} else if (function.startsWith("=")) {
							itemName = function.substring(1);
							directText = true;
						}
						Map<Filter, Map<String, String>> allFilterParameters = new HashMap<Filter, Map<String, String>>();
						if (tokens.hasNext() && (tokens.next() != null)) {
							throw new TemplateException("expected \"|\" token");
						}
						while (tokens.hasNext()) {
							String filterName = tokens.next();
							Filter filter = filters.get(filterName);
							if (filter == null) {
								throw new TemplateException("unknown filter: " + filterName);
							}
							System.out.println("added filter: " + filterName);
							Map<String, String> filterParameters = new HashMap<String, String>();
							while (tokens.hasNext()) {
								String parameterToken = tokens.next();
								if (parameterToken == null) {
									break;
								}
								int equals = parameterToken.indexOf('=');
								if (equals == -1) {
									throw new TemplateException("found parameter without \"=\" sign");
								}
								String key = parameterToken.substring(0, equals).trim();
								String value = parameterToken.substring(equals + 1);
								filterParameters.put(key, value);
							}
							System.out.println("parameters: " + filterParameters);
							allFilterParameters.put(filter, filterParameters);
						}
						System.out.println("itemName: " + itemName);
						if (directText) {
							parts.add(new FilteredTextPart(itemName, allFilterParameters.keySet(), allFilterParameters));
						} else {
							parts.add(new FilteredPart(itemName, allFilterParameters.keySet(), allFilterParameters));
						}
					}
				} else {
					currentTextPart.append((char) nextCharacter);
				}
				continue;
			}
			if (gotLeftAngleBracket) {
				if (nextCharacter == '%') {
					inAngleBracket = true;
					if (currentTextPart.length() > 0) {
						parts.add(new TextPart(currentTextPart.toString()));
						currentTextPart.setLength(0);
					}
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
		if (!partsStack.isEmpty()) {
			throw new TemplateException("Unbalanced template.");
		}
		return parts;
	}

	/**
	 * Parses the content of a tag into words, obeying syntactical rules about
	 * separators and quotes. Separators are parsed as {@code null}.
	 *
	 * @param tagContent
	 *            The content of the tag to parse
	 * @return The parsed words
	 */
	static List<String> parseTag(String tagContent) {
		List<String> expressions = new ArrayList<String>();
		boolean inSingleQuotes = false;
		boolean inDoubleQuotes = false;
		boolean inBackslash = false;
		StringBuilder currentExpression = new StringBuilder();
		for (char c : tagContent.toCharArray()) {
			if (inSingleQuotes) {
				if (c == '\'') {
					inSingleQuotes = false;
				} else {
					currentExpression.append(c);
				}
			} else if (inBackslash) {
				currentExpression.append(c);
				inBackslash = false;
			} else if (inDoubleQuotes) {
				if (c == '"') {
					inDoubleQuotes = false;
				} else {
					currentExpression.append(c);
				}
			} else {
				if (c == '\'') {
					inSingleQuotes = true;
				} else if (c == '"') {
					inDoubleQuotes = true;
				} else if (c == '\\') {
					inBackslash = true;
				} else if (c == '|') {
					if (currentExpression.toString().trim().length() > 0) {
						expressions.add(currentExpression.toString());
						currentExpression.setLength(0);
					}
					expressions.add(null);
				} else {
					if (c == ' ') {
						if (currentExpression.length() > 0) {
							expressions.add(currentExpression.toString());
							currentExpression.setLength(0);
						}
					} else {
						currentExpression.append(c);
					}
				}
			}
		}
		if (currentExpression.length() > 0) {
			expressions.add(currentExpression.toString());
		}
		return expressions;
	}

}
