package net.pterodactylus.util.telnet;

import java.util.List;

/**
 * Command that performs a garbage collection.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class GarbageCollectionCommand extends AbstractCommand {

	/**
	 * Creates a new garbage collection command.
	 */
	public GarbageCollectionCommand() {
		super("GC", "Performs a garbage collection.");
	}

	/**
	 * @see net.pterodactylus.util.telnet.Command#execute(java.util.List)
	 */
	@Override
	public Reply execute(List<String> parameters) {
		System.gc();
		return new Reply(200, "Garbage Collection suggested.");
	}

}