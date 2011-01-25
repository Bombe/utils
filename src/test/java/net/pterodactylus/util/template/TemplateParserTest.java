/*
 * utils - TemplateParserTest.java - Copyright © 2011 David Roden
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

import java.util.List;

import junit.framework.TestCase;

/**
 * TODO
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class TemplateParserTest extends TestCase {

	public void testTagParser() {
		String tagContent;
		List<String> tagWords;

		tagContent = " item | replace a=b";
		tagWords = TemplateParser.parseTag(tagContent);
		assertNotNull(tagWords);
		compare(tagWords, "item", null, "replace", "a=b");

		tagContent = " item | replace a=b|html";
		tagWords = TemplateParser.parseTag(tagContent);
		assertNotNull(tagWords);
		compare(tagWords, "item", null, "replace", "a=b", null, "html");

		tagContent = " item | replace a=b\\|html";
		tagWords = TemplateParser.parseTag(tagContent);
		assertNotNull(tagWords);
		compare(tagWords, "item", null, "replace", "a=b|html");

		tagContent = " item' '| replace a=b\\|html";
		tagWords = TemplateParser.parseTag(tagContent);
		assertNotNull(tagWords);
		compare(tagWords, "item ", null, "replace", "a=b|html");

		tagContent = " item\" \"| replace a=b\\|html";
		tagWords = TemplateParser.parseTag(tagContent);
		assertNotNull(tagWords);
		compare(tagWords, "item ", null, "replace", "a=b|html");

		tagContent = " item\" '\"| replace a=b\\|html";
		tagWords = TemplateParser.parseTag(tagContent);
		assertNotNull(tagWords);
		compare(tagWords, "item '", null, "replace", "a=b|html");

		tagContent = " item | replace a='<b foo=\"bar\">'|html";
		tagWords = TemplateParser.parseTag(tagContent);
		assertNotNull(tagWords);
		compare(tagWords, "item", null, "replace", "a=<b foo=\"bar\">", null, "html");
	}

	private void compare(List<String> actualWords, String... expectedWords) {
		assertEquals(expectedWords.length, actualWords.size());
		int counter = 0;
		for (String expectedWord : expectedWords) {
			assertEquals(expectedWord, actualWords.get(counter++));
		}
	}

}
