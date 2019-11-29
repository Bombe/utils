/*
 * utils - StorageTest.java - Copyright © 2011–2019 David Roden
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

import java.io.File;
import java.io.FilenameFilter;

import junit.framework.TestCase;

/**
 * Test case for {@link Storage}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class StorageTest extends TestCase {

	/** The directory in which to store the files. */
	private File directory;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void setUp() throws Exception {
		directory = new File(System.getProperty("java.io.tmpdir"), "storage-test.dir");
		directory.mkdirs();
		for (File file : directory.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".dat") || name.endsWith(".idx");
			}
		})) {
			file.delete();
		}
	}

	/**
	 * Tests {@link Storage} by creating a new Storage,
	 * {@link Storage#add(Storable) adding data}, adding more data,
	 * {@link Storage#remove(long) removing data}, and {@link Storage#compact()
	 * compacting the directory}, checking the {@link Storage#size() number of
	 * stored objects} and the {@link Storage#getDirectorySize() number of
	 * directory entries} after every action.
	 *
	 * @throws StorageException
	 */
	@SuppressWarnings("synthetic-access")
	public void testSimpleStorage() throws StorageException {
		Storage<Data> storage;
		Data data;

		storage = new Storage<Data>(new DataFactory(), directory, "data");
		data = new Data(0x12345678);
		try {
			storage.add(data);
			fail("exception expected");
		} catch (IllegalStateException ise1) {
			/* okay. */
		}

		storage.open();
		assertEquals(0, storage.size());
		assertEquals(0, storage.getDirectorySize());
		storage.add(data);
		assertEquals(1, storage.size());
		assertEquals(1, storage.getDirectorySize());
		storage.close();

		storage.open();
		assertEquals(1, storage.size());
		assertEquals(1, storage.getDirectorySize());
		data = storage.load(0x12345678);
		assertNotNull(data);
		storage.close();

		data = new Data(0x23456789);
		storage.open();
		assertEquals(1, storage.size());
		assertEquals(1, storage.getDirectorySize());
		storage.add(data);
		assertEquals(2, storage.size());
		assertEquals(2, storage.getDirectorySize());
		storage.close();

		storage.open();
		assertEquals(2, storage.size());
		assertEquals(2, storage.getDirectorySize());
		storage.remove(0x12345678);
		assertEquals(1, storage.size());
		assertEquals(2, storage.getDirectorySize());
		storage.close();

		storage.open();
		assertEquals(1, storage.size());
		assertEquals(2, storage.getDirectorySize());
		storage.remove(0x01234567);
		assertEquals(1, storage.size());
		assertEquals(2, storage.getDirectorySize());
		storage.close();

		storage.open();
		assertEquals(1, storage.size());
		assertEquals(2, storage.getDirectorySize());
		storage.remove(0x23456789);
		assertEquals(0, storage.size());
		assertEquals(2, storage.getDirectorySize());
		storage.close();

		storage.open();
		assertEquals(0, storage.size());
		assertEquals(2, storage.getDirectorySize());
		storage.close();

		storage.compact();
		storage.open();
		assertEquals(0, storage.size());
		assertEquals(0, storage.getDirectorySize());
		storage.close();
	}

	/**
	 * Minimal {@link Storable} implementation that only stores an ID.
	 *
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	private static class Data implements Storable {

		/** The ID of the data. */
		private final long id;

		/**
		 * Creates a new data container with the given ID.
		 *
		 * @param id
		 *            The ID of the data
		 */
		public Data(long id) {
			this.id = id;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public long getId() {
			return id;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public byte[] getBuffer() throws StorageException {
			byte[] buffer = new byte[8];
			Storable.Utils.putLong(id, buffer, 0);
			return buffer;
		}

	}

	/**
	 * {@link Factory} implementation that can create {@link Data} objects.
	 *
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	private static class DataFactory implements Factory<Data> {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Data restore(byte[] buffer) {
			long id = Storable.Utils.getLong(buffer, 0);
			return new Data(id);
		}

	}

}
