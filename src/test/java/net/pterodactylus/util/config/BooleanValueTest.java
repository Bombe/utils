/*
 * utils - BooleanValueTest.java - Copyright © 2019 David ‘Bombe’ Roden
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.pterodactylus.util.config;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

/**
 * Unit test for BooleanValue.
 */
public class BooleanValueTest {

	@Test
	public void nullIsBeingReturnedFromGetValue() throws ConfigurationException {
		Configuration configuration = new Configuration(new MapConfigurationBackend());
		configuration.getBooleanValue("Test").setValue(null);
		BooleanValue value = new BooleanValue(configuration, "Test");
		assertThat(value.getValue(), nullValue());
	}

	@Test
	public void defaultValueIsBeingReturnedFromGetValueWithDefaultValue() throws ConfigurationException {
		Configuration configuration = new Configuration(new MapConfigurationBackend());
		configuration.getBooleanValue("Test").setValue(null);
		BooleanValue value = new BooleanValue(configuration, "Test");
		assertThat(value.getValue(true), equalTo(true));
	}

}
