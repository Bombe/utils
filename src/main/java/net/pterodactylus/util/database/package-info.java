/**
 * <p>
 * Helper and abstraction classes for database interaction.
 * </p>
 * <h1>Database Backends</h1>
 * <p>
 * The {@link net.pterodactylus.util.database.Database} interface defines data
 * manipulation methods that can be used to perform 95% of all necessary
 * interaction with the database.
 * </p>
 * <h2>Getting a Single Value from the Database</h2>
 * <p>
 * The {@link net.pterodactylus.util.database.Database#getSingle(Query, ObjectCreator)}
 * method is used to return a single value or object from the database. This
 * method delegates the work of actually creating the return value to the given
 * {@link net.pterodactylus.util.database.ObjectCreator} instance which uses the first
 * row that is returned by the given query to create the appropriate object.
 * </p>
 * <h2>Getting Multiple Values from the Database</h2>
 * <p>
 * The
 * {@link net.pterodactylus.util.database.Database#getMultiple(Query, ObjectCreator)}
 * method is used to return multiple values or objects from the database. Again,
 * the actual creation process is delegated to the given object creator which
 * will be used for every row the query returns.
 * </p>
 * <h2>Inserting Values into the Database</h2>
 * <p>
 * Use the {@link net.pterodactylus.util.database.Database#insert(Query)} method to
 * execute a query that will insert one new record into a database. It will
 * return the first auto-generated ID, or <code>-1</code> if no ID was
 * generated.
 * </p>
 * <h2>Updating Values in the Database</h2>
 * <p>
 * Finally, the {@link net.pterodactylus.util.database.Database#update(Query)} method
 * will let you execute a query that updates values in the database. It will
 * return the number of changed records.
 * </p>
 * <h1>The {@link net.pterodactylus.util.database.AbstractDatabase} Implementation</h1>
 * <p>
 * This class introduces some helper classes the perform the actual SQL queries
 * and reduces the cost of writing new database backends to the implementation
 * of two methods:
 * {@link net.pterodactylus.util.database.AbstractDatabase#getConnection()
 * getConnection()} and
 * {@link net.pterodactylus.util.database.AbstractDatabase#returnConnection(java.sql.Connection)
 * returnConnection()} . It also contains a method to create a database from a
 * {@link javax.sql.DataSource} (
 * {@link net.pterodactylus.util.database.AbstractDatabase#fromDataSource(javax.sql.DataSource)}
 * ).
 * </p>
 * <h1>The {@link net.pterodactylus.util.database.Query} Class</h1>
 * <p>
 * This class stores an SQL query string and its parameters. It is able to
 * create a {@link java.sql.PreparedStatement} from the parameters and it can be
 * persisted easily.
 * </p>
 * <h1>The {@link net.pterodactylus.util.database.ObjectCreator} Class</h1>
 * <p>
 * An object creator is responsible for creating objects from the current row of
 * a {@link java.sql.ResultSet}. It is used by the helper classes in
 * {@link net.pterodactylus.util.database.AbstractDatabase} to allow the user to control
 * the conversion from a result set to a domain-specific object.
 * </p>
 */

package net.pterodactylus.util.database;

