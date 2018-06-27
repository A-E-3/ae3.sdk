/*
 * Created on 01.07.2004
 */
package ru.myx.ae3.extra;

import ru.myx.ae3.base.BaseNativeObject;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.binary.Transfer;
import ru.myx.ae3.common.Value;
import ru.myx.ae3.reflect.Reflect;

/**
 * @author myx
 *
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface External extends Value<Object> {
	
	
	/**
	 *
	 */
	static BaseObject PROTOTYPE = new BaseNativeObject(Reflect.classToBasePrototype(External.class));

	@Override
	Object baseValue();

	@Override
	boolean equals(Object anotherObject);

	/**
	 * workaround
	 * 
	 * @return
	 */
	default long getLength() {
		
		
		final Object value = this.baseValue();
		if (value instanceof CharSequence) {
			return ((CharSequence) value).length();
		}
		return Transfer.binarySize(value);
	}
	
	/**
	 * @return string
	 */
	String getIdentity();

	/**
	 * @return date
	 */
	long getRecordDate();

	/**
	 * @return issuer
	 */
	Object getRecordIssuer();

	@Override
	int hashCode();

	/**
	 * @return object (buffer or copier or stream or bytes - any type of binary)
	 */
	Object toBinary();

	@Override
	String toString();
}
