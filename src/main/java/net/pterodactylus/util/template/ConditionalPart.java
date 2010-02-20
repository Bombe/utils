/*
 * utils - ConditionalPart.java - Copyright © 2010 David Roden
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

import java.io.IOException;
import java.io.Writer;

/**
 * {@link ContainerPart} implementation that determines at render time whether
 * it should be rendered or not.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
class ConditionalPart extends ContainerPart {

	/** The condition. */
	private final Condition condition;

	/**
	 * Creates a new conditional part.
	 *
	 * @param condition
	 *            The condition
	 */
	public ConditionalPart(Condition condition) {
		super();
		this.condition = condition;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void render(DataProvider dataProvider, Writer writer) throws IOException, TemplateException {
		if (condition.isAllowed(dataProvider)) {
			super.render(dataProvider, writer);
		}
	}

	/**
	 * Condition that decides whether a {@link ConditionalPart} is rendered at
	 * render time.
	 *
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	public interface Condition {

		/**
		 * Returns whether the condition is fulfilled.
		 *
		 * @param dataProvider
		 *            The data provider
		 * @return {@code true} if the condition is fulfilled, {@code false}
		 *         otherwise
		 * @throws TemplateException
		 *             if a template variable can not be parsed or evaluated
		 */
		public boolean isAllowed(DataProvider dataProvider) throws TemplateException;

	}

}
