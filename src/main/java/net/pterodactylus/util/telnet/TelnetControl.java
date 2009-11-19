/*
 * utils - TelnetControl.java - Copyright © 2008-2009 David Roden
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.pterodactylus.util.io.Closer;
import net.pterodactylus.util.service.AbstractService;
import net.pterodactylus.util.telnet.Command.Reply;
import net.pterodactylus.util.text.StringEscaper;
import net.pterodactylus.util.text.TextException;
import net.pterodactylus.util.thread.DumpingThreadFactory;

/**
 * TODO
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class TelnetControl extends AbstractService {

	/** The logger. */
	private static final Logger logger = Logger.getLogger(TelnetControl.class.getName());

	/** The line break. */
	private static final String LINEFEED = "\r\n";

	/** Factory for connection handler threads. */
	private ThreadFactory threadFactory = new DumpingThreadFactory("TelnetControl ");

	/** The server socket. */
	private ServerSocket serverSocket;

	/** The port to listen on. */
	private int listenPort = 20013;

	/** Mapping from command names to commands. */
	private Map<String, Command> commands = new HashMap<String, Command>();

	/** Mapping from internal command names to commands. */
	private Map<String, Command> internalCommands = new HashMap<String, Command>();

	/**
	 * Creates a new telnet control.
	 */
	public TelnetControl() {
		super("Telnet Control");
		internalCommands.put("quit", new QuitCommand());
	}

	//
	// ACCESSORS
	//

	/**
	 * Sets the port to listen on.
	 *
	 * @param listenPort
	 *            The port to listen on
	 */
	public void setListenPort(int listenPort) {
		this.listenPort = listenPort;
	}

	/**
	 * Adds the given command to this control.
	 *
	 * @param command
	 *            The command to add
	 */
	public void addCommand(Command command) {
		commands.put(command.getName().toLowerCase(), command);
		internalCommands.put("help", new HelpCommand(commands.values()));
	}

	//
	// SERVICE METHODS
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void serviceRun() {
		logger.log(Level.INFO, "starting telnet control main loop");
		try {
			serverSocket = new ServerSocket(listenPort);
		} catch (IOException ioe1) {
			logger.log(Level.SEVERE, "could not create server socket on port " + listenPort, ioe1);
			return;
		}
		while (!shouldStop()) {
			try {
				Socket clientSocket = serverSocket.accept();
				logger.log(Level.INFO, "acception client connection on " + clientSocket.getRemoteSocketAddress());
				threadFactory.newThread(new ConnectionHandler(clientSocket)).start();
			} catch (IOException ioe1) {
				if (!shouldStop()) {
					logger.log(Level.WARNING, "could not accept client connection", ioe1);
				}
			}
		}
		logger.log(Level.INFO, "stopped telnet control main loop.");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void serviceStop() {
		Closer.close(serverSocket);
	}

	/**
	 * Handles a single client connection.
	 *
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	private class ConnectionHandler implements Runnable {

		/** The client’s socket. */
		private final Socket clientSocket;

		/** The client’s input stream. */
		private final InputStream clientInputStream;

		/** The client’s output stream. */
		private final OutputStream clientOutputStream;

		/**
		 * Creates a new connection handler for a client on the given socket.
		 *
		 * @param clientSocket
		 *            The client’s socket
		 * @throws IOException
		 *             if an I/O error occurs
		 */
		public ConnectionHandler(Socket clientSocket) throws IOException {
			this.clientSocket = clientSocket;
			clientInputStream = clientSocket.getInputStream();
			clientOutputStream = clientSocket.getOutputStream();
		}

		/**
		 * {@inheritDoc}
		 */
		@SuppressWarnings("synthetic-access")
		public void run() {
			InputStreamReader inputStreamReader = null;
			BufferedReader bufferedReader = null;
			PrintWriter printWriter = null;
			try {
				inputStreamReader = new InputStreamReader(clientInputStream);
				bufferedReader = new BufferedReader(inputStreamReader);
				printWriter = new PrintWriter(clientOutputStream);
				String line;
				boolean finished = false;
				while (!finished && ((line = bufferedReader.readLine()) != null)) {
					line = line.trim();
					if (line.length() == 0) {
						continue;
					}
					List<String> words;
					try {
						words = StringEscaper.parseLine(line);
					} catch (TextException te1) {
						writeReply(printWriter, new Reply(Reply.BAD_REQUEST).addLine("Syntax error."));
						continue;
					}
					if (words.isEmpty()) {
						continue;
					}
					String commandName = words.remove(0).toLowerCase();
					List<Command> foundCommands = findCommand(commandName);
					if (foundCommands.isEmpty()) {
						writeReply(printWriter, new Reply(Reply.NOT_FOUND).addLine("Command not found."));
					} else if (foundCommands.size() == 1) {
						Command command = foundCommands.get(0);
						try {
							Reply commandReply = command.execute(words);
							writeReply(printWriter, commandReply);
						} catch (IOException ioe1) {
							throw ioe1;
						} catch (Throwable t1) {
							writeReply(printWriter, new Reply(Reply.INTERNAL_SERVER_ERROR).addLine("Internal server error: " + t1.getMessage()));
						}
						if (command instanceof QuitCommand) {
							finished = true;
						}
					} else {
						Reply reply = new Reply(Reply.MULTIPLE_CHOICES, "Multiple choices found:");
						for (Command command : foundCommands) {
							reply.addLine(command.getName());
						}
						writeReply(printWriter, reply);
					}
				}
			} catch (IOException ioe1) {
				logger.log(Level.INFO, "could not handle connection", ioe1);
			} finally {
				Closer.close(printWriter);
				Closer.close(bufferedReader);
				Closer.close(inputStreamReader);
				Closer.close(clientSocket);
			}
		}

		//
		// PRIVATE METHODS
		//

		/**
		 * Searches both internal and user commands for a command. A command
		 * must have a name that equals or starts with the given name to be a
		 * match.
		 *
		 * @param name
		 *            The name of the command
		 * @return All found commands
		 */
		@SuppressWarnings("synthetic-access")
		private List<Command> findCommand(String name) {
			List<Command> foundCommands = new ArrayList<Command>();
			for (Command command : internalCommands.values()) {
				if (command.getName().toLowerCase().startsWith(name.toLowerCase())) {
					foundCommands.add(command);
				}
			}
			for (Command command : commands.values()) {
				if (command.getName().toLowerCase().startsWith(name.toLowerCase())) {
					foundCommands.add(command);
				}
			}
			return foundCommands;
		}

		/**
		 * Writes the given reply to the client’s output stream. The
		 * <code>reply</code> may be <code>null</code> in which case an
		 * appropriate error message is written.
		 *
		 * @param printWriter
		 *            The output stream writer
		 * @param reply
		 *            The reply to send
		 * @throws IOException
		 *             if an I/O error occurs
		 */
		private void writeReply(PrintWriter printWriter, Reply reply) throws IOException {
			if (reply == null) {
				printWriter.write("500 Internal server error." + LINEFEED);
				printWriter.flush();
				return;
			}
			int status = reply.getStatus();
			List<String> lines = reply.getLines();
			for (int lineIndex = 0, lineCount = lines.size(); lineIndex < lineCount; lineIndex++) {
				printWriter.write(status + ((lineIndex < (lineCount - 1)) ? "-" : " ") + lines.get(lineIndex) + LINEFEED);
			}
			if (lines.size() == 0) {
				printWriter.write("200 OK." + LINEFEED);
			}
			printWriter.flush();
		}

	}

	//
	// VM ENTRY
	//

	/**
	 * VM entry point for testing.
	 *
	 * @param arguments
	 *            Command-line arguments
	 * @throws InterruptedException
	 *             if {@link Thread#sleep(long)} is interrupted
	 */
	public static void main(String... arguments) throws InterruptedException {
		TelnetControl telnetControl = new TelnetControl();
		telnetControl.init();
		telnetControl.addCommand(new MemoryCommand());
		telnetControl.addCommand(new GarbageCollectionCommand());
		telnetControl.start();
		Thread.sleep(120 * 1000);
	}

}
