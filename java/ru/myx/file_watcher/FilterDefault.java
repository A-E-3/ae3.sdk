/**
 * 
 */
package ru.myx.file_watcher;

import java.io.File;
import java.io.FileFilter;

final class FilterDefault implements FileFilter {
	@Override
	public final boolean accept(final File file) {
		return file.isFile();
	}
}
