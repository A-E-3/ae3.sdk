package ru.myx.ae3.base;

import java.util.Iterator;

final class IteratorPropertiesAllToString<K> implements Iterator<String> {
	
	private BasePropertyData<K> property;

	IteratorPropertiesAllToString(final BasePropertyData<K> first) {
		
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
			this.property = this.property.next;
		}
	}

	@Override
	public void remove() {
		
		// ignore
	}
}
