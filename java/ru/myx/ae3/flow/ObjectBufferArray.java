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
public final class ObjectBufferArray<T> implements ObjectBuffer<T> {
	private final T[]	array;
	
	private int			position;
	
	private final int	length;
	
	/**
	 * @param array
	 */
	public ObjectBufferArray(final T[] array) {
		this.array = array;
		this.position = 0;
		this.length = array.length;
	}
	
	@Override
	public final boolean hasRemaining() {
		return this.position < this.length;
	}
	
	@Override
	public final boolean isDirect() {
		return this.position == 0;
	}
	
	@Override
	public final boolean isSequence() {
		return false;
	}
	
	@Override
	public final T next() {
		if (this.position == this.length) {
			return null;
		}
		return this.array[this.position++];
	}
	
	@Override
	public final int remaining() {
		return this.length - this.position;
	}
	
	@Override
	public final T[] toDirectArray() {
		if (this.position == 0) {
			return this.array;
		}
		final int length = this.length - this.position;
		final T[] result = Convert.Array.toAny( new Object[length] );
		System.arraycopy( this.array, this.position, result, 0, length );
		return result;
	}
	
	@Override
	public final ObjectBuffer<T>[] toSequence() {
		return Convert.Array.toAny( new ObjectBuffer[] { new ObjectBufferArray<>( this.toDirectArray() ) } );
	}
}
