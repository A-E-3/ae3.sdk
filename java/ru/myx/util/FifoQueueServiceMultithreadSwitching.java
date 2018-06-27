package ru.myx.util;

import ru.myx.ae3.Engine;
import ru.myx.ae3.help.Convert;

/**
 *
 * @author myx
 *
 * @param <T>
 */
public class FifoQueueServiceMultithreadSwitching<T> implements BasicQueue<T> {
	
	
	private boolean pendingChanges = false;

	private final FifoQueueMultithreadEnqueue<T>[] queue = Convert.Array.toAny(new FifoQueueMultithreadEnqueue[]{
			new FifoQueueMultithreadEnqueue<T>(), new FifoQueueMultithreadEnqueue<T>()
	});

	private volatile int queueIndex = 0;

	private final Object notifyTarget;

	private boolean switchPlanesWaitCancel = false;

	/**
	 * 'notifyTarget == this
	 */
	public FifoQueueServiceMultithreadSwitching() {
		this.notifyTarget = this;
	}

	/**
	 *
	 * @param notifyTarget
	 */
	public FifoQueueServiceMultithreadSwitching(final Object notifyTarget) {
		this.notifyTarget = notifyTarget == null
			? this
			: notifyTarget;
	}

	@Override
	public boolean hasNext() {
		
		
		return this.queue[this.queueIndex ^ 0x01].hasNext();
	}

	/**
	 * returns inactive queue
	 *
	 * @return
	 */
	public final FifoQueueMultithreadEnqueue<T> inactiveQueue() {
		
		
		return this.queue[this.queueIndex ^ 0x01];
	}

	@Override
	public boolean iterateAll(final BasicQueue.IterationAllCallback<T> callback) {
		
		
		return this.queue[0].iterateAll(callback) && this.queue[1].iterateAll(callback);
	}

	@Override
	public boolean offerFirst(final T element) {
		
		
		this.queue[this.queueIndex].offerFirst(element);
		if (this.pendingChanges) {
			return false;

		}
		synchronized (this.notifyTarget) {
			if (!this.pendingChanges) {
				this.pendingChanges = true;
				this.notifyTarget.notifyAll();
				return true;
			}
		}
		return false;
	}

	/**
	 * Synchronously notifies 'notifyTarget' when 'pendingChanges' (isEmpty())
	 * state changing.
	 *
	 * When there is no 'notifyTarget' (equals NULL) no synchronization is
	 * performed and no-one is notified.
	 *
	 * @param element
	 * @return is first?
	 */
	@Override
	public final boolean offerLast(final T element) {
		
		
		this.queue[this.queueIndex].offerLast(element);
		if (this.pendingChanges) {
			return false;

		}
		synchronized (this.notifyTarget) {
			if (!this.pendingChanges) {
				this.pendingChanges = true;
				this.notifyTarget.notifyAll();
				return true;
			}
		}
		return false;
	}

	@Override
	public T pollFirst() {
		
		
		{
			final FifoQueueMultithreadEnqueue<T> queue = this.queue[this.queueIndex ^ 0x01];
			final T next = queue.pollFirst();
			if (next != null) {
				return next;
			}
		}
		{
			final FifoQueueMultithreadEnqueue<T> queue = this.switchPlanes();
			if (queue == null) {
				return null;
			}
			return queue.pollFirst();
		}
	}

	/**
	 * approximate, maybe slow, for messages, etc
	 *
	 * @return
	 */
	@Override
	public int size() {
		
		
		return this.queue[0].size() + this.queue[1].size();
	}

	/**
	 * Switches planes, the one for 'enqueue' becomes one for 'read' and the one
	 * for 'read' becomes one for 'enqueue'
	 *
	 * @return NULL when there is nothing queued.
	 */
	public final FifoQueueMultithreadEnqueue<T> switchPlanes() {
		
		
		synchronized (this.notifyTarget) {
			/**
			 * reset cancel first in any case
			 */
			if (this.switchPlanesWaitCancel) {
				this.switchPlanesWaitCancel = false;
				return null;
			}
			if (!this.pendingChanges) {
				return null;
			}
			this.pendingChanges = false;
			final int queueIndex = this.queueIndex;
			this.queueIndex = queueIndex ^ 0x01;
			return this.queue[queueIndex];
		}
	}

	/**
	 * You must synchronize with 'notifyTarget' when checking this method before
	 * waiting for notification.
	 *
	 * @return
	 */
	public boolean switchPlanesIsReady() {
		
		
		return this.pendingChanges;
	}

	/**
	 * Switches planes, the one for 'enqueue' becomes one for 'read' and the one
	 * for 'read' becomes one for 'enqueue'
	 *
	 * NOTE: This method is 'spurious wakeup'-safe!
	 *
	 * @param timeout
	 *            use 0 for indefinite timeout
	 * @return NULL when there is nothing queued and timeout expired.
	 * @throws InterruptedException
	 */
	public final FifoQueueMultithreadEnqueue<T> switchPlanesWait(final long timeout) throws InterruptedException {
		
		
		synchronized (this.notifyTarget) {
			/**
			 * reset cancel first in any case
			 */
			if (this.switchPlanesWaitCancel) {
				this.switchPlanesWaitCancel = false;
				return null;
			}
			waitChanges : if (!this.pendingChanges) {
				final long expires = timeout == 0
					? 0
					: Engine.fastTime() + timeout;
				for (long left = timeout;;) {
					this.notifyTarget.wait(left);
					/**
					 * reset cancel first in any case
					 */
					if (this.switchPlanesWaitCancel) {
						this.switchPlanesWaitCancel = false;
						return null;
					}
					if (this.pendingChanges) {
						break waitChanges;
					}
					if (timeout == 0) {
						continue;
					}
					left = expires - Engine.fastTime();
					if (left <= 0) {
						return null;
					}
				}
			}
			this.pendingChanges = false;
			final int queueIndex = this.queueIndex;
			this.queueIndex = queueIndex ^ 0x01;
			return this.queue[queueIndex];
		}
	}

	/**
	 * @param timeout
	 * @return true when new data ready, false when time expired
	 * @throws InterruptedException
	 */
	public final boolean switchPlanesWaitReady(final long timeout) throws InterruptedException {
		
		
		/**
		 * without a lock
		 */
		if (this.pendingChanges || this.switchPlanesWaitCancel) {
			return true;
		}
		synchronized (this.notifyTarget) {
			/**
			 * do not reset 'cancel', will be used in switchPlanesXXX methods
			 */
			if (this.pendingChanges || this.switchPlanesWaitCancel) {
				return true;
			}

			if (timeout == 0) {
				for (;;) {
					//
					this.notifyTarget.wait(0);
					/**
					 * do not reset 'cancel', will be used in switchPlanesXXX
					 * methods
					 */
					if (this.pendingChanges || this.switchPlanesWaitCancel) {
						return true;
					}
				}
			}

			assert timeout > 0 : "Timeout value must not be negative!";

			final long expires = Engine.fastTime() + timeout;
			for (//
			long left = timeout; //
			left > 0; //
			left = expires - Engine.fastTime()) {
				//
				this.notifyTarget.wait(left);
				/**
				 * do not reset 'cancel', will be used in switchPlanesXXX
				 * methods
				 */
				if (this.pendingChanges || this.switchPlanesWaitCancel) {
					return true;
				}
			}
			return false;
		}
	}

	/**
	 * Cancels 'switchPlanesWait' if any, or the next one!
	 */
	public final void switchQueueWaitCancel() {
		
		
		if (this.switchPlanesWaitCancel) {
			return;
		}
		synchronized (this.notifyTarget) {
			if (!this.switchPlanesWaitCancel) {
				this.switchPlanesWaitCancel = true;
				this.notifyTarget.notifyAll();
			}
		}
	}
}
