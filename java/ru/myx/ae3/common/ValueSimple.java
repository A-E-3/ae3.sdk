package ru.myx.ae3.common;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseAbstract;
import ru.myx.ae3.base.BaseObjectNoOwnProperties;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BasePrimitiveString;

/**
 * @author myx
 * 
 * @param <T>
 */
public final class ValueSimple<T> extends BaseAbstract implements Value<T>, BaseObjectNoOwnProperties {
	
	
	private final T value;
	
	/**
	 * @param value
	 */
	public ValueSimple(final T value) {
		this.value = value;
	}
	
	@Override
	public BaseObject basePrototype() {
		
		
		return Base.forUnknown(this.value);
	}
	
	@Override
	public BasePrimitiveString baseToString() {
		
		
		return Base.forUnknown(this.value).baseToString();
	}
	
	@Override
	public String toString() {
		
		
		return String.valueOf(this.value);
	}
	
	@Override
	public T baseValue() {
		
		
		return this.value;
	}
}
