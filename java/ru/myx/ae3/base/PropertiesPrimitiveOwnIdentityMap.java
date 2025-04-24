/**
 *
 */
/*
 * @(#)HashMap.java 1.68 06/06/27
 *
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package ru.myx.ae3.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import ru.myx.ae3.Engine;
import ru.myx.ae3.help.Format;

/** Hash table based implementation of the <tt>Map</tt> interface. This implementation provides all
 * of the optional map operations, and permits <tt>null</tt> values and the <tt>null</tt> key. (The
 * <tt>HashMap</tt> class is roughly equivalent to <tt>Hashtable</tt>, except that it is
 * unsynchronized and permits nulls.) This class makes no guarantees as to the order of the map; in
 * particular, it does not guarantee that the order will remain constant over time.
 *
 * <p>
 * This implementation provides constant-time performance for the basic operations (<tt>get</tt> and
 * <tt>put</tt>), assuming the hash function disperses the elements properly among the buckets.
 * Iteration over collection views requires time proportional to the "capacity" of the
 * <tt>HashMap</tt> instance (the number of buckets) plus its size (the number of key-value
 * mappings). Thus, it's very important not to set the initial capacity too high (or the load factor
 * too low) if iteration performance is important.
 *
 * <p>
 * An instance of <tt>HashMap</tt> has two parameters that affect its performance: <i>initial
 * capacity</i> and <i>load factor</i>. The <i>capacity</i> is the number of buckets in the hash
 * table, and the initial capacity is simply the capacity at the time the hash table is created. The
 * <i>load factor</i> is a measure of how full the hash table is allowed to get before its capacity
 * is automatically increased. When the number of entries in the hash table exceeds the product of
 * the load factor and the current capacity, the capacity is roughly doubled by calling the
 * <tt>rehash</tt> method.
 *
 * <p>
 * As a general rule, the default load factor (.75) offers a good tradeoff between time and space
 * costs. Higher values decrease the space overhead but increase the lookup cost (reflected in most
 * of the operations of the <tt>HashMap</tt> class, including <tt>get</tt> and <tt>put</tt>). The
 * expected number of entries in the map and its load factor should be taken into account when
 * setting its initial capacity, so as to minimize the number of <tt>rehash</tt> operations. If the
 * initial capacity is greater than the maximum number of entries divided by the load factor, no
 * <tt>rehash</tt> operations will ever occur.
 *
 * <p>
 * If many mappings are to be stored in a <tt>HashMap</tt> instance, creating it with a sufficiently
 * large capacity will allow the mappings to be stored more efficiently than letting it perform
 * automatic rehashing as needed to grow the table.
 *
 * <p>
 * <b>Note that this implementation is not synchronized.</b> If multiple threads access this map
 * concurrently, and at least one of the threads modifies the map structurally, it <i>must</i> be
 * synchronized externally. (A structural modification is any operation that adds or deletes one or
 * more mappings; merely changing the value associated with a key that an instance already contains
 * is not a structural modification.) This is typically accomplished by synchronizing on some object
 * that naturally encapsulates the map. If no such object exists, the map should be "wrapped" using
 * the <tt>Collections.synchronizedMap</tt> method. This is best done at creation time, to prevent
 * accidental unsynchronized access to the map:
 *
 * <pre>
 *    Map&lt;?,?&gt; m = Collections.synchronizedMap(new HashMap(...));
 * </pre>
 *
 * <p>
 * The iterators returned by all of this class's "collection view methods" are <i>fail-fast</i>: if
 * the map is structurally modified at any time after the iterator is created, in any way except
 * through the iterator's own <tt>remove</tt> or <tt>add</tt> methods, the iterator will throw a
 * <tt>ConcurrentModificationException</tt>. Thus, in the face of concurrent modification, the
 * iterator fails quickly and cleanly, rather than risking arbitrary, non-deterministic behavior at
 * an undetermined time in the future.
 *
 * <p>
 * Note that the fail-fast behavior of an iterator cannot be guaranteed as it is, generally
 * speaking, impossible to make any hard guarantees in the presence of unsynchronized concurrent
 * modification. Fail-fast iterators throw <tt>ConcurrentModificationException</tt> on a best-effort
 * basis. Therefore, it would be wrong to write a program that depended on this exception for its
 * correctness: <i>the fail-fast behavior of iterators should be used only to detect bugs.</i>
 *
 * <p>
 * This class is a member of the <a href="{@docRoot} /../guide/collections/index.html"> Java
 * Collections Framework</a>.
 *
 * @author Doug Lea
 * @author Josh Bloch
 * @author Arthur van Hoff
 * @author Neal Gafter
 * @version 1.65, 03/03/05
 * @since 1.2 */

/** has only enumerable (not all) properties in the linked list
 *
 * for case-insencetive property map (like HTTP headers) */
final class PropertiesPrimitiveOwnIdentityMap //
		implements
			BaseProperties<BasePrimitiveString>,
			Serializable //
{
	
	/** @author myx
	 *
	 * @param <V> */
	static final class Entry<V> {
		
		final BasePrimitiveString key;
		
		Entry<V> next;
		
		V value;
		
		/** Create new entry. */
		Entry(final BasePrimitiveString k, final V v, final Entry<V> n) {
			
			this.value = v;
			this.next = n;
			this.key = k;
		}
		
		@Override
		public final boolean equals(final Object o) {
			
			if (o == this) {
				return true;
			}
			if (!(o instanceof Entry<?>)) {
				return false;
			}
			final Entry<?> e = (Entry<?>) o;
			final BasePrimitiveString k1 = this.key;
			final Object k2 = e.key;
			if (k1 == k2) {
				final V v1 = this.value;
				final Object v2 = e.value;
				if (v1 == v2 || v1 != null && v1.equals(v2)) {
					return true;
				}
			}
			return false;
		}
		
		@Override
		public final int hashCode() {
			
			return System.identityHashCode(this.key);
		}
		
		@Override
		public final String toString() {
			
			return this.key + "=" + this.value;
		}
		
		final BasePrimitiveString getKey() {
			
			return this.key;
		}
		
		final V getValue() {
			
			return this.value;
		}
		
		final V setValue(final V newValue) {
			
			final V oldValue = this.value;
			this.value = newValue;
			return oldValue;
		}
	}
	
	abstract class HashIterator<V> {
		
		Entry<?> current; // current entry
		int index; // current slot
		Entry<?> next; // next entry to return
		
		HashIterator() {
			
			final Entry<?>[] t = PropertiesPrimitiveOwnIdentityMap.this.table;
			this.current = this.next = null;
			this.index = 0;
			if (t != null && PropertiesPrimitiveOwnIdentityMap.this.size > 0) {
				// advance to first entry
				do {
					//
				} while (this.index < t.length && (this.next = t[this.index++]) == null);
			}
		}
		
		public final boolean hasNext() {
			
			return this.next != null;
		}
		
		public final void remove() {
			
			final Entry<?> p = this.current;
			if (p == null) {
				throw new IllegalStateException();
			}
			this.current = null;
			PropertiesPrimitiveOwnIdentityMap.this.removeEntryForKey(System.identityHashCode(p.key), p.key);
			// PropertiesPrimitiveOwnIdentityMap.this.delete(p.key);
		}
		
		final Entry<V> nextNode() {
			
			Entry<?>[] t;
			final Entry<?> e = this.next;
			if (e == null) {
				throw new NoSuchElementException();
			}
			if ((this.next = (this.current = e).next) == null && (t = PropertiesPrimitiveOwnIdentityMap.this.table) != null) {
				do {
					//
				} while (this.index < t.length && (this.next = t[this.index++]) == null);
			}
			
			@SuppressWarnings("unchecked")
			final Entry<V> result = (Entry<V>) e;
			return result;
		}
	}
	
	final class KeyIterator extends HashIterator<BasePrimitiveString> implements Iterator<BasePrimitiveString> {
		
		@Override
		public final BasePrimitiveString next() {
			
			return this.nextNode().key;
		}
	}
	
	/** The default initial capacity - MUST be a power of two. */
	private static final int DEFAULT_INITIAL_CAPACITY = Engine.MODE_SIZE
		? 8
		: 16;
	
	/** The load factor used when none specified in constructor. */
	private static final float DEFAULT_LOAD_FACTOR = Engine.MODE_SIZE
		? 6f
		: Engine.MODE_SPEED
			? 2f
			: 4f;
	
	/** The maximum capacity, used if a higher value is implicitly specified by either of the
	 * constructors with arguments. MUST be a power of two <= 1<<30. */
	private static final int MAXIMUM_CAPACITY = 1 << 30;
	
	private static final long serialVersionUID = 2888363574028818360L;
	
	/** all properties, including non-enumerable **/
	private BasePropertyData<BasePrimitiveString> first;
	
	/** all properties, including non-enumerable **/
	private BasePropertyData<BasePrimitiveString> last;
	
	/** The load factor for the hash table.
	 *
	 * @serial */
	private final float loadFactor;
	
	/** The number of key-value mappings contained in this identity hash map. */
	private transient int size;
	
	/** The table, resized as necessary. Length MUST Always be a power of two. */
	private transient Entry<BasePropertyData<BasePrimitiveString>>[] table;
	
	/** The next size value at which to resize (capacity * load factor).
	 *
	 * @serial */
	private int threshold;
	
	@SuppressWarnings("unchecked")
	PropertiesPrimitiveOwnIdentityMap() {
		
		this.loadFactor = PropertiesPrimitiveOwnIdentityMap.DEFAULT_LOAD_FACTOR;
		this.threshold = (int) (PropertiesPrimitiveOwnIdentityMap.DEFAULT_INITIAL_CAPACITY * PropertiesPrimitiveOwnIdentityMap.DEFAULT_LOAD_FACTOR);
		this.table = new Entry[PropertiesPrimitiveOwnIdentityMap.DEFAULT_INITIAL_CAPACITY];
	}
	
	@SuppressWarnings("unchecked")
	PropertiesPrimitiveOwnIdentityMap(final BasePropertyData<BasePrimitiveString> first) {
		
		this.loadFactor = PropertiesPrimitiveOwnIdentityMap.DEFAULT_LOAD_FACTOR;
		this.threshold = (int) (PropertiesPrimitiveOwnIdentityMap.DEFAULT_INITIAL_CAPACITY * PropertiesPrimitiveOwnIdentityMap.DEFAULT_LOAD_FACTOR);
		this.table = new Entry[PropertiesPrimitiveOwnIdentityMap.DEFAULT_INITIAL_CAPACITY];
		
		for (BasePropertyData<BasePrimitiveString> current = first;;) {
			
			assert current.name != null : "Null property name!";
			
			this.put(current.name, current);
			if ((current.attributes & BaseProperty.ATTR_ENUMERABLE) != 0) {
				if (this.last == null) {
					this.first = this.last = current;
				} else {
					current.prev = this.last;
					this.last.next = current;
					this.last = current;
				}
			}
			
			final BasePropertyData<BasePrimitiveString> next = current.next;
			if (next == null) {
				break;
			}
			current = next;

		}
		if (this.first != null) {
			this.first.prev = null;
		}
		if (this.last != null) {
			this.last.next = null;
		}
	}
	
	/** Special-case code for containsValue with null argument */
	private final boolean containsNullValue() {
		
		final Entry<BasePropertyData<BasePrimitiveString>>[] tab = this.table;
		for (final Entry<BasePropertyData<BasePrimitiveString>> element : tab) {
			for (Entry<BasePropertyData<BasePrimitiveString>> e = element; e != null; e = e.next) {
				if (e.value == null) {
					return true;
				}
			}
		}
		return false;
	}
	
	/** @param property
	 * @return */
	private BaseProperties<BasePrimitiveString> internAppend(final BasePropertyData<BasePrimitiveString> property) {
		
		final BasePropertyData<BasePrimitiveString> last = this.last;
		if (last == null) {
			this.first = this.last = property;
			return this;
		}
		last.next = property;
		(this.last = property).prev = last;
		return this;
	}
	
	private final BaseProperties<BasePrimitiveString> internDelete(final BasePropertyData<BasePrimitiveString> removed) {
		
		if (removed == this.first) {
			if (removed == this.last) {
				this.first = this.last = null;
				return this;
			}
			
			(this.first = removed.next).prev = null;
			return this;
		}
		
		if (removed == this.last) {
			(this.last = removed.prev).next = null;
			return this;
		}
		
		final BasePropertyData<BasePrimitiveString> next = removed.next;
		final BasePropertyData<BasePrimitiveString> prev = removed.prev;
		
		if (prev != null) {
			prev.next = next;
			removed.prev = null;
		}
		
		if (next != null) {
			next.prev = prev;
			/** don't clear removed.next here used for enumeration:
			 * <p>
			 * Properties of the object being enumerated may be deleted during enumeration. If a
			 * property that has not yet been visited during enumeration is deleted, then it will
			 * not be visited. If new properties are added to the object being enumerated during
			 * enumeration, the newly added properties are not guaranteed to be visited in the
			 * active enumeration. Deleted: The mechanics of enumerating the properties (step 5 in
			 * the first algorithm, step 6 in the second) is implementation dependent. The order of
			 * enumeration is defined by the object. */
		}
		
		return this;
	}
	
	/** @param property
	 * @param removed
	 * @return */
	private BaseProperties<BasePrimitiveString> internReplace(final BasePropertyData<BasePrimitiveString> property, final BasePropertyData<BasePrimitiveString> removed) {
		
		if (removed == this.first) {
			if (removed == this.last) {
				this.first = this.last = property;
				return this;
			}
			
			final BasePropertyData<BasePrimitiveString> next = property.next = removed.next;
			if (next != null) {
				next.prev = property;
			}
			this.first = property;
			return this;
		}
		
		final BasePropertyData<BasePrimitiveString> prev = property.prev = removed.prev;
		if (prev != null) {
			prev.next = property;
		}
		
		if (removed == this.last) {
			this.last = property;
			return this;
		}
		
		final BasePropertyData<BasePrimitiveString> next = property.next = removed.next;
		if (next != null) {
			next.prev = property;
		}
		
		return this;
	}
	
	/** Removes and returns the entry associated with the specified key in the HashMap. Returns null
	 * if the HashMap contains no mapping for this key.
	 *
	 * for iterators */
	private final Entry<BasePropertyData<BasePrimitiveString>> removeEntryForKey(final int hash, final BasePrimitiveString key) {
		
		final int i = hash & this.table.length - 1;
		Entry<BasePropertyData<BasePrimitiveString>> prev = this.table[i];
		Entry<BasePropertyData<BasePrimitiveString>> e = prev;
		
		while (e != null) {
			final Entry<BasePropertyData<BasePrimitiveString>> next = e.next;
			if (key == e.key) {
				this.size--;
				if (prev == e) {
					this.table[i] = next;
				} else {
					prev.next = next;
				}
				if ((e.value.attributes & BaseProperty.ATTR_ENUMERABLE) != 0) {
					this.internDelete(e.value);
				}
				return e;
			}
			prev = e;
			e = next;
		}
		
		return null;
	}
	
	/** Rehashes the contents of this map into a new array with a larger capacity. This method is
	 * called automatically when the number of keys in this map reaches its threshold.
	 *
	 * If current capacity is MAXIMUM_CAPACITY, this method does not resize the map, but sets
	 * threshold to Integer.MAX_VALUE. This has the effect of preventing future calls.
	 *
	 * @param newCapacity
	 *            the new capacity, MUST be a power of two; must be greater than current capacity
	 *            unless current capacity is MAXIMUM_CAPACITY (in which case value is
	 *            irrelevant). */
	private final void resize(final int newCapacity) {
		
		final Entry<BasePropertyData<BasePrimitiveString>>[] oldTable = this.table;
		final int oldCapacity = oldTable.length;
		if (oldCapacity == PropertiesPrimitiveOwnIdentityMap.MAXIMUM_CAPACITY) {
			this.threshold = Integer.MAX_VALUE;
			return;
		}
		
		@SuppressWarnings("unchecked")
		final Entry<BasePropertyData<BasePrimitiveString>>[] newTable = new Entry[newCapacity];
		this.transfer(newTable);
		this.table = newTable;
		this.threshold = (int) (newCapacity * this.loadFactor);
	}
	
	/** Transfer all entries from current table to newTable. */
	private void transfer(final Entry<BasePropertyData<BasePrimitiveString>>[] newTable) {
		
		final Entry<BasePropertyData<BasePrimitiveString>>[] src = this.table;
		final int newCapacity = newTable.length;
		for (int j = 0; j < src.length; j++) {
			Entry<BasePropertyData<BasePrimitiveString>> e = src[j];
			if (e != null) {
				src[j] = null;
				do {
					final Entry<BasePropertyData<BasePrimitiveString>> next = e.next;
					final int i = System.identityHashCode(e.key) & newCapacity - 1;
					e.next = newTable[i];
					newTable[i] = e;
					e = next;
				} while (e != null);
			}
		}
	}
	
	@Override
	public BaseProperties<BasePrimitiveString> add(final BaseObject instance, final BasePrimitiveString name, final BaseProperty property, final short attributes) {
		
		return this.add(
				name,
				Base.createPropertyPrimitive(
						instance, //
						name,
						property,
						attributes));
	}
	
	@Override
	public final BaseProperties<BasePrimitiveString> add(final BaseObject instance, final String name, final BaseProperty property, final short attributes) {
		
		return this.add(
				name,
				Base.createPropertyPrimitive(
						instance, //
						Base.forString(name),
						property,
						attributes));
	}
	
	@Override
	public final BaseProperties<BasePrimitiveString> add(final BasePrimitiveString name, final BaseObject value, final short attributes) {
		
		assert value != null : "NULL value";
		assert name != null : "NULL property name";
		
		final int i = System.identityHashCode(name) & this.table.length - 1;
		
		final boolean propertyEnumerable = (attributes & BaseProperty.ATTR_ENUMERABLE) != 0;
		for (Entry<BasePropertyData<BasePrimitiveString>> e = this.table[i]; e != null; e = e.next) {
			if (e.key == name) {
				final BasePropertyData<BasePrimitiveString> removed = e.value;
				final boolean removedEnumerable = (removed.attributes & BaseProperty.ATTR_ENUMERABLE) != 0;
				
				if (removedEnumerable == propertyEnumerable) {
					if (removed instanceof final BasePropertyHolderPrimitive holderPrimitive) {
						holderPrimitive.attributes = attributes;
						holderPrimitive.value = value;
						// e.value stays the same
						return this;
					}
				}
				
				final BasePropertyData<BasePrimitiveString> property = e.value = new BasePropertyHolderPrimitive(name, value, attributes);
				
				if (removedEnumerable) {
					if (propertyEnumerable) {
						return this.internReplace(property, removed);
					}
					return this.internDelete(removed);
				}
				
				if (propertyEnumerable) {
					return this.internAppend(property);
				}
				
				return this;
			}
		}
		{
			final BasePropertyData<BasePrimitiveString> property = new BasePropertyHolderPrimitive(name, value, attributes);
			
			final Entry<BasePropertyData<BasePrimitiveString>> e = this.table[i];
			this.table[i] = new Entry<>(name, property, e);
			if (this.size++ >= this.threshold) {
				this.resize(2 * this.table.length);
			}
			
			if (propertyEnumerable) {
				return this.internAppend(property);
			}
			
			return this;
		}
	}
	
	@Override
	public final BaseProperties<BasePrimitiveString> add(final BasePrimitiveString name, final BasePropertyData<BasePrimitiveString> property) {
		
		assert property != null : "NULL property";
		assert name != null : "NULL property name";
		assert property.next == null : "Property is already assigned!";
		
		property.name = name;
		
		final int i = System.identityHashCode(name) & this.table.length - 1;
		
		final boolean propertyEnumerable = (property.attributes & BaseProperty.ATTR_ENUMERABLE) != 0;
		for (Entry<BasePropertyData<BasePrimitiveString>> e = this.table[i]; e != null; e = e.next) {
			if (e.key == name) {
				final BasePropertyData<BasePrimitiveString> removed = e.value;
				final boolean removedEnumerable = (removed.attributes & BaseProperty.ATTR_ENUMERABLE) != 0;
				
				e.value = property;
				
				if (removedEnumerable) {
					if (propertyEnumerable) {
						return this.internReplace(property, removed);
					}
					return this.internDelete(removed);
				}
				
				if (propertyEnumerable) {
					return this.internAppend(property);
				}
				
				return this;
			}
		}
		{
			final Entry<BasePropertyData<BasePrimitiveString>> e = this.table[i];
			this.table[i] = new Entry<>(name, property, e);
			if (this.size++ >= this.threshold) {
				this.resize(2 * this.table.length);
			}
			
			if (propertyEnumerable) {
				return this.internAppend(property);
			}
			
			return this;
		}
	}
	
	@Override
	public final BaseProperties<BasePrimitiveString> add(final String name, final BaseObject value, final short attributes) {
		
		return this.add(Base.forString(name), value, attributes);
	}
	
	@Override
	public final BaseProperties<BasePrimitiveString> add(final String name, final BasePropertyData<BasePrimitiveString> property) {
		
		return this.add(Base.forString(name), property);
	}
	
	/** Removes all mappings from this map. */
	public final void clear() {
		
		final Entry<?>[] tab = this.table;
		for (int i = 0; i < tab.length; ++i) {
			tab[i] = null;
		}
		this.first = this.last = null;
		this.size = 0;
	}
	
	/** Returns <tt>true</tt> if this map contains a mapping for the specified key.
	 *
	 * @param key
	 *            The key whose presence in this map is to be tested
	 * @return <tt>true</tt> if this map contains a mapping for the specified key. */
	public final boolean containsKey(final BasePrimitiveString key) {
		
		final int hash = System.identityHashCode(key);
		final int i = hash & this.table.length - 1;
		
		for (Entry<BasePropertyData<BasePrimitiveString>> e = this.table[i]; e != null;) {
			if (key == e.key) {
				return true;
			}
			e = e.next;
		}
		return false;
	}
	
	/** Returns <tt>true</tt> if this map maps one or more keys to the specified value.
	 *
	 * @param value
	 *            value whose presence in this map is to be tested.
	 * @return <tt>true</tt> if this map maps one or more keys to the specified value. */
	public final boolean containsValue(final Object value) {
		
		if (value == null) {
			return this.containsNullValue();
		}
		final Entry<BasePropertyData<BasePrimitiveString>>[] tab = this.table;
		for (final Entry<BasePropertyData<BasePrimitiveString>> element : tab) {
			for (Entry<BasePropertyData<BasePrimitiveString>> e = element; e != null; e = e.next) {
				if (value.equals(e.value)) {
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public final BaseProperties<BasePrimitiveString> delete(final BasePrimitiveString name) {
		
		final BasePropertyData<BasePrimitiveString> removed = this.remove(name);
		if (removed == null) {
			return this;
		}
		
		if ((removed.attributes & BaseProperty.ATTR_ENUMERABLE) == 0) {
			/** nothing changed **/
			return this;
		}
		
		return this.internDelete(removed);
	}
	
	@Override
	public final BaseProperties<BasePrimitiveString> delete(final String name) {
		
		final BasePropertyData<BasePrimitiveString> found = this.find(name);
		if (found == null) {
			return this;
		}
		
		final BasePropertyData<BasePrimitiveString> removed = this.remove(found.name);
		if (removed == null) {
			return this;
		}
		
		if ((removed.attributes & BaseProperty.ATTR_ENUMERABLE) == 0) {
			/** nothing changed **/
			return this;
		}
		
		return this.internDelete(removed);
	}
	
	@Override
	public final BasePropertyData<BasePrimitiveString> find(final BasePrimitiveString name) {
		
		return this.get(name);
	}
	
	/** TODO: make it faster by removing Base.forString() */
	@Override
	public final BasePropertyData<BasePrimitiveString> find(final CharSequence name) {
		
		return this.get(Base.forString(name));
	}
	
	/** TODO: make it faster by removing Base.forString() */
	@Override
	public final BasePropertyData<BasePrimitiveString> find(final String name) {
		
		return this.get(Base.forString(name));
	}
	
	/** Returns the value to which the specified key is mapped in this identity hash map, or
	 * <tt>null</tt> if the map contains no mapping for this key. A return value of <tt>null</tt>
	 * does not <i>necessarily</i> indicate that the map contains no mapping for the key; it is also
	 * possible that the map explicitly maps the key to <tt>null</tt>. The <tt>containsKey</tt>
	 * method may be used to distinguish these two cases.
	 *
	 * @param key
	 *            the key whose associated value is to be returned.
	 * @return the value to which this map maps the specified key, or <tt>null</tt> if the map
	 *         contains no mapping for this key. */
	public final BasePropertyData<BasePrimitiveString> get(final BasePrimitiveString key) {
		
		final int i = System.identityHashCode(key) & this.table.length - 1;
		
		for (Entry<BasePropertyData<BasePrimitiveString>> e = this.table[i]; e != null; e = e.next) {
			if (e.key == key) {
				return e.value;
			}
		}
		return null;
	}
	
	@Override
	public final boolean hasEnumerableProperties() {
		
		return this.first != null;
	}
	
	/** Returns <tt>true</tt> if this map contains no key-value mappings.
	 *
	 * @return <tt>true</tt> if this map contains no key-value mappings. */
	public boolean isEmpty() {
		
		return this.size == 0;
	}
	
	/** COMMENTS:
	 *
	 * 1) put only enumerable properties to a linked chain, use this.keySet() for 'all'
	 *
	 * 2) use sequence - to put enumerable elements in the front. */
	@Override
	public final Iterator<BasePrimitiveString> iteratorAll() {
		
		return this.first == null
			? new KeyIterator()
			: new IteratorSequencePrimitive(this.iteratorEnumerableAsPrimitive(), new KeyIterator());
	}
	
	@Override
	public final Iterator<BasePrimitiveString> iteratorAllAsPrimitive() {
		
		return this.first == null
			? new KeyIterator()
			: new IteratorSequencePrimitive(this.iteratorEnumerableAsPrimitive(), new KeyIterator());
	}
	
	@Override
	public final Iterator<String> iteratorAllAsString() {
		
		return this.first == null
			? new IteratorStringForAnything(new KeyIterator())
			: new IteratorSequenceString(this.iteratorEnumerableAsString(), new IteratorStringForAnything(new KeyIterator()));
	}
	
	/** all properties in chain */
	@Override
	public final Iterator<BasePrimitiveString> iteratorEnumerable() {
		
		/** this linked list contains only enumerable properties **/
		final BasePropertyData<BasePrimitiveString> first = this.first;
		return first == null
			? BaseProperties.ITERATOR_EMPTY_PRIMITIVE_STRING
			: new IteratorPropertiesAll<>(first);
	}
	
	@Override
	public final Iterator<BasePrimitiveString> iteratorEnumerableAsPrimitive() {
		
		/** this linked list contains only enumerable properties **/
		final BasePropertyData<BasePrimitiveString> first = this.first;
		return first == null
			? BaseProperties.ITERATOR_EMPTY_PRIMITIVE_STRING
			: new IteratorPropertiesAll<>(first);
	}
	
	/** all properties in chain */
	@Override
	public final Iterator<String> iteratorEnumerableAsString() {
		
		/** this linked list contains only enumerable properties **/
		final BasePropertyData<BasePrimitiveString> first = this.first;
		return first == null
			? BaseObject.ITERATOR_EMPTY
			: new IteratorPropertiesAllToString<>(first);
	}
	
	/** Associates the specified value with the specified key in this map. If the map previously
	 * contained a mapping for this key, the old value is replaced.
	 *
	 * @param key
	 *            key with which the specified value is to be associated.
	 * @param value
	 *            value to be associated with the specified key.
	 * @return previous value associated with specified key, or <tt>null</tt> if there was no
	 *         mapping for key. A <tt>null</tt> return can also indicate that the HashMap previously
	 *         associated <tt>null</tt> with the specified key. */
	public final BasePropertyData<BasePrimitiveString> put(final BasePrimitiveString key, final BasePropertyData<BasePrimitiveString> value) {
		
		final int i = System.identityHashCode(key) & this.table.length - 1;
		for (Entry<BasePropertyData<BasePrimitiveString>> e = this.table[i]; e != null; e = e.next) {
			if (e.key == key) {
				final BasePropertyData<BasePrimitiveString> oldValue = e.value;
				e.value = value;
				return oldValue;
			}
		}
		
		final Entry<BasePropertyData<BasePrimitiveString>> e = this.table[i];
		this.table[i] = new Entry<>(key, value, e);
		if (this.size++ >= this.threshold) {
			this.resize(2 * this.table.length);
		}
		return null;
	}
	
	/** Removes the mapping for this key from this map if present.
	 *
	 * @param key
	 *            key whose mapping is to be removed from the map.
	 * @return previous value associated with specified key, or <tt>null</tt> if there was no
	 *         mapping for key. A <tt>null</tt> return can also indicate that the map previously
	 *         associated <tt>null</tt> with the specified key. */
	public final BasePropertyData<BasePrimitiveString> remove(final BasePrimitiveString key) {
		
		final int i = System.identityHashCode(key) & this.table.length - 1;
		Entry<BasePropertyData<BasePrimitiveString>> prev = this.table[i];
		Entry<BasePropertyData<BasePrimitiveString>> e = prev;
		
		while (e != null) {
			final Entry<BasePropertyData<BasePrimitiveString>> next = e.next;
			if (key == e.key) {
				this.size--;
				if (prev == e) {
					this.table[i] = next;
					return e.value;
				}
				prev.next = next;
				return e.value;
			}
			prev = e;
			e = next;
		}
		
		return null;
	}
	
	/** Returns the number of key-value mappings in this map.
	 *
	 * @return the number of key-value mappings in this map. */
	@Override
	public int size() {
		
		return this.size;
	}
	
	/** important for debugging */
	@Override
	public String toString() {
		
		final StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName());
		for (final Iterator<BasePrimitiveString> iterator = this.iteratorAllAsPrimitive(); iterator.hasNext();) {
			final BasePrimitiveString key = iterator.next();
			builder.append(
					builder.length() == 0
						? ": "
						: ", ");
			final BasePropertyData<BasePrimitiveString> data = this.find(key);
			builder.append(key);
			builder.append(" : ");
			builder.append(
					data != null
						? Format.Describe.toEcmaSource(data.propertyGet(null, key), "")
						: "?");
		}
		return builder.append("#;").toString();
	}
	
	/** @return */
	public Collection<BasePropertyData<BasePrimitiveString>> values() {
		
		final List<BasePropertyData<BasePrimitiveString>> result = new ArrayList<>(this.size);
		final Entry<BasePropertyData<BasePrimitiveString>>[] tab = this.table;
		for (final Entry<BasePropertyData<BasePrimitiveString>> element : tab) {
			for (Entry<BasePropertyData<BasePrimitiveString>> e = element; e != null; e = e.next) {
				result.add(e.value);
			}
		}
		return result;
	}
}
