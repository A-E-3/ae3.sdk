package ru.myx.ae3.vfs;

import ru.myx.ae3.base.BaseHostEmpty;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.reflect.Reflect;

/**
 * 
 * @author myx
 * @param <T>
 * 			
 */
public abstract class VfsTransactionAbstract<T extends VfsInterface> extends BaseHostEmpty implements TransactionVfs {
	
	private static BaseObject PROTOTYPE = Reflect.classToBasePrototype(VfsTransactionAbstract.class);
	
	/**
	 * 
	 */
	protected T parent;
	
	/**
	 * @param parent
	 */
	protected VfsTransactionAbstract(final T parent) {
		// TODO: assert parent != null : "Parent is NULL!";
		this.parent = parent;
	}
	
	@Override
	public BaseObject basePrototype() {
		
		return VfsTransactionAbstract.PROTOTYPE;
	}
	
	@Override
	public TransactionVfs createTransaction() {
		
		return new VfsTransactionBuffer(this);
	}
	
	@Override
	public Entry getRelative(final Entry share, final Entry focus, final String path, final TreeLinkType mode) {
		
		return this.parent.getRelative(share, focus, path, mode);
	}
	
	@Override
	public Entry getRelative(final Entry root, final String path, final TreeLinkType mode) {
		
		return this.parent.getRelative(root, path, mode);
	}
}
