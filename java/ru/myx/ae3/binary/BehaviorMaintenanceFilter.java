/**
 * 
 */
package ru.myx.ae3.binary;

import java.io.File;
import java.io.FileFilter;

final class BehaviorMaintenanceFilter implements FileFilter {
	private final long	deadTime;
	
	BehaviorMaintenanceFilter(final long deadTime) {
		this.deadTime = deadTime;
	}
	
	@Override
	public boolean accept(final File pathname) {
		if (pathname.isFile() && pathname.getName().endsWith( ".bin" ) && pathname.lastModified() < this.deadTime) {
			pathname.delete();
		}
		return false;
	}
}
