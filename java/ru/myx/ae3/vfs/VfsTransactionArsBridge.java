package ru.myx.ae3.vfs;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.know.Guid;
import ru.myx.ae3.vfs.ars.ArsArray;
import ru.myx.ae3.vfs.ars.ArsRecord;
import ru.myx.ae3.vfs.ars.ArsReference;
import ru.myx.ae3.vfs.ars.ArsTransaction;
import ru.myx.ae3.vfs.ars.EntryARS;
import ru.myx.util.FifoQueueLinked;

/**
 * 
 * @author myx
 * @param <O>
 * @param <R>
 * @param <A>
 * 
 */
public class VfsTransactionArsBridge<O extends ArsRecord, R extends ArsReference<O>, A extends ArsArray<R>> extends
		VfsTransactionAbstract<VfsInterface> {
	
	private ArsTransaction<O, R, A>	txn;
	
	/**
	 * 
	 * @param iface
	 * @param txn
	 */
	public VfsTransactionArsBridge(final VfsInterface iface, final ArsTransaction<O, R, A> txn) {
		super( iface );
	}
	
	@Override
	public void cancel() throws Exception {
		final ArsTransaction<O, R, A> txn = this.txn;
		if (txn == null) {
			return;
		}
		this.txn = null;
		txn.cancel();
	}
	
	@Override
	public void commit() throws Exception {
		final ArsTransaction<O, R, A> txn = this.txn;
		if (txn == null) {
			return;
		}
		this.txn = null;
		txn.commit();
	}
	
	@Override
	public void doUnlink(final Entry entry) {
		if (!entry.isExist()) {
			return;
		}
		this.doUnlink( entry.getParent(), entry.getKey() );
	}
	
	@Override
	public void doUnlink(final Entry container, final BaseObject key) {
		if (!container.isExist()) {
			return;
		}
		this.enqueue( new BufferTaskUnlink( container, Guid.forUnknown( key ) ) );
	}
	
	@Override
	public void doUnlink(final Entry container, final Guid key) {
		if (!container.isExist()) {
			return;
		}
		this.enqueue( new BufferTaskUnlink( container, key ) );
	}
	
	@Override
	public void doUnlink(final Entry container, final String key) {
		if (!container.isExist()) {
			return;
		}
		final EntryARS arsContainer = (EntryARS) container;
		final O keyObject = this.parent.createKeyForString( key );
		this.parent.doLinkDelete( null, keyObject );
		this.enqueue( new BufferTaskUnlink( container, Guid.forString( key ) ) );
	}
	
	private final void enqueue(final BufferTask task) {
		FifoQueueLinked<BufferTask> queue = this.queue;
		if (queue == null) {
			queue = this.queue = new FifoQueueLinked<>();
		}
		queue.offerLast( task );
	}
}
