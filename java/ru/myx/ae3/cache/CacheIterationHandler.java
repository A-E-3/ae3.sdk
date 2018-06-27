/*
 * Created on 25.04.2006
 */
package ru.myx.ae3.cache;

/**
 * @author myx
 * 
 */
public interface CacheIterationHandler {
	/**
	 * @param o
	 * @return boolean
	 */
	boolean next(final Entry<?> o);
}
