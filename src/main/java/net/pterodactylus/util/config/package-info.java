/**
 * API for handling application configurations. <h2>Using a Configuration</h2>
 * Using the Configuration API consists of only three small steps. First, you
 * need to create a {@link net.pterodactylus.util.config.ConfigurationBackend} for the
 * configuration you want to access. Using the configuration backend you can
 * then create the {@link net.pterodactylus.util.config.Configuration} and start reading
 * integral data types from it. By writing specialized parsers you can handle
 * more abstract objects (e.g. for databases or message connectors) as well.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
package net.pterodactylus.util.config;

