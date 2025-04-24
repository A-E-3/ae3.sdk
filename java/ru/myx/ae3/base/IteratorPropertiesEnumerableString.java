package ru.myx.ae3.base;

import java.util.Iterator;

final class IteratorPropertiesEnumerableString implements Iterator<String> {
	
	private BasePropertyData<String> property;

	IteratorPropertiesEnumerableString(final BasePropertyData<String> first) {
		
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
			return this.property.name;
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
