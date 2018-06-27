/*
 * Created on 11.12.2005
 */
package ru.myx.ae3.flow;

import ru.myx.ae3.help.Convert;

/**
 * @author myx
 *
 * @param <T>
 */
public interface ObjectBuffer<T> {
	
	/**
	 *
	 */
	public static final ObjectBuffer<?> NUL_BUFFER = new NulBuffer();

	/**
	 * indicates that there is at least one more object in buffer.
	 *
	 * @return boolean
	 */
	default boolean hasRemaining() {
		
		return this.remaining() > 0;
	}

	/**
	 * returns <b>true </b> if this buffer is an array backed buffer and can be
	 * converted to an array without any form of performance lost.
	 *
	 * @return boolean
	 */
	default boolean isDirect() {
		
		return false;
	}

	/**
	 * returns <b>true </b> if this buffer is a sequence and can be decomposed
	 * to a direct array of buffers.
	 *
	 * @return boolean
	 */
	default boolean isSequence() {
		
		return false;
	}

	/**
	 * returns next object in buffer.
	 *
	 * @return object
	 */
	T next();

	/**
	 * returns an amount of objects available in this buffer.
	 *
	 * @return int
	 */
	int remaining();

	/**
	 * @return object array
	 */
	default T[] toDirectArray() {
		
		final int length = this.remaining();
		final T[] result = Convert.Array.toAny(new Object[length]);
		for (int i = 0; i < length; ++i) {
			result[i] = this.next();
		}
		return result;
	}

	/**
	 * @return buffer array
	 */
	default ObjectBuffer<T>[] toSequence() {

		return Convert.Array.toAny(new ObjectBuffer[]{
				new ObjectBufferArray<>(this.toDirectArray())
		});
	}
}
