package ru.myx.ae3.vfs.filesystem;

import java.io.File;
import java.io.FilenameFilter;

import ru.myx.ae3.vfs.VfsDefaultLimitSortMatcher;

final class VfsFilenameLimitSortMatcher extends VfsDefaultLimitSortMatcher<String, String> implements FilenameFilter {
	
	public VfsFilenameLimitSortMatcher(final String keyStart,
			final String keyStop,
			final int limit,
			final boolean backwards) {
		super( keyStart, keyStop, limit, backwards );
	}
	
	@Override
	public boolean accept(final File dir, final String name) {
		this.put( name, name );
		return false;
	}
	
}
