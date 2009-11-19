/*
 * utils - Command.java - Copyright © 2008-2009 David Roden
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
import java.util.Arrays;
import java.util.List;

/**
 * Basic structure of a command that can be sent to a {@link TelnetControl}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public interface Command {

	/**
	 * Returns the name of this command. The name is parsed case-insensitively.
	 *
	 * @return The name of this command
	 */
	public String getName();

	/**
	 * Returns a brief description of this command.
	 *
	 * @return The brief description of this command
	 */
	public String getBriefDescription();

	/**
	 * Returns a detailed description of this command.
	 *
	 * @return The detailed description of this command
	 */
	public List<String> getDetailedDescription();

	/**
	 * Executes the command.
	 *
	 * @param parameters
	 *            The parameter of this command
	 * @return The reply of the command
	 */
	public Reply execute(List<String> parameters);

	/**
	 * A reply to a {@link Command}.
	 *
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	public class Reply {

		/** The “OK” status. */
		public static final int OK = 200;

		/** The “multiple choices” status. */
		public static final int MULTIPLE_CHOICES = 300;

		/** The “bad request” status. */
		public static final int BAD_REQUEST = 400;

		/** The “not found” status. */
		public static final int NOT_FOUND = 404;

		/** The “internal server error” status. */
		public static final int INTERNAL_SERVER_ERROR = 500;

		/** The status of the reply. */
		private final int status;

		/** The lines that make up the reply. */
		private final List<String> lines = new ArrayList<String>();

		/**
		 * Creates a new reply with the given status.
		 *
		 * @param status
		 *            The status of the reply
		 */
		public Reply(int status) {
			this.status = status;
		}

		/**
		 * Creates a new reply with the given status and line.
		 *
		 * @param status
		 *            The status of the reply
		 * @param line
		 *            The line of the reply
		 */
		public Reply(int status, String line) {
			this(status, new String[] { line });
		}

		/**
		 * Creates a new reply with the given status and lines.
		 *
		 * @param status
		 *            The status of the reply
		 * @param lines
		 *            The lines of the reply
		 */
		public Reply(int status, String... lines) {
			this(status, Arrays.asList(lines));
		}

		/**
		 * Creates a new reply with the given status and lines.
		 *
		 * @param status
		 *            The status of the reply
		 * @param lines
		 *            The lines of the reply
		 */
		public Reply(int status, List<String> lines) {
			this.status = status;
			this.lines.addAll(lines);
		}

		/**
		 * Adds a line to the reply.
		 *
		 * @param line
		 *            The line to add
		 * @return This reply (for method invocation chaining)
		 */
		public Reply addLine(String line) {
			lines.add(line);
			return this;
		}

		/**
		 * Returns the status of this command.
		 *
		 * @return The status of this command
		 */
		public int getStatus() {
			return status;
		}

		/**
		 * Returns the lines that make up this reply.
		 *
		 * @return The lines of this reply
		 */
		public List<String> getLines() {
			return lines;
		}

	}

}
