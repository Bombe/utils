/*
 * utils - ServiceMBean.java - Copyright © 2008-2009 David Roden
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
 * MBean interface for all {@link Service} implementations.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public interface ServiceMBean {

	/**
	 * Starts the service.
	 */
	public void start();

	/**
	 * Stops the service.
	 */
	public void stop();

	/**
	 * Returns the state of the service.
	 *
	 * @return The state of the service
	 */
	public State getState();

	/**
	 * Returns the current action of the service.
	 *
	 * @return The current action of the service
	 */
	public String getAction();

}
