/*
 * utils - FilesystemTemplateProvider.java - Copyright © 2012–2019 David Roden
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import net.pterodactylus.util.io.Closer;

/**
 * {@link TemplateProvider} implementation that looks for template files in the
 * filesystem, either at a hardcoded location or a location that is taken from
 * the template context parameter to
 * {@link #getTemplate(TemplateContext, String)}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class FilesystemTemplateProvider implements TemplateProvider {

	/** Name of template context option for the source path. */
	public static final String SOURCE_PATH_OPTION = "FilesystemProviderSourcePath";

	/** The default source path. */
	private final String sourcePath;

	/** The modification times. */
	private final Map<String, Long> modificationTimes = new HashMap<String, Long>();

	/** The template cache. */
	private final Map<String, Template> templateCache = new HashMap<String, Template>();

	/**
	 * Creates a new filesystem template provider that looks for template files
	 * at the given source path.
	 *
	 * @param sourcePath
	 *            The source path of the template files
	 */
	public FilesystemTemplateProvider(String sourcePath) {
		this.sourcePath = sourcePath;
	}

	//
	// PROVIDER METHODS
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Template getTemplate(TemplateContext templateContext, String templateName) {
		String sourcePath = this.sourcePath;
		if (templateContext.get(SOURCE_PATH_OPTION) != null) {
			sourcePath = String.valueOf(templateContext.get(SOURCE_PATH_OPTION));
		}
		File templateFile = new File(sourcePath, templateName);
		if (!templateFile.exists() || !templateFile.canRead()) {
			return null;
		}
		String templateFilename = templateFile.getAbsolutePath();
		long lastModificationTime = templateFile.lastModified();
		synchronized (modificationTimes) {
			if (modificationTimes.containsKey(templateFilename) && (modificationTimes.get(templateFilename) >= lastModificationTime)) {
				return templateCache.get(templateFilename);
			}

			InputStream templateInputStream = null;
			Reader templateReader = null;
			try {
				templateInputStream = new FileInputStream(templateFile);
				templateReader = new InputStreamReader(templateInputStream, "UTF-8");
				Template template = TemplateParser.parse(templateReader);
				modificationTimes.put(templateFilename, lastModificationTime);
				templateCache.put(templateFilename, template);
				return template;
			} catch (FileNotFoundException fnfe1) {
				/* we checked so this shouldn’t happen. */
				throw new RuntimeException("Could not find template file!", fnfe1);
			} catch (UnsupportedEncodingException uee1) {
				/* this is impossible, too. */
				throw new RuntimeException("UTF-8 encoding unknown!", uee1);
			} finally {
				Closer.close(templateReader);
				Closer.close(templateInputStream);
			}
		}
	}

}
