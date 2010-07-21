/*
 * utils - HelpCommand - Copyright © 2008-2009 David Roden
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

import java.util.Collection;
import java.util.List;

/**
 * Command that outputs help information about commands.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class HelpCommand extends AbstractCommand {

	/** The list of commands to show help for. */
	private final Collection<Command> commands;

	/**
	 * Creates a new HELP command.
	 *
	 * @param commands
	 *            The commands to show help about
	 */
	public HelpCommand(Collection<Command> commands) {
		super("HELP", "outputs help of all commands");
		this.commands = commands;
	}

	//
	// PRIVATE METHODS
	//

	//
	// INTERFACE Command
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Reply execute(List<String> parameters) {
		Reply reply = new Reply(200);

		if (parameters.isEmpty()) {
			for (Command command : commands) {
				reply.addLine(command.getName().toUpperCase() + ": " + command.getBriefDescription());
			}
		} else {
			String commandName = parameters.get(0).toLowerCase();
			for (Command command : commands) {
				if (command.getName().toLowerCase().startsWith(commandName)) {
					reply.addLine(command.getName().toUpperCase() + ": " + command.getBriefDescription());
					for (String detailDescription : command.getDetailedDescription()) {
						reply.addLine("  " + detailDescription);
					}
				}
			}
		}

		return reply;
	}

}
