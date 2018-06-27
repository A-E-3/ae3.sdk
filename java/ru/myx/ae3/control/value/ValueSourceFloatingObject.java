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
public final class ValueSourceFloatingObject extends ValueSource<BasePrimitiveNumber> {
	private final Object	object;
	
	/**
	 * 
	 * @param object
	 */
	public ValueSourceFloatingObject(final Object object) {
		this.object = object;
	}
	
	@Override
	public final BasePrimitiveNumber getObject(final Object argument) {
		return Base.forDouble( Convert.Any.toDouble( this.object, 0.0 ) );
	}
}
