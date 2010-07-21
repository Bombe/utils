/*
 * utils - ObjectCreator.java - Copyright © 2009 David Roden
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

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * An <em>object creator</em> is able to create a new object of type {@code T}
 * from the current row of a {@link ResultSet}.
 *
 * @param <T>
 *            The type of the created object
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public interface ObjectCreator<T> {

	/** A default integer creator. */
	public static final IntegerCreator INTEGER_CREATOR = new IntegerCreator();

	/** A default string creator. */
	public static final StringCreator STRING_CREATOR = new StringCreator();

	/**
	 * Creates a new object from the current row in the given result set.
	 *
	 * @param resultSet
	 *            The result set to create a new object from
	 * @return {@code null} if the result set is not on a valid row, i.e. if the
	 *         result set is empty or there are no more rows left in it
	 * @throws SQLException
	 *             if an SQL error occurs
	 */
	public T createObject(ResultSet resultSet) throws SQLException;

	/**
	 * Creates a single integer from a result set. When the column contained the
	 * SQL NULL value, {@code null} is returned instead of {@code 0}; this
	 * differs from {@link ResultSet#getInt(int)}.
	 *
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	public static class IntegerCreator implements ObjectCreator<Integer> {

		/** The index of the value. */
		private final int index;

		/**
		 * Creates a new object creator that returns the integer value of the
		 * first column of the result set.
		 */
		public IntegerCreator() {
			this(1);
		}

		/**
		 * Creates a new object creator that returns the integer value of the
		 * given column.
		 *
		 * @param index
		 *            The index of the column to return
		 */
		public IntegerCreator(int index) {
			this.index = index;
		}

		/**
		 * @see net.pterodactylus.util.database.ObjectCreator#createObject(java.sql.ResultSet)
		 */
		@Override
		public Integer createObject(ResultSet resultSet) throws SQLException {
			Integer result = resultSet.getInt(index);
			if (resultSet.wasNull()) {
				return null;
			}
			return result;
		}

	}

	/**
	 * Creates a single {@link String} from a result set.
	 *
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	public static class StringCreator implements ObjectCreator<String> {

		/** The index of the value. */
		private final int index;

		/**
		 * Creates a new object creator that returns the value of the result
		 * set’s first column as a {@link String}.
		 */
		public StringCreator() {
			this(1);
		}

		/**
		 * Creates a new object creator that returns the value of the result
		 * set’s given column as a {@link String}.
		 *
		 * @param index
		 *            The index of the column
		 */
		public StringCreator(int index) {
			this.index = index;
		}

		/**
		 * @see net.pterodactylus.util.database.ObjectCreator#createObject(java.sql.ResultSet)
		 */
		@Override
		public String createObject(ResultSet resultSet) throws SQLException {
			return resultSet.getString(index);
		}
	}

}
