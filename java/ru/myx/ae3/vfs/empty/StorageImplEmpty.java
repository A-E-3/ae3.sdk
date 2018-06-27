package ru.myx.ae3.vfs.empty;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.know.Guid;
import ru.myx.ae3.vfs.Entry;
import ru.myx.ae3.vfs.EntryContainer;
import ru.myx.ae3.vfs.TransactionVfs;
import ru.myx.ae3.vfs.TreeLinkType;
import ru.myx.ae3.vfs.VfsInterface;

/**
 * 
 * @author myx
 * 
 */
public class StorageImplEmpty implements VfsInterface {
	
	/**
	 * 
	 */
	public final static EntryContainer	ROOT	= null;
	
	
	private StorageImplEmpty() {
	
		//
	}
	
	
	@Override
	public TransactionVfs createTransaction() {
	
		return null;
	}
	
	
	@Override
	public void doUnlink(
			final Entry entry) {
	
		//
	}
	
	
	@Override
	public void doUnlink(
			final Entry container,
			final BaseObject key) {
	
		//
	}
	
	
	@Override
	public void doUnlink(
			final Entry container,
			final Guid key) {
	
		//
	}
	
	
	@Override
	public void doUnlink(
			final Entry container,
			final String key) {
	
		//
	}
	
	
	@Override
	public Entry getRelative(
			final Entry share,
			final Entry focus,
			final String path,
			final TreeLinkType mode) {
	
		return null;
	}
	
	
	@Override
	public Entry getRelative(
			final Entry root,
			final String path,
			final TreeLinkType mode) {
	
		return null;
	}
	
}
