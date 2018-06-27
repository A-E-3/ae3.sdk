package ru.myx.ae3.base;

import ru.myx.ae3.reflect.Reflect;
import ru.myx.ae3.reflect.ReflectionIgnore;

/**
 *
 * @author myx
 * 		
 * @param <V>
 */
@ReflectionIgnore
public abstract class BaseFutureAbstract<V> extends BaseAbstract implements BaseFuture<V>, BaseObjectNotDynamic {
	
	static {
		Reflect.classToBasePrototype(BaseFutureAbstract.class);
	}
	
	/**
	 * Waits for completion and returns an error or null
	 * 
	 * @return
	 */
	public abstract Throwable baseError();
	
	/**
	 * must wait for completion and return result or throw an exception
	 */
	@Override
	public abstract V baseValue();
}
