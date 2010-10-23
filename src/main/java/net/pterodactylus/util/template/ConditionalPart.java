/*
 * utils - ConditionalPart.java - Copyright © 2010 David Roden
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

import java.io.Writer;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

/**
 * {@link ContainerPart} implementation that determines at render time whether
 * it should be rendered or not.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
class ConditionalPart extends ContainerPart {

	/** The condition. */
	private final Condition condition;

	/**
	 * Creates a new conditional part.
	 *
	 * @param condition
	 *            The condition
	 */
	public ConditionalPart(Condition condition) {
		super();
		this.condition = condition;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void render(DataProvider dataProvider, Writer writer) throws TemplateException {
		if (condition.isAllowed(dataProvider)) {
			super.render(dataProvider, writer);
		}
	}

	/**
	 * Condition that decides whether a {@link ConditionalPart} is rendered at
	 * render time.
	 *
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	public interface Condition {

		/**
		 * Returns whether the condition is fulfilled.
		 *
		 * @param dataProvider
		 *            The data provider
		 * @return {@code true} if the condition is fulfilled, {@code false}
		 *         otherwise
		 * @throws TemplateException
		 *             if a template variable can not be parsed or evaluated
		 */
		public boolean isAllowed(DataProvider dataProvider) throws TemplateException;

	}

	/**
	 * {@link Condition} implements that inverts another condition.
	 *
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	public static class NotCondition implements Condition {

		/** The condition to invert. */
		private final Condition condition;

		/**
		 * Creates a inverting condition.
		 *
		 * @param condition
		 *            The condition to invert
		 */
		public NotCondition(Condition condition) {
			this.condition = condition;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isAllowed(DataProvider dataProvider) throws TemplateException {
			return !condition.isAllowed(dataProvider);
		}

	}

	/**
	 * {@link Condition} implementation that only returns true if all its
	 * conditions are true also.
	 *
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	public static class AndCondition implements Condition {

		/** The conditions. */
		private final Collection<Condition> conditions;

		/**
		 * Creates a new AND condition.
		 *
		 * @param conditions
		 *            The conditions
		 */
		public AndCondition(Condition... conditions) {
			this(Arrays.asList(conditions));
		}

		/**
		 * Creates a new AND condition.
		 *
		 * @param conditions
		 *            The conditions
		 */
		public AndCondition(Collection<Condition> conditions) {
			this.conditions = conditions;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isAllowed(DataProvider dataProvider) throws TemplateException {
			for (Condition condition : conditions) {
				if (!condition.isAllowed(dataProvider)) {
					return false;
				}
			}
			return true;
		}

	}

	/**
	 * {@link Condition} implementation that only returns false if all its
	 * conditions are false also.
	 *
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	public static class OrCondition implements Condition {

		/** The conditions. */
		private final Collection<Condition> conditions;

		/**
		 * Creates a new OR condition.
		 *
		 * @param conditions
		 *            The conditions
		 */
		public OrCondition(Condition... conditions) {
			this(Arrays.asList(conditions));
		}

		/**
		 * Creates a new OR condition.
		 *
		 * @param conditions
		 *            The conditions
		 */
		public OrCondition(Collection<Condition> conditions) {
			this.conditions = conditions;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isAllowed(DataProvider dataProvider) throws TemplateException {
			for (Condition condition : conditions) {
				if (condition.isAllowed(dataProvider)) {
					return true;
				}
			}
			return false;
		}

	}

	/**
	 * {@link Condition} implementation that asks the {@link DataProvider} for a
	 * {@link Boolean} value.
	 *
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	public static class DataCondition implements Condition {

		/** Whether to invert the result. */
		private final boolean invert;

		/** The name of the data item to check. */
		private final String itemName;

		/**
		 * Creates a new data condition.
		 *
		 * @param itemName
		 *            The name of the item to check
		 */
		public DataCondition(String itemName) {
			this(itemName, false);
		}

		/**
		 * Creates a new data condition.
		 *
		 * @param itemName
		 *            The name of the item to check
		 * @param invert
		 *            {@code true} to invert the result, {@code false} otherwise
		 */
		public DataCondition(String itemName, boolean invert) {
			this.invert = invert;
			this.itemName = itemName;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isAllowed(DataProvider dataProvider) throws TemplateException {
			return Boolean.valueOf(String.valueOf(dataProvider.getData(itemName))) ^ invert;
		}

	}

	/**
	 * {@link Condition} implementation that checks a given text for a
	 * {@link Boolean} value.
	 *
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	public static class DataTextCondition implements Condition {

		/** Whether to invert the result. */
		private final boolean invert;

		/** The text to check. */
		private final String text;

		/**
		 * Creates a new data condition.
		 *
		 * @param text
		 *            The text to check
		 */
		public DataTextCondition(String text) {
			this(text, false);
		}

		/**
		 * Creates a new data condition.
		 *
		 * @param text
		 *            The text to check
		 * @param invert
		 *            {@code true} to invert the result, {@code false} otherwise
		 */
		public DataTextCondition(String text, boolean invert) {
			this.invert = invert;
			this.text = text;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isAllowed(DataProvider dataProvider) throws TemplateException {
			return Boolean.valueOf(text) ^ invert;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			return "(" + text + " = " + !invert + ")";
		}

	}

	/**
	 * {@link Condition} implementation that asks the {@link DataProvider} for a
	 * {@link Boolean} value and checks whether it’s {@code null} or not.
	 *
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	public static class NullDataCondition implements Condition {

		/** Whether to invert the result. */
		private final boolean invert;

		/** The name of the data item to check. */
		private final String itemName;

		/**
		 * Creates a new data condition.
		 *
		 * @param itemName
		 *            The name of the item to check
		 */
		public NullDataCondition(String itemName) {
			this(itemName, false);
		}

		/**
		 * Creates a new data condition.
		 *
		 * @param itemName
		 *            The name of the item to check
		 * @param invert
		 *            {@code true} to invert the result, {@code false} otherwise
		 */
		public NullDataCondition(String itemName, boolean invert) {
			this.invert = invert;
			this.itemName = itemName;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isAllowed(DataProvider dataProvider) throws TemplateException {
			return (dataProvider.getData(itemName) == null) ^ invert;
		}

	}

	/**
	 * {@link Condition} implementation that filters the value from the data
	 * provider before checking whether it matches “true.”
	 *
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	public static class FilterCondition implements Condition {

		/** The name of the item. */
		private final String itemName;

		/** The filters. */
		private final Collection<Filter> filters;

		/** The filter parameters. */
		private final Map<Filter, Map<String, String>> filterParameters;

		/** Whether to invert the result. */
		private final boolean invert;

		/**
		 * Creates a new filter condition.
		 *
		 * @param itemName
		 *            The name of the item
		 * @param filters
		 *            The filters to run through
		 * @param filterParameters
		 *            The filter parameters
		 */
		public FilterCondition(String itemName, Collection<Filter> filters, Map<Filter, Map<String, String>> filterParameters) {
			this(itemName, filters, filterParameters, false);
		}

		/**
		 * Creates a new filter condition.
		 *
		 * @param itemName
		 *            The name of the item
		 * @param filters
		 *            The filters to run through
		 * @param filterParameters
		 *            The filter parameters
		 * @param invert
		 *            {@code true} to invert the result
		 */
		public FilterCondition(String itemName, Collection<Filter> filters, Map<Filter, Map<String, String>> filterParameters, boolean invert) {
			this.itemName = itemName;
			this.filters = filters;
			this.filterParameters = filterParameters;
			this.invert = invert;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isAllowed(DataProvider dataProvider) throws TemplateException {
			Object data = dataProvider.getData(itemName);
			for (Filter filter : filters) {
				data = filter.format(dataProvider, data, filterParameters.get(filter));
			}
			return Boolean.valueOf(String.valueOf(data)) ^ invert;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("([").append(itemName).append("]");
			for (Filter filter : filters) {
				stringBuilder.append("|").append(filter.getClass().getSimpleName());
				if (filterParameters.containsKey(filter)) {
					for (Entry<String, String> filterParameter : filterParameters.get(filter).entrySet()) {
						stringBuilder.append(" ").append(filterParameter.getKey()).append("=").append(filterParameter.getValue());
					}
				}
			}
			return stringBuilder.append(" = ").append(!invert).append(")").toString();
		}

	}

	/**
	 * {@link Condition} implementation that filters a given text before
	 * checking whether it matches “true.”
	 *
	 * @author <a href="mailto:david.roden@sysart.de">David Roden</a>
	 */
	public static class FilterTextCondition implements Condition {

		/** The text to filter. */
		private final String text;

		/** The filters. */
		private final Collection<Filter> filters;

		/** The filter parameters. */
		private final Map<Filter, Map<String, String>> filterParameters;

		/** Whether to invert the result. */
		private final boolean invert;

		/**
		 * Creates a new filter text condition.
		 *
		 * @param text
		 *            The text to filter
		 * @param filters
		 *            The filters to run through
		 * @param filterParameters
		 *            The filter parameters
		 */
		public FilterTextCondition(String text, Collection<Filter> filters, Map<Filter, Map<String, String>> filterParameters) {
			this(text, filters, filterParameters, false);
		}

		/**
		 * Creates a new filter text condition.
		 *
		 * @param text
		 *            The text to filter
		 * @param filters
		 *            The filters to run through
		 * @param filterParameters
		 *            The filter parameters
		 * @param invert
		 *            {@code true} to invert the result
		 */
		public FilterTextCondition(String text, Collection<Filter> filters, Map<Filter, Map<String, String>> filterParameters, boolean invert) {
			this.text = text;
			this.filters = filters;
			this.filterParameters = filterParameters;
			this.invert = invert;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isAllowed(DataProvider dataProvider) throws TemplateException {
			Object data = text;
			for (Filter filter : filters) {
				data = filter.format(dataProvider, data, filterParameters.get(filter));
			}
			return Boolean.valueOf(String.valueOf(data)) ^ invert;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("(").append(text);
			for (Filter filter : filters) {
				stringBuilder.append("|").append(filter.getClass().getSimpleName());
				if (filterParameters.containsKey(filter)) {
					for (Entry<String, String> filterParameter : filterParameters.get(filter).entrySet()) {
						stringBuilder.append(" ").append(filterParameter.getKey()).append("=").append(filterParameter.getValue());
					}
				}
			}
			return stringBuilder.append(" = ").append(!invert).append(")").toString();
		}

	}

}
