/*
 * utils - Notification.java - Copyright © 2010 David Roden
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

import java.io.IOException;
import java.io.Writer;
import java.util.Comparator;

import net.pterodactylus.util.io.Renderable;

/**
 * A notification can be used to keep track of things that a user needs to be
 * notified about.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public interface Notification extends Renderable {

	/** Sorts notifications by creation time, oldest first. */
	public static final Comparator<Notification> CREATED_TIME_SORTER = new Comparator<Notification>() {

		@Override
		public int compare(Notification leftNotification, Notification rightNotification) {
			return (int) Math.max(Integer.MIN_VALUE, Math.min(Integer.MAX_VALUE, leftNotification.getCreatedTime() - rightNotification.getCreatedTime()));
		}

	};

	/** Sorts notifications by last update time, newest first. */
	public static final Comparator<Notification> LAST_UPDATED_TIME_SORTER = new Comparator<Notification>() {

		@Override
		public int compare(Notification leftNotification, Notification rightNotification) {
			return (int) Math.max(Integer.MIN_VALUE, Math.min(Integer.MAX_VALUE, rightNotification.getLastUpdatedTime() - leftNotification.getLastUpdatedTime()));
		}

	};

	/**
	 * Adds the given notification listener.
	 *
	 * @param notificationListener
	 *            The listener to add
	 */
	public void addNotificationListener(NotificationListener notificationListener);

	/**
	 * Removes the given notification listener.
	 *
	 * @param notificationListener
	 *            The listener to remove
	 */
	public void removeNotificationListener(NotificationListener notificationListener);

	/**
	 * Returns the unique ID of this notification.
	 *
	 * @return The unique ID of this notification
	 */
	public String getId();

	/**
	 * Returns the time when this notifiation was last updated.
	 *
	 * @return The time when this notification was last updated (in milliseconds
	 *         since Jan 1 1970, UTC)
	 */
	public long getLastUpdatedTime();

	/**
	 * Returns the time when this notifiation was created.
	 *
	 * @return The time when this notification was created (in milliseconds
	 *         since Jan 1 1970, UTC)
	 */
	public long getCreatedTime();

	/**
	 * Returns whether this notification may be dismissed by the user.
	 *
	 * @return {@code true} if this notification is dismissable by the user,
	 *         {@code false} otherwise
	 */
	public boolean isDismissable();

	/**
	 * Dismisses this notification.
	 */
	public void dismiss();

	/**
	 * Renders this notification to the given writer.
	 *
	 * @param writer
	 *            The writer to render this notification to
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	@Override
	public void render(Writer writer) throws IOException;

}
