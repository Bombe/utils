/*
 * utils - ExtendedChoiceFormat.java - Copyright © 2012 David Roden
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

package net.pterodactylus.util.text;

import java.text.ChoiceFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import net.pterodactylus.util.collection.Pair;

/**
 * Number formatter that uses regular expressions to determine what text to show
 * for a given number. This can be used for languages that have more than two
 * plural forms, such as Polish which has three plurals that can not be mapped
 * using {@link ChoiceFormat}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class RegularExpressionNumberFormat extends NumberFormat {

	/** The patterns to match. */
	private final List<Pair<Pattern, String>> patterns = new ArrayList<Pair<Pattern, String>>();

	/** The default value. */
	private final String defaultValue;

	/**
	 * Creates a new regular expression number format.
	 *
	 * @param expression
	 *            The expression to parse for patterns and values
	 * @throws PatternSyntaxException
	 *             if the regular expressions can not be parsed
	 */
	public RegularExpressionNumberFormat(String expression) throws PatternSyntaxException {
		this(expression, '/', ';');
	}

	/**
	 * Creates a new regular expression number format.
	 *
	 * @param expression
	 *            The expression to parse for patterns and values
	 * @param separator
	 *            The separator between patterns and values
	 * @param patternSeparator
	 *            The separator between entries
	 * @throws PatternSyntaxException
	 *             if the regular expressions can not be parsed
	 */
	public RegularExpressionNumberFormat(String expression, char separator, char patternSeparator) throws PatternSyntaxException {
		String defaultValue = null;
		for (String pattern : expression.split(String.valueOf(patternSeparator))) {
			if (pattern.indexOf(separator) < 0) {
				defaultValue = pattern;
				break;
			}
			String regularExpression = pattern.substring(0, pattern.indexOf(separator));
			String value = pattern.substring(pattern.indexOf(separator) + 1);
			patterns.add(new Pair<Pattern, String>(Pattern.compile(regularExpression), value));
		}
		this.defaultValue = defaultValue;
	}

	//
	// NUMBERFORMAT METHODS
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {
		String numberString = String.valueOf(number);
		for (Pair<Pattern, String> pattern : patterns) {
			if (pattern.getLeft().matcher(numberString).matches()) {
				toAppendTo.append(pattern.getRight());
				return toAppendTo;
			}
		}
		if (defaultValue != null) {
			toAppendTo.append(defaultValue);
		}
		return toAppendTo;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition pos) {
		String numberString = String.valueOf(number);
		for (Pair<Pattern, String> pattern : patterns) {
			if (pattern.getLeft().matcher(numberString).matches()) {
				toAppendTo.append(pattern.getRight());
				return toAppendTo;
			}
		}
		if (defaultValue != null) {
			toAppendTo.append(defaultValue);
		}
		return toAppendTo;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Number parse(String source, ParsePosition parsePosition) {
		return null;
	}

}
