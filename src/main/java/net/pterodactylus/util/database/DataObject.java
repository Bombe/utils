/*
 * utils - DataObject.java - Copyright © 2010 David Roden
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.pterodactylus.util.database.Parameter.LongParameter;
import net.pterodactylus.util.database.Query.Type;

/**
 * A data object represents a data row from a database table. The
 * {@link DataObject} class is intended to make it easier to read and write
 * objects from and to a database. In order to achieve this some restrictions
 * had to be posed upon the data object and its representation in the database.
 * <ul>
 * <li>A data object is required to have an ID of the type {@code long}.</li>
 * <li>Every data object is required to have a corresponding entry in the
 * database. It is not possible to create a data object in memory first and then
 * save it to the database.</li>
 * </ul>
 *
 * @param <D>
 *            The type of the data object
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public abstract class DataObject<D extends DataObject<D>> {

	/** The data object factory that created this object. */
	private final DataObjectFactory<D> dataObjectFactory;

	/** The ID of the data object. */
	private final long id;

	/** The properties of this data object. */
	private final Map<String, Object> properties = new HashMap<String, Object>();

	/** Whether the object has been written to. */
	private boolean dirty;

	/**
	 * Creates a new data object.
	 *
	 * @param dataObjectFactory
	 *            The data object factory that create this object
	 * @param id
	 *            The ID of the data object
	 */
	protected DataObject(DataObjectFactory<D> dataObjectFactory, long id) {
		this.dataObjectFactory = dataObjectFactory;
		this.id = id;
	}

	//
	// ACCESSORS
	//

	/**
	 * Returns the ID of the data object.
	 *
	 * @return The ID of the data object
	 */
	public final long getId() {
		return id;
	}

	//
	// PROTECTED METHODS
	//

	/**
	 * Sets the property with the given name to the given value.
	 *
	 * @param name
	 *            The name of the property
	 * @param value
	 *            The value of the property
	 */
	protected void setProperty(String name, Object value) {
		properties.put(name, value);
		dirty |= getSaveFields().contains(new Field(name));
	}

	/**
	 * Returns the value of the property with the given name.
	 *
	 * @param name
	 *            The name of the property
	 * @return The value of the property, or {@code null} if the property does
	 *         not exist
	 */
	protected Object getProperty(String name) {
		return properties.get(name);
	}

	/**
	 * Clears the dirty flag.
	 */
	protected void clearDirtyFlag() {
		dirty = false;
	}

	//
	// METHODS FOR SUBCLASSES TO OVERRIDE
	//

	/**
	 * Retuns the fields that need to be saved when the object has changed.
	 *
	 * @return The fields that need to be saved
	 */
	protected abstract Set<ValueField> getSaveFields();

	//
	// ACTIONS
	//

	/**
	 * Saves this data object to the database if it is dirty, or if the
	 * {@code force} parameter is {@code true}.
	 *
	 * @param database
	 *            The database to store the object to
	 * @param force
	 *            {@code true} to force saving to the database, {@code false} to
	 *            not save if the object is not dirty
	 * @throws DatabaseException
	 *             if a database error occurs
	 */
	public void save(Database database, boolean force) throws DatabaseException {
		if (!dirty && !force) {
			return;
		}
		Query query = new Query(Type.UPDATE, dataObjectFactory.getTable());
		for (ValueField saveField : getSaveFields()) {
			query.addValueField(saveField);
		}
		query.addWhereClause(new ValueFieldWhereClause(new ValueField(dataObjectFactory.getIdentityColumn(), new LongParameter(id))));
		database.update(query);
		clearDirtyFlag();
	}

	//
	// STATIC METHODS
	//

	/**
	 * Loads a data object from the given database by the given ID.
	 *
	 * @param database
	 *            The database to load the object from
	 * @param dataObjectFactory
	 *            The data object factory
	 * @param id
	 *            The ID to load
	 * @param <D>
	 *            The type of the object to load
	 * @return The loaded object, or {@code null} if there was no object with
	 *         the given ID
	 * @throws DatabaseException
	 *             if a database error occurs
	 */
	public static <D extends DataObject<D>> D load(Database database, DataObjectFactory<D> dataObjectFactory, long id) throws DatabaseException {
		return loadByWhereClause(database, dataObjectFactory, new ValueFieldWhereClause(new ValueField(dataObjectFactory.getIdentityColumn(), new LongParameter(id))));
	}

	/**
	 * Loads the first data object that matches the given where clause from the
	 * given database.
	 *
	 * @param database
	 *            The database to load the object from
	 * @param dataObjectFactory
	 *            The data object factory
	 * @param whereClause
	 *            The where clause to match the object
	 * @param <D>
	 *            The type of the object to load
	 * @param orderFields
	 *            The fields to order the results by
	 * @return The first object that matched the wher clause, or {@code null} if
	 *         there was no object that matched
	 * @throws DatabaseException
	 *             if a database error occurs
	 */
	public static <D extends DataObject<D>> D loadByWhereClause(Database database, DataObjectFactory<D> dataObjectFactory, WhereClause whereClause, OrderField... orderFields) throws DatabaseException {
		Query query = new Query(Type.SELECT, dataObjectFactory.getTable());
		query.addWhereClause(whereClause);
		for (OrderField orderField : orderFields) {
			query.addOrderField(orderField);
		}
		return database.getSingle(query, dataObjectFactory);
	}

	/**
	 * Loads all data objects that match the given where clause from the given
	 * database.
	 *
	 * @param database
	 *            The database to load the objects from
	 * @param dataObjectFactory
	 *            The data object factory
	 * @param whereClause
	 *            The where clause to match the objects
	 * @param <D>
	 *            The type of the objects to load
	 * @param orderFields
	 *            The order fields to sort the results by
	 * @return All objects that matched the wher clause, or an empty list if
	 *         there was no object that matched
	 * @throws DatabaseException
	 *             if a database error occurs
	 */
	public static <D extends DataObject<D>> List<D> loadAllByWhereClause(Database database, DataObjectFactory<D> dataObjectFactory, WhereClause whereClause, OrderField... orderFields) throws DatabaseException {
		Query query = new Query(Type.SELECT, dataObjectFactory.getTable());
		query.addWhereClause(whereClause);
		query.addOrderField(orderFields);
		return database.getMultiple(query, dataObjectFactory);
	}

}
