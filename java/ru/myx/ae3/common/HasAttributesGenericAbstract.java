package ru.myx.ae3.common;

import ru.myx.ae3.base.BaseMap;
import ru.myx.ae3.base.BaseObject;

/**
 *
 * @author myx
 * @param <T>
 *
 */
public /* abstract */ class HasAttributesGenericAbstract<T extends HasAttributesGenericAbstract<?>> //
		extends
			HasAttributesAbstract
		implements
			HasAttributesGeneric<T> {
	
	
	@Override
	@SuppressWarnings("unchecked")
	public T setAttribute(final String name, final BaseObject value) {
		
		
		if (this.attributes == null) {
			this.attributes = BaseMap.create();
		}
		this.attributes.baseDefine(name, value);
		return (T) this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public final T setAttributes(final BaseObject attributes) {
		
		
		if (attributes != null && !attributes.baseIsPrimitive()) {
			if (this.attributes == null) {
				this.attributes = BaseMap.create();
			}
			this.attributes.baseDefineImportAllEnumerable(attributes);
		}
		return (T) this;
	}
}
