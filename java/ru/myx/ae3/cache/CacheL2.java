/*
 * Created on 25.04.2006
 */
package ru.myx.ae3.cache;


/**
 * @author myx
 * @param <V>
 * 
 */
public interface CacheL2<V> {
	/**
	 * 
	 */
	public void clear();
	
	/**
	 * @param <T>
	 * @param key
	 * @param extraParams
	 * @return object
	 */
	public <T extends V> T get(final String key, final String extraParams);
	
	/**
	 * @param <A>
	 *            Attachment type
	 * @param <R>
	 *            Result object type
	 * @param key
	 *            - NULL is not a valid argument
	 * @param extraParams
	 * @param creationAttachment
	 * @param creationKey
	 * @param creator
	 * @return object
	 */
	public <A, R extends V> R get(
			final String key,
			final String extraParams,
			final A creationAttachment,
			final String creationKey,
			final CreationHandlerObject<A, R> creator);
	
	/**
	 * @param key
	 *            - NULL is not a valid argument
	 * @param extraParams
	 * @param o
	 * @param ttl
	 */
	public void put(final String key, final String extraParams, final V o, final long ttl);
	
	/**
	 * @param key
	 *            - NULL is not a valid argument
	 */
	public void remove(final String key);
	
	/**
	 * @param id
	 * @param extraParams
	 */
	public void remove(final String id, final String extraParams);
	
	/**
	 * @return size
	 */
	public int size();
}
