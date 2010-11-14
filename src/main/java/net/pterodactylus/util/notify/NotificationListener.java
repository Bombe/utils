/*
 * utils - NotificationListener.java - Copyright © 2010 David Roden
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

package net.pterodactylus.util.notify;

import java.util.EventListener;

/**
 * Listener interface for objects that want to be notified on certain
 * {@link Notification} events, such as when the notification is dismissed.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public interface NotificationListener extends EventListener {

	/**
	 * Notifies a listener that the given notification was dismissed.
	 *
	 * @param notification
	 *            The dismissed notification
	 */
	public void notificationDismissed(Notification notification);

}
