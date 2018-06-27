package ru.myx.ae3.base;

import java.util.Iterator;

/**
 * @author myx
 * 
 */
public final class IteratorStringForAnything implements Iterator<String> {
	private final Iterator<?>	iterator;
	
	/**
	 * @param iterator
	 */
	public IteratorStringForAnything(final Iterator<?> iterator) {
		this.iterator = iterator;
	}
	
	@Override
	public boolean hasNext() {
		return this.iterator.hasNext();
	}
	
	@Override
	public String next() {
		return this.iterator.next().toString();
	}
	
	@Override
	public void remove() {
		// ignore
	}
}
