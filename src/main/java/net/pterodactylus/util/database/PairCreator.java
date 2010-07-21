/*
 * utils - PairCreator.java - Copyright © 2009 David Roden
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

import net.pterodactylus.util.collection.Pair;

/**
 * Object creator implementation that creates a pair from two other object
 * creators.
 *
 * @param <L>
 *            The type of the left object
 * @param <R>
 *            The type of the right object
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class PairCreator<L, R> implements ObjectCreator<Pair<L, R>> {

	/** The object creator for the left object. */
	private final ObjectCreator<L> leftObjectCreator;

	/** The object creator for the right object. */
	private final ObjectCreator<R> rightObjectCreator;

	/**
	 * Creates a new pair object creator.
	 *
	 * @param leftObjectCreator
	 *            The left object creator
	 * @param rightObjectCreator
	 *            The right object creator
	 */
	public PairCreator(ObjectCreator<L> leftObjectCreator, ObjectCreator<R> rightObjectCreator) {
		this.leftObjectCreator = leftObjectCreator;
		this.rightObjectCreator = rightObjectCreator;
	}

	/**
	 * @see net.pterodactylus.util.database.ObjectCreator#createObject(java.sql.ResultSet)
	 */
	@Override
	public Pair<L, R> createObject(ResultSet resultSet) throws SQLException {
		return new Pair<L, R>(leftObjectCreator.createObject(resultSet), rightObjectCreator.createObject(resultSet));
	}

}
