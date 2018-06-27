/*
 * Created on 25.04.2006
 */
package ru.myx.ae3.cache;

/**
 * @author myx
 * @param <V>
 * 
 */
public interface CacheL3<V> {
	/**
     * 
     */
	public void clear();
	
	/**
	 * @param <T>
	 * @param group
	 * @return cache
	 */
	public <T extends V> CacheL2<T> getCacheL2(final Object group);
}
