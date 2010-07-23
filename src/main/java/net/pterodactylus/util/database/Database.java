/*
 * utils - Database.java - Copyright © 2006-2009 David Roden
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

import java.util.List;

/**
 * Interface for the database abstraction. This interface holds methods that
 * allow defined access to a database.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public interface Database {

	/**
	 * Returns a single object from a database.
	 *
	 * @param <T>
	 *            The type of the object
	 * @param query
	 *            The query to execute
	 * @param objectCreator
	 *            The object creator
	 * @return The created object, or {@code null} if the {@code query} did not
	 *         yield any results
	 * @throws DatabaseException
	 *             if a database error occurs
	 */
	public <T> T getSingle(Query query, ObjectCreator<T> objectCreator) throws DatabaseException;

	/**
	 * Returns multiple query results.
	 *
	 * @param <T>
	 *            The type of the results
	 * @param query
	 *            The query to execute
	 * @param objectCreator
	 *            The object creator
	 * @return A list containing the objects from the database
	 * @throws DatabaseException
	 *             if a database error occurs
	 */
	public <T> List<T> getMultiple(Query query, ObjectCreator<T> objectCreator) throws DatabaseException;

	/**
	 * Inserts an object into the database and returns the automatically
	 * generated ID, if any.
	 *
	 * @param query
	 *            The query to execute
	 * @return The automatically generated ID, or {@code -1} if no ID was
	 *         generated
	 * @throws DatabaseException
	 *             if a database error occurs
	 */
	public long insert(Query query) throws DatabaseException;

	/**
	 * Updates objects in the database.
	 *
	 * @param query
	 *            The query to execute
	 * @return The number of changed objects in the database
	 * @throws DatabaseException
	 *             if a database error occurs
	 */
	public int update(Query query) throws DatabaseException;

	/**
	 * Processes the results of the given query with the given result processor.
	 *
	 * @param query
	 *            The query to execute
	 * @param resultProcessor
	 *            The result processor used to process the result set
	 * @throws DatabaseException
	 *             if a database error occurs
	 */
	public void process(Query query, ResultProcessor resultProcessor) throws DatabaseException;

}
