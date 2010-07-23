/*
 * Copyright © 2010 knorpelfisk.de
 */

package net.pterodactylus.util.database;


/**
 * A value field stores its name and its value.
 *
 * @author <a href="mailto:dr@knorpelfisk.de">David Roden</a>
 */
public class ValueField extends Field {

	/** The value of the field */
	private final Parameter<?> parameter;

	/**
	 * Creates a new value field.
	 *
	 * @param name
	 *            The name of the field
	 * @param parameter
	 *            The value of the field
	 */
	public ValueField(String name, Parameter<?> parameter) {
		super(name);
		this.parameter = parameter;
	}

	/**
	 * Returns the value of this field.
	 *
	 * @return The value of this field
	 */
	public Parameter<?> getParameter() {
		return parameter;
	}

}
