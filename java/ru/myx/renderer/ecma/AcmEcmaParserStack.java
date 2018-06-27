/*
 * Created on 18.10.2005
 */
package ru.myx.renderer.ecma;

import java.util.EmptyStackException;

final class AcmEcmaParserStack {
	/**
	 * The array buffer into which the elements of the ArrayList are stored. The
	 * capacity of the ArrayList is the length of this array buffer.
	 */
	private TokenStatement[]	elementData;
	
	/**
	 * The size of the ArrayList (the number of elements it contains).
	 * 
	 * @serial
	 */
	private int					size;
	
	/**
	 * Constructs an empty list with the specified initial capacity.
	 * 
	 * @exception IllegalArgumentException
	 *                if the specified initial capacity is negative
	 */
	AcmEcmaParserStack() {
		this.elementData = new TokenStatement[8];
	}
	
	/**
	 * Inserts the specified element at the specified position in this list.
	 * Shifts the element currently at that position (if any) and any subsequent
	 * elements to the right (adds one to their indices).
	 * 
	 * @param index
	 *            index at which the specified element is to be inserted.
	 * @param element
	 *            element to be inserted.
	 * @throws IndexOutOfBoundsException
	 *             if index is out of range
	 *             <tt>(index &lt; 0 || index &gt; size())</tt>.
	 */
	public final void add(final int index, final TokenStatement element) {
		if (index > this.size || index < 0) {
			throw new IndexOutOfBoundsException( "Index: " + index + ", Size: " + this.size );
		}
		this.ensureCapacity( this.size + 1 ); // Increments modCount!!
		System.arraycopy( this.elementData, index, this.elementData, index + 1, this.size - index );
		this.elementData[index] = element;
		this.size++;
	}
	
	/**
	 * Appends the specified element to the end of this list.
	 * 
	 * @param o
	 *            element to be appended to this list.
	 * @return <tt>true</tt> (as per the general contract of Collection.add).
	 */
	public final boolean add(final TokenStatement o) {
		this.ensureCapacity( this.size + 1 ); // Increments modCount!!
		this.elementData[this.size++] = o;
		return true;
	}
	
	/**
	 * Removes all of the elements from this list. The list will be empty after
	 * this call returns.
	 */
	public final void clear() {
		// Let gc do its work
		for (int i = this.size - 1; i >= 0; --i) {
			this.elementData[i] = null;
		}
		this.size = 0;
	}
	
	/**
	 * Increases the capacity of this <tt>ArrayList</tt> instance, if necessary,
	 * to ensure that it can hold at least the number of elements specified by
	 * the minimum capacity argument.
	 * 
	 * @param minCapacity
	 *            the desired minimum capacity.
	 */
	private final void ensureCapacity(final int minCapacity) {
		final int oldCapacity = this.elementData.length;
		if (minCapacity > oldCapacity) {
			final Object[] oldData = this.elementData;
			int newCapacity = oldCapacity * 3 / 2 + 1;
			if (newCapacity < minCapacity) {
				newCapacity = minCapacity;
			}
			this.elementData = new TokenStatement[newCapacity];
			System.arraycopy( oldData, 0, this.elementData, 0, this.size );
		}
	}
	
	/*
	 * Private remove method that skips bounds checking and does not return the
	 * value removed.
	 */
	private final void fastRemove(final int index) {
		final int numMoved = this.size - index - 1;
		if (numMoved > 0) {
			System.arraycopy( this.elementData, index + 1, this.elementData, index, numMoved );
		}
		this.elementData[--this.size] = null; // Let gc do its work
	}
	
	// Positional Access Operations
	/**
	 * Returns the element at the specified position in this list.
	 * 
	 * @param index
	 *            index of element to return.
	 * @return the element at the specified position in this list.
	 * @throws IndexOutOfBoundsException
	 *             if index is out of range <tt>(index
	 * 		  &lt; 0 || index &gt;= size())</tt>.
	 */
	public final TokenStatement get(final int index) {
		if (index >= this.size) {
			throw new IndexOutOfBoundsException( "Index: " + index + ", Size: " + this.size );
		}
		return this.elementData[index];
	}
	
	/**
	 * Tests if this list has no elements.
	 * 
	 * @return <tt>true</tt> if this list has no elements; <tt>false</tt>
	 *         otherwise.
	 */
	public final boolean isEmpty() {
		return this.size == 0;
	}
	
	/**
	 * Looks at the object at the top of this stack without removing it from the
	 * stack.
	 * 
	 * @return the object at the top of this stack (the last item of the
	 *         <tt>Vector</tt> object).
	 * @exception EmptyStackException
	 *                if this stack is empty.
	 */
	public final TokenStatement peek() {
		if (this.size == 0) {
			throw new EmptyStackException();
		}
		return this.elementData[this.size - 1];
	}
	
	/**
	 * Removes the object at the top of this stack and returns that object as
	 * the value of this function.
	 * 
	 * @return The object at the top of this stack (the last item of the
	 *         <tt>Vector</tt> object).
	 * @exception EmptyStackException
	 *                if this stack is empty.
	 * 
	 */
	public TokenStatement pop() {
		if (this.size == 0) {
			throw new EmptyStackException();
		}
		final TokenStatement obj = this.elementData[this.size - 1];
		this.elementData[--this.size] = null;
		return obj;
	}
	
	/**
	 * Pushes an item onto the top of this stack. This has exactly the same
	 * effect as: <blockquote>
	 * 
	 * <pre>
	 * addElement( item )
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param item
	 *            the item to be pushed onto this stack.
	 * @return the <code>item</code> argument.
	 * @see java.util.Vector#addElement
	 */
	public final TokenStatement push(final TokenStatement item) {
		this.ensureCapacity( this.size + 1 ); // Increments modCount!!
		this.elementData[this.size++] = item;
		return item;
	}
	
	/**
	 * Removes the element at the specified position in this list. Shifts any
	 * subsequent elements to the left (subtracts one from their indices).
	 * 
	 * @param index
	 *            the index of the element to removed.
	 * @return the element that was removed from the list.
	 * @throws IndexOutOfBoundsException
	 *             if index out of range <tt>(index
	 * 		  &lt; 0 || index &gt;= size())</tt>.
	 */
	public final TokenStatement remove(final int index) {
		if (index >= this.size) {
			throw new IndexOutOfBoundsException( "Index: " + index + ", Size: " + this.size );
		}
		final TokenStatement oldValue = this.elementData[index];
		final int numMoved = this.size - index - 1;
		if (numMoved > 0) {
			System.arraycopy( this.elementData, index + 1, this.elementData, index, numMoved );
		}
		this.elementData[--this.size] = null; // Let gc do its work
		return oldValue;
	}
	
	/**
	 * Removes a single instance of the specified element from this list, if it
	 * is present (optional operation). More formally, removes an element
	 * <tt>e</tt> such that <tt>(o==null ? e==null :
	 * o.equals(e))</tt>, if the list contains one or more such elements.
	 * Returns <tt>true</tt> if the list contained the specified element (or
	 * equivalently, if the list changed as a result of the call).
	 * <p>
	 * 
	 * @param o
	 *            element to be removed from this list, if present.
	 * @return <tt>true</tt> if the list contained the specified element.
	 */
	public boolean remove(final Object o) {
		if (o == null) {
			for (int index = 0; index < this.size; index++) {
				if (this.elementData[index] == null) {
					this.fastRemove( index );
					return true;
				}
			}
		} else {
			for (int index = 0; index < this.size; index++) {
				if (o.equals( this.elementData[index] )) {
					this.fastRemove( index );
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Replaces the element at the specified position in this list with the
	 * specified element.
	 * 
	 * @param index
	 *            index of element to replace.
	 * @param element
	 *            element to be stored at the specified position.
	 * @return the element previously at the specified position.
	 * @throws IndexOutOfBoundsException
	 *             if index out of range
	 *             <tt>(index &lt; 0 || index &gt;= size())</tt>.
	 */
	public final TokenStatement set(final int index, final TokenStatement element) {
		if (index >= this.size) {
			throw new IndexOutOfBoundsException( "Index: " + index + ", Size: " + this.size );
		}
		final TokenStatement oldValue = this.elementData[index];
		this.elementData[index] = element;
		return oldValue;
	}
	
	/**
	 * Returns the number of elements in this list.
	 * 
	 * @return the number of elements in this list.
	 */
	public final int size() {
		return this.size;
	}
	
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		for (int i = this.size - 1; i >= 0; --i) {
			builder.append( ',' );
			builder.append( this.elementData[i].getClass().getSimpleName() );
		}
		return builder.toString();
	}
}
