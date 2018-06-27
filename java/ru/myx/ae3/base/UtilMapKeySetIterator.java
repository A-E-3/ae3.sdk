package ru.myx.ae3.base;

import java.util.Iterator;

final class UtilMapKeySetIterator implements Iterator<String> {
	/**
	 * 
	 */
	private final BaseObject		baseObject;
	
	private final Iterator<String>	keys;
	
	private String					lastKey	= null;
	
	/**
	 * @param baseObject
	 */
	UtilMapKeySetIterator(final BaseObject baseObject) {
		this.baseObject = baseObject;
		this.keys = baseObject.baseKeysOwn();
	}
	
	@Override
	public boolean hasNext() {
		return this.keys.hasNext();
	}
	
	@Override
	public String next() {
		return this.lastKey = this.keys.next();
	}
	
	@Override
	public void remove() {
		this.baseObject.baseDelete( this.lastKey );
	}
}
