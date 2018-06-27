/*
 * Created on 03.05.2005
 */
package ru.myx.ae3.base;

import java.util.Collection;

/**
 * @author myx
 * 
 * @param <T>
 */
public class BaseLinkedListLimited<T extends Object> extends BaseLinkedList<T> {
	/**
     * 
     */
	private static final long	serialVersionUID	= 1978981599142444504L;
	
	private final int			limit;
	
	
	/**
	 * @param limit
	 *            max length to maintain
	 */
	public BaseLinkedListLimited(final int limit) {
	
		this.limit = limit;
	}
	
	
	@Override
	public void add(
			final int index,
			final T element) {
	
		super.add( index, element );
		if (super.size() > this.limit) {
			super.removeRange( this.limit, super.size() );
		}
	}
	
	
	@Override
	public boolean add(
			final T o) {
	
		try {
			return super.add( o );
		} finally {
			if (super.size() > this.limit) {
				super.removeRange( this.limit, super.size() );
			}
		}
	}
	
	
	@Override
	public boolean addAll(
			final Collection<? extends T> c) {
	
		try {
			return super.addAll( c );
		} finally {
			if (super.size() > this.limit) {
				super.removeRange( this.limit, super.size() );
			}
		}
	}
	
	
	@Override
	public boolean addAll(
			final int index,
			final Collection<? extends T> c) {
	
		try {
			return super.addAll( index, c );
		} finally {
			if (super.size() > this.limit) {
				super.removeRange( this.limit, super.size() );
			}
		}
	}
	
	
	@Override
	public void addFirst(
			final T o) {
	
		super.addFirst( o );
		if (super.size() > this.limit) {
			super.removeRange( this.limit, super.size() );
		}
	}
	
	
	@Override
	public void addLast(
			final T o) {
	
		super.addLast( o );
		if (super.size() > this.limit) {
			super.removeRange( this.limit, super.size() );
		}
	}
	
	
	@Override
	public BaseLinkedListLimited<T> clone() {
	
		final BaseLinkedListLimited<T> result = new BaseLinkedListLimited<>( this.limit );
		result.addAll( this );
		return result;
	}
}
