/*
 * utils - NotificationManager.java - Copyright © 2010–2019 David Roden
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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Manager for all current notifications.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class NotificationManager implements NotificationListener {

	/** All notifications. */
	private final Map<String, Notification> notifications = new HashMap<String, Notification>();

	//
	// ACCESSORS
	//

	/**
	 * Returns all current notifications.
	 *
	 * @return All current notifications
	 */
	public Set<Notification> getNotifications() {
		synchronized (notifications) {
			return new HashSet<Notification>(notifications.values());
		}
	}

	/**
	 * Returns the notification with the given ID.
	 *
	 * @param notificationId
	 *            The ID of the notification
	 * @return The notification, or {@code null} if there is no notification
	 *         with the given ID
	 */
	public Notification getNotification(String notificationId) {
		synchronized (notifications) {
			return notifications.get(notificationId);
		}
	}

	/**
	 * Adds the given notification.
	 *
	 * @param notification
	 *            The notification to add
	 */
	public void addNotification(Notification notification) {
		synchronized (notifications) {
			if (!notifications.containsKey(notification.getId())) {
				notifications.put(notification.getId(), notification);
				notification.addNotificationListener(this);
			}
		}
	}

	/**
	 * Removes the given notification.
	 *
	 * @param notification
	 *            The notification to remove
	 */
	public void removeNotification(Notification notification) {
		synchronized (notifications) {
			if (notifications.containsKey(notification.getId())) {
				notifications.remove(notification.getId());
				notification.removeNotificationListener(this);
			}
		}
	}

	//
	// NOTIFICATIONLISTENER METHODS
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void notificationDismissed(Notification notification) {
		removeNotification(notification);
	}

}
