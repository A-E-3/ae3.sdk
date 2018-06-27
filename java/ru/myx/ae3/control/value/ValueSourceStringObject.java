/*
 * Created on 12.05.2006
 */
package ru.myx.ae3.control.value;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BasePrimitiveString;
import ru.myx.ae3.help.Convert;

/**
 * 
 * @author myx
 * 
 */
public final class ValueSourceStringObject extends ValueSource<BasePrimitiveString> {
	private final Object	object;
	
	/**
	 * 
	 * @param object
	 */
	public ValueSourceStringObject(final Object object) {
		this.object = object;
	}
	
	@Override
	public final BasePrimitiveString getObject(final Object argument) {
		return Base.forString( Convert.Any.toString( this.object, "" ) );
	}
}
