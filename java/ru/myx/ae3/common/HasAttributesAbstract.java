package ru.myx.ae3.common;

import ru.myx.ae3.base.BaseMapEditable;
import ru.myx.ae3.base.BaseObject;

/**
 *
 * @author myx
 *
 */
public /* abstract */ class HasAttributesAbstract implements HasAttributes {
	
	
	/**
	 *
	 */
	protected BaseMapEditable attributes = null;
	
	@Override
	public BaseObject getAttributes() {
		
		
		return this.attributes;
	}
	
}
