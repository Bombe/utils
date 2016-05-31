/*
 * utils - FormattedMessage.java - Copyright © 2011–2016 David Roden
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

package net.pterodactylus.util.text;

/**
 * Container for a formatted message, consisting of a text format and
 * parameters. It can be used to encapsulate several parameters (e.g. in log
 * messages) but avoids actually formatting them until {@link #toString()} is
 * called.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class FormattedMessage {

	/** The format of the message. */
	private final String format;

	/** The parameters of the message. */
	private final Object[] parameters;

	/**
	 * Creates a new formatted message.
	 *
	 * @param format
	 *            The format of the message
	 * @param parameters
	 *            The parameters of the message
	 */
	public FormattedMessage(String format, Object... parameters) {
		this.format = format;
		this.parameters = parameters;
	}

	//
	// OBJECT METHODS
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return String.format(format, parameters);
	}

}
