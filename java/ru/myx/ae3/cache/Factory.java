/*
 * Created on 09.07.2004
 */
package ru.myx.ae3.cache;

/**
 * 
 * @author myx
 * @param <V>
 */
interface Factory<V> {
	/**
	 * 
	 * @param <T>
	 * @param keyLocal
	 * @param keyExact
	 * @param ttl
	 *            -1L if no TTL expiration supposed to happen
	 * @param object
	 * @return
	 */
	<T extends V> Entry<T> getEntry(final String keyLocal, final String keyExact, final long ttl, final T object);
	
	/**
	 * @return
	 */
	Class<? super V> getFactoryClass();
}
