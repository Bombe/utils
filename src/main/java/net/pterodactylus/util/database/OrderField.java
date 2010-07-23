/*
 * utils - OrderField.java - Copyright © 2010 David Roden
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
 * An order field stores the name of the field to order a query result by and
 * the direction of the sorting.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class OrderField {

	/**
	 * Defines the possible sort orders.
	 *
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	public enum Order {

		/** Sort the results in ascending (smallest first) order. */
		ASCENDING,

		/** Sort the results in descending (largest first) order. */
		DESCENDING

	}

	/** The field to sort by. */
	private final Field field;

	/** The sort direction. */
	private final Order order;

	/**
	 * Creates a new order field, sorting ascending by the given field.
	 *
	 * @param field
	 *            The field to sort by
	 */
	public OrderField(Field field) {
		this(field, Order.ASCENDING);
	}

	/**
	 * Creates a new order field, sorting in the given order by the given field.
	 *
	 * @param field
	 *            The field to sort by
	 * @param order
	 *            The sort direction
	 */
	public OrderField(Field field, Order order) {
		this.field = field;
		this.order = order;
	}

	/**
	 * Returns the name of the field to sort by.
	 *
	 * @return The name of the field to sort by
	 */
	public Field getField() {
		return field;
	}

	/**
	 * Returns the order of the sort.
	 *
	 * @return The sort order
	 */
	public Order getOrder() {
		return order;
	}

}
