/*
 * utils - ServiceListenerManager.java - Copyright © 2008-2009 David Roden
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

import net.pterodactylus.util.event.AbstractListenerManager;

/**
 * Listener manager for {@link ServiceListener}s.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ServiceListenerManager extends AbstractListenerManager<Service, ServiceListener> {

	/**
	 * Creates a new listener manager for {@link ServiceListener}s.
	 *
	 * @param service
	 *            The source service
	 */
	public ServiceListenerManager(Service service) {
		super(service);
	}

	/**
	 * Notifies listeners that a {@link Service} has been started.
	 */
	public void fireServiceStarted() {
		for (ServiceListener serviceListener : getListeners()) {
			serviceListener.serviceStarted(getSource());
		}
	}

	/**
	 * Notifies listeners that a {@link Service} has changed its {@link State}.
	 *
	 * @param oldState
	 *            The old state of the service
	 * @param newState
	 *            The new state of the service
	 */
	public void fireServiceStateChanged(State oldState, State newState) {
		for (ServiceListener serviceListener : getListeners()) {
			serviceListener.serviceStateChanged(getSource(), oldState, newState);
		}
	}

	/**
	 * Notifies listeners that a {@link Service} has been stopped.
	 *
	 * @param cause
	 *            The cause for stopping, will be <code>null</code> if service
	 *            stopped normally
	 */
	public void fireServiceStopped(Throwable cause) {
		for (ServiceListener serviceListener : getListeners()) {
			serviceListener.serviceStopped(getSource(), cause);
		}
	}

}
