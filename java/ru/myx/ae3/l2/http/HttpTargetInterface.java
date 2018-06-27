package ru.myx.ae3.l2.http;

import ru.myx.ae3.i3.TargetInterfaceAbstract;
import ru.myx.ae3.vfs.Entry;

/**
 * @author myx
 * 
 */
public class HttpTargetInterface extends TargetInterfaceAbstract {
	/**
	 * @param root
	 */
	public HttpTargetInterface(final Entry root) {
		super( root );
	}
	
	/**
	 * @param root
	 * @param base
	 */
	public HttpTargetInterface(final Entry root, final Entry base) {
		super( root, base );
	}
}
