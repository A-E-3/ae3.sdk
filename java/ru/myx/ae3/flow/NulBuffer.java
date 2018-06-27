/*
 * Created on 22.03.2006
 */
package ru.myx.ae3.flow;

@SuppressWarnings("unchecked")
final class NulBuffer implements ObjectBuffer<Object> {
	private static final Object[]				DUMMY_ARRAY		= new Object[0];
	
	private static final ObjectBuffer<Object>[]	DUMMY_SEQUENCE	= new ObjectBuffer[0];
	
	@Override
	public final boolean hasRemaining() {
		return false;
	}
	
	@Override
	public final boolean isDirect() {
		return false;
	}
	
	@Override
	public final boolean isSequence() {
		return false;
	}
	
	@Override
	public final Object next() {
		return null;
	}
	
	@Override
	public final int remaining() {
		return 0;
	}
	
	@Override
	public final Object[] toDirectArray() {
		return NulBuffer.DUMMY_ARRAY;
	}
	
	@Override
	public final ObjectBuffer<Object>[] toSequence() {
		return NulBuffer.DUMMY_SEQUENCE;
	}
}
