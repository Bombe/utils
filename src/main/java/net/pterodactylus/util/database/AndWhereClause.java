/*
 * utils - AndWhereClause.java - Copyright © 2010 David Roden
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

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * {@link WhereClause} implementation that performs an AND conjunction of an
 * arbitrary amount of where clauses.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class AndWhereClause implements WhereClause {

	/** The list of where clauses. */
	private final List<WhereClause> whereClauses = new ArrayList<WhereClause>();

	/**
	 * Creates a new AND where clause.
	 */
	public AndWhereClause() {
		/* do nothing. */
	}

	/**
	 * Creates a new AND where clause.
	 *
	 * @param whereClauses
	 *            The where clauses to connect
	 */
	public AndWhereClause(WhereClause... whereClauses) {
		for (WhereClause whereClause : whereClauses) {
			this.whereClauses.add(whereClause);
		}
	}

	/**
	 * Creates a new AND where clause.
	 *
	 * @param whereClauses
	 *            The where clauses to connect
	 */
	public AndWhereClause(Collection<WhereClause> whereClauses) {
		this.whereClauses.addAll(whereClauses);
	}

	/**
	 * Adds the given WHERE clause.
	 *
	 * @param whereClause
	 *            The WHERE clause to add
	 * @return This WHERE clause (to allow method chaining)
	 */
	public AndWhereClause add(WhereClause whereClause) {
		whereClauses.add(whereClause);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Parameter<?>> getParameters() {
		ArrayList<Parameter<?>> parameters = new ArrayList<Parameter<?>>();
		for (WhereClause whereClause : whereClauses) {
			parameters.addAll(whereClause.getParameters());
		}
		return parameters;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void render(Writer writer) throws IOException {
		if (whereClauses.isEmpty()) {
			writer.write("(1 = 1)");
		}
		boolean first = true;
		for (WhereClause whereClause : whereClauses) {
			if (!first) {
				writer.write(" AND ");
			}
			writer.write("(");
			whereClause.render(writer);
			writer.write(")");
			first = false;
		}
	}

}
