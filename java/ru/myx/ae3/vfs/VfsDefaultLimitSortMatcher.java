package ru.myx.ae3.vfs;

import java.util.Iterator;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Use it when an unsorted input needs to be filtered and limited.
 * 
 * 
 * @author myx
 * 
 * @param <K>
 * @param <V>
 */
public class VfsDefaultLimitSortMatcher<K extends Comparable<? super K>, V> {
	private final K						keyStart;
	
	private final K						keyStop;
	
	private int							left;
	
	private final boolean				backwards;
	
	private final NavigableMap<K, V>	map;
	
	/**
	 * 
	 * @param keyStart
	 * @param keyStop
	 * @param limit
	 * @param backwards
	 */
	public VfsDefaultLimitSortMatcher(final K keyStart, final K keyStop, final int limit, final boolean backwards) {
		assert limit > 0 : "Use another class for unlimited matcher";
		this.keyStart = keyStart;
		this.keyStop = keyStop;
		this.left = limit;
		this.backwards = backwards;
		this.map = new TreeMap<>();
	}
	
	/**
	 * 
	 * @return
	 */
	public Iterator<V> iteratorAscending() {
		return this.map.values().iterator();
	}
	
	/**
	 * 
	 * @return
	 */
	public Iterator<V> iteratorDescending() {
		return this.map.descendingMap().values().iterator();
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void put(final K key, final V value) {
		// for the same key: earlier one stays
		if (this.map.get( key ) != null) {
			return;
		}
		if (this.keyStart != null && (this.backwards
				? this.keyStart.compareTo( key ) < 0
				: this.keyStart.compareTo( key ) > 0)) {
			return;
		}
		if (this.keyStop != null && (this.backwards
				? this.keyStop.compareTo( key ) >= 0
				: this.keyStop.compareTo( key ) <= 0)) {
			return;
		}
		if (this.left > 0) {
			--this.left;
			this.map.put( key, value );
			return;
		}
		if (this.backwards) {
			final K smallest = this.map.firstKey();
			// for the same key: earlier one stays
			if (smallest.compareTo( key ) < 0) {
				this.map.pollFirstEntry();
				this.map.put( key, value );
			}
			return;
		}
		{
			final K biggest = this.map.lastKey();
			// for the same key: earlier one stays
			if (biggest.compareTo( key ) > 0) {
				this.map.pollLastEntry();
				this.map.put( key, value );
			}
			return;
		}
	}
}
