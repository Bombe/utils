/*
 * utils - URLDataSource.java - Copyright © 2010 David Roden
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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

/**
 * {@link DataSource} implementation that creates connections from a JDBC URL
 * using {@link DriverManager#getConnection(String)}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class URLDataSource implements DataSource {

	/** The URL to connect to. */
	private final String connectionUrl;

	/** The log writer. */
	private PrintWriter logWriter;

	/** The login timeout. */
	private int loginTimeout;

	/** The login properties. */
	private final Properties loginProperties = new Properties();

	/**
	 * Creates a URL data source.
	 *
	 * @param connectionUrl
	 *            The URL to connect to
	 */
	public URLDataSource(String connectionUrl) {
		this.connectionUrl = connectionUrl;
		loginProperties.setProperty("connectTimeout", "0");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return logWriter;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLogWriter(PrintWriter logWriter) throws SQLException {
		this.logWriter = logWriter;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLoginTimeout(int loginTimeout) throws SQLException {
		this.loginTimeout = loginTimeout;
		loginProperties.setProperty("connectTimeout", String.valueOf(loginTimeout * 1000L));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getLoginTimeout() throws SQLException {
		return loginTimeout;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		throw new SQLException("No wrapped object found for " + iface.getClass().getName() + ".");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(connectionUrl, loginProperties);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		Properties userProperties = new Properties(loginProperties);
		userProperties.setProperty("user", username);
		userProperties.setProperty("password", password);
		return DriverManager.getConnection(connectionUrl, username, password);
	}

}
