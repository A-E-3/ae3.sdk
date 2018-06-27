package ru.myx.ae3.vfs.filesystem;

import java.io.File;
import java.io.FilenameFilter;

final class ArrayFilesystemRange extends ArrayFilesystem implements FilenameFilter {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 7500142593281745192L;
	
	final String				keyStart;
	
	final String				keyStop;
	
	int							left;
	
	ArrayFilesystemRange(final String keyStart, final String keyStop, final int limit) {
		this.keyStart = keyStart;
		this.keyStop = keyStop;
		this.left = limit;
	}
	
	@Override
	public boolean accept(final File file, final String key) {
		if (this.keyStart != null && this.keyStart.compareTo( key ) >= 0) {
			return false;
		}
		if (this.keyStop != null && this.keyStop.compareTo( key ) < 0) {
			return false;
		}
		/**
		 * TODO: INVALID: must be sorted
		 */
		if (--this.left == 0) {
			return false;
		}
		this.add( new ReferenceFilesystem( new RecordFilesystem( new File( file, key ) ) ) );
		return false;
	}
}
