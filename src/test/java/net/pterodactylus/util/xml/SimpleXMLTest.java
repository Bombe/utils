package net.pterodactylus.util.xml;

import java.io.StringReader;
import org.junit.Test;
import org.w3c.dom.Document;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class SimpleXMLTest {

	@Test
	public void xmlFilesWithDoctypesCanBeParsed() {
		String documentWithEntity = "<?xml version='1.0' encoding='utf-8' ?>\n<!DOCTYPE foo>\n<foo><bar>baz</bar></foo>\n";
		Document document = XML.transformToDocument(new StringReader(documentWithEntity));
		SimpleXML simpleXml = SimpleXML.fromDocument(document);
		assertThat(simpleXml.getName(), equalTo("foo"));
		assertThat(simpleXml.getNode("bar").getValue(), equalTo("baz"));
	}

	@Test
	public void xmlFilesWithCommentsAfterTheRootNodeCanBeParsed() {
		String documentWithEntity = "<?xml version='1.0' encoding='utf-8' ?>\n<foo><bar>baz</bar></foo>\n<!-- comment -->";
		Document document = XML.transformToDocument(new StringReader(documentWithEntity));
		SimpleXML simpleXml = SimpleXML.fromDocument(document);
		assertThat(simpleXml.getName(), equalTo("foo"));
		assertThat(simpleXml.getNode("bar").getValue(), equalTo("baz"));
	}

}
