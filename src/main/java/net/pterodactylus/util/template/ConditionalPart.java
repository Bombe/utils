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
import java.util.List;
import java.util.Map.Entry;

import net.pterodactylus.util.template.TemplateParser.FilterDefinition;

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
	 * @param line
	 *            The line number of the tag
	 * @param column
	 *            The column number of the tag
	 * @param condition
	 *            The condition
	 */
	public ConditionalPart(int line, int column, Condition condition) {
		super(line, column);
		this.condition = condition;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void render(TemplateContext templateContext, Writer writer) throws TemplateException {
		if (condition.isAllowed(templateContext)) {
			super.render(templateContext, writer);
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
		 * @param templateContext
		 *            The template context
		 * @return {@code true} if the condition is fulfilled, {@code false}
		 *         otherwise
		 * @throws TemplateException
		 *             if a template variable can not be parsed or evaluated
		 */
		public boolean isAllowed(TemplateContext templateContext) throws TemplateException;

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
		public boolean isAllowed(TemplateContext templateContext) throws TemplateException {
			return !condition.isAllowed(templateContext);
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
		public boolean isAllowed(TemplateContext templateContext) throws TemplateException {
			for (Condition condition : conditions) {
				if (!condition.isAllowed(templateContext)) {
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
		public boolean isAllowed(TemplateContext templateContext) throws TemplateException {
			for (Condition condition : conditions) {
				if (condition.isAllowed(templateContext)) {
					return true;
				}
			}
			return false;
		}

	}

	/**
	 * {@link Condition} implementation that asks the {@link TemplateContext}
	 * for a {@link Boolean} value.
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
		public boolean isAllowed(TemplateContext templateContext) throws TemplateException {
			return Boolean.valueOf(String.valueOf(templateContext.get(itemName))) ^ invert;
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
		public boolean isAllowed(TemplateContext templateContext) throws TemplateException {
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
	 * {@link Condition} implementation that asks the {@link TemplateContext}
	 * for a {@link Boolean} value and checks whether it’s {@code null} or not.
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
		public boolean isAllowed(TemplateContext templateContext) throws TemplateException {
			return (templateContext.get(itemName) == null) ^ invert;
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
		private final List<FilterDefinition> filterDefinitions;

		/** Whether to invert the result. */
		private final boolean invert;

		/**
		 * Creates a new filter condition.
		 *
		 * @param itemName
		 *            The name of the item
		 * @param filterDefinitions
		 *            The filters to apply
		 */
		public FilterCondition(String itemName, List<FilterDefinition> filterDefinitions) {
			this(itemName, filterDefinitions, false);
		}

		/**
		 * Creates a new filter condition.
		 *
		 * @param itemName
		 *            The name of the item
		 * @param filterDefinitions
		 *            The filters to apply
		 * @param invert
		 *            {@code true} to invert the result
		 */
		public FilterCondition(String itemName, List<FilterDefinition> filterDefinitions, boolean invert) {
			this.itemName = itemName;
			this.filterDefinitions = filterDefinitions;
			this.invert = invert;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isAllowed(TemplateContext templateContext) throws TemplateException {
			Object data = templateContext.get(itemName);
			for (FilterDefinition filterDefinition : filterDefinitions) {
				Filter filter = templateContext.getFilter(filterDefinition.getName());
				if (filter != null) {
					data = filter.format(templateContext, data, filterDefinition.getParameters());
				}
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
			for (FilterDefinition filterDefinition : filterDefinitions) {
				stringBuilder.append("|").append(filterDefinition.getName());
				for (Entry<String, String> filterParameter : filterDefinition.getParameters().entrySet()) {
					stringBuilder.append(" ").append(filterParameter.getKey()).append("=").append(filterParameter.getValue());
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

		/** The filters to apply. */
		private final List<FilterDefinition> filterDefinitions;

		/** Whether to invert the result. */
		private final boolean invert;

		/**
		 * Creates a new filter text condition.
		 *
		 * @param text
		 *            The text to filter
		 * @param filterDefinitions
		 *            The filters to apply
		 */
		public FilterTextCondition(String text, List<FilterDefinition> filterDefinitions) {
			this(text, filterDefinitions, false);
		}

		/**
		 * Creates a new filter text condition.
		 *
		 * @param text
		 *            The text to filter
		 * @param filterDefinitions
		 *            The filters to apply
		 * @param invert
		 *            {@code true} to invert the result
		 */
		public FilterTextCondition(String text, List<FilterDefinition> filterDefinitions, boolean invert) {
			this.text = text;
			this.filterDefinitions = filterDefinitions;
			this.invert = invert;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isAllowed(TemplateContext templateContext) throws TemplateException {
			Object data = text;
			for (FilterDefinition filterDefinition : filterDefinitions) {
				Filter filter = templateContext.getFilter(filterDefinition.getName());
				if (filter != null) {
					data = filter.format(templateContext, data, filterDefinition.getParameters());
				}
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
			for (FilterDefinition filterDefinition : filterDefinitions) {
				stringBuilder.append("|").append(filterDefinition.getName());
				for (Entry<String, String> filterParameter : filterDefinition.getParameters().entrySet()) {
					stringBuilder.append(" ").append(filterParameter.getKey()).append("=").append(filterParameter.getValue());
				}
			}
			return stringBuilder.append(" = ").append(!invert).append(")").toString();
		}

	}

}
