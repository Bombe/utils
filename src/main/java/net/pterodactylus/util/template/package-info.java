/*
 * utils - package-info.java - Copyright © 2010–2019 David Roden
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
 * Light-weight template system.
 *
 * <h3>Writing Templates</h3>
 *
 * <p>
 * Inserting values and control structures such as loops into a template uses
 * a syntax that is well-known by other preprocessors, such es PHP or JSPs.
 * For example:
 * </p>
 *
 * <pre>
 * &lt;html&gt;
 * &lt;head&gt;
 * &lt;title&gt;
 * &lt;% title &gt;
 * &lt;/title&gt;
 * &lt;/head&gt;
 * &lt;/html&gt;
 * </pre>
 *
 * <p>
 * This will insert the value of the template variable named “title” into the
 * template.
 * </p>
 *
 * <h3>Rendering Templates</h3>
 *
 * <p>
 * To render a {@link net.pterodactylus.util.template.Template} you need a
 * {@link net.pterodactylus.util.template.TemplateContext}.
 * </p>
 *
 * <pre>
 * Template template = TemplateParser.parse(new Filter("template.html"));
 * TemplateContext templateContext = new TemplateContext();
 * StringWriter writer = new StringWriter();
 * template.render(templateContext, writer);
 * </pre>
 *
 * <p>
 * Templates do not hold their own data anymore. To achieve thread safety
 * without having to synchronize on the whole template, the {@code TemplateContext}
 * was introduced. Its intended lifetime is restricted to a single rendering
 * pass. A {@link net.pterodactylus.util.template.TemplateContextFactory} can
 * be used to easily create {@code TemplateContext}s that hold the desired
 * {@link net.pterodactylus.util.template.Filter}s,
 * {@link net.pterodactylus.util.template.Plugin}s,
 * {@link net.pterodactylus.util.template.Accessor}s,
 * {@link net.pterodactylus.util.template.TemplateProvider}s, and objects. Template
 * contexts can have parent contexts and use those recursively when any kind
 * of data is requested.
 * </p>
 *
 * <p>
 * A template has an initial template context. It can be used to set values in the
 * template that never change during the lifetime of the template. Any values that
 * are set on the template are merged into the template context that is used for
 * the current rendering pass, overwriting all other values set in that context
 * or its parent contexts.
 * </p>
 *
 * <h3>Setting Template Variables</h3>
 *
 * <p>
 * Variables in a template are set using the
 * {@link net.pterodactylus.util.template.TemplateContext#set(String, Object)} method.
 * </p>
 *
 * <pre>
 * Template template = TempalteParser.parse(new FileReader("template.html"));
 * TemplateContext templateContext = templateContextFactory.createTemplateContext();
 * templateContext.set("variable", "value");
 * templateContext.set("items", Arrays.asList("foo", "bar", "baz"));
 * template.render(templateContext, outputWriter);
 * </pre>
 *
 * <h3>Looping Over Collections</h3>
 *
 * <p>
 * You can set the value of a template variable to an instance of
 * {@link java.util.Collection}. This allows you to iterate over the items in said collection:
 * </p>
 *
 * <pre>
 * &lt;ul&gt;
 * &lt;%foreach pointCollection pointItem&gt;
 * &lt;li&gt;
 * Item: &lt;% pointItem&gt;
 * &lt;/li&gt;
 * &lt;%/foreach&gt;
 * &lt;/ul&gt;
 * </pre>
 *
 * This will output the value of each item in the collection.
 *
 * <h4>Looping Over Maps</h4>
 *
 * <p>
 * It is also possible to loop over {@link java.util.Map}s. The syntax is identical:
 * </p>
 *
 * <pre>
 * &lt;ul&gt;
 * &lt;%foreach itemValues itemValue&gt;
 * &lt;li&gt;
 * &lt;% itemValue.key&gt;: &lt;% itemValue.value&gt;
 * &lt;/li&gt;
 * &lt;%/foreach&gt;
 * &lt;/ul&gt;
 * </pre>
 *
 * <p>
 * Instead of looping only over the keys or the values of the {@code Map},
 * the loop will iterate over the {@link java.util.Map.Entry}s of the map,
 * requiring you to access the keys by using the “key” property, and the
 * value by using the “value” property. (This requires that you install
 * a {@link net.pterodactylus.util.template.ReflectionAccessor} on your
 * template context, or a similar {@link net.pterodactylus.util.template.Accessor}
 * that allows to access the {@link java.util.Map.Entry}.)
 * </p>
 *
 * <h3>Loop Properties</h3>
 *
 * <p>
 * Each iteration of a loop has numerous properties, such as being the first
 * iteration, or the last, or neither of them. These properties can be
 * accessed in a template.
 * </p>
 *
 * <pre>
 * &lt;%foreach pointCollection pointItem&gt;
 * &lt;%first&gt;
 * The collection contains the following items:
 * &lt;ul&gt;
 * &lt;%/first&gt;
 * &lt;li&gt;
 * Item: &lt;% pointItem&gt;
 * &lt;/li&gt;
 * &lt;%last&gt;
 * &lt;/ul&gt;
 * &lt;%/last&gt;
 * &lt;%/foreach&gt;
 * </pre>
 *
 * <p>
 * The loop properties that can be accessed in this way are: {@code size},
 * {@code count} (running index from 0 to (size - 1)), {@code first},
 * {@code last}, {@code notfirst}, {@code notlast}, {@code odd}, {@code even}.
 * </p>
 *
 * <h3>Item Properties</h3>
 *
 * <p>
 * Template variable names can specify a hierarchy: “item.index” specifies the
 * member “index” of the value of template variable “item”. The default
 * template context implementation can only handle getting members of template
 * variables that contain instances of  {@link java.util.Map}; it is possible
 * to define member accessors for your own types (see below).
 * </p>
 *
 * <pre>
 * &lt;ul&gt;
 * &lt;%foreach itemCollection item&gt;
 * &lt;li&gt;
 * Item: &lt;a href="item?id=&lt;% item.id&gt;">&lt;% item.name&gt;&lt;/a&gt;
 * &lt;/li&gt;
 * &lt;%/foreach&gt;
 * &lt;/ul&gt;
 * </pre>
 *
 * <p>
 * When {@code itemCollection} is properly set up this will print each item’s
 * name with a link to an item page that receives the ID of the item as
 * parameter. If {@code item} does not refer to a {@link java.util.Map} instance,
 * a custom accessor (see below) is necessary.
 * </p>
 *
 * <h3>Handling Custom Types</h3>
 *
 * <p>
 * The template system can be extended using
 * {@link net.pterodactylus.util.template.Accessor}s. An accessor is used to
 * allow template variable syntax like “object.foo”. Depending on the type of
 * {@code object}  the appropriate accessor is used to find the value of the
 * member “foo” (which can e.g. be retrieved by calling a complicated operation
 * on {@code object}).
 * </p>
 *
 * <p>
 * With a custom type {@code Item} that exposes both an ID and a name (using
 * {@code getID()} and {@code getName()} methods), the following
 * {@link net.pterodactylus.util.template.Accessor} will allow the above
 * example to work.
 * </p>
 *
 * <pre>
 * public class ItemAccessor implements Accessor {
 * 	public Object get(TemplateContext templateContext, Object object, String member) {
 * 		Item item = (Item) object;
 * 		if ("name".equals(member)) { return item.getName(); }
 * 		if ("id".equals(member)) { return item.getId(); }
 * 		return null;
 * 	}
 * }
 * </pre>
 *
 * <h4>The Reflection Accessor</h4>
 *
 * <p>
 * A very convenient accessor (and good base for custom accessors) is the
 * {@link net.pterodactylus.util.template.ReflectionAccessor}. It uses
 * reflection to find members of an object. Accessing the member “fooData”
 * would look for the methods “getFooData()”, “isFooData()”, “fooData()”, and
 * call the first method it can locate without parameters. The value returned
 * by the method is the value returned to the template.
 * </p>
 *
 * <p>
 * When writing custom {@link net.pterodactylus.util.template.Accessor}s
 * it is very convenient to subclass
 * {@link net.pterodactylus.util.template.ReflectionAccessor} and only add
 * members that are not present in the original class, defaulting to calling
 * “super.get()” when a “standard” member is requested.
 * </p>
 *
 * <h3>Conditional Execution</h3>
 *
 * <p>
 * With a loop and its constructs (e.g. &lt;%first&gt; or &lt;%even&gt;) you
 * can already shape your formatted text in quite some ways. If for some
 * reason this is not enough you do have another possibility.
 * </p>
 *
 * <pre>
 * &lt;p&gt;
 *     The color is:
 *     &lt;%if color.black&gt;
 *         black
 *     &lt;%elseif color.red&gt;
 *         red
 *     &lt;%elseif ! color.green&gt;
 *         not green
 *     &lt;%else&gt;
 *         green
 *     &lt;%/if&gt;
 * &lt;/p&gt;
 * </pre>
 *
 * <p>
 * At the moment the {@code <%if>} directive requires a single argument
 * which has to evaluate to a {@link java.lang.Boolean} object. The object may
 * be prepended by an exclamation point (“!”, either with or without
 * whitespace following it) to signify that the condition should be reversed.
 * Using a custom accessor this can easily be accomplished. Any further
 * parsing (and expression evaluation) would make the template parser and
 * renderer almost infinitely more complex (and very not-light-weight-anymore).
 * </p>
 *
 * <p>
 * It is also possible to react on the result of a
 * {@link net.pterodactylus.util.template.Filter}; see below.
 * </p>
 *
 * <h3>Filtering Output</h3>
 *
 * <p>
 * One large issue when handling text in web pages is escaping the HTML code
 * so that the content of a web page does not have the capability of inserting
 * custom code into a web site, or destroying its design by unbalanced tags.
 * The template supports filtering output but does not have any output filters
 * added to it by default.
 * </p>
 *
 * <pre>
 * TemplateContext templateContext = templateContextFactory.createTemplateContext();
 * templateContext.addFilter("html", new HtmlFilter());
 * </pre>
 *
 * <p>
 * This will a filter for HTML to the list of available filters. If you want
 * to escape some text in your template, apply it using the pipe character
 * (“|”, with or without whitespace around it).
 * </p>
 *
 * <pre>
 * &lt;div&gt;Your name is &lt;% name|html&gt;, right?&lt;/div&gt;
 * </pre>
 *
 * <p>
 * You can also use several filters for a single variable output. Those are
 * executed in the order they are specified.
 * </p>
 *
 * <pre>
 * &lt;div&gt;Your name is &lt;% name|html|replace needle=Frank replacement=Steve&gt;, right?&lt;/div&gt;
 * </pre>
 *
 * <p>
 * It is also possible to use a filter in an &lt;%if&gt; construct.
 * </p>
 *
 * <pre>
 * &lt;%if name|match value=Frank&gt;You’re Frank!&lt;%else&gt;You’re not Frank.&lt;%/if&gt;
 * </pre>
 *
 * <p>
 * The {@link java.lang.String} conversion of the final filter result has to
 * match “true” in order to be evaluated as {@code true}, so not only
 * {@link java.lang.Boolean}s but any other object can result in an
 * if-branch being executed.
 * </p>
 *
 * <h3>Storing Values in the Template</h3>
 *
 * <p>
 * Sometimes it can be necessary to store a value in the template context for later
 * use. In conjunction with a replacement filter this can be used to include
 * template variables in strings that are output by other filters, e.g. an
 * i18n filter.
 * </p>
 *
 * <pre>
 * <% user | html | store key='htmlUser'>
 * <% HelloText | i18n | html | insert needle='${user}' key='htmlUser'>
 * </pre>
 *
 * <p>
 * The “insert” filter can also read variables directly from the template.
 * </p>
 *
 * <h3>Internationalization / Localization (i18n, l10n)</h3>
 *
 * <p>
 * When creating web pages for larger projects you often have to deal with
 * multiple languages. One possibility to achieve multilingual support in a
 * template system would be to simply ignore language support in the template
 * system and create a new template for each language. However, this solution
 * is not desirable for any of the participating parties: the programmer has
 * to load a different template depending on the language; the designer has to
 * copy a desgin change into every translated template again; the translator
 * needs to copy and process a complete template, potentially missing
 * translatable items in a sea of design markup.
 * </p>
 *
 * <p>
 * One possible solution is the possibility to hardcode values in the template
 * and run them through arbitrary filters.
 * </p>
 *
 * <pre>
 * &lt;div&gt;&lt;%= Item.Name | i18n | html&gt;&lt;/div&gt;
 * &lt;div&gt;&lt;% item.name | html&gt;&lt;/div&gt;
 * &lt;div&gt;&lt;%= Item.ID | i18n | html&gt;&lt;/div&gt;
 * &lt;div&gt;&lt;% item.id | html&gt;&lt;/div&gt;
 * </pre>
 *
 * <p>
 * In this example the strings “Item.Name” and “Item.ID” are run through a
 * custom {@link net.pterodactylus.util.template.Filter} that replaces a
 * language-independent key into a translated string. To prevent nasty
 * surprises the translated string is also run through a HTML filter before
 * the final value is printed on the web page.
 * </p>
 *
 * <h3>Whitespace</h3>
 *
 * <p>
 * Sometimes, e.g. when using {@code <%=} or filters, whitespace in a filter
 * directive needs to be preserved. This can be accomplished by using single
 * quotes, double quotes, or a backslash.
 * </p>
 *
 * <h4>Single Quotes</h4>
 *
 * <p>
 * Single quotes preserve all characters until another single quote character
 * is encountered.
 * </p>
 *
 * <h4>Double Quotes</h4>
 *
 * <p>
 * Double quotes preserve all characters until either another double quote or
 * a backslash is encountered.
 * </p>
 *
 * <h4>Backslash</h4>
 *
 * <p>
 * The backslash preserves the next character but is discarded itself.
 * </p>
 *
 * <h4>Examples</h4>
 *
 * <pre>
 * &lt;%= foo | replace needle=foo replacement="bar baz"&gt;
 * </pre>
 *
 * <p>
 * This will replace the text “foo” with the text “bar baz”.
 * </p>
 *
 * <pre>
 * &lt;%= "foo bar" | replace needle=foo replacement="bar baz"&gt;
 * </pre>
 *
 * <p>
 * This will replace the text “foo bar” with the text “bar baz bar”.
 * </p>
 *
 * <pre>
 * &lt;%= "foo bar" | replace needle='foo bar' replacement="bar baz"&gt;
 * </pre>
 *
 * <p>
 * This will replace the text “foo bar” with the text “bar baz”.
 * </p>
 *
 * <pre>
 * &lt;%= "foo bar" | replace needle=foo\ bar replacement="bar baz"&gt;
 * </pre>
 *
 * <p>
 * This will also replace the text “foo bar” with the text “bar baz”.
 * </p>
 *
 * <pre>
 * &lt;%= "foo bar" | replace needle="foo\ bar" replacement="bar baz"&gt;
 * </pre>
 *
 * <p>
 * This will also replace the text “foo bar” with the text “bar baz”.
 * </p>
 *
 * <pre>
 * &lt;%= "foo bar" | replace needle='foo\ bar' replacement="bar baz"&gt;
 * </pre>
 *
 * <p>
 * This will not replace the text “foo bar” with anything because the text
 * “foo\ bar” is not found.
 * </p>
 *
 */

package net.pterodactylus.util.template;

