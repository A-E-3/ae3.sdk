/*
 * Created on 02.12.2003
 * 
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author myx
 * 
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 * @param <K>
 * @param <V>
 */
public class MapNoNulls<K, V> implements Map<K, V> {
	private final V			defaultValue;
	
	private final Map<K, V>	innerMap;
	
	/**
	 * @param innerMap
	 * @param defaultValue
	 * @throws IllegalArgumentException
	 */
	public MapNoNulls(final Map<K, V> innerMap, final V defaultValue) throws IllegalArgumentException {
		if (defaultValue == null) {
			throw new IllegalArgumentException( "Default value can not be equal to NULL!" );
		}
		if (innerMap == null) {
			throw new IllegalArgumentException( "Inner map can not be equal to NULL!" );
		}
		this.defaultValue = defaultValue;
		this.innerMap = innerMap;
	}
	
	/**
	 * @param defaultValue
	 * @throws IllegalArgumentException
	 */
	public MapNoNulls(final V defaultValue) throws IllegalArgumentException {
		if (defaultValue == null) {
			throw new IllegalArgumentException( "Default value can not be equal to NULL!" );
		}
		this.defaultValue = defaultValue;
		this.innerMap = new TreeMap<>();
	}
	
	@Override
	public void clear() {
		this.innerMap.clear();
	}
	
	@Override
	public Object clone() {
		return new MapNoNulls<>( new TreeMap<>( this.innerMap ), this.defaultValue );
	}
	
	@Override
	public boolean containsKey(final Object key) {
		return this.innerMap.containsKey( key );
	}
	
	@Override
	public boolean containsValue(final Object value) {
		return this.innerMap.containsValue( value );
	}
	
	@Override
	public Set<Map.Entry<K, V>> entrySet() {
		return this.innerMap.entrySet();
	}
	
	@Override
	public boolean equals(final Object obj) {
		return this.innerMap.equals( obj );
	}
	
	@Override
	public V get(final Object key) {
		final V result = this.innerMap.get( key );
		return result == null
				? this.defaultValue
				: result;
	}
	
	@Override
	public int hashCode() {
		return this.innerMap.hashCode();
	}
	
	@Override
	public boolean isEmpty() {
		return this.innerMap.isEmpty();
	}
	
	@Override
	public Set<K> keySet() {
		return this.innerMap.keySet();
	}
	
	@Override
	public V put(final K key, final V value) {
		return this.innerMap.put( key, value );
	}
	
	@Override
	public void putAll(final Map<? extends K, ? extends V> t) {
		this.innerMap.putAll( t );
	}
	
	@Override
	public V remove(final Object key) {
		return this.innerMap.remove( key );
	}
	
	@Override
	public int size() {
		return this.innerMap.size();
	}
	
	@Override
	public String toString() {
		return "NO_NULLS('" + this.defaultValue + "')" + this.innerMap.toString();
	}
	
	@Override
	public Collection<V> values() {
		return this.innerMap.values();
	}
}
