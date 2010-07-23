/*
 * utils - DatabaseTest.java - Copyright © 2010 David Roden
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

package net.pterodactylus.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import junit.framework.TestCase;
import net.pterodactylus.util.database.AbstractDatabase;
import net.pterodactylus.util.database.Database;
import net.pterodactylus.util.database.DatabaseException;
import net.pterodactylus.util.database.Parameter.StringParameter;
import net.pterodactylus.util.database.Query;
import net.pterodactylus.util.database.Query.Type;
import net.pterodactylus.util.database.ResultProcessor;
import net.pterodactylus.util.database.ValueField;
import net.pterodactylus.util.database.ValueFieldWhereClause;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

/**
 * TODO
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class DatabaseTest extends TestCase {

	public void testSelect() throws DatabaseException {
		MysqlDataSource dataSource = new MysqlDataSource();
		dataSource.setServerName("localhost");
		dataSource.setUser("root");
		dataSource.setPassword("admin64");
		Database database = AbstractDatabase.fromDataSource(dataSource);
		Query query = new Query(Type.SELECT, "mysql.user");
		query.addWhereClause(new ValueFieldWhereClause(new ValueField("User", new StringParameter("root"))));
		database.process(query, new ResultProcessor() {

			@Override
			public void processResult(ResultSet resultSet) throws SQLException {
				System.out.println(resultSet.getString("Password"));
			}
		});
	}

}
