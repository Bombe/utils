/*
 * utils - LoopPart.java - Copyright © 2010 David Roden
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

package net.pterodactylus.util.template;

import java.io.Writer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link Part} implementation that loops over a {@link Collection}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
class LoopPart extends ContainerPart {

	/** Accessor for {@link LoopStructure}s. */
	@SuppressWarnings("synthetic-access")
	private final Accessor LOOP_STRUCTURE_ACCESSOR = new LoopStructureAccessor();

	/** The name of the collection to loop over. */
	private final String collectionName;

	/** The name under which to store the current item. */
	private final String itemName;

	/** The name under which to store the loop structure. */
	private final String loopName;

	/**
	 * Creates a new loop part.
	 *
	 * @param collectionName
	 *            The name of the collection
	 * @param itemName
	 *            The name under which to store the current item
	 */
	public LoopPart(String collectionName, String itemName) {
		this(collectionName, itemName, "loop");
	}

	/**
	 * Creates a new loop part.
	 *
	 * @param collectionName
	 *            The name of the collection
	 * @param itemName
	 *            The name under which to store the current item
	 * @param loopName
	 *            The name of the loop
	 */
	public LoopPart(String collectionName, String itemName, String loopName) {
		this.collectionName = collectionName;
		this.itemName = itemName;
		this.loopName = loopName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void render(DataProvider dataProvider, Writer writer) throws TemplateException {
		Collection<?> collection = (Collection<?>) dataProvider.getData(collectionName);
		if ((collection == null) || collection.isEmpty()) {
			return;
		}
		LoopStructure loopStructure = new LoopStructure(collection.size());
		Map<String, Object> overrideObjects = new HashMap<String, Object>();
		overrideObjects.put(loopName, loopStructure);
		for (Object object : collection) {
			overrideObjects.put(itemName, object);
			DataProvider loopDataProvider = new OverrideDataProvider(dataProvider, overrideObjects);
			loopDataProvider.addAccessor(LoopStructure.class, LOOP_STRUCTURE_ACCESSOR);
			for (Part part : parts) {
				part.render(loopDataProvider, writer);
			}
			loopStructure.incCount();
		}
	}

	/**
	 * Container for information about a loop.
	 *
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	private static class LoopStructure {

		/** The size of the loop. */
		private final int size;

		/** The current counter of the loop. */
		private int count;

		/**
		 * Creates a new loop structure for a loop with the given size.
		 *
		 * @param size
		 *            The size of the loop
		 */
		public LoopStructure(int size) {
			this.size = size;
		}

		/**
		 * Returns the size of the loop.
		 *
		 * @return The size of the loop
		 */
		public int getSize() {
			return size;
		}

		/**
		 * Returns the current counter of the loop.
		 *
		 * @return The current counter of the loop, in the range from {@code 0}
		 *         to {@link #getSize() getSize() - 1}
		 */
		public int getCount() {
			return count;
		}

		/**
		 * Increments the current counter of the loop.
		 */
		public void incCount() {
			++count;
		}

		/**
		 * Returns whether the current iteration if the first one.
		 *
		 * @return {@code true} if the curren iteration is the first one,
		 *         {@code false} otherwise
		 */
		public boolean isFirst() {
			return count == 0;
		}

		/**
		 * Returns whether the current iteration if the last one.
		 *
		 * @return {@code true} if the curren iteration is the last one, {@code
		 *         false} otherwise
		 */
		public boolean isLast() {
			return count == (size - 1);
		}

		/**
		 * Returns whether the current loop count is odd, i.e. not divisible by
		 * {@code 2}.
		 *
		 * @return {@code true} if the loop count is odd, {@code false}
		 *         otherwise
		 */
		public boolean isOdd() {
			return (count & 1) == 1;
		}

		/**
		 * Returns whether the current loop count is even, i.e. divisible by
		 * {@code 2}.
		 *
		 * @return {@code true} if the loop count is even, {@code false}
		 *         otherwise
		 */
		public boolean isEven() {
			return (count & 1) == 0;
		}

	}

	/**
	 * {@link Accessor} implementation that handles a {@link LoopStructure},
	 * allowing access via the members “size”, “count”, “first”, and “last”.
	 *
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	private static class LoopStructureAccessor implements Accessor {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Object get(DataProvider dataProvider, Object object, String member) {
			LoopStructure loopStructure = (LoopStructure) object;
			if ("size".equals(member)) {
				return loopStructure.getSize();
			} else if ("count".equals(member)) {
				return loopStructure.getCount();
			} else if ("first".equals(member)) {
				return loopStructure.isFirst();
			} else if ("last".equals(member)) {
				return loopStructure.isLast();
			} else if ("odd".equals(member)) {
				return loopStructure.isOdd();
			} else if ("even".equals(member)) {
				return loopStructure.isEven();
			}
			return null;
		}

	}

}
