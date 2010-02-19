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

import java.io.IOException;
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

	/** The name of the collection to loop over. */
	private final String collectionName;

	/** The name under which to store the current item. */
	private final String itemName;

	/** The name under which to store the loop structure. */
	private final String loopName;

	/**
	 * Creates a new loop part.
	 *
	 * @param dataProvider
	 *            The part’s data provider
	 * @param collectionName
	 *            The name of the collection
	 * @param itemName
	 *            The name under which to store the current item
	 */
	public LoopPart(DataProvider dataProvider, String collectionName, String itemName) {
		this(dataProvider, collectionName, itemName, "loop");
	}

	/**
	 * Creates a new loop part.
	 *
	 * @param dataProvider
	 *            The part’s data provider
	 * @param collectionName
	 *            The name of the collection
	 * @param itemName
	 *            The name under which to store the current item
	 * @param loopName
	 *            The name of the loop
	 */
	public LoopPart(DataProvider dataProvider, String collectionName, String itemName, String loopName) {
		super(dataProvider);
		this.collectionName = collectionName;
		this.itemName = itemName;
		this.loopName = loopName;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws TemplateException
	 */
	@Override
	public void render(Writer writer) throws IOException {
		Collection<?> collection = (Collection<?>) dataProvider.getData(collectionName);
		int loopCounter = 0;
		int loopSize = collection.size();
		Map<String, Object> loopStructure = new HashMap<String, Object>();
		loopStructure.put("size", loopSize);
		Map<String, Object> overrideObjects = new HashMap<String, Object>();
		overrideObjects.put(loopName, loopStructure);
		for (Object object : collection) {
			loopStructure.put("first", loopCounter == 0);
			loopStructure.put("last", loopCounter == (loopSize - 1));
			loopStructure.put("count", loopCounter++);
			overrideObjects.put(itemName, object);
			DataProvider loopDataProvider = new OverrideDataProvider(dataProvider, overrideObjects);
			for (Part part : parts) {
				part.setDataProvider(loopDataProvider);
				part.render(writer);
			}
		}
	}

}
