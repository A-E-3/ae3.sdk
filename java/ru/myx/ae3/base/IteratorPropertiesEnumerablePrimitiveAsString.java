package ru.myx.ae3.base;

import java.util.Iterator;

final class IteratorPropertiesEnumerablePrimitiveAsString<K> implements Iterator<String> {
	
	private BasePropertyData<K> property;

	IteratorPropertiesEnumerablePrimitiveAsString(final BasePropertyData<K> first) {
		
		this.property = first;
	}

	@Override
	public boolean hasNext() {
		
		return this.property != null;
	}

	@Override
	public String next() {
		
		assert this.property.name != null : "Property name is NULL, " + this.getClass().getSimpleName();
		try {
			return this.property.name.toString();
		} finally {
			for (;;) {
				final BasePropertyData<?> property = this.property = this.property.next;
				/** now ^^^^^^ it is for sure we don't need to pass 'name' to 'isEnumerable' */
				if (property == null || (property.attributes & BaseProperty.ATTR_ENUMERABLE) != 0) {
					break;
				}
			}
		}
	}

	@Override
	public void remove() {
		
		// ignore
	}
}
