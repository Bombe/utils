/*
 * utils - ValueFieldWhereClause.java - Copyright © 2010 David Roden
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
import java.util.Arrays;
import java.util.List;

/**
 * A WHERE clause that requires a field to match a value.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ValueFieldWhereClause implements WhereClause {

	/** The field and the value to match. */
	private final ValueField valueField;

	/** Whether to invert the result. */
	private final boolean invert;

	/**
	 * Creates a new WHERE clause that checks a field for the given value.
	 *
	 * @param valueField
	 *            The field and the value to check for
	 */
	public ValueFieldWhereClause(ValueField valueField) {
		this(valueField, false);
	}

	/**
	 * Creates a new WHERE clause that checks a field for the given value.
	 *
	 * @param valueField
	 *            The field and the value to check for
	 * @param invert
	 *            {@code true} to invert the result
	 */
	public ValueFieldWhereClause(ValueField valueField, boolean invert) {
		this.valueField = valueField;
		this.invert = invert;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Parameter<?>> getParameters() {
		return Arrays.<Parameter<?>> asList(valueField.getParameter());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void render(Writer writer) throws IOException {
		writer.write("(" + valueField.getName() + " " + (invert ? "!=" : "=") + " ?)");
	}

}
