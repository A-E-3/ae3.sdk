/*
 * Created on 25.11.2003
 * 
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.util;

import java.util.Map;

/**
 * @author myx
 * 
 * @param <K>
 * @param <V>
 */
public final class MapSetNullButRemove<K, V> extends MapWithDefaults<K, V> {
	/**
	 * @param values
	 * @param defaults
	 */
	public MapSetNullButRemove(final Map<K, V> values, final Map<K, V> defaults) {
		super( values, defaults );
	}
	
	@Override
	public final void clear() {
		for (final Map.Entry<K, V> current : this.values.entrySet()) {
			current.setValue( null );
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public final V remove(final Object key) {
		final K k = (K) key;
		return this.values.put( k, null );
	}
}
