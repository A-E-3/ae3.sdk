package ru.myx.util;

/**
 *
 * @author myx
 *
 * @param <T>
 */
public interface BasicQueue<T> {
	
	
	/**
	 *
	 * @author myx
	 *
	 * @param <T>
	 */
	interface IterationAllCallback<T> {
		
		boolean onNextItem(T item);
	}

	/**
	 * returns true if queue is not empty
	 *
	 * @return
	 */
	public boolean hasNext();

	/**
	 * @param callback
	 *            - TRUE is continue, FALSE is stop.
	 * @return false if stopped
	 */
	public boolean iterateAll(BasicQueue.IterationAllCallback<T> callback);

	/**
	 * adds first element to the queue
	 *
	 * @param element
	 * @return
	 */
	public boolean offerFirst(T element);

	/**
	 * adds last to the queue
	 *
	 * @param element
	 * @return
	 */
	public boolean offerLast(T element);

	/**
	 * returns first and removes it from queue
	 *
	 * @return
	 */
	public T pollFirst();

	/**
	 * approximate, maybe slow, for messages, etc
	 *
	 * TODO: remove?
	 *
	 * @return
	 */
	public int size();

}
