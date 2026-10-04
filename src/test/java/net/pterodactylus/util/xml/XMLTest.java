package net.pterodactylus.util.xml;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.w3c.dom.Document;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class XMLTest {

	@Test
	public void documentsAreLoadedWithoutExternalEntities() throws IOException {
		File externalFile = temporaryFolder.newFile();
		Files.write(externalFile.toPath(), "secret".getBytes(UTF_8));
		String documentWithEntity = "<?xml version='1.0' encoding='utf-8' ?>\n<!DOCTYPE foo [<!ENTITY bar SYSTEM '" + externalFile.toURI() + "'>]>\n<foo>foo: &bar; baz</foo>\n";
		Document document = XML.transformToDocument(new StringReader(documentWithEntity));
		assertThat(document.getChildNodes().item(1).getChildNodes().item(0).getNodeValue(), equalTo("foo:  baz"));
	}

	@Rule
	public final TemporaryFolder temporaryFolder = new TemporaryFolder();

}
