/*
 * Created on 25.03.2004
 * 
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ru.myx.util;

import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author myx
 * @param <K>
 * @param <V>
 */
public class MapWithDefaults<K, V> implements Map<K, V> {
	/**
	 * 
	 */
	protected final Map<K, V>	values;
	
	private final Map<K, V>		defaults;
	
	/**
	 * @param values
	 * @param defaults
	 */
	public MapWithDefaults(final Map<K, V> values, final Map<K, V> defaults) {
		if (defaults == null) {
			throw new NullPointerException( "default data map is null!" );
		}
		if (values == null) {
			throw new NullPointerException( "value data map is null!" );
		}
		if (defaults == this) {
			throw new NullPointerException( "default data map equals to this!" );
		}
		if (values == this) {
			throw new NullPointerException( "value data map equals to this!" );
		}
		this.values = values;
		this.defaults = defaults;
	}
	
	/**
	 * Clears values, but works only when
	 * ParamsCollectionWithDefaults(ae1.base.ParamsProvider Defaults)
	 * constructor was used to create an object!
	 */
	@Override
	public void clear() {
		this.values.clear();
	}
	
	@Override
	public boolean containsKey(final Object key) {
		return this.values.containsKey( key ) || this.defaults.containsKey( key );
	}
	
	@Override
	public boolean containsValue(final Object value) {
		return this.values.containsValue( value );
	}
	
	@Override
	public Set<Map.Entry<K, V>> entrySet() {
		if (this.isEmpty()) {
			return Collections.emptySet();
		}
		return new AbstractSet<>() {
			@Override
			public Iterator<Map.Entry<K, V>> iterator() {
				return new Iterator<>() {
					private final Iterator<K>	e	= MapWithDefaults.this.keys();
					
					@Override
					public boolean hasNext() {
						return this.e.hasNext();
					}
					
					@Override
					public Map.Entry<K, V> next() {
						final K key = this.e.next();
						return new EntrySimple<>( key, MapWithDefaults.this.get( key ) );
					}
					
					@Override
					public void remove() {
						this.e.remove();
					}
				};
			}
			
			@Override
			public int size() {
				return MapWithDefaults.this.size();
			}
		};
	}
	
	@Override
	public V get(final Object key) {
		if (this.values.containsKey( key )) {
			return this.values.get( key );
		}
		return this.defaults.get( key );
	}
	
	@Override
	public boolean isEmpty() {
		return this.values.isEmpty() && this.defaults.isEmpty();
	}
	
	/**
	 * Returns Keys enumeration for both default and real values
	 * 
	 * @return keys iterator
	 */
	Iterator<K> keys() {
		final Set<K> result = new TreeSet<>( this.defaults.keySet() );
		result.addAll( this.values.keySet() );
		return new Iterator<>() {
			private final Iterator<K>	e	= result.iterator();
			
			private K					key;
			
			@Override
			public boolean hasNext() {
				return this.e.hasNext();
			}
			
			@Override
			public K next() {
				return this.key = this.e.next();
			}
			
			@Override
			public void remove() {
				MapWithDefaults.this.remove( this.key );
			}
		};
	}
	
	@Override
	public Set<K> keySet() {
		if (this.isEmpty()) {
			return Collections.emptySet();
		}
		return new AbstractSet<>() {
			@Override
			public Iterator<K> iterator() {
				return MapWithDefaults.this.keys();
			}
			
			@Override
			public int size() {
				return MapWithDefaults.this.size();
			}
		};
	}
	
	@Override
	public V put(final K key, final V value) {
		return this.values.put( key, value );
	}
	
	@Override
	public void putAll(final Map<? extends K, ? extends V> t) {
		this.values.putAll( t );
	}
	
	/**
	 * Returns Keys enumeration for only non-default values
	 */
	@Override
	public V remove(final Object key) {
		return this.values.remove( key );
	}
	
	@Override
	public int size() {
		final Set<K> result = new TreeSet<>( this.defaults.keySet() );
		result.addAll( this.values.keySet() );
		return result.size();
	}
	
	@Override
	public String toString() {
		return "[" + this.getClass().getName() + ", data=" + this.values + ", defaults=" + this.defaults + "]";
	}
	
	@Override
	public Collection<V> values() {
		if (this.isEmpty()) {
			return Collections.emptySet();
		}
		return new AbstractCollection<>() {
			@Override
			public Iterator<V> iterator() {
				return new Iterator<>() {
					private final Iterator<K>	e	= MapWithDefaults.this.keys();
					
					@Override
					public boolean hasNext() {
						return this.e.hasNext();
					}
					
					@Override
					public V next() {
						return MapWithDefaults.this.get( this.e.next() );
					}
					
					@Override
					public void remove() {
						this.e.remove();
					}
				};
			}
			
			@Override
			public int size() {
				return MapWithDefaults.this.size();
			}
		};
	}
}
