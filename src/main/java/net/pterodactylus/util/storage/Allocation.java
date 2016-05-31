/*
 * utils - Allocation.java - Copyright © 2011–2016 David Roden
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

package net.pterodactylus.util.storage;

/**
 * An allocation is stored in the allocation table of a {@link Storage}. It
 * keeps track of which {@link Storable} is stored in which position of the data
 * file and how long it is.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Allocation implements Storable {

	/** Factory to create {@link Allocation}s from {@code byte[]}s. */
	public static final Factory<Allocation> FACTORY = new AllocationFactory();

	/** The ID of the {@link Storable}. */
	private final long id;

	/** The block position of the data. */
	private final int position;

	/** The size of the data (in bytes). */
	private final int size;

	/**
	 * Creates a new allocation table entry.
	 *
	 * @param id
	 *            The ID of the {@link Storable}
	 * @param position
	 *            The block position of the data
	 * @param size
	 *            The size of the data (in bytes)
	 */
	public Allocation(long id, int position, int size) {
		this.id = id;
		this.position = position;
		this.size = size;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getId() {
		return id;
	}

	/**
	 * Returns the block position of the data.
	 *
	 * @return The block position of the data
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * Returns the size of the data.
	 *
	 * @return The size of the data (in bytes)
	 */
	public int getSize() {
		return size;
	}

	//
	// INTERFACE Storable
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] getBuffer() {
		byte[] buffer = new byte[16];
		Storable.Utils.putLong(id, buffer, 0);
		Storable.Utils.putInt(position, buffer, 8);
		Storable.Utils.putInt(size, buffer, 12);
		return buffer;
	}

	//
	// OBJECT METHODS
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return String.format("%s[id=%d,position=%d,size=%d]", getClass().getName(), id, position, size);
	}

	/**
	 * {@link Factory} implementation that can create {@link Allocation}
	 * objects.
	 *
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	public static class AllocationFactory implements Factory<Allocation> {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Allocation restore(byte[] buffer) {
			return new Allocation(Storable.Utils.getLong(buffer, 0), Storable.Utils.getInt(buffer, 8), Storable.Utils.getInt(buffer, 12));
		}

	}

}
