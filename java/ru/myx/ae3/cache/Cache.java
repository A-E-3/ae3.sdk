package ru.myx.ae3.cache;

import ru.myx.ae3.AbstractSAPI;
import ru.myx.ae3.base.BaseFunction;
import ru.myx.ae3.base.BaseObject;

/**
 * @author myx
 * 
 */
public class Cache extends AbstractSAPI {
	private static final AbstractCacheImpl	BASE_IMPL;
	
	static {
		/**
		 * this block should go last
		 */
		{
			BASE_IMPL = AbstractSAPI.createObject( "ru.myx.ae3.cache.ImplementCache" );
		}
	}
	
	/**
	 * 
	 * @param function
	 * @param thisValue
	 * @return
	 */
	public static final CreationHandlerObject<Object, BaseObject> createBaseFactory(
			final BaseFunction function,
			final BaseObject thisValue) {
		return new BaseFunctionCreationHandler( thisValue, function );
	}
	
	/**
	 * @param <V>
	 * @param key
	 * @param cacheType
	 * @return cache
	 */
	public static final <V> CacheL1<V> createL1(final String key, final CacheType cacheType) {
		return Cache.BASE_IMPL.createL1( key, cacheType );
	}
	
	/**
	 * @param <V>
	 * @param title
	 * @param type
	 * @return cache
	 */
	public static final <V> CacheL2<V> createL2(final String title, final CacheType type) {
		return Cache.BASE_IMPL.createL2( "", title, type );
	}
	
	/**
	 * @param <V>
	 * @param key
	 * @param title
	 * @return cache
	 */
	public static final <V> CacheL2<V> createL2(final String key, final String title) {
		return Cache.BASE_IMPL.createL2( key, title, CacheType.NORMAL_JAVA_SOFT );
	}
	
	private Cache() {
		//
	}
}
