package net.pterodactylus.util.telnet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.pterodactylus.util.io.Closer;
import net.pterodactylus.util.logging.Logging;
import net.pterodactylus.util.service.AbstractService;
import net.pterodactylus.util.telnet.Command.Reply;
import net.pterodactylus.util.text.StringEscaper;
import net.pterodactylus.util.text.TextException;

/**
 * Handles a single client connection.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
class ControlConnection extends AbstractService {

	/** The logger. */
	private static final Logger logger = Logging.getLogger(ControlConnection.class.getName());

	/** The line break. */
	private static final String LINEFEED = "\r\n";

	/** The client’s input stream. */
	private final InputStream clientInputStream;

	/** The client’s output stream. */
	private final OutputStream clientOutputStream;

	/** Mapping from command names to commands. */
	Map<String, Command> commands = new HashMap<String, Command>();

	/** Mapping from internal command names to commands. */
	Map<String, Command> internalCommands = new HashMap<String, Command>();

	/**
	 * Creates a new connection handler for a client on the given socket.
	 *
	 * @param clientInputStream
	 *            The client input stream
	 * @param clientOutputStream
	 *            The client output stream
	 */
	public ControlConnection(InputStream clientInputStream, OutputStream clientOutputStream) {
		this.clientInputStream = clientInputStream;
		this.clientOutputStream = clientOutputStream;
		internalCommands.put("quit", new QuitCommand());
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serviceRun() {
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
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void serviceStop() {
		Closer.close(clientInputStream);
		Closer.close(clientOutputStream);
	}

	//
	// PRIVATE METHODS
	//

	/**
	 * Searches both internal and user commands for a command. A command must
	 * have a name that equals or starts with the given name to be a match.
	 *
	 * @param name
	 *            The name of the command
	 * @return All found commands
	 */
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
	 * <code>reply</code> may be <code>null</code> in which case an appropriate
	 * error message is written.
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
