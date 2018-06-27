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
public final class ValueSourceLongConst extends ValueSource<BasePrimitiveNumber> {
	private final BasePrimitiveNumber	value;
	
	/**
	 * 
	 * @param object
	 */
	public ValueSourceLongConst(final Object object) {
		this.value = Base.forLong( Convert.Any.toLong( object, 0L ) );
	}
	
	@Override
	public final BasePrimitiveNumber getObject(final Object argument) {
		return this.value;
	}
}
