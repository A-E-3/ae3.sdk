package ru.myx.ae3.base;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;

final class UtilMapKeySet extends AbstractSet<String> {
	/**
	 * 
	 */
	private final BaseObject	baseObject;
	
	/**
	 * @param baseObject
	 */
	UtilMapKeySet(final BaseObject baseObject) {
		assert baseObject instanceof Map : "Have to be an instance of java.util.Map";
		this.baseObject = baseObject;
	}
	
	@Override
	public boolean contains(final Object x) {
		if (x instanceof BasePrimitiveString) {
			return this.baseObject.baseGetOwnProperty( (BasePrimitiveString) x ) != null;
		}
		return this.baseObject.baseGetOwnProperty( String.valueOf( x ) ) != null;
	}
	
	@Override
	public Iterator<String> iterator() {
		return new UtilMapKeySetIterator( this.baseObject );
	}
	
	@Override
	public boolean remove(final Object x) {
		return this.baseObject.baseDelete( String.valueOf( x ) );
	}
	
	@Override
	public int size() {
		return ((Map<?, ?>) this.baseObject).size();
	}
}
