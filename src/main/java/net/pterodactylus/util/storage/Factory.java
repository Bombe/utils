package net.pterodactylus.util.storage;

/**
 * Interface for factory classes that can create objects from a byte array.
 *
 * @param <T>
 *            The type of the object to create
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public interface Factory<T> {

	/**
	 * Creates an object from the given byte array.
	 *
	 * @param buffer
	 *            The byte array with the object’s contents
	 * @return The object
	 */
	public T restore(byte[] buffer);

}