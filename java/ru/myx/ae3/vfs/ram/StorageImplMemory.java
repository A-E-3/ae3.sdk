package ru.myx.ae3.vfs.ram;

import ru.myx.ae3.Engine;
import ru.myx.ae3.vfs.ars.ArsStorageImpl;
import ru.myx.ae3.vfs.ram.size.StorageImplMemorySize;
import ru.myx.ae3.vfs.ram.speed.StorageImplMemorySpeed;

/**
 * 
 * @author myx
 * 
 */
public class StorageImplMemory {
	/**
	 * 
	 * @param name
	 * @return
	 */
	public static final ArsStorageImpl<?, ?, ?> create(final String name) {
		return Engine.MODE_SPEED
				? new StorageImplMemorySpeed( name )
				: new StorageImplMemorySize( name );
	}
	
	private StorageImplMemory() {
		//
	}
}
