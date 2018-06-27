package ru.myx.ae3.l2.file;

import ru.myx.ae3.i3.TargetInterfaceAbstract;
import ru.myx.ae3.vfs.Entry;

/**
 * @author myx
 * 
 */
public class FileTargetInterface extends TargetInterfaceAbstract {
	/**
	 * @param root
	 */
	public FileTargetInterface(final Entry root) {
		super( root );
	}
	
	/**
	 * @param root
	 * @param base
	 */
	public FileTargetInterface(final Entry root, final Entry base) {
		super( root, base );
	}
	
}
