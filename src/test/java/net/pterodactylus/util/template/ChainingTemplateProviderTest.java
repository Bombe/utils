package net.pterodactylus.util.template;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;

public class ChainingTemplateProviderTest {

	@Test
	public void providerForwardsRequestToFirstGivenProvider() {
		TemplateProvider provider = new ChainingTemplateProvider(testTemplateProvider);
		Template returnedTemplate = provider.getTemplate(null, "test");
		assertThat(returnedTemplate, sameInstance(template));
	}

	@Test
	public void providerDoesNotForwardRequestToSecondProviderIfFirstProviderReturnedResult() {
		TemplateProvider secondProvider = (templateContext, templateName) -> new Template();
		TemplateProvider provider = new ChainingTemplateProvider(testTemplateProvider, secondProvider);
		Template returnedTemplate = provider.getTemplate(null, "test");
		assertThat(returnedTemplate, sameInstance(template));
	}

	@Test
	public void providerForwardsRequestToSecondProviderIfFirstProviderDoesNotReturnResult() {
		TemplateProvider provider = new ChainingTemplateProvider(nullTemplateProvider, testTemplateProvider);
		Template returnedTemplate = provider.getTemplate(null, "test");
		assertThat(returnedTemplate, sameInstance(template));
	}

	@Test
	public void providerReturnsNullWhenNoProviderReturnsATemplate() {
		TemplateProvider provider = new ChainingTemplateProvider(nullTemplateProvider);
		Template returnedTemplate = provider.getTemplate(null, "test");
		assertThat(returnedTemplate, nullValue());
	}

	private final Template template = new Template();
	private final TemplateProvider testTemplateProvider = (context, name) -> name.equals("test") ? template : null;
	private final TemplateProvider nullTemplateProvider = (context, name) -> null;

}
