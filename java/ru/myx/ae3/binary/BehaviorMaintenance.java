/**
 * 
 */
package ru.myx.ae3.binary;

import java.io.File;

import ru.myx.ae3.Engine;
import ru.myx.ae3.act.Act;

final class BehaviorMaintenance implements Runnable {
	private static final long	CHECK_INTERVAL		= 1000L * 60L * 30L;
	
	private static final long	DEAD_TEMP_INTERVAL	= 1000L * 60L * 60L * 24L;
	
	private final File			tempFolder;
	
	BehaviorMaintenance(final File tempFolder) {
		this.tempFolder = tempFolder;
	}
	
	@Override
	public void run() {
		this.tempFolder.listFiles( new BehaviorMaintenanceFilter( Engine.fastTime()
				- BehaviorMaintenance.DEAD_TEMP_INTERVAL ) );
		Act.later( null, this, BehaviorMaintenance.CHECK_INTERVAL );
	}
}
