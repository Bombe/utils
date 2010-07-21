/*
 * utils - MapCreator.java - Copyright © 2009 David Roden
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
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link ObjectCreator} implementation that creates a {@link Map} from the
 * current row of a {@link ResultSet}. The display names of the columns are used
 * as keys, the values are mapped to objects of a fitting type.
 * <p>
 * The following {@link Types} are currently mapped:
 * <ul>
 * <li>{@link Types#INTEGER} is mapped to {@link Integer}.</li>
 * <li>{@link Types#BIGINT} is mapped to {@link Long}.</li>
 * <li>{@link Types#BOOLEAN} is mapped to {@link Boolean}.</li>
 * <li>{@link Types#VARCHAR} is mapped to {@link String}.</li>
 * </ul>
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class MapCreator implements ObjectCreator<Map<String, Object>> {

	/**
	 * @see net.pterodactylus.util.database.ObjectCreator#createObject(java.sql.ResultSet)
	 */
	@Override
	public Map<String, Object> createObject(ResultSet resultSet) throws SQLException {
		Map<String, Object> result = new HashMap<String, Object>();
		ResultSetMetaData metadata = resultSet.getMetaData();
		for (int column = 1; column <= metadata.getColumnCount(); column++) {
			int columnType = metadata.getColumnType(column);
			String columnLabel = metadata.getColumnLabel(column);
			if (columnType == Types.INTEGER) {
				result.put(columnLabel, resultSet.getInt(column));
			} else if (columnType == Types.BIGINT) {
				result.put(columnLabel, resultSet.getLong(column));
			} else if (columnType == Types.DECIMAL) {
				result.put(columnLabel, resultSet.getDouble(column));
			} else if (columnType == Types.BOOLEAN) {
				result.put(columnLabel, resultSet.getBoolean(column));
			} else if (columnType == Types.VARCHAR) {
				result.put(columnLabel, resultSet.getString(column));
			} else if (columnType == Types.LONGVARCHAR) {
				result.put(columnLabel, resultSet.getString(column));
			} else if (columnType == Types.DATE) {
				result.put(columnLabel, resultSet.getDate(column));
			} else {
				System.out.println("unknown type (" + columnType + ") for column “" + columnLabel + "”.");
			}
		}
		return result;
	}

}
