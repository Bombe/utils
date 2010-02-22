package net.pterodactylus.util.template;

import java.util.Map;

/**
 * {@link Filter} implementation that replaces parts of a value.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ReplaceFilter implements Filter {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String format(Object data, Map<String, String> parameters) {
		String input = String.valueOf(data);
		String needle = parameters.get("needle");
		String replacement = parameters.get("replacement");
		return input.replace(needle, replacement);
	}

}