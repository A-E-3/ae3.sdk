package ru.myx.ae3.base;

import java.util.Iterator;

final class IteratorPropertiesAll<K> implements Iterator<K> {

	private BasePropertyData<K> property;
	
	IteratorPropertiesAll(final BasePropertyData<K> first) {

		this.property = first;
	}
	
	@Override
	public boolean hasNext() {

		return this.property != null;
	}
	
	@Override
	public K next() {

		assert this.property.name != null : "Property name is NULL, " + this.getClass().getSimpleName();
		try {
			return this.property.name;
		} finally {
			this.property = this.property.next;
		}
	}
	
	@Override
	public void remove() {

		// ignore
	}
}
