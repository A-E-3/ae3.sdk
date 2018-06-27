package ru.myx.ae3.cache;

abstract class AbstractCacheImpl {
	public abstract <T> CacheL1<T> createL1(String key, CacheType cacheType);
	
	public abstract <T> CacheL2<T> createL2(String key, String title, CacheType cacheType);
}
