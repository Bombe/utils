/*
 * utils - Validation.java - Copyright © 2009–2019 David Roden
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

package net.pterodactylus.util.validation;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Helps with parameter validation. Parameters can be checked using a construct
 * like this:
 * </p>
 * <code><pre>
 * public void copy(Object[] object, int leftValue, int ríghtValue) {
 *     Validation.begin().isNotNull(object, &quot;object&quot;).check()
 *         .isPositive(leftValue, &quot;leftValue&quot;).isLess(leftValue, object.length, &quot;leftValue&quot;).check()
 *         .isPositive(rightValue, &quot;rightValue&quot;).isLess(rightValue, object.length, &quot;rightValue&quot;).isGreater(rightValue, leftValue, &quot;rightValue&quot;).check();
 *     // do something with the values
 * }
 * </pre></code>
 * <p>
 * This example will perform several checks. Only the {@link #check()} method
 * will throw an {@link IllegalArgumentException} if one of the previous checks
 * failed, so you can gather several reasons for a validation failure before
 * throwing an exception which will in turn decrease the time spent in
 * debugging.
 * </p>
 * <p>
 * In the example, <code>object</code> is first checked for a non-
 * <code>null</code> value and an {@link IllegalArgumentException} is thrown if
 * <code>object</code> is <code>null</code>. Afterwards <code>leftValue</code>
 * is checked for being a positive value that is also smaller than the length of
 * the array <code>object</code>. The {@link IllegalArgumentException} that is
 * thrown if the checks failed will contain a message for each of the failed
 * checks. At last <code>rightValue</code> is checked for being positive,
 * smaller than the array’s length and larger than <code>leftValue</code>.
 * </p>
 * <p>
 * Remember to call the {@link #check()} method after performing the checks,
 * otherwise the {@link IllegalArgumentException} will never be thrown!
 * </p>
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Validation {

	/** The validation runners. */
	private List<ValidationRunner<?>> validationRunners = new ArrayList<ValidationRunner<?>>();

	/**
	 * Private constructor to prevent construction from the outside.
	 */
	private Validation() {
		/* do nothing. */
	}

	/**
	 * Returns a new {@link Validation} object.
	 *
	 * @return A new validation
	 */
	public static Validation begin() {
		return new Validation();
	}

	/**
	 * Checks if one of the previous checks failed and throws an
	 * {@link IllegalArgumentException} if a previous check did fail.
	 *
	 * @return This {@link Validation} object to allow method chaining
	 * @throws IllegalArgumentException
	 *             if a previous check failed
	 */
	public Validation check() throws IllegalArgumentException {
		List<String> failedChecks = new ArrayList<String>();
		for (ValidationRunner<?> validationRunner : validationRunners) {
			if (!validationRunner.validate()) {
				failedChecks.add(validationRunner.getObjectName() + " failed " + validationRunner.getValidator());
			}
		}
		validationRunners.clear();
		if (failedChecks.isEmpty()) {
			return this;
		}
		StringBuilder message = new StringBuilder();
		message.append("Failed checks: ");
		for (String failedCheck : failedChecks) {
			message.append(failedCheck).append(", ");
		}
		message.setLength(message.length() - 2);
		message.append('.');
		failedChecks.clear();
		throw new IllegalArgumentException(message.toString());
	}

	/**
	 * Checks if the given value is {@code true}.
	 *
	 * @param objectName
	 *            The object’s name
	 * @param value
	 *            The value to check
	 * @return This {@link Validation} object to allow method chaining
	 */
	public Validation is(String objectName, boolean value) {
		return isEqual(objectName, value, true);
	}

	/**
	 * Checks if the given value is {@code false}.
	 *
	 * @param objectName
	 *            The object’s name
	 * @param value
	 *            The value to check
	 * @return This {@link Validation} object to allow method chaining
	 */
	public Validation isNot(String objectName, boolean value) {
		return isEqual(objectName, value, false);
	}

	/**
	 * Checks if the given object is not <code>null</code>.
	 *
	 * @param <T>
	 *            The type of object being validated
	 * @param objectName
	 *            The object’s name
	 * @param object
	 *            The object to check
	 * @return This {@link Validation} object to allow method chaining
	 */
	public <T> Validation isNotNull(String objectName, T object) {
		validationRunners.add(new ValidationRunner<T>(objectName, object, new NotNullValidator<T>()));
		return this;
	}

	/**
	 * Checks if the given object is <code>null</code>.
	 *
	 * @param <T>
	 *            The type of object being validated
	 * @param objectName
	 *            The object’s name
	 * @param object
	 *            The object to check
	 * @return This {@link Validation} object to allow method chaining
	 */
	public <T> Validation isNull(String objectName, T object) {
		validationRunners.add(new ValidationRunner<T>(objectName, object, new NotValidator<T>(new NotNullValidator<T>())));
		return this;
	}

	/**
	 * Checks whether the given {@code object} matches any of the {@code
	 * expecteds} values.
	 *
	 * @param <T>
	 *            The type of object being validated
	 * @param objectName
	 *            The name of the object
	 * @param object
	 *            The object to check
	 * @param expecteds
	 *            The values of which at least one has to be
	 *            {@link Object#equals(Object) equal}
	 * @return This {@link Validation} object to allow method chaining
	 */
	public <T> Validation isEither(String objectName, T object, T... expecteds) {
		OrValidator<T> orValidator = new OrValidator<T>();
		for (T expected : expecteds) {
			orValidator.addValidator(new EqualityValidator<T>(expected));
		}
		validationRunners.add(new ValidationRunner<T>(objectName, object, orValidator));
		return this;
	}

	/**
	 * Checks whether the given {@code object} matches all of the
	 * {@code expecteds} values.
	 *
	 * @param <T>
	 *            The type of object being validated
	 * @param objectName
	 *            The name of the object
	 * @param object
	 *            The object to check
	 * @param expecteds
	 *            The values that have to be {@link Object#equals(Object) equal}
	 * @return This {@link Validation} object to allow method chaining
	 */
	public <T> Validation isAll(String objectName, T object, T... expecteds) {
		AndValidator<T> andValidator = new AndValidator<T>();
		for (T expected : expecteds) {
			andValidator.addValidator(new EqualityValidator<T>(expected));
		}
		validationRunners.add(new ValidationRunner<T>(objectName, object, andValidator));
		return this;
	}

	/**
	 * Checks if <code>value</code> is less than <code>upperBound</code>.
	 *
	 * @param objectName
	 *            The object’s name
	 * @param value
	 *            The value to check
	 * @param upperBound
	 *            The upper bound to check <code>value</code> against
	 * @return This {@link Validation} object to allow method chaining
	 */
	public Validation isLess(String objectName, long value, long upperBound) {
		validationRunners.add(new ValidationRunner<Long>(objectName, value, new LongRangeValidator(Long.MIN_VALUE, upperBound - 1)));
		return this;
	}

	/**
	 * Checks if <code>value</code> is less than <code>upperBound</code>.
	 *
	 * @param objectName
	 *            The object’s name
	 * @param value
	 *            The value to check
	 * @param upperBound
	 *            The upper bound to check <code>value</code> against
	 * @return This {@link Validation} object to allow method chaining
	 */
	public Validation isLess(String objectName, double value, double upperBound) {
		validationRunners.add(new ValidationRunner<Double>(objectName, value, new DoubleRangeValidator(Double.NEGATIVE_INFINITY, false, upperBound, false)));
		return this;
	}

	/**
	 * Checks if <code>value</code> is less than <code>upperBound</code>.
	 *
	 * @param objectName
	 *            The object’s name
	 * @param value
	 *            The value to check
	 * @param upperBound
	 *            The upper bound to check <code>value</code> against
	 * @return This {@link Validation} object to allow method chaining
	 */
	public Validation isLessOrEqual(String objectName, long value, long upperBound) {
		validationRunners.add(new ValidationRunner<Long>(objectName, value, new LongRangeValidator(Long.MIN_VALUE, upperBound)));
		return this;
	}

	/**
	 * Checks if <code>value</code> is less than <code>upperBound</code>.
	 *
	 * @param objectName
	 *            The object’s name
	 * @param value
	 *            The value to check
	 * @param upperBound
	 *            The upper bound to check <code>value</code> against
	 * @return This {@link Validation} object to allow method chaining
	 */
	public Validation isLessOrEqual(String objectName, double value, double upperBound) {
		validationRunners.add(new ValidationRunner<Double>(objectName, value, new DoubleRangeValidator(Double.NEGATIVE_INFINITY, false, upperBound, true)));
		return this;
	}

	/**
	 * Checks if the given value is equal to the expected value.
	 *
	 * @param <T>
	 *            The type of object being validated
	 * @param objectName
	 *            The object’s name
	 * @param value
	 *            The value to check
	 * @param expected
	 *            The expected value to check <code>value</code> against
	 * @return This {@link Validation} object to allow method chaining
	 */
	public <T> Validation isEqual(String objectName, T value, T expected) {
		validationRunners.add(new ValidationRunner<T>(objectName, value, new EqualityValidator<T>(expected)));
		return this;
	}

	/**
	 * Checks if the given value is the same as the expected value.
	 *
	 * @param <T>
	 *            The type of object being validated
	 * @param objectName
	 *            The object’s name
	 * @param value
	 *            The value to check
	 * @param expected
	 *            The expected value to check <code>value</code> against
	 * @return This {@link Validation} object to allow method chaining
	 */
	public <T> Validation isSame(String objectName, T value, T expected) {
		validationRunners.add(new ValidationRunner<T>(objectName, value, new IdentityValidator<T>(expected)));
		return this;
	}

	/**
	 * Checks if the given value is not equal to the expected value.
	 *
	 * @param <T>
	 *            The type of object being validated
	 * @param objectName
	 *            The object’s name
	 * @param value
	 *            The value to check
	 * @param expected
	 *            The expected value to check <code>value</code> against
	 * @return This {@link Validation} object to allow method chaining
	 */
	public <T> Validation isNotEqual(String objectName, T value, T expected) {
		validationRunners.add(new ValidationRunner<T>(objectName, value, new NotValidator<T>(new EqualityValidator<T>(expected))));
		return this;
	}

	/**
	 * Checks if <code>value</code> is greater than <code>lowerBound</code>.
	 *
	 * @param objectName
	 *            The object’s name
	 * @param value
	 *            The value to check
	 * @param lowerBound
	 *            The lower bound to check <code>value</code> against
	 * @return This {@link Validation} object to allow method chaining
	 */
	public Validation isGreater(String objectName, long value, long lowerBound) {
		validationRunners.add(new ValidationRunner<Long>(objectName, value, new LongRangeValidator(lowerBound + 1, Long.MAX_VALUE)));
		return this;
	}

	/**
	 * Checks if <code>value</code> is greater than <code>lowerBound</code>.
	 *
	 * @param objectName
	 *            The object’s name
	 * @param value
	 *            The value to check
	 * @param lowerBound
	 *            The lower bound to check <code>value</code> against
	 * @return This {@link Validation} object to allow method chaining
	 */
	public Validation isGreater(String objectName, double value, double lowerBound) {
		validationRunners.add(new ValidationRunner<Double>(objectName, value, new DoubleRangeValidator(lowerBound, false, Double.MAX_VALUE, true)));
		return this;
	}

	/**
	 * Checks if <code>value</code> is greater than <code>lowerBound</code>.
	 *
	 * @param objectName
	 *            The object’s name
	 * @param value
	 *            The value to check
	 * @param lowerBound
	 *            The lower bound to check <code>value</code> against
	 * @return This {@link Validation} object to allow method chaining
	 */
	public Validation isGreaterOrEqual(String objectName, long value, long lowerBound) {
		validationRunners.add(new ValidationRunner<Long>(objectName, value, new LongRangeValidator(lowerBound, Long.MAX_VALUE)));
		return this;
	}

	/**
	 * Checks if <code>value</code> is greater than <code>lowerBound</code>.
	 *
	 * @param objectName
	 *            The object’s name
	 * @param value
	 *            The value to check
	 * @param lowerBound
	 *            The lower bound to check <code>value</code> against
	 * @return This {@link Validation} object to allow method chaining
	 */
	public Validation isGreaterOrEqual(String objectName, double value, double lowerBound) {
		validationRunners.add(new ValidationRunner<Double>(objectName, value, new DoubleRangeValidator(lowerBound, true, Double.MAX_VALUE, true)));
		return this;
	}

	/**
	 * Checks if the given value is greater to or equal to <code>0</code>.
	 *
	 * @param objectName
	 *            The object’s name
	 * @param value
	 *            The value to check
	 * @return This {@link Validation} object to allow method chaining
	 */
	public Validation isPositive(String objectName, long value) {
		return isGreaterOrEqual(objectName, value, 0);
	}

	/**
	 * Checks if the given value is greater to or equal to <code>0</code>.
	 *
	 * @param objectName
	 *            The object’s name
	 * @param value
	 *            The value to check
	 * @return This {@link Validation} object to allow method chaining
	 */
	public Validation isPositive(String objectName, double value) {
		return isGreaterOrEqual(objectName, value, 0);
	}

	/**
	 * Checks if the given value is less than <code>0</code>.
	 *
	 * @param objectName
	 *            The object’s name
	 * @param value
	 *            The value to check
	 * @return This {@link Validation} object to allow method chaining
	 */
	public Validation isNegative(String objectName, long value) {
		return isLess(objectName, value, 0);
	}

	/**
	 * Checks if the given value is less than <code>0</code>.
	 *
	 * @param objectName
	 *            The object’s name
	 * @param value
	 *            The value to check
	 * @return This {@link Validation} object to allow method chaining
	 */
	public Validation isNegative(String objectName, double value) {
		return isLess(objectName, value, 0);
	}

	/**
	 * Checks whether the given object is assignable to an object of the given
	 * class.
	 *
	 * @param objectName
	 *            The object’s name
	 * @param object
	 *            The object to check
	 * @param clazz
	 *            The class the object should be representable as
	 * @return This {@link Validation} object to allow method chaining
	 */
	public Validation isInstanceOf(String objectName, Object object, Class<?> clazz) {
		validationRunners.add(new ValidationRunner<Object>(objectName, object, new SubClassValidator(clazz)));
		return this;
	}

	/**
	 * Container for objects that are required to perform a single validation.
	 *
	 * @param <T>
	 *            The type of the object being validation
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	private static class ValidationRunner<T> {

		/** The name of the object being validated. */
		private final String objectName;

		/** The value being validated. */
		private final T value;

		/** The validator performing the validation. */
		private final Validator<T> validator;

		/**
		 * Creates a new validation runner.
		 *
		 * @param objectName
		 *            The name of the object being validated
		 * @param value
		 *            The object being validated
		 * @param validator
		 *            The validator performing the validation
		 */
		public ValidationRunner(String objectName, T value, Validator<T> validator) {
			this.objectName = objectName;
			this.value = value;
			this.validator = validator;
		}

		/**
		 * Returns the name of the object being validated.
		 *
		 * @return The name of the object being validated
		 */
		public String getObjectName() {
			return objectName;
		}

		/**
		 * Returns the validator performing the validation.
		 *
		 * @return The validator performing the validation
		 */
		public Validator<T> getValidator() {
			return validator;
		}

		/**
		 * Whether the validation succeeded.
		 *
		 * @return {@code true} if the validation succeeded, {@code false}
		 *         otherwise
		 */
		public boolean validate() {
			return validator.validate(value);
		}

	}

}
