/*
 * utils - NotificationListenerManager.java - Copyright © 2010 David Roden
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

import net.pterodactylus.util.event.AbstractListenerManager;

/**
 * Manager for {@link NotificationListener}s and {@link Notification} events.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class NotificationListenerManager extends AbstractListenerManager<Notification, NotificationListener> {

	/**
	 * Creates a new {@link NotificationListener} manager.
	 *
	 * @param source
	 *            The notification that emits all events
	 */
	public NotificationListenerManager(Notification source) {
		super(source);
	}

	//
	// ACTIONS
	//

	/**
	 * Notifies all listeners that a notification was dismissed.
	 *
	 * @see NotificationListener#notificationDismissed(Notification)
	 */
	void fireNotificationDismissed() {
		for (NotificationListener notificationListener : getListeners()) {
			notificationListener.notificationDismissed(getSource());
		}
	}

}
