/*
 * Created on 27.01.2005
 */
package ru.myx.util;

import ru.myx.ae3.Engine;
import java.util.function.Function;

/**
 * Why? It's fast! No synchronizations needed - it's all in there.
 *
 * 'enqueue' (writing) is multi-thread safe, 'next' (reading) is single-thread.
 *
 * @param <T>
 */
public final class FifoQueueMultithreadEnqueue<T> implements BasicQueue<T> {
	
	
	/**
	 * >= 2
	 */
	private static final int QUEUE_COUNT = Math.max(4, Math.min(Engine.PARALLELISM, 8));

	private static final int QUEUE_MASK = FifoQueueMultithreadEnqueue.QUEUE_COUNT - 1;

	private final FifoQueueLinked<T>[] queues;

	// private final FifoQueueBuffered<T>[] queues;

	private int queueHead = 0;

	private int lastQueueHead = 0;

	/**
	 *
	 */
	@SuppressWarnings("unchecked")
	public FifoQueueMultithreadEnqueue() {
		this.queues = new FifoQueueLinked[FifoQueueMultithreadEnqueue.QUEUE_COUNT];
		// this.queues = new
		// FifoQueueBuffered[FifoQueueMultithreadEnqueue.QUEUE_COUNT];
		for (int i = FifoQueueMultithreadEnqueue.QUEUE_MASK; i >= 0; --i) {
			this.queues[i] = new FifoQueueLinked<>();
			// this.queues[i] = new FifoQueueBuffered<>();
		}
	}

	/**
	 * same order (approx, excluding race conditions)
	 *
	 * @param target
	 * @return count
	 * @throws Throwable
	 */
	public final int flush(final Function<T, ?> target) throws Throwable {
		
		
		int addedCount = 0;
		/**
		 * same order (approx, excluding race conditions)
		 */
		for (int m = FifoQueueMultithreadEnqueue.QUEUE_MASK, i = this.lastQueueHead, l = m; l >= 0;) {
			final T next = this.queues[--i & m].pollFirst();
			if (next == null) {
				--l;
				continue;
			}
			l = m;
			++addedCount;
			target.apply(next);
		}
		this.lastQueueHead = this.queueHead;
		return addedCount;
	}

	@Override
	public boolean hasNext() {
		
		
		for (int m = FifoQueueMultithreadEnqueue.QUEUE_MASK, l = m; l >= 0; --l) {
			final BasicQueue<T> queue = this.queues[--this.lastQueueHead & m];
			/**
			 * Sorry, have to do it, since there could be one or more tasks
			 * waiting for enqueue.
			 */
			synchronized (queue) {
				if (queue.hasNext()) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean iterateAll(final BasicQueue.IterationAllCallback<T> callback) {
		
		
		for (int i = FifoQueueMultithreadEnqueue.QUEUE_MASK; i >= 0; --i) {
			if (!this.queues[i].iterateAll(callback)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean offerFirst(final T target) {
		
		
		final int index = --this.queueHead & FifoQueueMultithreadEnqueue.QUEUE_MASK;
		final BasicQueue<T> queue = this.queues[index];
		synchronized (queue) {
			return queue.offerFirst(target);
		}
	}

	/**
	 * multi-thread safe
	 *
	 * @param target
	 */
	@Override
	public final boolean offerLast(final T target) {
		
		
		final int index = --this.queueHead & FifoQueueMultithreadEnqueue.QUEUE_MASK;
		final BasicQueue<T> queue = this.queues[index];
		synchronized (queue) {
			return queue.offerLast(target);
		}
	}

	/**
	 * async, non multi-thread safe. same order.
	 *
	 * @return
	 */
	@Override
	public final T pollFirst() {
		
		
		/**
		 * same order (approx, excluding race conditions)
		 */
		for (int m = FifoQueueMultithreadEnqueue.QUEUE_MASK, l = m; l >= 0; --l) {
			final BasicQueue<T> queue = this.queues[--this.lastQueueHead & m];
			final T next;
			/**
			 * Sorry, have to do it, since there could be one or more tasks
			 * waiting for enqueue.
			 */
			synchronized (queue) {
				next = queue.pollFirst();
			}
			if (next != null) {
				return next;
			}
		}
		return null;
	}

	/**
	 * approximate, maybe slow, for messages, etc
	 *
	 * @return
	 */
	@Override
	public int size() {
		
		
		int size = 0;
		for (int i = FifoQueueMultithreadEnqueue.QUEUE_MASK; i >= 0; --i) {
			size += this.queues[i].size();
		}
		return size;
	}

	/**
	 * Ensures that all write operations started on this queue are finished.
	 *
	 * multi-thread safe
	 *
	 * @return
	 */
	public final FifoQueueMultithreadEnqueue<T> sync() {
		
		
		final BasicQueue<T> queue = this.queues[--this.queueHead & FifoQueueMultithreadEnqueue.QUEUE_MASK];
		synchronized (queue) {
			queue.getClass();
		}
		return this;
	}
}
