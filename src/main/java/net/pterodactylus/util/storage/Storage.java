/*
 * utils - Storage.java - Copyright © 2011–2019 David Roden
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

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.pterodactylus.util.io.Closer;
import net.pterodactylus.util.logging.Logging;
import net.pterodactylus.util.validation.Validation;

/**
 * Storage for {@link Storable}s. The storage persists the objects to a data
 * file, using a second index file to keep track of allocated blocks.
 *
 * @param <T>
 *            The type of the stored object
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Storage<T extends Storable> implements Closeable {

	/** The logger. */
	private static final Logger logger = Logging.getLogger(Storage.class);

	/** The internal block size. */
	private final int blockSize;

	/** Factory for {@link Storable}s. */
	private final Factory<T> factory;

	/** The index file. */
	private RandomAccessFile indexFile;

	/** The data file. */
	private RandomAccessFile dataFile;

	/** Lock for synchronization. */
	private final ReadWriteLock lock = new ReentrantReadWriteLock();

	/** The directory entries in on-disk order. */
	private final List<Allocation> directoryEntries = new ArrayList<Allocation>();

	/** Maps Storables’ IDs to directory indexes. */
	private final Map<Long, Integer> idDirectoryIndexes = new HashMap<Long, Integer>();

	/** Keeps track of empty directory entries. */
	private final BitSet emptyDirectoryEntries = new BitSet();

	/** Keeps track of allocated data blocks. */
	private final BitSet allocations = new BitSet();

	/** Whether the store is opened. */
	private boolean opened;

	/** The directory to store the files in. */
	private final File directory;

	/** The base name for the files. */
	private final String name;

	/**
	 * Creates a new storage with a default block size of 512 bytes.
	 *
	 * @param factory
	 *            The factory that creates the objects
	 * @param directory
	 *            The directory to store the files in
	 * @param name
	 *            The base name of the files
	 */
	public Storage(Factory<T> factory, File directory, String name) {
		this(512, factory, directory, name);
	}

	/**
	 * Creates a new storage.
	 *
	 * @param blockSize
	 *            The block size
	 * @param factory
	 *            The factory that creates the objects
	 * @param directory
	 *            The directory to store the files in
	 * @param name
	 *            The base name of the files
	 */
	public Storage(int blockSize, Factory<T> factory, File directory, String name) {
		Validation.begin().isNotNull("Factory", factory).isNotNull("Directory", directory).isNotNull("Name", name).check().isGreater("Block Size", blockSize, 0).check();
		this.blockSize = blockSize;
		this.factory = factory;
		this.directory = directory;
		this.name = name;
	}

	//
	// ACTIONS
	//

	/**
	 * Opens this storage.
	 *
	 * @throws StorageException
	 *             if the storage can not be opened
	 */
	public void open() throws StorageException {
		logger.log(Level.INFO, "[%s] Opening Storage…", name);
		lock.writeLock().lock();
		try {
			if (opened) {
				throw new IllegalStateException("Storage already opened.");
			}
			logger.log(Level.FINE, "[%s] Opening Data and Index Files…", name);
			try {
				indexFile = new RandomAccessFile(new File(directory, name + ".idx"), "rws");
				dataFile = new RandomAccessFile(new File(directory, name + ".dat"), "rws");
			} catch (FileNotFoundException fnfe1) {
				throw new StorageException("Could not create data and/or index files!", fnfe1);
			}
			logger.log(Level.FINE, "[%s] Files opened.", name);
			long indexLength = indexFile.length();
			if ((indexLength % 16) != 0) {
				throw new IOException("Invalid Index Length: " + indexLength);
			}
			emptyDirectoryEntries.clear();
			directoryEntries.clear();
			idDirectoryIndexes.clear();
			allocations.clear();
			logger.log(Level.FINE, "[%s] Reading " + (indexLength / 16) + " existing Directory Entries…", name);
			for (int directoryIndex = 0; directoryIndex < (indexLength / 16); ++directoryIndex) {
				byte[] allocationBuffer = new byte[16];
				indexFile.readFully(allocationBuffer);
				Allocation allocation = Allocation.FACTORY.restore(allocationBuffer);
				logger.log(Level.FINEST, "[%s] Read Allocation: %s", new Object[] { name, allocation });
				if ((allocation.getId() == 0) && (allocation.getPosition() == 0) && (allocation.getSize() == 0)) {
					emptyDirectoryEntries.set(directoryIndex);
					directoryEntries.add(null);
				} else {
					directoryEntries.add(allocation);
					idDirectoryIndexes.put(allocation.getId(), directoryIndex);
					allocations.set(allocation.getPosition(), allocation.getPosition() + getBlocks(allocation.getSize()));
				}
			}
			opened = true;
		} catch (IOException ioe1) {
			throw new StorageException("Could not open storage!", ioe1);
		} finally {
			lock.writeLock().unlock();
		}
		logger.log(Level.INFO, "[%s] Storage opened.", name);
	}

	/**
	 * Adds a storable to this storage.
	 *
	 * @param storable
	 *            The storable to store
	 * @throws StorageException
	 *             if a store error occurs
	 */
	public void add(T storable) throws StorageException {
		Validation.begin().isNotNull("Storable", storable).check();
		logger.log(Level.INFO, "[%s] Adding Storable %s…", new Object[] { name, storable });
		lock.writeLock().lock();
		try {
			if (!opened) {
				throw new IllegalStateException("Storage not opened!");
			}
			byte[] storableBytes = storable.getBuffer();
			int storableLength = storableBytes.length;
			int blocks = getBlocks(storableLength);
			int position = findFreeRegion(blocks);
			logger.log(Level.FINEST, "[%s] Will add Storable at %d, for %d blocks.", new Object[] { name, position, blocks });

			/* first, write data. */
			logger.log(Level.FINE, "[%s] Writing Storable Data…", name);
			allocations.set(position, position + blocks);
			if (dataFile.length() < (position * blockSize + storableLength)) {
				dataFile.setLength(position * blockSize + storableLength);
			}
			dataFile.seek(position * blockSize);
			dataFile.write(storableBytes);
			logger.log(Level.FINE, "[%s] Storable Data written.", name);

			/* now directory entry. */
			int oldIndex = -1;
			Allocation allocation = new Allocation(storable.getId(), position, storableLength);
			int directoryIndex = emptyDirectoryEntries.nextSetBit(0);
			if (directoryIndex == -1) {
				/* append. */
				directoryIndex = directoryEntries.size();
				directoryEntries.add(allocation);
				logger.log(Level.FINEST, "[%s] Appending to Directory, Entry %d…", new Object[] { name, directoryIndex });
			} else {
				directoryEntries.set(directoryIndex, allocation);
				emptyDirectoryEntries.clear(directoryIndex);
				logger.log(Level.FINEST, "[%s] Replacing Directory Entry %d…", new Object[] { name, directoryIndex });
			}
			if (idDirectoryIndexes.containsKey(storable.getId())) {
				oldIndex = idDirectoryIndexes.get(storable.getId());
				Allocation oldAllocation = directoryEntries.set(oldIndex, null);
				emptyDirectoryEntries.set(oldIndex);
				logger.log(Level.FINE, "[%s] Freeing Directory Index %d…", new Object[] { name, oldIndex });
				allocations.clear(oldAllocation.getPosition(), oldAllocation.getPosition() + getBlocks(oldAllocation.getSize()));
			}
			emptyDirectoryEntries.clear(directoryIndex);
			idDirectoryIndexes.put(storable.getId(), directoryIndex);

			/* now write directory to disk. */
			writeAllocation(directoryIndex, allocation);

			/* if an old index was deleted, wipe it. */
			if (oldIndex > -1) {
				writeAllocation(oldIndex, null);
			}
		} catch (IOException ioe1) {
			throw new StorageException("Could not add Storable: " + storable + "!", ioe1);
		} finally {
			lock.writeLock().unlock();
		}
		logger.log(Level.FINE, "[%s] Storable added.", name);
	}

	/**
	 * Returns the number of storables in this storage.
	 *
	 * @return The number of storables
	 */
	public int size() {
		lock.readLock().lock();
		try {
			if (!opened) {
				throw new IllegalStateException("Storage not opened!");
			}
			return directoryEntries.size() - emptyDirectoryEntries.cardinality();
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * Loads a storable.
	 *
	 * @param id
	 *            The ID of the storable to load
	 * @return The storable
	 * @throws StorageException
	 *             if the Storable could not be loaded
	 */
	public T load(long id) throws StorageException {
		logger.log(Level.INFO, "[%s] Loading Storable %d…", new Object[] { name, id });
		lock.readLock().lock();
		Allocation allocation;
		try {
			if (!opened) {
				throw new IllegalStateException("Storage not opened!");
			}
			Integer directoryIndex = idDirectoryIndexes.get(id);
			logger.log(Level.FINEST, "[%s] Directory Index: %d", new Object[] { name, directoryIndex });
			if (directoryIndex == null) {
				return null;
			}
			allocation = directoryEntries.get(directoryIndex);
			logger.log(Level.FINEST, "[%s] Allocation: %s", new Object[] { name, allocation });
		} finally {
			lock.readLock().unlock();
		}
		byte[] buffer = new byte[allocation.getSize()];
		lock.writeLock().lock();
		try {
			logger.log(Level.FINEST, "[%s] Reading %d Bytes…", new Object[] { name, allocation.getSize() });
			dataFile.seek(allocation.getPosition() * blockSize);
			dataFile.readFully(buffer);
		} catch (IOException ioe1) {
			throw new StorageException("Could not load Storable!", ioe1);
		} finally {
			lock.writeLock().unlock();
		}
		logger.log(Level.INFO, "[%s] Read Storable, restoring from Factory…", name);
		return factory.restore(buffer);
	}

	/**
	 * Returns the size of the directory. Note that this can be larger than
	 * {@link #size()} because it also includes empty directory entries!
	 *
	 * @return The size of the directory
	 */
	public int getDirectorySize() {
		lock.readLock().lock();
		try {
			if (!opened) {
				throw new IllegalStateException("Storage not opened!");
			}
			return directoryEntries.size();
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * Returns the allocation at the given directory index.
	 *
	 * @param directoryIndex
	 *            The directory index
	 * @return The allocation at the given index, or {@code null} if there is no
	 *         allocation at the given index
	 */
	public Allocation getAllocation(int directoryIndex) {
		lock.readLock().lock();
		Validation.begin().isGreater("Directory Index", directoryIndex, -1).isLess("Directory Index", directoryIndex, directoryEntries.size()).check();
		try {
			if (!opened) {
				throw new IllegalStateException("Storage not opened!");
			}
			return directoryEntries.get(directoryIndex);
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * Removes a storable.
	 *
	 * @param storable
	 *            The storable to remove
	 * @throws StorageException
	 *             if the index file can not be written to
	 */
	public void remove(T storable) throws StorageException {
		Validation.begin().isNotNull("Storable", storable).check();
		remove(storable.getId());
	}

	/**
	 * Removes the storable with the given ID.
	 *
	 * @param id
	 *            The ID of the storable to remove
	 * @throws StorageException
	 *             if the index file can not be written to
	 */
	public void remove(long id) throws StorageException {
		logger.log(Level.INFO, "[%s] Removing Storable %d…", new Object[] { name, id });
		lock.writeLock().lock();
		try {
			if (!opened) {
				throw new IllegalStateException("Storage not opened!");
			}
			Integer directoryIndex = idDirectoryIndexes.remove(id);
			logger.log(Level.FINEST, "[%s] Directory Index: %s", new Object[] { name, directoryIndex });
			if (directoryIndex == null) {
				return;
			}
			Allocation allocation = directoryEntries.set(directoryIndex, null);
			emptyDirectoryEntries.set(directoryIndex);
			allocations.clear(allocation.getPosition(), allocation.getPosition() + getBlocks(allocation.getSize()));
			logger.log(Level.FINE, "[%s] Clearing Directory Index %d…", new Object[] { name, directoryIndex });
			indexFile.seek(directoryIndex * 16);
			indexFile.write(new byte[16]);
		} catch (IOException ioe1) {
			throw new StorageException("Could not write to index file!", ioe1);
		} finally {
			lock.writeLock().unlock();
		}
		logger.log(Level.FINE, "[%s] Storable removed.", name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() {
		logger.log(Level.INFO, "[%s] Closing Storage…", name);
		lock.writeLock().lock();
		try {
			if (!opened) {
				throw new IllegalStateException("Storage not opened!");
			}
			Closer.close(indexFile);
			Closer.close(dataFile);
			opened = false;
		} finally {
			lock.writeLock().unlock();
		}
		logger.log(Level.INFO, "[%s] Storage closed.", name);
	}

	/**
	 * Compacts the directory. This may reduce the size of the index file but
	 * will not touch the data file.
	 *
	 * @throws StorageException
	 *             if the index file can not be compacted
	 */
	public void compact() throws StorageException {
		logger.log(Level.INFO, "[%s] Compacting Storage…", name);
		lock.writeLock().lock();
		try {
			if (opened) {
				throw new IllegalStateException("Storage is opened!");
			}

			/* open index file and check length. */
			try {
				indexFile = new RandomAccessFile(new File(directory, name + ".idx"), "rws");
			} catch (FileNotFoundException fnfe1) {
				throw new StorageException("Could not create data and/or index files!", fnfe1);
			}
			long indexLength = indexFile.length();
			if ((indexLength % 16) != 0) {
				throw new StorageException("Invalid Index Length: " + indexLength);
			}

			/* first, read the directory entries. */
			logger.log(Level.FINE, "[%s] Reading Directory…", name);
			directoryEntries.clear();
			for (int directoryIndex = 0; directoryIndex < (indexLength / 16); ++directoryIndex) {
				byte[] allocationBuffer = new byte[16];
				indexFile.readFully(allocationBuffer);
				Allocation allocation = Allocation.FACTORY.restore(allocationBuffer);
				if ((allocation.getId() == 0) && (allocation.getPosition() == 0) && (allocation.getSize() == 0)) {
					directoryEntries.add(null);
				} else {
					directoryEntries.add(allocation);
				}
			}
			logger.log(Level.FINE, "[%s] Read %d Directory Entries.", new Object[] { name, directoryEntries.size() });

			/* now write an index file without the null values. */
			int directoryIndex = 0;
			indexFile.seek(0);
			for (Allocation allocation : directoryEntries) {
				if (allocation == null) {
					continue;
				}
				writeAllocation(directoryIndex++, allocation);
			}
			logger.log(Level.FINE, "[%s] Wrote %d Directory Entries.", new Object[] { name, directoryIndex });

			/* truncate the index file. */
			logger.log(Level.FINE, "[%s] Truncating Directory File…", name);
			indexFile.setLength(indexFile.getFilePointer());
		} catch (IOException ioe1) {
			throw new StorageException("Could not compact index!", ioe1);
		} finally {
			Closer.close(indexFile);
			lock.writeLock().unlock();
		}
		logger.log(Level.INFO, "[%s] Storage compacted.", name);
	}

	//
	// PRIVATE METHODS
	//

	/**
	 * Locates the first free region that is large enough to hold the given
	 * number of blocks sequentially.
	 *
	 * @param blocks
	 *            The number of blocks
	 * @return The index at which the blocks can be stored
	 */
	private int findFreeRegion(int blocks) {
		int currentBlock = -1;
		while (true) {
			int nextUsedBlock = allocations.nextSetBit(currentBlock + 1);
			if ((nextUsedBlock == -1) || ((nextUsedBlock - currentBlock - 1) >= blocks)) {
				return currentBlock + 1;
			}
			currentBlock = nextUsedBlock;
		}
	}

	/**
	 * Returns the number of blocks for the given number of bytes.
	 *
	 * @param size
	 *            The size of the data
	 * @return The numbers of blocks
	 */
	private int getBlocks(long size) {
		if (size == 0) {
			return 1;
		}
		return (int) ((size - 1) / blockSize + 1);
	}

	/**
	 * Writes the given allocation to the given directory index in the index
	 * file. This will also work with {@code null} allocations which will result
	 * in an {@link Allocation}-sized byte array filled with {@code 0}’s to be
	 * written. This method requires that the index file has been opened for
	 * writing and the write lock of {@link #lock} has been acquired!
	 *
	 * @param directoryIndex
	 *            The index in the directory
	 * @param allocation
	 *            The allocation to write, or {@code null} to clear the
	 *            directory entry
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	private void writeAllocation(int directoryIndex, Allocation allocation) throws IOException {
		indexFile.seek(directoryIndex * 16);
		if (allocation == null) {
			indexFile.write(new byte[16]);
		} else {
			indexFile.write(allocation.getBuffer());
		}
	}

}
