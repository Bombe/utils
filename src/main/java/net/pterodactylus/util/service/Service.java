/*
 * utils - Service.java - Copyright © 2006-2009 David Roden
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
 * Interface for services. Services are the workhorses of every application. On
 * application startup, services are started and begin interacting with each
 * other, thus forming the application. Services can also contain “service
 * attributes” which are basically {@link Object}s mapped to {@link String}s.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public interface Service extends ServiceMBean {

	/**
	 * Returns the name of this service.
	 *
	 * @return The name of this service
	 */
	public String getName();

	/**
	 * Returns the current state of this service.
	 *
	 * @return The current state of this service
	 */
	@Override
	public State getState();

	/**
	 * Returns the reason for the current state. The reason will never be
	 * <code>null</code>.
	 *
	 * @deprecated Use {@link #getAction()} instead
	 * @return The reason for the current state
	 */
	@Deprecated
	public String getStateReason();

	/**
	 * Returns the current action of the service.
	 *
	 * @see net.pterodactylus.util.service.ServiceMBean#getAction()
	 * @return The current action of the service
	 */
	@Override
	public String getAction();

	/**
	 * Adds the given service listener to the list of service listeners that
	 * will be notified on service events.
	 *
	 * @param serviceListener
	 *            The service listener to add
	 */
	public void addServiceListener(ServiceListener serviceListener);

	/**
	 * Removes the given service listeners from the list of service listeners.
	 *
	 * @param serviceListener
	 *            The service listener to remove
	 */
	public void removeServiceListener(ServiceListener serviceListener);

	/**
	 * Initializes the service.
	 */
	public void init();

	/**
	 * Starts the service.
	 */
	@Override
	public void start();

	/**
	 * Stops the service.
	 */
	@Override
	public void stop();

	/**
	 * Destroys the service and frees all used resources.
	 */
	public void destroy();

	/**
	 * Sets the service attribute with the given name to the given value.
	 *
	 * @param attributeName
	 *            The name of the attribute
	 * @param attributeValue
	 *            The value of the attribute
	 */
	public void setServiceAttribute(String attributeName, Object attributeValue);

	/**
	 * Returns the value of the service attribute with the given name.
	 *
	 * @param attributeName
	 *            The name of the attribute
	 * @return The value of the attribute
	 */
	public Object getServiceAttribute(String attributeName);

	/**
	 * Returns whether this service contains a service attribute with the given
	 * name.
	 *
	 * @param attributeName
	 *            The name of the attribute
	 * @return <code>true</code> if this service has a service attribute with
	 *         the given name, <code>false</code> otherwise
	 */
	public boolean hasServiceAttribute(String attributeName);

}
