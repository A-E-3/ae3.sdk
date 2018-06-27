/*
 * Created on 12.05.2006
 */
package ru.myx.ae3.control.value;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BasePrimitiveString;

/**
 * 
 * @author myx
 * 
 */
public final class ValueSourceStringConst extends ValueSource<BasePrimitiveString> {
	private final BasePrimitiveString	value;
	
	/**
	 * 
	 * @param value
	 */
	public ValueSourceStringConst(final String value) {
		this.value = Base.forString( value );
	}
	
	@Override
	public final BasePrimitiveString getObject(final Object argument) {
		return this.value;
	}
}
