package ru.myx.ae3.base;

import java.util.Map;

final class UtilMapEntrySetMapEntry implements Map.Entry<String, Object> {
	
	private final BaseObject baseObject;
	
	private final String key;
	
	UtilMapEntrySetMapEntry(final BaseObject baseObject, final String key) {
		this.baseObject = baseObject;
		this.key = key;
	}
	
	@Override
	public String getKey() {
		
		return this.key;
	}
	
	@Override
	public Object getValue() {
		
		return this.baseObject.baseGet(this.key, BaseObject.UNDEFINED).baseValue();
	}
	
	@Override
	public Object setValue(final Object value) {
		
		try {
			return this.baseObject.baseGet(this.key, BaseObject.UNDEFINED).baseValue();
		} finally {
			this.baseObject.baseDefine(this.key, Base.forUnknown(value));
		}
	}
}
