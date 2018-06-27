/*
 * Created on 11.05.2003
 * 
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author barachta
 * 
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 * @param <K>
 * @param <V>
 */
public class MapFilter<K, V> implements Map<K, V> {
	/**
	 * 
	 */
	protected Map<K, V>	parent;
	
	/**
	 * @param parent
	 */
	public MapFilter(final Map<K, V> parent) {
		this.parent = parent;
	}
	
	@Override
	public void clear() {
		this.parent.clear();
	}
	
	@Override
	public boolean containsKey(final Object key) {
		return this.parent.containsKey( key );
	}
	
	@Override
	public boolean containsValue(final Object value) {
		return this.parent.containsValue( value );
	}
	
	@Override
	public Set<Map.Entry<K, V>> entrySet() {
		return this.parent.entrySet();
	}
	
	@Override
	public boolean equals(final Object obj) {
		return this.parent.equals( obj );
	}
	
	@Override
	public V get(final Object key) {
		return this.parent.get( key );
	}
	
	@Override
	public int hashCode() {
		return this.parent.hashCode();
	}
	
	@Override
	public boolean isEmpty() {
		return this.parent.isEmpty();
	}
	
	@Override
	public Set<K> keySet() {
		return this.parent.keySet();
	}
	
	@Override
	public V put(final K key, final V value) {
		return this.parent.put( key, value );
	}
	
	@Override
	public void putAll(final Map<? extends K, ? extends V> map) {
		this.parent.putAll( map );
	}
	
	@Override
	public V remove(final Object key) {
		return this.parent.remove( key );
	}
	
	@Override
	public int size() {
		return this.parent.size();
	}
	
	@Override
	public String toString() {
		return this.parent.toString();
	}
	
	@Override
	public Collection<V> values() {
		return this.parent.values();
	}
}
