/*
 * utils - DateFilter.java - Copyright © 2010 David Roden
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

package net.pterodactylus.util.template;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link Filter} implementation that formats a date. The date may be given
 * either as a {@link Date} or a {@link Long} object.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class DateFilter implements Filter {

	/** The date format cache. */
	private static final Map<String, DateFormat> dateFormats = new HashMap<String, DateFormat>();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String format(DataProvider dataProvider, Object data, Map<String, String> parameters) {
		String format = parameters.get("format");
		DateFormat dateFormat = getDateFormat(format);
		if (data instanceof Date) {
			return dateFormat.format((Date) data);
		} else if (data instanceof Long) {
			return dateFormat.format(new Date((Long) data));
		}
		return "";
	}

	//
	// PRIVATE METHODS
	//

	/**
	 * Returns a {@link DateFormat} for the given format. If the format is
	 * {@code null} or an empty {@link String}, a default {@link DateFormat}
	 * instance is returned.
	 *
	 * @param format
	 *            The format of the formatter
	 * @return A suitable date format
	 */
	private DateFormat getDateFormat(String format) {
		if ((format == null) || (format.trim().length() == 0)) {
			return DateFormat.getInstance();
		}
		DateFormat dateFormat = dateFormats.get(format);
		if (dateFormat == null) {
			dateFormat = new SimpleDateFormat(format);
			dateFormats.put(format, dateFormat);
		}
		return dateFormat;
	}

}
