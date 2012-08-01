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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import net.pterodactylus.util.template.TemplateParser.Filters;

/**
 * {@link Part} implementation that loops over a {@link Collection}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
class LoopPart extends ContainerPart {

	/** Reflection accessor used to iterator over the {@link LoopStructure}. */
	private static final ReflectionAccessor REFLECTION_ACCESSOR = new ReflectionAccessor();

	/** The name of the collection to loop over. */
	private final String collectionName;

	/** The name under which to store the current item. */
	private final String itemName;

	/** The name under which to store the loop structure. */
	private final String loopName;

	/** The filters for the collection. */
	private final Filters filters;

	/**
	 * Creates a new loop part.
	 *
	 * @param line
	 *            The line number of the tag
	 * @param column
	 *            The column number of the tag
	 * @param collectionName
	 *            The name of the collection
	 * @param itemName
	 *            The name under which to store the current item
	 */
	public LoopPart(int line, int column, String collectionName, String itemName) {
		this(line, column, collectionName, itemName, "loop", new Filters());
	}

	/**
	 * Creates a new loop part.
	 *
	 * @param line
	 *            The line number of the tag
	 * @param column
	 *            The column number of the tag
	 * @param collectionName
	 *            The name of the collection
	 * @param itemName
	 *            The name under which to store the current item
	 * @param loopName
	 *            The name of the loop
	 * @param filters
	 *            The filters to apply
	 */
	public LoopPart(int line, int column, String collectionName, String itemName, String loopName, Filters filters) {
		super(line, column);
		this.collectionName = collectionName;
		this.itemName = itemName;
		this.loopName = loopName;
		this.filters = filters;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void render(TemplateContext templateContext, Writer writer) throws TemplateException {
		TemplateContext outerLoopContext = new TemplateContext(templateContext, true);
		Object collectionObject = filters.filter(getLine(), getColumn(), templateContext, templateContext.get(collectionName));
		if (collectionObject == null) {
			return;
		}
		Collection<?> collection;
		if (collectionObject instanceof Collection<?>) {
			collection = (Collection<?>) collectionObject;
			if (collection.isEmpty()) {
				return;
			}
		} else if (collectionObject instanceof Map<?, ?>) {
			Map<?, ?> map = (Map<?, ?>) collectionObject;
			collection = map.entrySet();
		} else if (collectionObject instanceof Iterable<?>) {
			collection = new ArrayList<Object>();
			for (Object object : (Iterable<?>) collectionObject) {
				((Collection<Object>) collection).add(object);
			}
		} else {
			collection = Arrays.asList(collectionObject);
		}
		LoopStructure loopStructure = new LoopStructure(collection.size());
		for (Object object : collection) {
			TemplateContext loopContext = new TemplateContext(outerLoopContext);
			loopContext.addAccessor(LoopStructure.class, REFLECTION_ACCESSOR);
			loopContext.set(loopName, loopStructure);
			loopContext.set(itemName, object);
			super.render(loopContext, writer);
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
		@SuppressWarnings("unused")
		public int getSize() {
			return size;
		}

		/**
		 * Returns the current counter of the loop.
		 *
		 * @return The current counter of the loop, in the range from {@code 0}
		 *         to {@link #getSize() getSize() - 1}
		 */
		@SuppressWarnings("unused")
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
		@SuppressWarnings("unused")
		public boolean isFirst() {
			return count == 0;
		}

		/**
		 * Returns whether the current iteration if the last one.
		 *
		 * @return {@code true} if the curren iteration is the last one,
		 *         {@code false} otherwise
		 */
		@SuppressWarnings("unused")
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
		@SuppressWarnings("unused")
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
		@SuppressWarnings("unused")
		public boolean isEven() {
			return (count & 1) == 0;
		}

	}

}
