package ru.myx.util;

/**
 * 
 * @author myx
 * 
 * @param <T>
 */
public interface BasicStack<T> {
	/**
	 * returns first from the stack
	 * 
	 * @return
	 */
	public T peek();
	
	/**
	 * removes first from the stack
	 * 
	 * @return
	 */
	public T pop();
	
	/**
	 * adds first to the stack
	 * 
	 * @param element
	 */
	public void push(T element);
	
}
