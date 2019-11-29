/*
 * utils - TemplateNotification.java - Copyright © 2010–2019 David Roden
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

package net.pterodactylus.util.notify;

import java.io.IOException;
import java.io.Writer;

import net.pterodactylus.util.template.Accessor;
import net.pterodactylus.util.template.Filter;
import net.pterodactylus.util.template.Part;
import net.pterodactylus.util.template.Plugin;
import net.pterodactylus.util.template.Template;
import net.pterodactylus.util.template.TemplateContext;
import net.pterodactylus.util.template.TemplateException;

/**
 * {@link Template}-based implementation of a {@link Notification}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class TemplateNotification extends AbstractNotification implements Part {

	/** The template to render. */
	private final Template template;

	/**
	 * Template context to merge with the template’s initial context and the
	 * context given when rendering the template.
	 */
	private final TemplateContext mergeContext;

	/**
	 * Creates a new notification.
	 *
	 * @param template
	 *            The template to render
	 */
	public TemplateNotification(Template template) {
		super();
		this.template = template;
		this.mergeContext = new TemplateContext(template.getInitialContext());
	}

	/**
	 * Creates a new notification.
	 *
	 * @param id
	 *            The ID of the notification
	 * @param template
	 *            The template to render
	 */
	public TemplateNotification(String id, Template template) {
		super(id);
		this.template = template;
		this.mergeContext = new TemplateContext(template.getInitialContext());
	}

	/**
	 * Creates a new notification.
	 *
	 * @param id
	 *            The ID of the notification
	 * @param creationTime
	 *            The creation time of the notification
	 * @param lastUpdatedTime
	 *            The time the notification was last udpated
	 * @param dismissable
	 *            {@code true} if this notification is dismissable by the user
	 * @param template
	 *            The template to render
	 */
	public TemplateNotification(String id, long creationTime, long lastUpdatedTime, boolean dismissable, Template template) {
		super(id, creationTime, lastUpdatedTime, dismissable);
		this.template = template;
		this.mergeContext = new TemplateContext(template.getInitialContext());
	}

	//
	// ACCESSORS
	//

	/**
	 * Returns the template that renders this notification.
	 *
	 * @return The template that renders this notification
	 */
	public TemplateContext getTemplateContext() {
		return new TemplateContext(template.getInitialContext());
	}

	/**
	 * Expose the used template to subclasses.
	 *
	 * @return The template
	 */
	protected Template getTemplate() {
		return template;
	}

	/**
	 * Sets a template variable.
	 *
	 * @param name
	 *            The name of the template variable
	 * @param value
	 *            The value of the template variable
	 * @return This template notification
	 */
	public TemplateNotification set(String name, Object value) {
		mergeContext.set(name, value);
		touch();
		return this;
	}

	/**
	 * Returns the template variable with the given name. The resolution is
	 * delegated to {@link TemplateContext#get(String)} and will use
	 * {@link Accessor}s, {@link Filter}s, and {@link Plugin}s registered with
	 * the notification’s template.
	 *
	 * @param name
	 *            The name of the template variable
	 * @return The value of the template variable
	 */
	public Object get(String name) {
		return mergeContext.get(name);
	}

	//
	// NOTIFICATION METHODS
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void render(Writer writer) throws IOException {
		template.render(mergeContext, writer);
	}

	//
	// PART METHODS
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void render(TemplateContext templateContext, Writer writer) throws TemplateException {
		template.render(new TemplateContext().mergeContext(templateContext).mergeContext(template.getInitialContext()).mergeContext(mergeContext), writer);
	}

}
