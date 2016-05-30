package net.pterodactylus.util.io;

import static net.pterodactylus.util.io.MimeTypes.getMimeType;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

/**
 * Unit test for {@link MimeTypes}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class MimeTypesTest {

	@Test
	public void mimeTypeForTarBz2IsApplicationXGtar() {
		assertThat(getMimeType("tar.bz2"), is("application/x-gtar"));
	}

}
