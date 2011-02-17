/*
 * utils - Converters.java - Copyright © 2011 David Roden
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

package net.pterodactylus.util.collection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * TODO
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Converters {

	public static <I, O> List<O> convertList(List<I> inputList, Converter<I, O> converter) {
		List<O> outputList = new ArrayList<O>();
		for (I input : inputList) {
			outputList.add(converter.convert(input));
		}
		return outputList;
	}

	public static <I, O> Set<O> convertSet(Set<I> inputSet, Converter<I, O> converter) {
		Set<O> outputSet = new HashSet<O>();
		for (I input : inputSet) {
			outputSet.add(converter.convert(input));
		}
		return outputSet;
	}

}
