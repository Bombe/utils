/*
 * utils - AbstractCommand.java - Copyright © 2008-2009 David Roden
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

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base implementation of a {@link Command}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public abstract class AbstractCommand implements Command {

	/** The name of this command. */
	private final String name;

	/** The brief description of this command. */
	private final String briefDescription;

	/** The detailed description of this command. */
	private final List<String> detailedDescription = new ArrayList<String>();

	/**
	 * Creates a new command with the given name and description.
	 *
	 * @param name
	 *            The name of the command
	 * @param briefDescription
	 *            The brief description of this command.
	 */
	protected AbstractCommand(String name, String briefDescription) {
		this.name = name;
		this.briefDescription = briefDescription;
	}

	/**
	 * Creates a new command with the given name, brief description and detailed
	 * description.
	 *
	 * @param name
	 *            The name of the command
	 * @param briefDescription
	 *            The brief description of this command.
	 * @param detailedDescriptions
	 *            The detailed descriptions of this command
	 */
	protected AbstractCommand(String name, String briefDescription, String... detailedDescriptions) {
		this.name = name;
		this.briefDescription = briefDescription;
		for (String detailedDescription : detailedDescriptions) {
			this.detailedDescription.add(detailedDescription);
		}
	}

	/**
	 * Creates a new command with the given name, brief description and detailed
	 * description.
	 *
	 * @param name
	 *            The name of the command
	 * @param briefDescription
	 *            The brief description of this command.
	 * @param detailedDescription
	 *            The detailed description of this command
	 */
	protected AbstractCommand(String name, String briefDescription, List<String> detailedDescription) {
		this(name, briefDescription);
		this.detailedDescription.addAll(detailedDescription);
	}

	//
	// INTERFACE Command
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getBriefDescription() {
		return briefDescription;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getDetailedDescription() {
		return detailedDescription;
	}

}
