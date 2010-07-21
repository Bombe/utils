/*
 * utils - State.java - Copyright © 2006-2009 David Roden
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

package net.pterodactylus.util.service;

/**
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class State implements Comparable<State> {

	/** Basic State for services that are not running. */
	public static final State offline = new State("offline", 0);

	/** Basic State for services that are starting. */
	public static final State starting = new State("starting", 1);

	/** Basic State for services that are running. */
	public static final State online = new State("online", 2);

	/** Basic state for services that are stopping. */
	public static final State stopping = new State("stopping", 3);

	/** Basic static for services in an unknown state. */
	public static final State unknown = new State("unknown", 4);

	/** The basic state of this state. */
	private final State basicState;

	/** The name of the state. */
	private final String name;

	/** The ordinal number of the state. */
	private final int ordinal;

	/**
	 * Constructs a new basic state with the given name and ordinal.
	 *
	 * @param name
	 *            The name of the new basic state
	 * @param ordinal
	 *            The ordinal of the new basic state
	 */
	private State(String name, int ordinal) {
		this.basicState = this;
		this.name = name;
		this.ordinal = ordinal;
	}

	/**
	 * Constructs a new state that is derived from the given basic state and has
	 * the given name.
	 *
	 * @param basicState
	 *            The basic state of the new state
	 * @param name
	 *            The name of the new state
	 */
	public State(State basicState, String name) {
		if (basicState == null) {
			throw new IllegalArgumentException("basic state must not be null");
		}
		this.basicState = basicState;
		this.name = name;
		this.ordinal = basicState.ordinal;
	}

	/**
	 * Returns the basic state of this state. If this state is one of the
	 * predefined basic state, the state itself is returned so that this method
	 * <em>never</em> returns <code>null</code>.
	 *
	 * @return The basic state of this state
	 */
	public State getBasicState() {
		return basicState;
	}

	/**
	 * Returns the name of this state.
	 *
	 * @return The name of this state
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the ordinal number of this state. If this state is a derived
	 * state the ordinal of the basic state is returned.
	 *
	 * @return The ordinal of this state
	 */
	public int getOrdinal() {
		return ordinal;
	}

	//
	// INTERFACE Comparable<State>
	//

	/**
	 * Compares this state to the given state. A State is considered as smaller
	 * than another state if the basic state’s ordinal of the first state is
	 * smaller than the second state’s basic state’s ordinal.
	 *
	 * @param state
	 *            The state that should be compared with this state
	 * @return A negative number if this state’s ordinal is smaller than the
	 *         ordinal of the given state’s basic state
	 */
	@Override
	public int compareTo(State state) {
		return this.basicState.ordinal - state.basicState.ordinal;
	}

	/**
	 * Returns a textual representation of this state, consisting of the name of
	 * this state and, if different, the name of its basic state.
	 *
	 * @return A textual representation of this state
	 */
	@Override
	public String toString() {
		if (this != basicState) {
			return name + " (" + basicState + ")";
		}
		return name;
	}

}
