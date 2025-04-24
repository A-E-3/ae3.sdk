package ru.myx.ae3.base;

import java.util.Iterator;

final class IteratorPropertiesAllToPrimitive implements Iterator<BasePrimitiveString> {

	private BasePropertyData<String> property;
	
	IteratorPropertiesAllToPrimitive(final BasePropertyData<String> first) {

		this.property = first;
	}
	
	@Override
	public boolean hasNext() {

		return this.property != null;
	}
	
	@Override
	public BasePrimitiveString next() {

		assert this.property.name != null : "Property name is NULL, " + this.getClass().getSimpleName();
		try {
			return Base.forString(this.property.name);
		} finally {
			this.property = this.property.next;
		}
	}
	
	@Override
	public void remove() {

		// ignore
	}
}
