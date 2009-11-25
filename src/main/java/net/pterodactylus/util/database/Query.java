/*
 * utils - Query.java - Copyright © 2009 David Roden
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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * Container for SQL queries and their parameters.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Query {

	/** The query string. */
	private final String query;

	/** The parameters. */
	private final Map<Integer, Parameter<?>> parameters = new HashMap<Integer, Parameter<?>>();

	/**
	 * Creates a new query from the given string.
	 *
	 * @param query
	 *            The query string
	 */
	public Query(String query) {
		this.query = query;
	}

	//
	// ACTIONS
	//

	/**
	 * Sets the integer at the given index to the given value.
	 *
	 * @param index
	 *            The index of the value to set
	 * @param value
	 *            The value to set
	 */
	public void setInt(int index, int value) {
		parameters.put(index, new IntegerParameter(value));
	}

	/**
	 * Sets the integer at the given index to the given value.
	 *
	 * @param index
	 *            The index of the value to set
	 * @param value
	 *            The value to set
	 */
	public void setParameter(int index, int value) {
		setInt(index, value);
	}

	/**
	 * Sets the long at the given index to the given value.
	 *
	 * @param index
	 *            The index of the value to set
	 * @param value
	 *            The value to set
	 */
	public void setLong(int index, long value) {
		parameters.put(index, new LongParameter(value));
	}

	/**
	 * Sets the long at the given index to the given value.
	 *
	 * @param index
	 *            The index of the value to set
	 * @param value
	 *            The value to set
	 */
	public void setParameter(int index, long value) {
		setLong(index, value);
	}

	/**
	 * Sets the string at the given index to the given value.
	 *
	 * @param index
	 *            The index of the value to set
	 * @param value
	 *            The value to set
	 */
	public void setString(int index, String value) {
		parameters.put(index, new StringParameter(value));
	}

	/**
	 * Sets the string at the given index to the given value.
	 *
	 * @param index
	 *            The index of the value to set
	 * @param value
	 *            The value to set
	 */
	public void setParameter(int index, String value) {
		setString(index, value);
	}

	/**
	 * Creates a {@link PreparedStatement} from this query and its parameters.
	 *
	 * @param connection
	 *            The connection to create a statement on
	 * @return The created prepared statement
	 * @throws SQLException
	 *             if an SQL error occurs
	 */
	public PreparedStatement createStatement(Connection connection) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		int index = 1;
		while (parameters.containsKey(index)) {
			Parameter<?> parameterValue = parameters.get(index);
			parameterValue.setValue(preparedStatement, index);
			index++;
		}
		return preparedStatement;
	}

	/**
	 * Abstract base class for parameters.
	 *
	 * @param <T>
	 *            The type of the parameter’s value
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	private abstract static class Parameter<T> {

		/** The value. */
		protected final T value;

		/**
		 * Creates a new parameter value.
		 *
		 * @param value
		 *            The value of the parameter
		 */
		protected Parameter(T value) {
			this.value = value;
		}

		/**
		 * Sets the value of this parameter on the given statement at the given
		 * index.
		 *
		 * @param preparedStatement
		 *            The prepared statement to set the value on
		 * @param index
		 *            The index of the value to set
		 * @throws SQLException
		 *             if an SQL error occurs
		 */
		public abstract void setValue(PreparedStatement preparedStatement, int index) throws SQLException;

	}

	/**
	 * A parameter that holds an integer value.
	 *
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	private static class IntegerParameter extends Parameter<Integer> {

		/**
		 * Creates a new integer parameter with the given value.
		 *
		 * @param value
		 *            The value of the parameter
		 */
		public IntegerParameter(int value) {
			super(value);
		}

		/**
		 * @see net.pterodactylus.util.database.Query.Parameter#setValue(java.sql.PreparedStatement,
		 *      int)
		 */
		@Override
		public void setValue(PreparedStatement preparedStatement, int index) throws SQLException {
			preparedStatement.setInt(index, value);
		}

	}

	/**
	 * A parameter that holds a {@link Long} value.
	 *
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	private static class LongParameter extends Parameter<Long> {

		/**
		 * Creates a new long parameter with the given value.
		 *
		 * @param value
		 *            The value of the parameter
		 */
		public LongParameter(long value) {
			super(value);
		}

		/**
		 * @see net.pterodactylus.util.database.Query.Parameter#setValue(java.sql.PreparedStatement,
		 *      int)
		 */
		@Override
		public void setValue(PreparedStatement preparedStatement, int index) throws SQLException {
			preparedStatement.setLong(index, value);
		}

	}

	/**
	 * A parameter that holds a string value.
	 *
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	private static class StringParameter extends Parameter<String> {

		/**
		 * Creates a new string parameter with the given value.
		 *
		 * @param value
		 *            The value of the parameter
		 */
		public StringParameter(String value) {
			super(value);
		}

		/**
		 * @see net.pterodactylus.util.database.Query.Parameter#setValue(java.sql.PreparedStatement,
		 *      int)
		 */
		@Override
		public void setValue(PreparedStatement preparedStatement, int index) throws SQLException {
			preparedStatement.setString(index, value);
		}

	}

}
