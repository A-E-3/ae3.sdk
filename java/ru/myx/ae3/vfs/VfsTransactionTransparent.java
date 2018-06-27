package ru.myx.ae3.vfs;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.know.Guid;

/**
 * 
 * @author myx
 * 
 */
public class VfsTransactionTransparent implements TransactionVfs {
	private final VfsInterface	iface;
	
	/**
	 * 
	 * @param iface
	 */
	public VfsTransactionTransparent(final VfsInterface iface) {
		this.iface = iface;
	}
	
	@Override
	public void cancel() throws Exception {
		// ignore
	}
	
	@Override
	public void commit() throws Exception {
		// ignore
	}
	
	@Override
	public TransactionVfs createTransaction() {
		return new VfsTransactionBuffer( this );
	}
	
	@Override
	public void doUnlink(final Entry entry) {
		this.iface.doUnlink( entry );
	}
	
	@Override
	public void doUnlink(final Entry container, final BaseObject key) {
		this.iface.doUnlink( container, key );
	}
	
	@Override
	public void doUnlink(final Entry container, final Guid key) {
		this.iface.doUnlink( container, key );
	}
	
	@Override
	public void doUnlink(final Entry container, final String key) {
		this.iface.doUnlink( container, key );
	}
	
	@Override
	public Entry getRelative(final Entry share, final Entry focus, final String path, final TreeLinkType mode) {
		return this.iface.getRelative( share, focus, path, mode );
	}
	
	@Override
	public Entry getRelative(final Entry root, final String path, final TreeLinkType mode) {
		return this.iface.getRelative( root, path, mode );
	}
}
