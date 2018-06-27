package ru.myx.ae3.vfs.roots;

import java.util.Map;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.know.Guid;
import ru.myx.ae3.vfs.Entry;
import ru.myx.ae3.vfs.TransactionVfs;
import ru.myx.ae3.vfs.TreeLinkType;
import ru.myx.ae3.vfs.VfsInterface;

/**
 * 
 * @author myx
 *
 */
public class StorageImplRoots implements VfsInterface {
	private Map<String, EntryRoots>	roots;
	
	
	StorageImplRoots(final Entry[] roots) {
	
		//
	}
	
	
	@Override
	public TransactionVfs createTransaction() {
	
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public void doUnlink(
			final Entry entry) {
	
		// TODO Auto-generated method stub
		
	}
	
	
	@Override
	public void doUnlink(
			final Entry container,
			final BaseObject key) {
	
		// TODO Auto-generated method stub
		
	}
	
	
	@Override
	public void doUnlink(
			final Entry container,
			final Guid key) {
	
		// TODO Auto-generated method stub
		
	}
	
	
	@Override
	public void doUnlink(
			final Entry container,
			final String key) {
	
		// TODO Auto-generated method stub
		
	}
	
	
	@Override
	public Entry getRelative(
			final Entry share,
			final Entry focus,
			final String path,
			final TreeLinkType mode) {
	
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public Entry getRelative(
			final Entry root,
			final String path,
			final TreeLinkType mode) {
	
		// TODO Auto-generated method stub
		return null;
	}
	
}
