
package net.pterodactylus.util.template;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

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
	 */
	public void testStringTemplatesWithoutParameters() throws IOException {
		Template template;
		String templateString;
		StringWriter outputWriter;
		String output;

		templateString = "This is a template without parameters.";
		outputWriter = new StringWriter();
		template = new Template();
		template.setInput(new StringReader(templateString));
		template.render(outputWriter);
		output = outputWriter.toString();
		assertEquals(templateString, output);

		templateString = "This is a template without parameters but with a CR character in it.\r";
		outputWriter = new StringWriter();
		template = new Template();
		template.setInput(new StringReader(templateString));
		template.render(outputWriter);
		output = outputWriter.toString();
		assertEquals(templateString, output);

		templateString = "This is a template without parameters but with an LF character in it.\n";
		outputWriter = new StringWriter();
		template = new Template();
		template.setInput(new StringReader(templateString));
		template.render(outputWriter);
		output = outputWriter.toString();
		assertEquals(templateString, output);

		templateString = "This is a template without parameters but with a CR/LF character combination in it.\r\n";
		outputWriter = new StringWriter();
		template = new Template();
		template.setInput(new StringReader(templateString));
		template.render(outputWriter);
		output = outputWriter.toString();
		assertEquals(templateString, output);
	}

	/**
	 * Test case for template strings that contain parameters.
	 *
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public void testStringTemplatesWithParameters() throws IOException {
		Template template;
		String templateString;
		StringWriter outputWriter;
		String output;

		templateString = "This is a template with <%one> parameter.";
		outputWriter = new StringWriter();
		template = new Template();
		template.setInput(new StringReader(templateString));
		template.set("one", "two");
		template.render(outputWriter);
		output = outputWriter.toString();
		assertEquals("This is a template with two parameter.", output);

		templateString = "This is a template with <% one > parameter.";
		outputWriter = new StringWriter();
		template = new Template();
		template.setInput(new StringReader(templateString));
		template.set("one", "two");
		template.render(outputWriter);
		output = outputWriter.toString();
		assertEquals("This is a template with two parameter.", output);

		templateString = "This is a template with <%one> parameter, one <% left > and one <% right>.";
		outputWriter = new StringWriter();
		template = new Template();
		template.setInput(new StringReader(templateString));
		template.set("one", "two");
		template.set("left", "on top");
		template.set("right", "below");
		template.render(outputWriter);
		output = outputWriter.toString();
		assertEquals("This is a template with two parameter, one on top and one below.", output);

	}

}
