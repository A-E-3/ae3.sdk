/*
 * Created on 11.05.2003
 * 
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.util;

import java.util.Map;

/**
 * @author barachta
 * @param <K>
 * @param <V>
 */
public final class EntrySimple<K, V> implements Map.Entry<K, V>, Comparable<Map.Entry<K, V>> {
	private final K	key;
	
	private V		value;
	
	/**
	 * @param key
	 * @param value
	 */
	public EntrySimple(final K key, final V value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public int compareTo(final Map.Entry<K, V> entry) {
		if (entry == this) {
			return 0;
		}
		if (entry == null) {
			return 1;
		}
		if (this.key == null) {
			return entry.getKey() == null
					? 0
					: -1;
		}
		return this.key.toString().compareTo( String.valueOf( entry.getKey() ) );
	}
	
	@Override
	public K getKey() {
		return this.key;
	}
	
	@Override
	public V getValue() {
		return this.value;
	}
	
	@Override
	public V setValue(final V value) {
		try {
			return this.value;
		} finally {
			this.value = value;
		}
	}
	
	@Override
	public String toString() {
		return this.key + "=" + this.value;
	}
}
