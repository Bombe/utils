/*
 * utils - Mappers.java - Copyright © 2011–2016 David Roden
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
package net.pterodactylus.util.collection.mapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Helper methods for using the {@link Mapper} interface with {@link Set}s and
 * {@link List}s. This helper class replaces the now deprecated
 * {@code Converters} class.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Mappers {

	/**
	 * Maps all objects in the given list and returns a list that contains the
	 * mapped objects in the same order as the input objects in the original
	 * list.
	 *
	 * @param <I>
	 *            The type of the input objects
	 * @param <O>
	 *            The type of the output objects
	 * @param inputList
	 *            The list with the input object
	 * @param mapper
	 *            The mapper to use
	 * @return The list containing the mapped objects
	 */
	public static <I, O> List<O> mappedList(List<I> inputList, Mapper<I, O> mapper) {
		List<O> outputList = new ArrayList<O>();
		for (I input : inputList) {
			outputList.add(mapper.map(input));
		}
		return outputList;
	}

	/**
	 * Maps all objects in the given list and returns a list that contains the
	 * mapped objects.
	 *
	 * @param <I>
	 *            The type of the input objects
	 * @param <O>
	 *            The type of the output objects
	 * @param inputSet
	 *            The set with the input object
	 * @param mapper
	 *            The mapper to use
	 * @return The set containing the mapped objects
	 */
	public static <I, O> Set<O> mappedSet(Set<I> inputSet, Mapper<I, O> mapper) {
		Set<O> outputSet = new HashSet<O>();
		for (I input : inputSet) {
			outputSet.add(mapper.map(input));
		}
		return outputSet;
	}

}
