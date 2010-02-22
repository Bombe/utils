/*
 * utils - DefaultTemplateFactory.java - Copyright © 2010 David Roden
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

import java.io.Reader;

/**
 * Default {@link TemplateFactory} implementation that creates {@link Template}s
 * with {@link HtmlFilter}s and {@link ReplaceFilter}s added.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class DefaultTemplateFactory implements TemplateFactory {

	/** The default instance. */
	private static DefaultTemplateFactory instance;

	/** Whether to add an {@link HtmlFilter} to created templates. */
	private boolean addHtmlFilter;

	/** Whether to add a {@link ReplaceFilter} to created templates. */
	private boolean addReplaceFilter;

	/**
	 * Creates a new default template factory that adds both an
	 * {@link HtmlFilter} and a {@link ReplaceFilter} to created templates.
	 */
	public DefaultTemplateFactory() {
		this(true, true);
	}

	/**
	 * Creates a new default template factory.
	 *
	 * @param addHtmlFilter
	 *            {@code true} to add an {@link HtmlFilter} to created
	 *            templates, {@code false} otherwise
	 * @param addReplaceFilter
	 *            {@code true} to add a {@link ReplaceFilter} to created
	 *            templates, {@code false} otherwise
	 */
	public DefaultTemplateFactory(boolean addHtmlFilter, boolean addReplaceFilter) {
		this.addHtmlFilter = addHtmlFilter;
		this.addReplaceFilter = addReplaceFilter;
	}

	/**
	 * Returns the static default instance of this template factory.
	 *
	 * @return The default template factory
	 */
	public synchronized static TemplateFactory getInstance() {
		if (instance == null) {
			instance = new DefaultTemplateFactory();
		}
		return instance;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Template createTemplate(Reader templateSource) {
		Template template = new Template(templateSource);
		if (addHtmlFilter) {
			template.addFilter("html", new HtmlFilter());
		}
		if (addReplaceFilter) {
			template.addFilter("replace", new ReplaceFilter());
		}
		return template;
	}

}
