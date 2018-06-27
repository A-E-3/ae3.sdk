package ru.myx.ae3.base;

import java.util.Iterator;

final class IteratorBaseArrayKeyPrimitive implements Iterator<BasePrimitive<?>> {
	private final BaseArray	array;
	
	private int						index;
	
	IteratorBaseArrayKeyPrimitive(final BaseArray array) {
		this.array = array;
		this.index = 0;
	}
	
	@Override
	public boolean hasNext() {
		return this.index < this.array.length();
	}
	
	@Override
	public BasePrimitive<?> next() {
		return Base.forInteger( this.index++ );
	}
	
	@Override
	public void remove() {
		// ignore
	}
}
