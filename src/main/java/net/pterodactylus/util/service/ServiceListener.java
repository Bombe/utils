/*
 * utils - ServiceListener.java - Copyright © 2006-2009 David Roden
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

import java.util.EventListener;

/**
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public interface ServiceListener extends EventListener {

	/**
	 * Notifies listeners that a {@link Service} has been started.
	 *
	 * @param service
	 *            The service that started
	 */
	public void serviceStarted(Service service);

	/**
	 * Notifies listeners that a {@link Service} has changed its {@link State}.
	 *
	 * @param service
	 *            The service that changed its state
	 * @param oldState
	 *            The old state of the service
	 * @param newState
	 *            The new state of the service
	 */
	public void serviceStateChanged(Service service, State oldState, State newState);

	/**
	 * Notifies listeners that a {@link Service} has been stopped.
	 *
	 * @param service
	 *            The service that stopped
	 * @param cause
	 *            The cause for stopping, will be <code>null</code> if service
	 *            stopped normally
	 */
	public void serviceStopped(Service service, Throwable cause);

}
