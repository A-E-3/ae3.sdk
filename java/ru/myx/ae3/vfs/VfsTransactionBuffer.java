package ru.myx.ae3.vfs;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.know.Guid;
import ru.myx.util.FifoQueueLinked;

/**
 * 
 * @author myx
 * 
 */
public class VfsTransactionBuffer extends VfsTransactionAbstract<VfsInterface> {
	/**
	 * buffering all requests.
	 */
	private FifoQueueLinked<BufferTask>	queue;
	
	/**
	 * 
	 * @param iface
	 */
	public VfsTransactionBuffer(final VfsInterface iface) {
		super( iface );
	}
	
	@Override
	public void cancel() throws Exception {
		this.queue = null;
	}
	
	@Override
	public void commit() throws Exception {
		final FifoQueueLinked<BufferTask> queue = this.queue;
		if (queue == null) {
			return;
		}
		this.queue = null;
		for (BufferTask task;;) {
			task = queue.pollFirst();
			if (task == null) {
				break;
			}
			task.execute( this.parent );
		}
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
