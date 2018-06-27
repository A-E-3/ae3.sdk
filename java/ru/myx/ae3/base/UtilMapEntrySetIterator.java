package ru.myx.ae3.base;

import java.util.Iterator;
import java.util.Map;

final class UtilMapEntrySetIterator implements Iterator<Map.Entry<String, Object>> {
	/**
	 * 
	 */
	private final BaseObject		baseObject;
	
	private final Iterator<String>	keys;
	
	private String					lastKey	= null;
	
	/**
	 * @param utilMapEntrySet
	 */
	UtilMapEntrySetIterator(final BaseObject baseObject) {
		this.baseObject = baseObject;
		this.keys = baseObject.baseKeysOwn();
	}
	
	@Override
	public boolean hasNext() {
		return this.keys.hasNext();
	}
	
	@Override
	public java.util.Map.Entry<String, Object> next() {
		final String key = this.lastKey = this.keys.next();
		return new UtilMapEntrySetMapEntry( this.baseObject, key );
	}
	
	@Override
	public void remove() {
		this.baseObject.baseDelete( this.lastKey );
	}
}
