/*
 * Created on 22.03.2006
 */
package ru.myx.ae3.flow;

/**
 * @author myx
 *
 * @param <T>
 */
public final class ObjectBufferSingleton<T> implements ObjectBuffer<T> {
	
	private final T o;

	private boolean remaining;

	/**
	 * @param o
	 */
	public ObjectBufferSingleton(final T o) {
		this.o = o;
		this.remaining = true;
	}

	@Override
	public final boolean hasRemaining() {
		
		return this.remaining;
	}

	@Override
	public final T next() {
		
		final boolean remains = this.remaining;
		this.remaining = false;
		return remains
			? this.o
			: null;
	}

	@Override
	public final int remaining() {
		
		return this.remaining
			? 1
			: 0;
	}
}
