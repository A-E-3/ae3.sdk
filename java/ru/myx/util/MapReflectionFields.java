/*
 * Created on 11.05.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** @author myx */
public final class MapReflectionFields implements Map<String, Object> {

	final class EntrySet extends AbstractSet<Map.Entry<String, Object>> {

		final class EntrySetIterator implements Iterator<Map.Entry<String, Object>> {

			private int counter = MapReflectionFields.this.fs.length - 1;

			@Override
			public boolean hasNext() {

				return this.counter >= 0;
			}

			@Override
			public Map.Entry<String, Object> next() {

				try {
					return new EntrySimple<>(//
							MapReflectionFields.this.fs[this.counter].getName(),
							MapReflectionFields.this.fs[this.counter--].get(MapReflectionFields.this.o)//
					);
				} catch (final IllegalAccessException e) {
					throw new RuntimeException(e);
				}
			}

			@Override
			public void remove() {

				// empty
			}
		}

		@Override
		public Iterator<Map.Entry<String, Object>> iterator() {

			return new EntrySetIterator();
		}

		@Override
		public int size() {

			return MapReflectionFields.this.fs.length;
		}
	}

	final class KeySet extends AbstractSet<String> {

		final class KeySetIterator implements Iterator<String> {

			private int counter = MapReflectionFields.this.fs.length - 1;

			@Override
			public boolean hasNext() {

				return this.counter >= 0;
			}

			@Override
			public String next() {

				return MapReflectionFields.this.fs[this.counter--].getName();
			}

			@Override
			public void remove() {

				// empty
			}
		}

		@Override
		public Iterator<String> iterator() {

			return new KeySetIterator();
		}

		@Override
		public int size() {

			return MapReflectionFields.this.fs.length;
		}
	}

	final class ValueCollection extends AbstractCollection<Object> {

		final class ValueCollectionIterator implements Iterator<Object> {

			private int counter = MapReflectionFields.this.fs.length - 1;

			@Override
			public boolean hasNext() {

				return this.counter >= 0;
			}

			@Override
			public Object next() {

				try {
					return MapReflectionFields.this.fs[this.counter--].get(MapReflectionFields.this.o);
				} catch (final IllegalAccessException e) {
					throw new RuntimeException(e);
				}
			}

			@Override
			public void remove() {

				// empty
			}
		}

		@Override
		public Iterator<Object> iterator() {

			return new ValueCollectionIterator();
		}

		@Override
		public int size() {

			return MapReflectionFields.this.fs.length;
		}
	}

	final Object o;

	private final Class<?> c;

	final Field[] fs;

	private KeySet keySet;

	private ValueCollection valueCollection;

	private EntrySet entrySet;

	/** @param o */
	public MapReflectionFields(final Object o) {

		this.o = o;
		this.c = o.getClass();
		final Field[] all = this.c.getFields();
		final List<Field> temp = new ArrayList<>();
		for (final Field current : all) {
			if ((current.getModifiers() & Modifier.PUBLIC) != 0) {
				temp.add(current);
			}
		}
		this.fs = temp.toArray(new Field[temp.size()]);
	}

	@Override
	public final void clear() {

		// empty
	}

	@Override
	public final boolean containsKey(final Object key) {

		for (int i = this.fs.length - 1; i >= 0; --i) {
			if (this.fs[i].getName().equals(key)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public final boolean containsValue(final Object value) {

		for (int i = this.fs.length - 1; i >= 0; --i) {
			try {
				final Object current = this.fs[i].get(this.o);
				if (current == null) {
					if (value == null) {
						return true;
					}
					continue;
				}
				if (current.equals(value)) {
					return true;
				}
			} catch (final IllegalAccessException e) {
				continue;
			}
		}
		return false;
	}

	@Override
	public final Set<Map.Entry<String, Object>> entrySet() {

		return this.entrySet == null
			? this.entrySet = new EntrySet()
			: this.entrySet;
	}

	@Override
	public final Object get(final Object key) {

		try {
			for (int i = this.fs.length - 1; i >= 0; --i) {
				if (this.fs[i].getName().equals(key)) {
					return this.fs[i].get(this.o);
				}
			}
		} catch (final IllegalAccessException e) {
			// ignore
		}
		return null;
	}

	@Override
	public final boolean isEmpty() {

		return this.fs.length == 0;
	}

	@Override
	public final Set<String> keySet() {

		return this.keySet == null
			? this.keySet = new KeySet()
			: this.keySet;
	}

	@Override
	public final Object put(final String key, final Object value) {

		try {
			for (int i = this.fs.length - 1; i >= 0; --i) {
				if (this.fs[i].getName().equals(key)) {
					try {
						return this.fs[i].get(this.o);
					} finally {
						this.fs[i].set(this.o, value);
					}
				}
			}
		} catch (final IllegalAccessException e) {
			// ignore
		}
		return null;
	}

	@Override
	public final void putAll(final Map<? extends String, ? extends Object> map) {

		if (!map.isEmpty()) {
			for (final Map.Entry<? extends String, ? extends Object> current : map.entrySet()) {
				this.put(current.getKey(), current.getValue());
			}
		}
	}

	@Override
	public final Object remove(final Object key) {

		return this.get(key);
	}

	@Override
	public final int size() {

		return this.fs.length;
	}

	@Override
	public final Collection<Object> values() {

		return this.valueCollection == null
			? this.valueCollection = new ValueCollection()
			: this.valueCollection;
	}
}
