/*
 * utils - Parameter.java - Copyright © 2010 David Roden
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

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

/**
 * A {@code Parameter} stores a parameter value in its native type and can set
 * it on a {@link PreparedStatement} using correct type information.
 *
 * @param <T>
 *            The type of the parameter value
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public abstract class Parameter<T> {

	/** The value of the parameter. */
	protected final T value;

	/**
	 * Creates a new parameter.
	 *
	 * @param value
	 *            The value of the parameter
	 */
	protected Parameter(T value) {
		this.value = value;
	}

	/**
	 * Sets the value on the prepared statement.
	 *
	 * @param preparedStatement
	 *            The prepared statement to set the value on
	 * @param index
	 *            The index of the parameter
	 * @throws SQLException
	 *             if an SQL error occurs
	 */
	public abstract void set(PreparedStatement preparedStatement, int index) throws SQLException;

	/**
	 * {@link Parameter} implemetation for a {@link Boolean} value that supports
	 * NULL values.
	 *
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	public static class BooleanParameter extends Parameter<Boolean> {

		/**
		 * Creates a new parameter.
		 *
		 * @param value
		 *            The value of the parameter (may be {@code null})
		 */
		public BooleanParameter(Boolean value) {
			super(value);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void set(PreparedStatement preparedStatement, int index) throws SQLException {
			if (value == null) {
				preparedStatement.setNull(index, Types.BOOLEAN);
			} else {
				preparedStatement.setBoolean(index, value);
			}
		}

	}

	/**
	 * {@link Parameter} implemetation for a {@link Integer} value that supports
	 * NULL values.
	 *
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	public static class IntegerParameter extends Parameter<Integer> {

		/**
		 * Creates a new parameter.
		 *
		 * @param value
		 *            The value of the parameter (may be {@code null})
		 */
		public IntegerParameter(Integer value) {
			super(value);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void set(PreparedStatement preparedStatement, int index) throws SQLException {
			if (value == null) {
				preparedStatement.setNull(index, Types.INTEGER);
			} else {
				preparedStatement.setInt(index, value);
			}
		}

	}

	/**
	 * {@link Parameter} implemetation for a {@link Long} value that supports
	 * NULL values.
	 *
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	public static class LongParameter extends Parameter<Long> {

		/**
		 * Creates a new parameter.
		 *
		 * @param value
		 *            The value of the parameter (may be {@code null})
		 */
		public LongParameter(Long value) {
			super(value);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void set(PreparedStatement preparedStatement, int index) throws SQLException {
			if (value == null) {
				preparedStatement.setNull(index, Types.BIGINT);
			} else {
				preparedStatement.setLong(index, value);
			}
		}

	}

	/**
	 * {@link Parameter} implemetation for a {@link String} value that supports
	 * NULL values.
	 *
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	public static class StringParameter extends Parameter<String> {

		/**
		 * Creates a new parameter.
		 *
		 * @param value
		 *            The value of the parameter (may be {@code null})
		 */
		public StringParameter(String value) {
			super(value);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void set(PreparedStatement preparedStatement, int index) throws SQLException {
			if (value == null) {
				preparedStatement.setNull(index, Types.VARCHAR);
			} else {
				preparedStatement.setString(index, value);
			}
		}
	}

}
