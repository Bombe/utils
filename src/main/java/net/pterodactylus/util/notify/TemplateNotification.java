/*
 * utils - TemplateNotification.java - Copyright © 2010 David Roden
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
import java.io.StringWriter;
import java.io.Writer;

import net.pterodactylus.util.io.Closer;
import net.pterodactylus.util.template.Template;

/**
 * {@link Template}-based implementation of a {@link Notification}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class TemplateNotification extends AbstractNotification {

	/** The template to render. */
	private final Template template;

	/**
	 * Creates a new notification.
	 *
	 * @param template
	 *            The template to render
	 */
	public TemplateNotification(Template template) {
		super();
		this.template = template;
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
	}

	//
	// ACCESSORS
	//

	/**
	 * Returns the template that renders this notification.
	 *
	 * @return The template that renders this notification
	 */
	public Template getTemplate() {
		return template;
	}

	//
	// NOTIFICATION METHODS
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void render(Writer writer) throws IOException {
		template.render(writer);
	}

	//
	// OBJECT METHODS
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringWriter stringWriter = new StringWriter();
		try {
			render(stringWriter);
		} catch (IOException ioe1) {
			/* A StringWriter never throws. */
		} finally {
			Closer.close(stringWriter);
		}
		return stringWriter.toString();
	}

}
