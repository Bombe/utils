/*
 * utils - Limit.java - Copyright © 2010 David Roden
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

package net.pterodactylus.util.database;

/**
 * A LIMIT clause limits the results that are returned.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Limit {

	/** The index of the first result to retrieve, 0-based. */
	private final long start;

	/** The number of results to retrieve. */
	private final long number;

	/**
	 * Creates a new limit clause that retrieves the given number of results.
	 *
	 * @param number
	 *            The number of results to retrieve
	 */
	public Limit(long number) {
		this(0, number);
	}

	/**
	 * Creates a new limit clause that retrieves the given number of results,
	 * starting at the given index.
	 *
	 * @param start
	 *            The index of the first result to retrieve
	 * @param number
	 *            The number of results to retrieve
	 */
	public Limit(long start, long number) {
		this.start = start;
		this.number = number;
	}

	/**
	 * Returns the index of the first result to retrieve.
	 *
	 * @return The index of the first result to retrieve
	 */
	public long getStart() {
		return start;
	}

	/**
	 * Returns the number of results to retreive.
	 *
	 * @return The number of results to retrieve
	 */
	public long getNumber() {
		return number;
	}

}
