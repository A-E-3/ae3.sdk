package ru.myx.ae3.pack;

import java.io.File;

import ru.myx.ae3.base.BaseHostObject;
import ru.myx.ae3.vfs.Entry;
import ru.myx.ae3.vfs.Storage;
import ru.myx.ae3.vfs.filesystem.StorageImplFilesystem;
import ru.myx.ae3.vfs.resources.StorageImplResources;

/**
 * @author myx
 * 		
 */
public class PackImpl extends BaseHostObject implements Pack {
	
	private final String name;
	
	private final Entry root;
	
	/**
	 * 
	 * @param folder
	 *            root folder
	 */
	public PackImpl(final Entry folder) {
		this.name = folder.getKey();
		this.root = folder;
	}
	
	/**
	 * 
	 * @param folder
	 *            root folder
	 */
	public PackImpl(final File folder) {
		this.name = folder.getName();
		this.root = Storage.createRoot(new StorageImplFilesystem(folder, false));
	}
	
	/**
	 * Name is relative to anchor
	 * 
	 * @param name
	 * @param anchor
	 *            anchor point
	 */
	public PackImpl(final String name, final Class<?> anchor) {
		this.name = name;
		this.root = Storage.createRoot(new StorageImplResources(name, anchor));
	}
	
	/**
	 * 
	 * @param name
	 * @param folder
	 *            root folder
	 */
	public PackImpl(final String name, final Entry folder) {
		this.name = name;
		this.root = folder;
	}
	
	@Override
	public String getName() {
		
		return this.name;
	}
	
	@Override
	public Entry getRoot() {
		
		return this.root;
	}
	
	@Override
	public String toString() {
		
		return "[object ae3.Pack(name=" + this.getName() + ")]";
	}
}
