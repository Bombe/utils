/*
 * utils - TemplateTest.java - Copyright © 2010 David Roden
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

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

import junit.framework.TestCase;

/*
 * stew - TemplateTest.java - Copyright © 2010 David Roden
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

/**
 * Test cases for {@link Template}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class TemplateTest extends TestCase {

	/**
	 * Test cases for template strings that contain no parameters.
	 *
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws TemplateException
	 */
	public void testStringTemplatesWithoutParameters() throws IOException, TemplateException {
		Template template;
		TemplateContext templateContext;
		String templateString;
		StringWriter outputWriter;
		String output;

		templateString = "This is a template without parameters.";
		outputWriter = new StringWriter();
		template = TemplateParser.parse(new StringReader(templateString));
		templateContext = new TemplateContext();
		template.render(templateContext, outputWriter);
		output = outputWriter.toString();
		assertEquals(templateString, output);

		templateString = "This is a template without parameters but with a CR character in it.\r";
		outputWriter = new StringWriter();
		template = TemplateParser.parse(new StringReader(templateString));
		templateContext = new TemplateContext();
		template.render(templateContext, outputWriter);
		output = outputWriter.toString();
		assertEquals(templateString, output);

		templateString = "This is a template without parameters but with an LF character in it.\n";
		outputWriter = new StringWriter();
		template = TemplateParser.parse(new StringReader(templateString));
		templateContext = new TemplateContext();
		template.render(templateContext, outputWriter);
		output = outputWriter.toString();
		assertEquals(templateString, output);

		templateString = "This is a template without parameters but with a CR/LF character combination in it.\r\n";
		outputWriter = new StringWriter();
		template = TemplateParser.parse(new StringReader(templateString));
		templateContext = new TemplateContext();
		template.render(templateContext, outputWriter);
		output = outputWriter.toString();
		assertEquals(templateString, output);
	}

	/**
	 * Test case for template strings that contain parameters.
	 *
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws TemplateException
	 */
	public void testStringTemplatesWithParameters() throws IOException, TemplateException {
		Template template;
		TemplateContext templateContext;
		String templateString;
		StringWriter outputWriter;
		String output;

		templateString = "This is a template with <%one> parameter.";
		outputWriter = new StringWriter();
		template = TemplateParser.parse(new StringReader(templateString));
		templateContext = new TemplateContext();
		templateContext.set("one", "two");
		template.render(templateContext, outputWriter);
		output = outputWriter.toString();
		assertEquals("This is a template with two parameter.", output);

		templateString = "This is a template with <% one > parameter.";
		outputWriter = new StringWriter();
		template = TemplateParser.parse(new StringReader(templateString));
		templateContext = new TemplateContext();
		templateContext.set("one", "two");
		template.render(templateContext, outputWriter);
		output = outputWriter.toString();
		assertEquals("This is a template with two parameter.", output);

		templateString = "This is a template with <%one> parameter, one <% left > and one <% right>.";
		outputWriter = new StringWriter();
		template = TemplateParser.parse(new StringReader(templateString));
		templateContext = new TemplateContext();
		templateContext.set("one", "two");
		templateContext.set("left", "on top");
		templateContext.set("right", "below");
		template.render(templateContext, outputWriter);
		output = outputWriter.toString();
		assertEquals("This is a template with two parameter, one on top and one below.", output);
	}

	/**
	 * Tests for string templates that loop over collections.
	 *
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws TemplateException
	 */
	@SuppressWarnings("synthetic-access")
	public void testStringTemplatesWithCollection() throws IOException, TemplateException {
		Template template;
		TemplateContext templateContext;
		String templateString;
		StringWriter outputWriter;
		String output;
		Collection<Object> collection;
		Collection<Object> innerCollection;

		templateString = "This template repeats: <% foreach items item>item: <% item> - <% /foreach>";
		outputWriter = new StringWriter();
		template = TemplateParser.parse(new StringReader(templateString));
		templateContext = new TemplateContext();
		collection = new ArrayList<Object>();
		collection.add("first");
		collection.add("second");
		templateContext.set("items", collection);
		template.render(templateContext, outputWriter);
		output = outputWriter.toString();
		assertEquals("This template repeats: item: first - item: second - ", output);

		templateString = "This template repeats: <% foreach items item>item: <% item> - <% /foreach>";
		outputWriter = new StringWriter();
		template = TemplateParser.parse(new StringReader(templateString));
		templateContext = new TemplateContext();
		templateContext.set("items", "first");
		template.render(templateContext, outputWriter);
		output = outputWriter.toString();
		assertEquals("This template repeats: item: first - ", output);

		templateString = "This template repeats: <% foreach items item>item: <% item> (<%foreach inners inner>[<%item>: <%inner>]<%/foreach>) - <% /foreach>";
		outputWriter = new StringWriter();
		template = TemplateParser.parse(new StringReader(templateString));
		templateContext = new TemplateContext();
		collection = new ArrayList<Object>();
		collection.add("first");
		collection.add("second");
		templateContext.set("items", collection);
		innerCollection = new ArrayList<Object>();
		innerCollection.add("1");
		innerCollection.add("2");
		innerCollection.add("3");
		templateContext.set("inners", innerCollection);
		template.render(templateContext, outputWriter);
		output = outputWriter.toString();
		assertEquals("This template repeats: item: first ([first: 1][first: 2][first: 3]) - item: second ([second: 1][second: 2][second: 3]) - ", output);

		templateString = "This template repeats: <% foreach items item><%foreach item inner><%inner> <%/foreach><% /foreach>";
		outputWriter = new StringWriter();
		template = TemplateParser.parse(new StringReader(templateString));
		templateContext = new TemplateContext();
		collection = new ArrayList<Object>();
		innerCollection = new ArrayList<Object>();
		innerCollection.add("1");
		innerCollection.add("2");
		innerCollection.add("3");
		collection.add(innerCollection);
		innerCollection = new ArrayList<Object>();
		innerCollection.add("4");
		innerCollection.add("5");
		innerCollection.add("6");
		collection.add(innerCollection);
		templateContext.set("items", collection);
		template.render(templateContext, outputWriter);
		output = outputWriter.toString();
		assertEquals("This template repeats: 1 2 3 4 5 6 ", output);

		templateString = "Items: <%foreach items item><%item.name><%notlast>, <%/notlast><%last>.<%/last><%/foreach>";
		outputWriter = new StringWriter();
		template = TemplateParser.parse(new StringReader(templateString));
		templateContext = new TemplateContext();
		List<Item> items = new ArrayList<Item>();
		items.add(new Item("first"));
		items.add(new Item("second"));
		items.add(new Item("last"));
		templateContext.addAccessor(Item.class, new ItemAccessor());
		templateContext.set("items", items);
		template.render(templateContext, outputWriter);
		output = outputWriter.toString();
		assertEquals("Items: first, second, last.", output);
	}

	/**
	 * Tests filtering a collection before the {@code %foreach} loop.
	 */
	public void testTemplateWithFilteredCollection() {
		Template template;
		TemplateContext templateContext;
		StringWriter outputWriter;
		Collection<? extends Object> collection;
		Map<String, Integer> map;

		template = TemplateParser.parse(new StringReader("<%foreach list item|sort><% item><%notlast>, <%/notlast><%/foreach>"));
		outputWriter = new StringWriter();
		templateContext = new TemplateContext();
		templateContext.addFilter("sort", new CollectionSortFilter());
		collection = Arrays.asList(4, 7, 1);
		templateContext.set("list", collection);
		template.render(templateContext, outputWriter);
		assertEquals("1, 4, 7", outputWriter.toString());

		template = TemplateParser.parse(new StringReader("<%foreach map item|sort><% item.key>: <%item.value><%notlast>, <%/notlast><%/foreach>"));
		outputWriter = new StringWriter();
		templateContext = new TemplateContext();
		templateContext.addFilter("sort", new CollectionSortFilter());
		templateContext.addAccessor(Entry.class, new ReflectionAccessor());
		map = new HashMap<String, Integer>();
		map.put("one", 1);
		map.put("two", 2);
		map.put("three", 3);
		map.put("four", 4);
		templateContext.set("map", map);
		template.render(templateContext, outputWriter);
		assertEquals("four: 4, one: 1, three: 3, two: 2", outputWriter.toString());
	}

	/**
	 * Tests iteration over a {@link Map}’s {@link Map#entrySet() entries} and
	 * access to both {@link Entry#getKey() key} and {@link Entry#getValue()}.
	 */
	public void testTemplateWithMap() {
		Template template;
		TemplateContext templateContext;
		String templateString;
		StringWriter outputWriter;
		String output;
		Map<String, String> map;

		templateString = "<%foreach map entry><%entry.key>=<%entry.value><%if !loop.last>, <%/if><%/foreach>";
		outputWriter = new StringWriter();
		template = TemplateParser.parse(new StringReader(templateString));
		templateContext = new TemplateContext();
		map = new LinkedHashMap<String, String>();
		map.put("a", "1");
		map.put("b", "2");
		templateContext.set("map", map);
		templateContext.addAccessor(Object.class, new ReflectionAccessor());
		template.render(templateContext, outputWriter);
		output = outputWriter.toString();
		assertEquals("a=1, b=2", output);
	}

	/**
	 * Tests for string templates that loop over collections.
	 *
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws TemplateException
	 */
	public void testStringTemplatesWithCollectionAndLoopStructure() throws IOException, TemplateException {
		Template template;
		TemplateContext templateContext;
		String templateString;
		StringWriter outputWriter;
		String output;
		Collection<Object> collection;
		Collection<Object> innerCollection;

		templateString = "This template repeats: <% foreach items item>item: <% loop.count> - <% /foreach>";
		outputWriter = new StringWriter();
		template = TemplateParser.parse(new StringReader(templateString));
		templateContext = new TemplateContext();
		collection = new ArrayList<Object>();
		collection.add("first");
		collection.add("second");
		templateContext.set("items", collection);
		template.render(templateContext, outputWriter);
		output = outputWriter.toString();
		assertEquals("This template repeats: item: 0 - item: 1 - ", output);

		templateString = "This template repeats: <% foreach items item itemLoop>item: <% itemLoop.count> - <% /foreach>";
		outputWriter = new StringWriter();
		template = TemplateParser.parse(new StringReader(templateString));
		templateContext = new TemplateContext();
		collection = new ArrayList<Object>();
		collection.add("first");
		collection.add("second");
		templateContext.set("items", collection);
		template.render(templateContext, outputWriter);
		output = outputWriter.toString();
		assertEquals("This template repeats: item: 0 - item: 1 - ", output);

		templateString = "This template repeats: <% foreach items item><% loop.count> <%foreach item inner><% loop.count> <%inner> <%/foreach><% /foreach>";
		outputWriter = new StringWriter();
		template = TemplateParser.parse(new StringReader(templateString));
		templateContext = new TemplateContext();
		collection = new ArrayList<Object>();
		innerCollection = new ArrayList<Object>();
		innerCollection.add("1");
		innerCollection.add("2");
		innerCollection.add("3");
		collection.add(innerCollection);
		innerCollection = new ArrayList<Object>();
		innerCollection.add("4");
		innerCollection.add("5");
		innerCollection.add("6");
		collection.add(innerCollection);
		templateContext.set("items", collection);
		template.render(templateContext, outputWriter);
		output = outputWriter.toString();
		assertEquals("This template repeats: 0 0 1 1 2 2 3 1 0 4 1 5 2 6 ", output);
	}

	/**
	 * Tests for string templates that loop over collections.
	 *
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws TemplateException
	 */
	public void testStringTemplatesWithEmptyCollections() throws IOException, TemplateException {
		Template template;
		TemplateContext templateContext;
		String templateString;
		StringWriter outputWriter;
		String output;
		Collection<Object> collection;
		Collection<Object> innerCollection;

		templateString = "This template repeats: <% foreach items item>item: <% loop.count> - <%foreachelse>nothing!<% /foreach>";
		outputWriter = new StringWriter();
		template = TemplateParser.parse(new StringReader(templateString));
		templateContext = new TemplateContext();
		collection = new ArrayList<Object>();
		templateContext.set("items", collection);
		template.render(templateContext, outputWriter);
		output = outputWriter.toString();
		assertEquals("This template repeats: nothing!", output);
	}

	/**
	 * Tests for string templates that loop over collections.
	 *
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws TemplateException
	 */
	public void testStringTemplatesWithCollectionsFirst() throws IOException, TemplateException {
		Template template;
		TemplateContext templateContext;
		String templateString;
		StringWriter outputWriter;
		String output;
		Collection<Object> collection;
		Collection<Object> innerCollection;

		templateString = "This template repeats: <% foreach items item><%first>first <%/first>item: <% loop.count> - <%foreachelse>nothing!<% /foreach>";
		outputWriter = new StringWriter();
		template = TemplateParser.parse(new StringReader(templateString));
		templateContext = new TemplateContext();
		collection = new ArrayList<Object>();
		collection.add(1);
		collection.add(2);
		templateContext.set("items", collection);
		template.render(templateContext, outputWriter);
		output = outputWriter.toString();
		assertEquals("This template repeats: first item: 0 - item: 1 - ", output);
	}

	/**
	 * Tests for string templates that loop over collections.
	 *
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws TemplateException
	 */
	public void testStringTemplatesWithCollectionsLast() throws IOException, TemplateException {
		Template template;
		TemplateContext templateContext;
		String templateString;
		StringWriter outputWriter;
		String output;
		Collection<Object> collection;
		Collection<Object> innerCollection;

		templateString = "This template repeats: <% foreach items item><%first>first <%/first>item: <% loop.count> - <%last>end<%/last><%foreachelse>nothing!<% /foreach>";
		outputWriter = new StringWriter();
		template = TemplateParser.parse(new StringReader(templateString));
		templateContext = new TemplateContext();
		collection = new ArrayList<Object>();
		collection.add(1);
		collection.add(2);
		templateContext.set("items", collection);
		template.render(templateContext, outputWriter);
		output = outputWriter.toString();
		assertEquals("This template repeats: first item: 0 - item: 1 - end", output);
	}

	/**
	 * Tests for string templates that uses a custom accessor.
	 *
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws TemplateException
	 */
	public void testStringTemplatesWithAccessors() throws IOException, TemplateException {
		Template template;
		TemplateContext templateContext;
		String templateString;
		StringWriter outputWriter;
		String output;
		Collection<Object> collection;
		Accessor callableAccessor = new Accessor() {

			/**
			 * {@inheritDoc}
			 */
			@Override
			@SuppressWarnings("unchecked")
			public Object get(TemplateContext templateContext, Object object, String member) {
				try {
					return ((Callable<String>) object).call();
				} catch (Exception e1) {
					/* ignore. */
				}
				return null;
			}
		};

		templateString = "List: <%foreach items item><%item.call> <%/foreach>";
		outputWriter = new StringWriter();
		template = TemplateParser.parse(new StringReader(templateString));
		templateContext = new TemplateContext();
		templateContext.addAccessor(Callable.class, callableAccessor);
		collection = new ArrayList<Object>();
		collection.add(new Callable<String>() {

			@Override
			public String call() {
				return "Yay!";
			}
		});
		collection.add(new Callable<String>() {

			@Override
			public String call() {
				return "Hooray!";
			}
		});
		templateContext.set("items", collection);
		template.render(templateContext, outputWriter);
		output = outputWriter.toString();
		assertEquals("List: Yay! Hooray! ", output);
	}

	/**
	 * Tests for string templates that contain a collection and the <%notfirst>
	 * directive.
	 *
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws TemplateException
	 */
	public void testStringTemplatesWithCollectionNotFirst() throws IOException, TemplateException {
		Template template;
		TemplateContext templateContext;
		String templateString;
		StringWriter outputWriter;
		String output;
		Collection<Object> collection;

		templateString = "List: <%foreach items item><%first><%item><%/first><%notfirst>, <%item><%/notfirst><%/foreach>.";
		outputWriter = new StringWriter();
		template = TemplateParser.parse(new StringReader(templateString));
		templateContext = new TemplateContext();
		collection = new ArrayList<Object>();
		collection.add(1);
		collection.add(2);
		collection.add(3);
		collection.add(4);
		templateContext.set("items", collection);
		template.render(templateContext, outputWriter);
		output = outputWriter.toString();
		assertEquals("List: 1, 2, 3, 4.", output);
	}

	/**
	 * Tests for string templates that contain a collection and the <%notlast>
	 * directive.
	 *
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws TemplateException
	 */
	public void testStringTemplatesWithCollectionNotLast() throws IOException, TemplateException {
		Template template;
		TemplateContext templateContext;
		String templateString;
		StringWriter outputWriter;
		String output;
		Collection<Object> collection;

		templateString = "List: <%foreach items item><%item><%notlast>, <%/notlast><%last>.<%/last><%/foreach>";
		outputWriter = new StringWriter();
		template = TemplateParser.parse(new StringReader(templateString));
		templateContext = new TemplateContext();
		collection = new ArrayList<Object>();
		collection.add(1);
		collection.add(2);
		collection.add(3);
		collection.add(4);
		templateContext.set("items", collection);
		template.render(templateContext, outputWriter);
		output = outputWriter.toString();
		assertEquals("List: 1, 2, 3, 4.", output);
	}

	/**
	 * Tests for string templates that contain a collection and the <%odd>
	 * directive.
	 */
	public void testStringTemplatesWithCollectionOdd() {
		Template template;
		TemplateContext templateContext;
		String templateString;
		StringWriter outputWriter;
		String output;
		Collection<Object> collection;

		templateString = "List: <%foreach items item><%odd><% item> (odd) <%/odd><%/foreach>";
		outputWriter = new StringWriter();
		template = TemplateParser.parse(new StringReader(templateString));
		templateContext = new TemplateContext();
		collection = new ArrayList<Object>();
		collection.add(1);
		collection.add(2);
		collection.add(3);
		collection.add(4);
		templateContext.set("items", collection);
		template.render(templateContext, outputWriter);
		output = outputWriter.toString();
		assertEquals("List: 2 (odd) 4 (odd) ", output);
	}

	/**
	 * Tests for string templates that contain a collection and the <%even>
	 * directive.
	 */
	public void testStringTemplatesWithCollectionEven() {
		Template template;
		TemplateContext templateContext;
		String templateString;
		StringWriter outputWriter;
		String output;
		Collection<Object> collection;

		templateString = "List: <%foreach items item><%even><% item> (even) <%/even><%/foreach>";
		outputWriter = new StringWriter();
		template = TemplateParser.parse(new StringReader(templateString));
		templateContext = new TemplateContext();
		collection = new ArrayList<Object>();
		collection.add(1);
		collection.add(2);
		collection.add(3);
		collection.add(4);
		templateContext.set("items", collection);
		template.render(templateContext, outputWriter);
		output = outputWriter.toString();
		assertEquals("List: 1 (even) 3 (even) ", output);
	}

	/**
	 * Tests for string templates that contain <%if>.
	 *
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws TemplateException
	 */
	public void testStringTemplatesWithIf() throws IOException, TemplateException {
		Template template;
		TemplateContext templateContext;
		String templateString;
		StringWriter outputWriter;
		String output;

		templateString = "A: <%if a>true<%/if>";
		outputWriter = new StringWriter();
		template = TemplateParser.parse(new StringReader(templateString));
		templateContext = new TemplateContext();
		templateContext.set("a", true);
		template.render(templateContext, outputWriter);
		output = outputWriter.toString();
		assertEquals("A: true", output);

		templateString = "A: <%if ! a>true<%/if>";
		outputWriter = new StringWriter();
		template = TemplateParser.parse(new StringReader(templateString));
		templateContext = new TemplateContext();
		templateContext.set("a", true);
		template.render(templateContext, outputWriter);
		output = outputWriter.toString();
		assertEquals("A: ", output);

		templateString = "A: <%if a>true<%/if>";
		outputWriter = new StringWriter();
		template = TemplateParser.parse(new StringReader(templateString));
		templateContext = new TemplateContext();
		templateContext.set("a", false);
		template.render(templateContext, outputWriter);
		output = outputWriter.toString();
		assertEquals("A: ", output);

		templateString = "A: <%if !a>true<%/if>";
		outputWriter = new StringWriter();
		template = TemplateParser.parse(new StringReader(templateString));
		templateContext = new TemplateContext();
		templateContext.set("a", false);
		template.render(templateContext, outputWriter);
		output = outputWriter.toString();
		assertEquals("A: true", output);

		templateString = "A: <%if a>true<%if b>true<%/if><%/if>";
		outputWriter = new StringWriter();
		template = TemplateParser.parse(new StringReader(templateString));
		templateContext = new TemplateContext();
		templateContext.set("a", true);
		templateContext.set("b", true);
		template.render(templateContext, outputWriter);
		output = outputWriter.toString();
		assertEquals("A: truetrue", output);

		templateString = "A: <%if a>true<%if b>true<%/if><%/if>";
		outputWriter = new StringWriter();
		template = TemplateParser.parse(new StringReader(templateString));
		templateContext = new TemplateContext();
		templateContext.set("a", false);
		templateContext.set("b", true);
		template.render(templateContext, outputWriter);
		output = outputWriter.toString();
		assertEquals("A: ", output);

		templateString = "A: <%if a>true<%else>false<%/if>";
		outputWriter = new StringWriter();
		template = TemplateParser.parse(new StringReader(templateString));
		templateContext = new TemplateContext();
		templateContext.set("a", true);
		template.render(templateContext, outputWriter);
		output = outputWriter.toString();
		assertEquals("A: true", output);

		templateString = "A: <%if !a>true<%else>false<%/if>";
		outputWriter = new StringWriter();
		template = TemplateParser.parse(new StringReader(templateString));
		templateContext = new TemplateContext();
		templateContext.set("a", true);
		template.render(templateContext, outputWriter);
		output = outputWriter.toString();
		assertEquals("A: false", output);

		templateString = "A: <%if !a>true<%else>false<%/if>";
		outputWriter = new StringWriter();
		template = TemplateParser.parse(new StringReader(templateString));
		templateContext = new TemplateContext();
		templateContext.set("a", false);
		template.render(templateContext, outputWriter);
		output = outputWriter.toString();
		assertEquals("A: true", output);

		templateString = "A: <%if a>true<%else>false<%/if>";
		outputWriter = new StringWriter();
		template = TemplateParser.parse(new StringReader(templateString));
		templateContext = new TemplateContext();
		templateContext.set("a", false);
		template.render(templateContext, outputWriter);
		output = outputWriter.toString();
		assertEquals("A: false", output);

		templateString = "A: <%if a>(a)<%elseif b>(b)<%else>false<%/if>";
		outputWriter = new StringWriter();
		template = TemplateParser.parse(new StringReader(templateString));
		templateContext = new TemplateContext();
		templateContext.set("a", false);
		templateContext.set("b", true);
		template.render(templateContext, outputWriter);
		output = outputWriter.toString();
		assertEquals("A: (b)", output);

		templateString = "A: <%if a>(a)<%elseif b>(b)<%elseif c>(c)<%else>false<%/if>";
		outputWriter = new StringWriter();
		template = TemplateParser.parse(new StringReader(templateString));
		templateContext = new TemplateContext();
		templateContext.set("a", false);
		templateContext.set("b", true);
		templateContext.set("c", true);
		template.render(templateContext, outputWriter);
		output = outputWriter.toString();
		assertEquals("A: (b)", output);

		templateString = "A: <%if a>(a)<%elseif !b>(b)<%elseif !c>(c)<%else>false<%/if>";
		outputWriter = new StringWriter();
		template = TemplateParser.parse(new StringReader(templateString));
		templateContext = new TemplateContext();
		templateContext.set("a", false);
		templateContext.set("b", true);
		templateContext.set("c", true);
		template.render(templateContext, outputWriter);
		output = outputWriter.toString();
		assertEquals("A: false", output);

		templateString = "<%if a.b>a.b<%/if>";
		outputWriter = new StringWriter();
		template = TemplateParser.parse(new StringReader(templateString));
		templateContext = new TemplateContext();
		Map<String, String> map = new HashMap<String, String>();
		map.put("b", "true");
		templateContext.set("a", map);
		template.render(templateContext, outputWriter);
		output = outputWriter.toString();
		assertEquals("a.b", output);

		templateString = "<%if !a.b>a.b<%/if>";
		outputWriter = new StringWriter();
		template = TemplateParser.parse(new StringReader(templateString));
		templateContext = new TemplateContext();
		map = new HashMap<String, String>();
		map.put("b", "true");
		templateContext.set("a", map);
		template.render(templateContext, outputWriter);
		output = outputWriter.toString();
		assertEquals("", output);

		templateString = "<%if !a.b>a.b<%/if>";
		outputWriter = new StringWriter();
		template = TemplateParser.parse(new StringReader(templateString));
		templateContext = new TemplateContext();
		map = new HashMap<String, String>();
		map.put("b", "false");
		templateContext.set("a", map);
		template.render(templateContext, outputWriter);
		output = outputWriter.toString();
		assertEquals("a.b", output);

		templateString = "<%if a.c>a.b<%/if>";
		outputWriter = new StringWriter();
		template = TemplateParser.parse(new StringReader(templateString));
		templateContext = new TemplateContext();
		map = new HashMap<String, String>();
		map.put("b", "true");
		templateContext.set("a", map);
		template.render(templateContext, outputWriter);
		output = outputWriter.toString();
		assertEquals("", output);

		templateString = "<%if a.c>a.b<%/if>";
		outputWriter = new StringWriter();
		template = TemplateParser.parse(new StringReader(templateString));
		templateContext = new TemplateContext();
		template.render(templateContext, outputWriter);
		output = outputWriter.toString();
		assertEquals("", output);

		templateString = "<%ifnull a.c>a.b<%/if>";
		outputWriter = new StringWriter();
		template = TemplateParser.parse(new StringReader(templateString));
		templateContext = new TemplateContext();
		template.render(templateContext, outputWriter);
		output = outputWriter.toString();
		assertEquals("a.b", output);

		templateString = "<%if a><%if b><%if c>abc<%else>ab<%/if><%else><%if c>ac<%else>a<%/if><%/if><%else><%if b><%if c>bc<%else>b<%/if><%else><%if c>c<%else><%/if><%/if><%/if>";
		template = TemplateParser.parse(new StringReader(templateString));
		templateContext = new TemplateContext();
		template.render(templateContext, outputWriter);
		outputWriter = new StringWriter();
		output = outputWriter.toString();
		assertEquals("", output);

		templateContext.set("a", true);
		outputWriter = new StringWriter();
		template.render(templateContext, outputWriter);
		assertEquals("a", outputWriter.toString());

		templateContext.set("b", true);
		outputWriter = new StringWriter();
		template.render(templateContext, outputWriter);
		assertEquals("ab", outputWriter.toString());

		templateContext.set("c", true);
		outputWriter = new StringWriter();
		template.render(templateContext, outputWriter);
		assertEquals("abc", outputWriter.toString());

		templateContext.set("b", false);
		outputWriter = new StringWriter();
		template.render(templateContext, outputWriter);
		assertEquals("ac", outputWriter.toString());

		templateString = "A: <%if a>(a)<%else>wrong<%else>false<%/if>";
		outputWriter = new StringWriter();
		try {
			template = TemplateParser.parse(new StringReader(templateString));
			templateContext = new TemplateContext();
			template.render(templateContext, outputWriter);
			fail();
		} catch (TemplateException te1) {
			/* ignore. */
		}

		templateString = "A: <%if a>(a)<%elseif b>(b)<%else>wrong<%elseif c>(c)<%else>false<%/if>";
		outputWriter = new StringWriter();
		try {
			template = TemplateParser.parse(new StringReader(templateString));
			templateContext = new TemplateContext();
			template.render(templateContext, outputWriter);
			fail();
		} catch (TemplateException te1) {
			/* ignore. */
		}
	}

	/**
	 * Tests for string templates that uses filters.
	 *
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws TemplateException
	 */
	@SuppressWarnings("synthetic-access")
	public void testStringTemplatesWithFilter() throws IOException, TemplateException {
		Template template;
		Template innerTemplate;
		TemplateContext templateContext;
		String templateString;
		StringWriter outputWriter;
		String output;

		templateString = "<%a> (<%a|test>)";
		outputWriter = new StringWriter();
		template = TemplateParser.parse(new StringReader(templateString));
		templateContext = new TemplateContext();
		templateContext.addFilter("test", new TestFilter());
		templateContext.set("a", 4);
		template.render(templateContext, outputWriter);
		output = outputWriter.toString();
		assertEquals("4 ([java.lang.Integer@4])", output);

		templateString = "<%a> (<%  a | test  >)";
		outputWriter = new StringWriter();
		template = TemplateParser.parse(new StringReader(templateString));
		templateContext = new TemplateContext();
		templateContext.addFilter("test", new TestFilter());
		templateContext.set("a", 4);
		template.render(templateContext, outputWriter);
		output = outputWriter.toString();
		assertEquals("4 ([java.lang.Integer@4])", output);

		templateString = "<%= foo |test>";
		outputWriter = new StringWriter();
		template = TemplateParser.parse(new StringReader(templateString));
		templateContext = new TemplateContext();
		templateContext.addFilter("test", new TestFilter());
		template.render(templateContext, outputWriter);
		output = outputWriter.toString();
		assertEquals("[java.lang.String@101574]", output);

		templateString = "<%= foo |replace needle==foo replacement==bar>";
		outputWriter = new StringWriter();
		template = TemplateParser.parse(new StringReader(templateString));
		templateContext = new TemplateContext();
		templateContext.addFilter("replace", new ReplaceFilter());
		templateContext.set("foo", "baz");
		template.render(templateContext, outputWriter);
		output = outputWriter.toString();
		assertEquals("bar", output);

		templateString = "<%= foo |replace needle==foo replacement=='<bar>'>";
		outputWriter = new StringWriter();
		template = TemplateParser.parse(new StringReader(templateString));
		templateContext = new TemplateContext();
		templateContext.addFilter("replace", new ReplaceFilter());
		template.render(templateContext, outputWriter);
		output = outputWriter.toString();
		assertEquals("<bar>", output);

		templateString = "test <%= test | replace needle=='t' replacement=='b' | replace needle=='sb' replacement=='ao'> test";
		outputWriter = new StringWriter();
		template = TemplateParser.parse(new StringReader(templateString));
		templateContext = new TemplateContext();
		templateContext.addFilter("replace", new ReplaceFilter());
		template.render(templateContext, outputWriter);
		output = outputWriter.toString();
		assertEquals("test beao test", output);

		template = TemplateParser.parse(new StringReader("<%include t>"));
		innerTemplate = TemplateParser.parse(new StringReader("<%=abc|replace needle==b replacement==d>"));
		outputWriter = new StringWriter();
		templateContext = new TemplateContext();
		templateContext.addTemplateProvider(TemplateProvider.TEMPLATE_CONTEXT_PROVIDER);
		templateContext.addFilter("replace", new ReplaceFilter());
		templateContext.set("t", innerTemplate);
		template.render(templateContext, outputWriter);
		output = outputWriter.toString();
		assertEquals("adc", output);
	}

	public void testTemplatesWithIfAndFilter() {
		Template template;
		TemplateContext templateContext;
		Filter filter;
		StringWriter stringWriter;

		filter = new Filter() {

			@Override
			public Object format(TemplateContext templateContext, Object data, Map<String, Object> parameters) {
				return ((Integer) data) > 1;
			}
		};

		template = TemplateParser.parse(new StringReader("<%if a|gt1>> 1<%else><= 1<%/if>"));
		templateContext = new TemplateContext();
		templateContext.addFilter("gt1", filter);
		stringWriter = new StringWriter();

		templateContext.set("a", 1);
		template.render(templateContext, stringWriter);
		assertEquals("<= 1", stringWriter.toString());

		stringWriter = new StringWriter();
		templateContext.set("a", 2);
		template.render(templateContext, stringWriter);
		assertEquals("> 1", stringWriter.toString());

		stringWriter = new StringWriter();
		template = TemplateParser.parse(new StringReader("<%if a|match value==0>is 0<%else>is something<%/if>"));
		templateContext = new TemplateContext();
		templateContext.addFilter("match", new MatchFilter());
		templateContext.set("a", 0);
		template.render(templateContext, stringWriter);
		assertEquals("is 0", stringWriter.toString());

		stringWriter = new StringWriter();
		template = TemplateParser.parse(new StringReader("<%if ! a|match value==0>is 0<%else>is something<%/if>"));
		templateContext = new TemplateContext();
		templateContext.addFilter("match", new MatchFilter());
		templateContext.set("a", 0);
		template.render(templateContext, stringWriter);
		assertEquals("is something", stringWriter.toString());

		stringWriter = new StringWriter();
		template = TemplateParser.parse(new StringReader("<%if a|match key==b>equal<%else>not equal<%/if>"));
		templateContext = new TemplateContext();
		templateContext.addFilter("match", new MatchFilter());
		templateContext.set("a", "This is a string.");
		templateContext.set("b", "This is a string.");
		template.render(templateContext, stringWriter);
		assertEquals("equal", stringWriter.toString());

		stringWriter = new StringWriter();
		template = TemplateParser.parse(new StringReader("<%if a|match value=b>equal<%else>not equal<%/if>"));
		templateContext = new TemplateContext();
		templateContext.addFilter("match", new MatchFilter());
		templateContext.set("a", "This is a string.");
		templateContext.set("b", "This is a string.");
		template.render(templateContext, stringWriter);
		assertEquals("equal", stringWriter.toString());

		stringWriter = new StringWriter();
		template = TemplateParser.parse(new StringReader("<%if ! a|match key==b>not equal<%else>equal<%/if>"));
		templateContext = new TemplateContext();
		templateContext.addFilter("match", new MatchFilter());
		templateContext.set("a", "This is a string.");
		templateContext.set("b", "This is not a string.");
		template.render(templateContext, stringWriter);
		assertEquals("not equal", stringWriter.toString());
	}

	public void testStoreFilter() {
		Template template;
		TemplateContext templateContext;
		String templateString;
		StringWriter outputWriter;
		String output;

		templateString = "<% name | store key=='foo'>Hello, <% foo>!";
		outputWriter = new StringWriter();
		template = TemplateParser.parse(new StringReader(templateString));
		templateContext = new TemplateContext();
		templateContext.addFilter("store", new StoreFilter());
		templateContext.set("name", "User");
		template.render(templateContext, outputWriter);
		output = outputWriter.toString();
		assertEquals("Hello, User!", output);
	}

	public void testStoreAndInsertFilter() {
		Template template;
		TemplateContext templateContext;
		String templateString;
		StringWriter outputWriter;
		String output;

		templateString = "<% name | store key=='foo'>Hello, <%= ${name} | insert needle=='${name}' key=='foo'>!";
		outputWriter = new StringWriter();
		template = TemplateParser.parse(new StringReader(templateString));
		templateContext = new TemplateContext();
		templateContext.addFilter("store", new StoreFilter());
		templateContext.addFilter("insert", new InsertFilter());
		templateContext.set("name", "User");
		template.render(templateContext, outputWriter);
		output = outputWriter.toString();
		assertEquals("Hello, User!", output);
	}

	/**
	 * Test case for the “parent” parameter in the {@link StoreFilter}.
	 */
	public void testStoreAndConditionalFilter() {
		Template template;
		TemplateContext templateContext;
		String templateString;
		StringWriter outputWriter;
		String output;

		templateString = "<%foreach list item><%= true|store key==shown><%/foreach><%if shown>true<%else>false<%/if>";
		outputWriter = new StringWriter();
		template = TemplateParser.parse(new StringReader(templateString));
		templateContext = new TemplateContext();
		templateContext.addFilter("store", new StoreFilter());
		templateContext.set("list", Arrays.asList(1));
		template.render(templateContext, outputWriter);
		output = outputWriter.toString();
		assertEquals("false", output);

		templateString = "<%foreach list item><%= true|store key==shown parent==true><%/foreach><%if shown>true<%else>false<%/if>";
		outputWriter = new StringWriter();
		template = TemplateParser.parse(new StringReader(templateString));
		templateContext = new TemplateContext();
		templateContext.addFilter("store", new StoreFilter());
		templateContext.set("list", Arrays.asList(1));
		template.render(templateContext, outputWriter);
		output = outputWriter.toString();
		assertEquals("true", output);
	}

	public void testInsertFilter() {
		Template template;
		TemplateContext templateContext;
		String templateString;
		StringWriter outputWriter;
		String output;

		templateString = "Hello, <%= ${name} | insert needle=='${name}' key=='map.a'>!";
		outputWriter = new StringWriter();
		template = TemplateParser.parse(new StringReader(templateString));
		templateContext = new TemplateContext();
		templateContext.addFilter("store", new StoreFilter());
		templateContext.addFilter("insert", new InsertFilter());
		Map<String, String> map = new HashMap<String, String>();
		map.put("a", "b");
		templateContext.set("map", map);
		template.render(templateContext, outputWriter);
		output = outputWriter.toString();
		assertEquals("Hello, b!", output);
	}

	public void testDefaultFilter() {
		Template template;
		TemplateContext templateContext;
		String templateString;
		StringWriter outputWriter;
		String output;

		templateString = "Test: <% value | default value=='0'><% noValue | default value=='1'>";
		outputWriter = new StringWriter();
		template = TemplateParser.parse(new StringReader(templateString));
		templateContext = new TemplateContext();
		templateContext.addFilter("default", new DefaultFilter());
		templateContext.set("value", "2");
		template.render(templateContext, outputWriter);
		output = outputWriter.toString();
		assertEquals("Test: 21", output);
	}

	/**
	 * Tests template inclusion with the “<%include>” directive.
	 */
	@SuppressWarnings("synthetic-access")
	public void testTemplateInclusion() {
		Template outerTemplate;
		Template innerTemplate;
		Template innerstTemplate;
		TemplateContext outerTemplateContext;
		TemplateContext innerTemplateContext;
		StringWriter stringWriter;

		outerTemplate = TemplateParser.parse(new StringReader("Line.\n<%include test>\nLine."));
		innerTemplate = TemplateParser.parse(new StringReader("Sentence!"));
		stringWriter = new StringWriter();

		outerTemplateContext = new TemplateContext();
		outerTemplateContext.addTemplateProvider(TemplateProvider.TEMPLATE_CONTEXT_PROVIDER);
		outerTemplateContext.set("test", innerTemplate);
		outerTemplate.render(outerTemplateContext, stringWriter);
		assertEquals("Line.\nSentence!\nLine.", stringWriter.toString());

		outerTemplate = TemplateParser.parse(new StringReader("Line.\n<%include test>\nLine."));
		outerTemplateContext = new TemplateContext();
		outerTemplateContext.addTemplateProvider(TemplateProvider.TEMPLATE_CONTEXT_PROVIDER);
		innerTemplate = TemplateParser.parse(new StringReader("<% a>"));
		innerTemplateContext = new TemplateContext(outerTemplateContext);
		stringWriter = new StringWriter();

		innerTemplateContext.set("a", "a");
		outerTemplateContext.set("test", innerTemplate);
		outerTemplate.render(innerTemplateContext, stringWriter);
		assertEquals("Line.\na\nLine.", stringWriter.toString());

		outerTemplate = TemplateParser.parse(new StringReader("Line.\n<%include test>\nLine."));
		outerTemplateContext = new TemplateContext();
		outerTemplateContext.addTemplateProvider(TemplateProvider.TEMPLATE_CONTEXT_PROVIDER);
		innerTemplate = TemplateParser.parse(new StringReader("<% a>"));
		innerTemplateContext = new TemplateContext(outerTemplateContext);
		stringWriter = new StringWriter();

		innerTemplateContext.set("a", "a");
		outerTemplateContext.set("test", innerTemplate);
		outerTemplateContext.set("a", "b");
		outerTemplate.render(innerTemplateContext, stringWriter);
		assertEquals("Line.\na\nLine.", stringWriter.toString());

		outerTemplate = TemplateParser.parse(new StringReader("a: <%include inner>"));
		outerTemplateContext = new TemplateContext();
		outerTemplateContext.addTemplateProvider(TemplateProvider.TEMPLATE_CONTEXT_PROVIDER);
		innerTemplate = TemplateParser.parse(new StringReader("<% b.test>"));
		innerTemplateContext = new TemplateContext(outerTemplateContext);
		stringWriter = new StringWriter();

		outerTemplateContext.set("a", "a");
		outerTemplateContext.set("inner", innerTemplate);
		innerTemplateContext.set("b", new Integer(1));
		innerTemplateContext.addAccessor(Integer.class, new Accessor() {

			@Override
			public Object get(TemplateContext templateContext, Object object, String member) {
				return templateContext.get("a");
			}
		});
		outerTemplate.render(innerTemplateContext, stringWriter);
		assertEquals("a: a", stringWriter.toString());

		outerTemplate = TemplateParser.parse(new StringReader("a: <% a|store key==b><%include inner>"));
		outerTemplateContext = new TemplateContext();
		outerTemplateContext.addTemplateProvider(TemplateProvider.TEMPLATE_CONTEXT_PROVIDER);
		outerTemplateContext.addFilter("store", new StoreFilter());
		innerTemplate = TemplateParser.parse(new StringReader("<% b>"));
		stringWriter = new StringWriter();

		outerTemplateContext.set("inner", innerTemplate);
		outerTemplateContext.set("a", "a");
		outerTemplate.render(outerTemplateContext, stringWriter);
		assertEquals("a: a", stringWriter.toString());

		outerTemplate = TemplateParser.parse(new StringReader("a: <% a|store key==b><%include inner>"));
		outerTemplateContext = new TemplateContext();
		outerTemplateContext.addTemplateProvider(TemplateProvider.TEMPLATE_CONTEXT_PROVIDER);
		outerTemplateContext.addFilter("store", new StoreFilter());
		innerTemplate = TemplateParser.parse(new StringReader("<% b>"));
		innerTemplateContext = new TemplateContext(outerTemplateContext);
		stringWriter = new StringWriter();

		outerTemplateContext.set("inner", innerTemplate);
		outerTemplateContext.set("a", "a");
		innerTemplateContext.set("b", "b");
		outerTemplate.render(innerTemplateContext, stringWriter);
//		assertEquals("a: b", stringWriter.toString());

		outerTemplate = TemplateParser.parse(new StringReader("a: <% a|store key==b><%include inner>"));
		outerTemplateContext = new TemplateContext();
		outerTemplateContext.addTemplateProvider(TemplateProvider.TEMPLATE_CONTEXT_PROVIDER);
		outerTemplateContext.addFilter("store", new StoreFilter());
		innerTemplate = TemplateParser.parse(new StringReader("<% b|store key==c><%include innerst>"));
		innerTemplateContext = new TemplateContext(outerTemplateContext);
		innerTemplateContext.addTemplateProvider(TemplateProvider.TEMPLATE_CONTEXT_PROVIDER);
		innerTemplateContext.addFilter("store", new StoreFilter());
		innerstTemplate = TemplateParser.parse(new StringReader("<% c>"));
		stringWriter = new StringWriter();

		outerTemplateContext.set("inner", innerTemplate);
		innerTemplateContext.set("innerst", innerstTemplate);
		outerTemplateContext.set("a", "a");
		outerTemplate.render(innerTemplateContext, stringWriter);
		assertEquals("a: a", stringWriter.toString());

		outerTemplate = TemplateParser.parse(new StringReader("items: <%include itemTemplate>"));
		outerTemplateContext = new TemplateContext();
		outerTemplateContext.addTemplateProvider(TemplateProvider.TEMPLATE_CONTEXT_PROVIDER);
		innerTemplate = TemplateParser.parse(new StringReader("<%foreach items item><%item.name><%/foreach>"));
		innerTemplateContext = new TemplateContext(outerTemplateContext);
		outerTemplateContext.set("itemTemplate", innerTemplate);
		Collection<Item> items = new ArrayList<Item>();
		items.add(new Item("foo"));
		items.add(new Item("bar"));
		outerTemplateContext.set("items", items);
		outerTemplateContext.addAccessor(Item.class, new ItemAccessor());
		stringWriter = new StringWriter();
		outerTemplate.render(innerTemplateContext, stringWriter);
		assertEquals("items: foobar", stringWriter.toString());

		outerTemplate = TemplateParser.parse(new StringReader("<%=1|store key==a><%include t><%a>"));
		outerTemplateContext = new TemplateContext();
		outerTemplateContext.addFilter("store", new StoreFilter());
		outerTemplateContext.addTemplateProvider(TemplateProvider.TEMPLATE_CONTEXT_PROVIDER);
		innerTemplate = TemplateParser.parse(new StringReader("<%=2|store key==a>"));
		outerTemplateContext.set("t", innerTemplate);
		stringWriter = new StringWriter();
		outerTemplate.render(outerTemplateContext, stringWriter);
		assertEquals("1", stringWriter.toString());

		outerTemplate = TemplateParser.parse(new StringReader("<%include t>"));
		innerTemplate = TemplateParser.parse(new StringReader("<%include u>"));
		innerstTemplate = TemplateParser.parse(new StringReader("<%=ä|html>"));
		outerTemplateContext = new TemplateContext();
		outerTemplateContext.addFilter("html", new HtmlFilter());
		outerTemplateContext.addTemplateProvider(TemplateProvider.TEMPLATE_CONTEXT_PROVIDER);
		outerTemplateContext.set("t", innerTemplate);
		outerTemplateContext.set("u", innerstTemplate);
		stringWriter = new StringWriter();
		outerTemplate.render(outerTemplateContext, stringWriter);
		assertEquals("&auml;", stringWriter.toString());
	}

	public void testTemplateInclusionWithParameters() {
		Template outerTemplate;
		Template innerTemplate;
		Template innerstTemplate;
		TemplateContext outerTemplateContext;
		TemplateContext innerTemplateContext;
		StringWriter stringWriter;

		outerTemplate = TemplateParser.parse(new StringReader("<%include t a=b>"));
		outerTemplateContext = new TemplateContext();
		outerTemplateContext.addTemplateProvider(TemplateProvider.TEMPLATE_CONTEXT_PROVIDER);
		innerTemplate = TemplateParser.parse(new StringReader("<%a>"));
		outerTemplateContext.set("a", "1");
		outerTemplateContext.set("b", "2");
		outerTemplateContext.set("t", innerTemplate);

		stringWriter = new StringWriter();
		outerTemplate.render(outerTemplateContext, stringWriter);
		assertEquals("2", stringWriter.toString());

		outerTemplate = TemplateParser.parse(new StringReader("<%include t a==2>"));
		outerTemplateContext = new TemplateContext();
		outerTemplateContext.addTemplateProvider(TemplateProvider.TEMPLATE_CONTEXT_PROVIDER);
		innerTemplate = TemplateParser.parse(new StringReader("<%a>"));
		outerTemplateContext.set("a", "1");
		outerTemplateContext.set("b", "2");
		outerTemplateContext.set("t", innerTemplate);

		stringWriter = new StringWriter();
		outerTemplate.render(outerTemplateContext, stringWriter);
		assertEquals("2", stringWriter.toString());

		outerTemplate = TemplateParser.parse(new StringReader("<%include t a==2>"));
		outerTemplateContext = new TemplateContext();
		outerTemplateContext.addTemplateProvider(TemplateProvider.TEMPLATE_CONTEXT_PROVIDER);
		innerTemplate = TemplateParser.parse(new StringReader("<%include t a==1>"));
		innerTemplateContext = new TemplateContext(outerTemplateContext);
		innerTemplateContext.addTemplateProvider(TemplateProvider.TEMPLATE_CONTEXT_PROVIDER);
		innerstTemplate = TemplateParser.parse(new StringReader("<%a>"));
		outerTemplateContext.set("a", "1");
		outerTemplateContext.set("b", "2");
		outerTemplateContext.set("t", innerTemplate);
		innerTemplateContext.set("t", innerstTemplate);

		stringWriter = new StringWriter();
//		outerTemplate.render(outerTemplateContext, stringWriter);
//		assertEquals("1", stringWriter.toString());
	}

	public void testTemplatePlugins() {
		Template template;
		TemplateContext templateContext;
		StringWriter stringWriter;

		template = TemplateParser.parse(new StringReader("<%:double key=a><% a>"));
		templateContext = new TemplateContext();
		templateContext.addPlugin("double", new Plugin() {

			@Override
			public void execute(TemplateContext templateContext, Map<String, String> parameters) {
				String key = parameters.get("key");
				templateContext.set(key, String.valueOf(templateContext.get(key)) + String.valueOf(templateContext.get(key)));
			}

		});
		templateContext.set("a", "test");
		stringWriter = new StringWriter();

		template.render(templateContext, stringWriter);
		assertEquals("testtest", stringWriter.toString());
	}

	/**
	 * Tests the {@link DeletePlugin}.
	 */
	public void testDeletePlugin() {
		Template template;
		TemplateContext templateContext;
		StringWriter stringWriter;

		template = TemplateParser.parse(new StringReader("<%a><%:delete key=a><%a>"));
		templateContext = new TemplateContext();
		templateContext.addPlugin("delete", new DeletePlugin());
		templateContext.set("a", "a");
		stringWriter = new StringWriter();

		template.render(templateContext, stringWriter);
		assertEquals("a", stringWriter.toString());
	}

	private static class TestFilter implements Filter {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String format(TemplateContext templateContext, Object data, Map<String, Object> parameters) {
			return "[" + data.getClass().getName() + "@" + data.hashCode() + "]";
		}

	}

	/**
	 * Small container class used in some of the tests.
	 *
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	private static class Item {

		/** The name of this item. */
		private final String name;

		/**
		 * Creates a new item.
		 *
		 * @param name
		 *            The name of the item
		 */
		public Item(String name) {
			this.name = name;
		}

		/**
		 * Returns the name of this item.
		 *
		 * @return The name of this item
		 */
		public String getName() {
			return name;
		}

	}

	/**
	 * {@link Accessor} implementation for {@link Item}s.
	 *
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	private static class ItemAccessor implements Accessor {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Object get(TemplateContext templateContext, Object object, String member) {
			return ((Item) object).getName();
		}

	}

}
