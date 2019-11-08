/*
 * Created on 30.03.2006
 */
package ru.myx.util;

/** @author myx
 *
 * @param <T> */
public final class FifoQueueBuffered<T> implements BasicQueue<T> {
	
	private static final int INIT = 512;

	private static final int INIT_MASK = FifoQueueBuffered.INIT - 1;

	@SuppressWarnings("unchecked")
	private final static <T> T[] createArray(final int size) {
		
		return (T[]) new Object[size];
	}

	private T[] buffer;

	private int capacity;

	private int end = 0;

	private int mask;

	private int size = 0;

	private int start = 0;

	private final int limit;

	/**
	 *
	 */
	public FifoQueueBuffered() {

		this.buffer = FifoQueueBuffered.createArray(FifoQueueBuffered.INIT);
		this.capacity = FifoQueueBuffered.INIT;
		this.mask = FifoQueueBuffered.INIT_MASK;
		this.limit = Integer.MAX_VALUE;
	}

	/** @param limitFactor */
	public FifoQueueBuffered(final int limitFactor) {

		final int init = 1 << limitFactor;
		this.buffer = FifoQueueBuffered.createArray(init);
		this.capacity = init;
		this.mask = init - 1;
		this.limit = init;
	}

	@Override
	public boolean hasNext() {
		
		return this.size > 0;
	}

	@Override
	public boolean iterateAll(final BasicQueue.IterationAllCallback<T> callback) {
		
		for (int i = this.start & this.mask, l = this.size; l > 0; l--) {
			if (!callback.onNextItem(this.buffer[i++ & this.mask])) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean offerFirst(final T buffer) {
		
		if (this.size >= this.capacity) {
			if (this.size >= this.limit) {
				return false;
			}
			this.start &= this.mask;
			this.end &= this.mask;
			final int newCapacity = this.capacity << 1;
			final T[] newBuffer = FifoQueueBuffered.createArray(newCapacity);
			if (this.start < this.end || this.end == 0) {
				System.arraycopy(this.buffer, this.start, newBuffer, 0, this.size);
			} else {
				final int firstLength = this.capacity - this.start;
				System.arraycopy(this.buffer, this.start, newBuffer, 0, firstLength);
				System.arraycopy(this.buffer, 0, newBuffer, firstLength, this.end);
			}
			this.start = 0;
			this.end = this.size;
			this.capacity = newCapacity;
			this.mask = this.capacity - 1;
			this.buffer = newBuffer;
		}
		this.buffer[--this.start & this.mask] = buffer;
		this.size++;
		return true;
	}

	/** Take care - it's not synchronized. returns false when queue declines to enqueue this object.
	 *
	 * @param buffer
	 * @return boolean of success */
	@Override
	public final boolean offerLast(final T buffer) {
		
		if (this.size >= this.capacity) {
			if (this.size >= this.limit) {
				return false;
			}
			this.start &= this.mask;
			this.end &= this.mask;
			final int newCapacity = this.capacity << 1;
			final T[] newBuffer = FifoQueueBuffered.createArray(newCapacity);
			if (this.start < this.end || this.end == 0) {
				System.arraycopy(this.buffer, this.start, newBuffer, 0, this.size);
			} else {
				final int firstLength = this.capacity - this.start;
				System.arraycopy(this.buffer, this.start, newBuffer, 0, firstLength);
				System.arraycopy(this.buffer, 0, newBuffer, firstLength, this.end);
			}
			this.start = 0;
			this.end = this.size;
			this.capacity = newCapacity;
			this.mask = this.capacity - 1;
			this.buffer = newBuffer;
		}
		this.buffer[this.end++ & this.mask] = buffer;
		this.size++;
		return true;
	}

	/** Take care - it's not synchronized.
	 *
	 * @return next entry in queue */
	@Override
	public final T pollFirst() {
		
		if (this.size == 0) {
			return null;
		}
		final int index = this.start++ & this.mask;
		try {
			return this.buffer[index];
		} finally {
			this.buffer[index] = null;
			this.size--;
		}
	}

	/** @return current size */
	@Override
	public final int size() {
		
		return this.size;
	}
}
