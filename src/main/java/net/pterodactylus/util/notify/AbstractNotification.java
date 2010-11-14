/*
 * utils - AbstractNotification.java - Copyright © 2010 David Roden
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

import java.util.UUID;

/**
 * Abstract base implementation of a {@link Notification} that takes care of
 * everything but creating the text of the notification, so only
 * {@link Notification#render(java.io.Writer)} needs to be override by
 * subclasses.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public abstract class AbstractNotification implements Notification {

	/** The listener manager. */
	private final NotificationListenerManager notificationListenerManager = new NotificationListenerManager(this);

	/** The ID of this notification. */
	private final String id;

	/** The time when this notification was created. */
	private final long createdTime;

	/** The time when this notification was last updated. */
	private volatile long lastUpdatedTime;

	/** Whether this notification is dismissable. */
	private volatile boolean dismissable;

	/**
	 * Creates a new notification with a random ID, the current time as creation
	 * and last udpate time and is dismissable by the user.
	 */
	public AbstractNotification() {
		this(UUID.randomUUID().toString());
	}

	/**
	 * Creates a new notification with the current time as creation and last
	 * udpate time and is dismissable by the user.
	 *
	 * @param id
	 *            The ID of the notification
	 */
	public AbstractNotification(String id) {
		this(id, System.currentTimeMillis());
	}

	/**
	 * Creates a new notification with the given time as creation and last
	 * update time and is dismissable by the user.
	 *
	 * @param id
	 *            The ID of the notification
	 * @param createdTime
	 *            The time when this notification was created
	 */
	public AbstractNotification(String id, long createdTime) {
		this(id, createdTime, createdTime);
	}

	/**
	 * Creates a new notification with the given creation and last update time
	 * and is dismissable by the user.
	 *
	 * @param id
	 *            The ID of the notification
	 * @param createdTime
	 *            The time when this notification was created
	 * @param lastUpdatedTime
	 *            The time when this notification was last udpated
	 */
	public AbstractNotification(String id, long createdTime, long lastUpdatedTime) {
		this(id, createdTime, lastUpdatedTime, true);
	}

	/**
	 * Creates a new notification.
	 *
	 * @param id
	 *            The ID of the notification
	 * @param createdTime
	 *            The time when this notification was created
	 * @param lastUpdatedTime
	 *            The time when this notification was last udpated
	 * @param dismissable
	 *            {@code true} if this notification is dismissable by the user
	 */
	public AbstractNotification(String id, long createdTime, long lastUpdatedTime, boolean dismissable) {
		this.id = id;
		this.createdTime = createdTime;
		this.lastUpdatedTime = lastUpdatedTime;
		this.dismissable = dismissable;
	}

	//
	// LISTENER MANAGEMENT
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addNotificationListener(NotificationListener notificationListener) {
		notificationListenerManager.addListener(notificationListener);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeNotificationListener(NotificationListener notificationListener) {
		notificationListenerManager.removeListener(notificationListener);
	}

	//
	// ACCESSORS
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getId() {
		return id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getCreatedTime() {
		return createdTime;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getLastUpdatedTime() {
		return lastUpdatedTime;
	}

	/**
	 * Sets the last updated time.
	 *
	 * @param lastUpdateTime
	 *            The time this notification was last updated
	 */
	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdatedTime = lastUpdateTime;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isDismissable() {
		return dismissable;
	}

	/**
	 * Sets whether this notification is dismissable.
	 *
	 * @param dismissable
	 *            {@code true} if this notification is dismissable
	 */
	public void setDismissable(boolean dismissable) {
		this.dismissable = dismissable;
	}

	//
	// ACTIONS
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dismiss() {
		notificationListenerManager.fireNotificationDismissed();
	}

	/**
	 * Updates the {@link #getLastUpdatedTime() last update time} to the current
	 * time.
	 */
	protected void touch() {
		setLastUpdateTime(System.currentTimeMillis());
	}

}
