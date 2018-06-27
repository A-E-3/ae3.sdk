/*
 * Created on 12.05.2006
 */
package ru.myx.ae3.control.value;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BasePrimitiveNumber;
import ru.myx.ae3.help.Convert;

/**
 * 
 * @author myx
 * 
 */
public final class ValueSourceFloatingConst extends ValueSource<BasePrimitiveNumber> {
	private final BasePrimitiveNumber	value;
	
	/**
	 * 
	 * @param attribute
	 */
	public ValueSourceFloatingConst(final Object attribute) {
		this.value = Base.forDouble( Convert.Any.toDouble( attribute, 0.0 ) );
	}
	
	@Override
	public final BasePrimitiveNumber getObject(final Object argument) {
		return this.value;
	}
}
