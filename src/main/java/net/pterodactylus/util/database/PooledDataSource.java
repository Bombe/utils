/*
 * utils - PooledDataSource.java - Copyright © 2006-2009 David Roden
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

import java.io.PrintWriter;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.sql.DataSource;

import net.pterodactylus.util.collection.Pair;

/**
 * A data source that uses an internal pool of opened connections. Whenever a
 * connection is requested with {@link #getConnection()} or
 * {@link #getConnection(String, String)} a new connection is created if there
 * is none available. This will allow the pool to grow on demand.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class PooledDataSource implements DataSource {

	/** The original data source. */
	private DataSource originalDataSource;

	/** The pool of currently opened connections. */
	private Set<Connection> currentConnections = new HashSet<Connection>();

	/**
	 * The pool of currently opened connections with custom username/password
	 * combinations.
	 */
	private Map<Pair<String, String>, Set<Connection>> usernamePasswordConnections = new HashMap<Pair<String, String>, Set<Connection>>();

	/**
	 * Creates a new pooled data source that wraps the given data source.
	 *
	 * @param originalDataSource
	 *            The original data source
	 */
	public PooledDataSource(DataSource originalDataSource) {
		this.originalDataSource = originalDataSource;
	}

	/**
	 * @see javax.sql.DataSource#getConnection()
	 */
	@Override
	public Connection getConnection() throws SQLException {
		synchronized (currentConnections) {
			if (currentConnections.isEmpty()) {
				Connection newConnection = new PooledConnection(originalDataSource.getConnection());
				currentConnections.add(newConnection);
			}
			Connection connection = currentConnections.iterator().next();
			currentConnections.remove(connection);
			return connection;
		}
	}

	/**
	 * @see javax.sql.DataSource#getConnection(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		Pair<String, String> usernamePasswordPair = new Pair<String, String>(username, password);
		synchronized (usernamePasswordConnections) {
			if (!usernamePasswordConnections.containsKey(usernamePasswordPair)) {
				Set<Connection> connections = new HashSet<Connection>();
				usernamePasswordConnections.put(usernamePasswordPair, connections);
			}
			Set<Connection> connections = usernamePasswordConnections.get(usernamePasswordPair);
			if (usernamePasswordConnections.isEmpty()) {
				Connection newConnection = new PooledUsernamePasswordConnection(originalDataSource.getConnection(username, password), username, password);
				connections.add(newConnection);
			}
			Connection connection = connections.iterator().next();
			connections.remove(connection);
			return connection;
		}
	}

	/**
	 * @see javax.sql.CommonDataSource#getLogWriter()
	 */
	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return originalDataSource.getLogWriter();
	}

	/**
	 * @see javax.sql.CommonDataSource#getLoginTimeout()
	 */
	@Override
	public int getLoginTimeout() throws SQLException {
		return originalDataSource.getLoginTimeout();
	}

	/**
	 * @see javax.sql.CommonDataSource#setLogWriter(java.io.PrintWriter)
	 */
	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		originalDataSource.setLogWriter(out);
	}

	/**
	 * @see javax.sql.CommonDataSource#setLoginTimeout(int)
	 */
	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		originalDataSource.setLoginTimeout(seconds);
	}

	/**
	 * @see java.sql.Wrapper#isWrapperFor(java.lang.Class)
	 */
	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return originalDataSource.isWrapperFor(iface);
	}

	/**
	 * @see java.sql.Wrapper#unwrap(java.lang.Class)
	 */
	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return originalDataSource.unwrap(iface);
	}

	/**
	 * Wrapper around a connection that was created by the
	 * {@link PooledDataSource#originalDataSource}. This wrapper only overrides
	 * the {@link Connection#close()} method to not close the connection but to
	 * return it to the connection pool instead.
	 *
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	private class PooledConnection implements Connection {

		/** The original connection. */
		private final Connection originalConnection;

		/**
		 * Creates a new pooled connection.
		 *
		 * @param originalConnection
		 *            The original connection to wrap
		 */
		public PooledConnection(Connection originalConnection) {
			this.originalConnection = originalConnection;
		}

		//
		// PROTECTED METHODS
		//

		/**
		 * Returns the original connection that is wrapped by this pooled
		 * connection.
		 *
		 * @return The original connection
		 */
		protected Connection getOriginalConnection() {
			return originalConnection;
		}

		//
		// INTERFACE Connection
		//

		/**
		 * @see java.sql.Connection#clearWarnings()
		 */
		@Override
		public void clearWarnings() throws SQLException {
			originalConnection.clearWarnings();
		}

		/**
		 * @see java.sql.Connection#close()
		 */
		@Override
		@SuppressWarnings("synthetic-access")
		public void close() throws SQLException {
			if (!isValid(1)) {
				originalConnection.close();
				return;
			}
			synchronized (currentConnections) {
				currentConnections.add(this);
			}
		}

		/**
		 * @see java.sql.Connection#commit()
		 */
		@Override
		public void commit() throws SQLException {
			originalConnection.commit();
		}

		/**
		 * @see java.sql.Connection#createArrayOf(java.lang.String,
		 *      java.lang.Object[])
		 */
		@Override
		public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
			return originalConnection.createArrayOf(typeName, elements);
		}

		/**
		 * @see java.sql.Connection#createBlob()
		 */
		@Override
		public Blob createBlob() throws SQLException {
			return originalConnection.createBlob();
		}

		/**
		 * @see java.sql.Connection#createClob()
		 */
		@Override
		public Clob createClob() throws SQLException {
			return originalConnection.createClob();
		}

		/**
		 * @see java.sql.Connection#createNClob()
		 */
		@Override
		public NClob createNClob() throws SQLException {
			return originalConnection.createNClob();
		}

		/**
		 * @see java.sql.Connection#createSQLXML()
		 */
		@Override
		public SQLXML createSQLXML() throws SQLException {
			return originalConnection.createSQLXML();
		}

		/**
		 * @see java.sql.Connection#createStatement()
		 */
		@Override
		public Statement createStatement() throws SQLException {
			return originalConnection.createStatement();
		}

		/**
		 * @see java.sql.Connection#createStatement(int, int, int)
		 */
		@Override
		public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
			return originalConnection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
		}

		/**
		 * @see java.sql.Connection#createStatement(int, int)
		 */
		@Override
		public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
			return originalConnection.createStatement(resultSetType, resultSetConcurrency);
		}

		/**
		 * @see java.sql.Connection#createStruct(java.lang.String,
		 *      java.lang.Object[])
		 */
		@Override
		public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
			return originalConnection.createStruct(typeName, attributes);
		}

		/**
		 * @see java.sql.Connection#getAutoCommit()
		 */
		@Override
		public boolean getAutoCommit() throws SQLException {
			return originalConnection.getAutoCommit();
		}

		/**
		 * @see java.sql.Connection#getCatalog()
		 */
		@Override
		public String getCatalog() throws SQLException {
			return originalConnection.getCatalog();
		}

		/**
		 * @see java.sql.Connection#getClientInfo()
		 */
		@Override
		public Properties getClientInfo() throws SQLException {
			return originalConnection.getClientInfo();
		}

		/**
		 * @see java.sql.Connection#getClientInfo(java.lang.String)
		 */
		@Override
		public String getClientInfo(String name) throws SQLException {
			return originalConnection.getClientInfo(name);
		}

		/**
		 * @see java.sql.Connection#getHoldability()
		 */
		@Override
		public int getHoldability() throws SQLException {
			return originalConnection.getHoldability();
		}

		/**
		 * @see java.sql.Connection#getMetaData()
		 */
		@Override
		public DatabaseMetaData getMetaData() throws SQLException {
			return originalConnection.getMetaData();
		}

		/**
		 * @see java.sql.Connection#getTransactionIsolation()
		 */
		@Override
		public int getTransactionIsolation() throws SQLException {
			return originalConnection.getTransactionIsolation();
		}

		/**
		 * @see java.sql.Connection#getTypeMap()
		 */
		@Override
		public Map<String, Class<?>> getTypeMap() throws SQLException {
			return originalConnection.getTypeMap();
		}

		/**
		 * @see java.sql.Connection#getWarnings()
		 */
		@Override
		public SQLWarning getWarnings() throws SQLException {
			return originalConnection.getWarnings();
		}

		/**
		 * @see java.sql.Connection#isClosed()
		 */
		@Override
		public boolean isClosed() throws SQLException {
			return originalConnection.isClosed();
		}

		/**
		 * @see java.sql.Connection#isReadOnly()
		 */
		@Override
		public boolean isReadOnly() throws SQLException {
			return originalConnection.isReadOnly();
		}

		/**
		 * @see java.sql.Connection#isValid(int)
		 */
		@Override
		public boolean isValid(int timeout) throws SQLException {
			return originalConnection.isValid(timeout);
		}

		/**
		 * @see java.sql.Wrapper#isWrapperFor(java.lang.Class)
		 */
		@Override
		public boolean isWrapperFor(Class<?> iface) throws SQLException {
			return originalConnection.isWrapperFor(iface);
		}

		/**
		 * @see java.sql.Connection#nativeSQL(java.lang.String)
		 */
		@Override
		public String nativeSQL(String sql) throws SQLException {
			return originalConnection.nativeSQL(sql);
		}

		/**
		 * @see java.sql.Connection#prepareCall(java.lang.String, int, int, int)
		 */
		@Override
		public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
			return originalConnection.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
		}

		/**
		 * @see java.sql.Connection#prepareCall(java.lang.String, int, int)
		 */
		@Override
		public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
			return originalConnection.prepareCall(sql, resultSetType, resultSetConcurrency);
		}

		/**
		 * @see java.sql.Connection#prepareCall(java.lang.String)
		 */
		@Override
		public CallableStatement prepareCall(String sql) throws SQLException {
			return originalConnection.prepareCall(sql);
		}

		/**
		 * @see java.sql.Connection#prepareStatement(java.lang.String, int, int,
		 *      int)
		 */
		@Override
		public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
			return originalConnection.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
		}

		/**
		 * @see java.sql.Connection#prepareStatement(java.lang.String, int, int)
		 */
		@Override
		public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
			return originalConnection.prepareStatement(sql, resultSetType, resultSetConcurrency);
		}

		/**
		 * @see java.sql.Connection#prepareStatement(java.lang.String, int)
		 */
		@Override
		public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
			return originalConnection.prepareStatement(sql, autoGeneratedKeys);
		}

		/**
		 * @see java.sql.Connection#prepareStatement(java.lang.String, int[])
		 */
		@Override
		public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
			return originalConnection.prepareStatement(sql, columnIndexes);
		}

		/**
		 * @see java.sql.Connection#prepareStatement(java.lang.String,
		 *      java.lang.String[])
		 */
		@Override
		public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
			return originalConnection.prepareStatement(sql, columnNames);
		}

		/**
		 * @see java.sql.Connection#prepareStatement(java.lang.String)
		 */
		@Override
		public PreparedStatement prepareStatement(String sql) throws SQLException {
			return originalConnection.prepareStatement(sql);
		}

		/**
		 * @see java.sql.Connection#releaseSavepoint(java.sql.Savepoint)
		 */
		@Override
		public void releaseSavepoint(Savepoint savepoint) throws SQLException {
			originalConnection.releaseSavepoint(savepoint);
		}

		/**
		 * @see java.sql.Connection#rollback()
		 */
		@Override
		public void rollback() throws SQLException {
			originalConnection.rollback();
		}

		/**
		 * @see java.sql.Connection#rollback(java.sql.Savepoint)
		 */
		@Override
		public void rollback(Savepoint savepoint) throws SQLException {
			originalConnection.rollback(savepoint);
		}

		/**
		 * @see java.sql.Connection#setAutoCommit(boolean)
		 */
		@Override
		public void setAutoCommit(boolean autoCommit) throws SQLException {
			originalConnection.setAutoCommit(autoCommit);
		}

		/**
		 * @see java.sql.Connection#setCatalog(java.lang.String)
		 */
		@Override
		public void setCatalog(String catalog) throws SQLException {
			originalConnection.setCatalog(catalog);
		}

		/**
		 * @see java.sql.Connection#setClientInfo(java.util.Properties)
		 */
		@Override
		public void setClientInfo(Properties properties) throws SQLClientInfoException {
			originalConnection.setClientInfo(properties);
		}

		/**
		 * @see java.sql.Connection#setClientInfo(java.lang.String,
		 *      java.lang.String)
		 */
		@Override
		public void setClientInfo(String name, String value) throws SQLClientInfoException {
			originalConnection.setClientInfo(name, value);
		}

		/**
		 * @see java.sql.Connection#setHoldability(int)
		 */
		@Override
		public void setHoldability(int holdability) throws SQLException {
			originalConnection.setHoldability(holdability);
		}

		/**
		 * @see java.sql.Connection#setReadOnly(boolean)
		 */
		@Override
		public void setReadOnly(boolean readOnly) throws SQLException {
			originalConnection.setReadOnly(readOnly);
		}

		/**
		 * @see java.sql.Connection#setSavepoint()
		 */
		@Override
		public Savepoint setSavepoint() throws SQLException {
			return originalConnection.setSavepoint();
		}

		/**
		 * @see java.sql.Connection#setSavepoint(java.lang.String)
		 */
		@Override
		public Savepoint setSavepoint(String name) throws SQLException {
			return originalConnection.setSavepoint(name);
		}

		/**
		 * @see java.sql.Connection#setTransactionIsolation(int)
		 */
		@Override
		public void setTransactionIsolation(int level) throws SQLException {
			originalConnection.setTransactionIsolation(level);
		}

		/**
		 * @see java.sql.Connection#setTypeMap(java.util.Map)
		 */
		@Override
		public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
			originalConnection.setTypeMap(map);
		}

		/**
		 * @see java.sql.Wrapper#unwrap(java.lang.Class)
		 */
		@Override
		public <T> T unwrap(Class<T> iface) throws SQLException {
			return originalConnection.unwrap(iface);
		}

	}

	/**
	 * Wrapper around a connection that was created with a username and a
	 * password. This wrapper also only overrides the {@link Connection#close}
	 * method but returns the connection to the appropriate connection pool in
	 * {@link PooledDataSource#usernamePasswordConnections}.
	 *
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	private class PooledUsernamePasswordConnection extends PooledConnection {

		/** The username of the connection. */
		private final String username;

		/** The password of the connection. */
		private final String password;

		/**
		 * Creates a new pooled connection that was created with a username and
		 * a password.
		 *
		 * @param originalConnection
		 *            The original connection to wrap
		 * @param username
		 *            The username the connection was created with
		 * @param password
		 *            The password the connection was created with
		 */
		public PooledUsernamePasswordConnection(Connection originalConnection, String username, String password) {
			super(originalConnection);
			this.username = username;
			this.password = password;
		}

		/**
		 * @see net.pterodactylus.util.database.PooledDataSource.PooledConnection#close()
		 */
		@Override
		@SuppressWarnings("synthetic-access")
		public void close() throws SQLException {
			if (!isValid(1)) {
				getOriginalConnection().close();
				return;
			}
			synchronized (usernamePasswordConnections) {
				usernamePasswordConnections.get(new Pair<String, String>(username, password)).add(this);
			}
		}

	}

}
