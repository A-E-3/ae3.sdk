package ru.myx.ae3.control.value;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BasePrimitiveString;
import ru.myx.ae3.base.BaseString;
import ru.myx.ae3.help.Convert;

/*
 * Created on 29.06.2004
 */

/**
 * TODO: replace with (extends from) simple function?
 * 
 * @author myx
 * 
 * @param <T>
 */
public abstract class ValueSource<T extends BaseObject> {
	/**
	 * @param arg
	 * @return double
	 */
	public double getDoubleValue(final Object arg) {
		return Convert.Any.toDouble( this.getObject( arg ), 0.0 );
	}
	
	/**
	 * @param arg
	 * @return long
	 */
	public long getLongValue(final Object arg) {
		return Convert.Any.toLong( this.getObject( arg ), 0L );
	}
	
	/**
	 * @param arg
	 * @return object
	 */
	public abstract T getObject(final Object arg);
	
	/**
	 * @param arg
	 * @return string
	 */
	public BasePrimitiveString getStringValue(final Object arg) {
		final BaseObject object = this.getObject( arg );
		return object == BaseObject.UNDEFINED || object == BaseObject.NULL
				? BaseString.EMPTY
				: object.baseToString();
	}
}
