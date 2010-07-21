/*
 * utils - VersionCommand.java - Copyright © 2008-2009 David Roden
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

package net.pterodactylus.util.telnet;

import java.util.List;

/**
 * Replies with the name of the application and the version number.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class VersionCommand extends AbstractCommand {

	/** The name of the application. */
	private final String application;

	/** The version of the application. */
	private final String version;

	/**
	 * Creates a new version command.
	 *
	 * @param application
	 *            The name of the application
	 * @param version
	 *            The version of the application
	 */
	public VersionCommand(String application, String version) {
		super("VERSION", "Shows version information about " + application + ".");
		this.application = application;
		this.version = version;
	}

	/**
	 * @see net.pterodactylus.util.telnet.Command#execute(java.util.List)
	 */
	@Override
	public Reply execute(List<String> parameters) {
		return new Reply(200, application + " " + version);
	}

}
