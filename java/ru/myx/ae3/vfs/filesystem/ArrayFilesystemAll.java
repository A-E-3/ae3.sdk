package ru.myx.ae3.vfs.filesystem;

import java.io.File;
import java.io.FileFilter;

final class ArrayFilesystemAll extends ArrayFilesystem implements FileFilter {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 7500142593281745192L;
	
	ArrayFilesystemAll() {
		//
	}
	
	@Override
	public boolean accept(final File file) {
		this.add( new ReferenceFilesystem( new RecordFilesystem( file ) ) );
		return false;
	}
}
