package ru.myx.util;

/**
 * @author myx
 * 
 * @param <T>
 */
public class QueueStackRecord<T> {
	private QueueStackRecord<T>	next;
	
	private T					value;
	
	/**
	 * Creates queue / stack
	 */
	public QueueStackRecord() {
		//
	}
	
	/**
	 * Internal, created record
	 * 
	 * @param value
	 */
	private QueueStackRecord(final T value) {
		this.value = value;
	}
	
	/**
	 * cleans everything
	 */
	public void clear() {
		this.next = null;
		this.value = null;
	}
	
	/**
	 * @param value
	 * @return
	 */
	public boolean enqueue(final T value) {
		final QueueStackRecord<T> next = new QueueStackRecord<>( value );
		next.next = this.next == null
				? next
				: this.next.next;
		this.next = this.next == null
				? next
				: (this.next.next = next);
		return true;
	}
	
	/**
	 * @return
	 */
	public T first() {
		if (this.next == null) {
			return null;
		}
		if (this.next.next == this.next) {
			return this.next.value;
		}
		return this.next.next.value;
	}
	
	/**
	 * @return true when queue/stack contains no data
	 */
	public boolean isEmpty() {
		return this.next == null;
	}
	
	/**
	 * @return
	 */
	public T next() {
		if (this.next == null) {
			return null;
		}
		if (this.next.next == this.next) {
			try {
				return this.next.value;
			} finally {
				this.next = null;
			}
		}
		try {
			return this.next.next.value;
		} finally {
			this.next.next = this.next.next.next;
		}
	}
	
	/**
	 * @return
	 */
	public T peek() {
		return this.next == null
				? null
				: this.next.value;
	}
	
	/**
	 * @return
	 */
	public T pop() {
		if (this.next == null) {
			return null;
		}
		try {
			return this.next.value;
		} finally {
			this.next = this.next.next;
		}
	}
	
	/**
	 * @param value
	 * @return
	 */
	public boolean push(final T value) {
		final QueueStackRecord<T> record = new QueueStackRecord<>( value );
		record.next = this.next;
		this.next = record;
		return true;
	}
}
