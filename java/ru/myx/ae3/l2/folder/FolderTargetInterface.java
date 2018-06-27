package ru.myx.ae3.l2.folder;

import ru.myx.ae3.i3.TargetInterfaceAbstract;
import ru.myx.ae3.vfs.Entry;

/**
 * @author myx
 * 
 */
public class FolderTargetInterface extends TargetInterfaceAbstract {
	
	/**
	 * @param root
	 * 
	 */
	public FolderTargetInterface(final Entry root) {
		super( root );
	}
}
