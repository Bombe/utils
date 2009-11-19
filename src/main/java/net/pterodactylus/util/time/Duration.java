/*
 * utils - Duration.java - Copyright © 2006-2009 David Roden
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

package net.pterodactylus.util.time;

/**
 * A duration is the length between two events in time.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Duration {

	/** This duration in milliseconds. */
	private final long duration;

	/** The number of whole weeks in this duration. */
	private final long weeks;

	/** The number of days in the last week of this duration. */
	private final int days;

	/** The number of hours in the last day of this duration. */
	private final int hours;

	/** The number of minutes in the last hour of this duration. */
	private final int minutes;

	/** The number of seconds in the last minute of this duration. */
	private final int seconds;

	/** The number of milliseconds in the last second of this duration. */
	private final int milliseconds;

	/**
	 * Creates a new duration with the specified length.
	 *
	 * @param duration
	 *            The length of this duration in milliseconds
	 */
	public Duration(long duration) {
		this.duration = duration;
		milliseconds = (int) (duration % 1000);
		seconds = (int) ((duration / 1000) % 60);
		minutes = (int) ((duration / (1000 * 60)) % 60);
		hours = (int) ((duration / (1000 * 60 * 60)) % 24);
		days = (int) ((duration / (1000 * 60 * 60 * 24)) % 7);
		weeks = duration / (1000 * 60 * 60 * 24 * 7);
	}

	/**
	 * Returns the number of days in the last week of this duration
	 *
	 * @return The number of days
	 */
	public int getDays() {
		return days;
	}

	/**
	 * Returns the length of this duration.
	 *
	 * @return The length of this duration (in milliseconds)
	 */
	public long getDuration() {
		return duration;
	}

	/**
	 * Returns the number of hours in the last day of this duration.
	 *
	 * @return The number of hours
	 */
	public int getHours() {
		return hours;
	}

	/**
	 * Returns the number of milliseconds in the last second of this duration.
	 *
	 * @return The number of milliseconds
	 */
	public int getMilliseconds() {
		return milliseconds;
	}

	/**
	 * Returns the number of minutes in the last hour of this duration.
	 *
	 * @return The number of minutes
	 */
	public int getMinutes() {
		return minutes;
	}

	/**
	 * Returns the number of seconds in the last minute of this duration.
	 *
	 * @return The number of seconds
	 */
	public int getSeconds() {
		return seconds;
	}

	/**
	 * Returns the number of whole weeks in this duration.
	 *
	 * @return The number of weeks
	 */
	public long getWeeks() {
		return weeks;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return toString(true);
	}

	/**
	 * Returns a textual representation of this duration.
	 *
	 * @param showMilliseconds
	 *            <code>true</code> if the milliseconds should be shown,
	 *            <code>false</code> otherwise
	 * @return The textual representation of this duration
	 */
	public String toString(boolean showMilliseconds) {
		StringBuilder durationBuilder = new StringBuilder();
		if ((milliseconds != 0) && showMilliseconds) {
			int ms = milliseconds;
			while ((ms % 10) == 0) {
				ms /= 10;
			}
			durationBuilder.append(seconds).append('.').append(ms).append('s');
		} else if (seconds != 0) {
			durationBuilder.append(seconds).append('s');
		} else if ((minutes == 0) && (hours == 0) && (days == 0) && (weeks == 0)) {
			durationBuilder.append("0s");
		}
		if (minutes != 0) {
			durationBuilder.insert(0, "m ").insert(0, minutes);
		}
		if (hours != 0) {
			durationBuilder.insert(0, "h ").insert(0, hours);
		}
		if (days != 0) {
			durationBuilder.insert(0, "d ").insert(0, days);
		}
		if (weeks != 0) {
			durationBuilder.insert(0, "w ").insert(0, weeks);
		}
		return durationBuilder.toString();
	}

}
