package ru.myx.util;

/**
 *
 * @author myx
 *
 * @param <T>
 */
public class FifoQueueLinked<T> implements BasicQueue<T> {
	
	
	static class Record<T> {
		
		Record<T> next;

		T value;
	}

	private Record<T> head;

	private Record<T> tail;

	@Override
	public boolean hasNext() {
		
		return this.head != null;
	}

	@Override
	public boolean iterateAll(final BasicQueue.IterationAllCallback<T> callback) {
		
		for (Record<T> record = this.head; record != null; record = record.next) {
			if (!callback.onNextItem(record.value)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean offerFirst(final T element) {
		
		final Record<T> record = new Record<>();
		record.value = element;
		if (this.tail == null) {
			this.head = this.tail = record;
		} else {
			record.next = this.head;
			this.head = record;
		}
		return true;
	}

	@Override
	public boolean offerLast(final T element) {
		
		final Record<T> record = new Record<>();
		record.value = element;
		if (this.tail == null) {
			this.head = this.tail = record;
		} else {
			this.tail.next = record;
			this.tail = record;
		}
		return true;
	}

	@Override
	public T pollFirst() {
		
		final Record<T> record = this.head;
		if (record == null) {
			return null;
		}
		if ((this.head = record.next) == null) {
			this.tail = null;
		}
		return record.value;
	}

	/**
	 *
	 * @param element
	 * @param previous
	 * @return
	 */
	public boolean replaceLast(final T element, final T previous) {
		
		if (this.tail != null && this.tail.value == previous) {
			this.tail.value = element;
			return true;
		}
		throw new IllegalStateException("Unexpected tail record: " + this.tail);
	}

	/**
	 * approximate, slow (iterates through), for messages, etc
	 *
	 * @return
	 */
	@Override
	public int size() {
		
		int size = 0;
		for (Record<T> record = this.head; record != null; record = record.next) {
			++size;
		}
		return size;
	}
}
