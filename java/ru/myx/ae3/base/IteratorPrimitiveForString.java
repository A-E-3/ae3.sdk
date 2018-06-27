package ru.myx.ae3.base;

import java.util.Iterator;

/**
 * @author myx
 * 
 */
public final class IteratorPrimitiveForString implements Iterator<BasePrimitiveString> {
	private final Iterator<String>	iterator;
	
	/**
	 * @param iterator
	 */
	public IteratorPrimitiveForString(final Iterator<String> iterator) {
		this.iterator = iterator;
	}
	
	@Override
	public boolean hasNext() {
		return this.iterator.hasNext();
	}
	
	@Override
	public BasePrimitiveString next() {
		return Base.forString( this.iterator.next() );
	}
	
	@Override
	public void remove() {
		// ignore
	}
}
