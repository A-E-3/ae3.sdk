/*
 * Created on 04.01.2005
 */
package ru.myx.geo;

import java.io.File;
import java.io.FileFilter;

final class FilterDB implements FileFilter {
	@Override
	public boolean accept(final File file) {
		return file.isFile() && file.getName().endsWith( ".24db" );
	}
}
