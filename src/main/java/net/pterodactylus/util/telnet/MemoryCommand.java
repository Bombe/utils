/*
 * utils - MemoryCommand.java - Copyright © 2008-2009 David Roden
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

import net.pterodactylus.util.number.Digits;
import net.pterodactylus.util.number.SI;

/**
 * Command that outputs some memory statistics.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class MemoryCommand extends AbstractCommand {

	/**
	 * Creates a new memory command.
	 */
	public MemoryCommand() {
		super("MEMORY", "Shows memory statistics.");
	}

	/**
	 * @see net.pterodactylus.util.telnet.Command#execute(java.util.List)
	 */
	@Override
	public Reply execute(List<String> parameters) {
		long freeMemory = Runtime.getRuntime().freeMemory();
		long totalMemory = Runtime.getRuntime().totalMemory();
		long maxMemory = Runtime.getRuntime().maxMemory();
		long usedMemory = totalMemory - freeMemory;
		List<String> lines = new ArrayList<String>();
		lines.add("Used Memory: " + SI.format(usedMemory, 1, true, true) + "B");
		lines.add("Reversed Memory: " + SI.format(totalMemory, 1, true, true) + "B, " + Digits.formatFractions(usedMemory * 100.0 / totalMemory, 1, false) + "% used");
		lines.add("Maximum Memory: " + SI.format(maxMemory, 1, true, true) + "B, " + Digits.formatFractions(usedMemory * 100.0 / maxMemory, 1, false) + "% used");
		return new Reply(200, lines);
	}

}
