/*
 * utils - Join.java - Copyright © 2011–2019 David Roden
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
 * Models a join between two database tables.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Join {

	/** The type of the join. */
	public enum JoinType {

		/** An inner join. */
		INNER,

		/** An outer join. */
		OUTER,

		/** A left join. */
		LEFT,

		/** A right join. */
		RIGHT,

		/** A natural join. */
		NATURAL

	}

	/** The type of the join. */
	private final JoinType type;

	/** The table to join. */
	private final String table;

	/** The field of the previous table to match. */
	private final Field leftField;

	/** The field of the joined table to match. */
	private final Field rightField;

	/**
	 * Creates a new join between two tables.
	 *
	 * @param type
	 *            The type of the join
	 * @param table
	 *            The table to join with
	 * @param leftField
	 *            The field of the previous table to match with
	 * @param rightField
	 *            The field of the joined table to match with
	 */
	public Join(JoinType type, String table, Field leftField, Field rightField) {
		this.type = type;
		this.table = table;
		this.leftField = leftField;
		this.rightField = rightField;
	}

	//
	// ACCESSORS
	//

	/**
	 * Returns the join type.
	 *
	 * @return The join type
	 */
	public JoinType getType() {
		return type;
	}

	/**
	 * Returns the table to join with.
	 *
	 * @return The table to join with
	 */
	public String getTable() {
		return table;
	}

	/**
	 * Returns the field of the previous table to match.
	 *
	 * @return The field of the previous table to match
	 */
	public Field getLeftField() {
		return leftField;
	}

	/**
	 * Returns the field of the joined table to match with.
	 *
	 * @return The field of the joined table to match with
	 */
	public Field getRightField() {
		return rightField;
	}

}
