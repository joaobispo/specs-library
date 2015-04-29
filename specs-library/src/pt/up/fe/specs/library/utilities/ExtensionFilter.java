package pt.up.fe.specs.library.utilities;

import java.io.File;
import java.io.FilenameFilter;

public class ExtensionFilter implements FilenameFilter {

	final private static String DEFAULT_EXTENSION_SEPARATOR = ".";

	public ExtensionFilter(String extension) {
		this.extension = extension;
		this.separator = DEFAULT_EXTENSION_SEPARATOR;
		// this.separator = "";
	}

	@Override
	public boolean accept(File dir, String name) {
		String suffix = separator + extension.toLowerCase();
		return name.toLowerCase().endsWith(suffix);
	}

	private String extension;
	private String separator;
}
