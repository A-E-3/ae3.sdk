/*
 * Created on 22.03.2006
 */
package ru.myx.ae3.flow;

import ru.myx.ae3.help.Convert;

/**
 * @author myx
 *
 * @param <T>
 */
public final class ObjectBufferSequence<T> implements ObjectBuffer<T> {
	
	private final ObjectBuffer<T>[] sequence;

	private int remaining = 0;

	private int index = 0;

	/**
	 * @param sequence
	 */
	public ObjectBufferSequence(final ObjectBuffer<T>[] sequence) {
		this.sequence = sequence;
		for (int i = sequence.length - 1; i >= 0; --i) {
			this.remaining += sequence[i].remaining();
		}
	}

	@Override
	public final boolean isSequence() {
		
		return true;
	}

	@Override
	public final T next() {
		
		if (this.remaining == 0) {
			return null;
		}
		this.remaining--;
		if (!this.sequence[this.index].hasRemaining()) {
			this.index++;
		}
		return this.sequence[this.index].next();
	}

	@Override
	public final int remaining() {
		
		return this.remaining;
	}

	@Override
	public final ObjectBuffer<T>[] toSequence() {
		
		this.remaining = 0;
		if (this.index == 0) {
			return this.sequence;
		}
		final int length = this.sequence.length - this.index;
		final ObjectBuffer<T>[] result = Convert.Array.toAny(new ObjectBuffer[length]);
		System.arraycopy(this.sequence, this.index, result, 0, length);
		return result;
	}
}
