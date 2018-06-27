/*
 * Created on 25.04.2006
 */
package ru.myx.ae3.cache;

/**
 * @author myx
 * @param <V>
 */
public interface CacheL1<V> {
	/**
	 * 
	 */
	public void clear();
	
	/**
	 * @param <T>
	 * @param key
	 * @return entry
	 */
	public <T extends V> T get(final String key);
	
	/**
	 * @param <A>
	 *            Attachment type
	 * @param <R>
	 *            Result object type
	 * @param key
	 *            - NULL is not a valid argument
	 * @param creationAttachment
	 * @param creationKey
	 * @param creator
	 * @return object
	 */
	public <A, R extends V> R getCreate(
			final String key,
			final A creationAttachment,
			final String creationKey,
			final CreationHandlerObject<A, R> creator);
	
	/**
	 * @param handler
	 */
	public void iterate(final CacheIterationHandler handler);
	
	/**
	 * 
	 * @param key
	 * @param object
	 * @param ttl
	 */
	public void put(final String key, final V object, final long ttl);
	
	/**
	 * @param key
	 *            - NULL is not a valid argument
	 * @return entry
	 */
	public boolean remove(final String key);
	
	/**
	 * @return size
	 */
	public int size();
	
}
