package net.pterodactylus.util.template;

import org.junit.Test;

import java.io.StringWriter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

public class ClassPathTemplateProviderTest {

	@Test
	public void providerReturnsNullForTemplateThatDoesNotExist() {
		TemplateProvider provider = new ClassPathTemplateProvider(ClassPathTemplateProviderTest.class);
		assertThat(provider.getTemplate(null, "does-not-exist.txt"), nullValue());
	}

	@Test
	public void providerCanLoadTemplateFromClasspath() {
		TemplateProvider provider = new ClassPathTemplateProvider(ClassPathTemplateProviderTest.class);
		assertThat(renderTemplate(provider.getTemplate(null, "net/pterodactylus/util/template/classpath-template.txt")), equalTo("foo"));
	}

	@Test
	public void providerCanLoadTemplateFromClasspathWithDifferentResourcePath() {
		TemplateProvider provider = new ClassPathTemplateProvider(ClassPathTemplateProviderTest.class, "/net/pterodactylus/util/template/");
		assertThat(renderTemplate(provider.getTemplate(null, "classpath-template.txt")), equalTo("foo"));
	}

	@Test
	public void providerRefusesToLoadTemplateThatCannotBeParsed() {
		TemplateProvider provider = new ClassPathTemplateProvider(ClassPathTemplateProviderTest.class, "/net/pterodactylus/util/template/");
		assertThat(provider.getTemplate(null, "classpath-broken-template.txt"), nullValue());
	}

	private String renderTemplate(Template template) {
		StringWriter stringWriter = new StringWriter();
		template.render(null, stringWriter);
		return stringWriter.toString().trim();
	}

}
