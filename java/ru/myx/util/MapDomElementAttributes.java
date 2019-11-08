/*
 * Created on 19.05.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.util;

import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

/** @author myx */
public final class MapDomElementAttributes implements Map<String, Object> {

	final class EntrySet extends AbstractSet<Map.Entry<String, Object>> {

		final class EntrySetIterator implements Iterator<Map.Entry<String, Object>> {

			private int counter = EntrySet.this.size() - 1;

			@Override
			public boolean hasNext() {

				return this.counter >= 0;
			}

			@Override
			public Map.Entry<String, Object> next() {

				return new EntrySimple<>(//
						MapDomElementAttributes.this.map.item(this.counter).getNodeName(),
						MapDomElementAttributes.this.map.item(this.counter--).getNodeValue()//
				);
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

			return MapDomElementAttributes.this.map.getLength();
		}
	}

	final class KeySet extends AbstractSet<String> {

		final class KeySetIterator implements Iterator<String> {

			private int counter = KeySet.this.size() - 1;

			@Override
			public boolean hasNext() {

				return this.counter >= 0;
			}

			@Override
			public String next() {

				return MapDomElementAttributes.this.map.item(this.counter--).getNodeName();
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

			return MapDomElementAttributes.this.map.getLength();
		}
	}

	final class ValueCollection extends AbstractCollection<Object> {

		final class ValueCollectionIterator implements Iterator<Object> {

			private int counter = ValueCollection.this.size() - 1;

			@Override
			public boolean hasNext() {

				return this.counter >= 0;
			}

			@Override
			public Object next() {

				return MapDomElementAttributes.this.map.item(this.counter--).getNodeValue();
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

			return MapDomElementAttributes.this.map.getLength();
		}
	}

	private final Element element;

	final NamedNodeMap map;

	private Set<String> keySet;

	private ValueCollection valueCollection;

	private Set<Map.Entry<String, Object>> entrySet;

	/** @param element */
	public MapDomElementAttributes(final Element element) {

		this.element = element;
		this.map = element.getAttributes();
	}

	@Override
	public void clear() {

		for (int i = this.map.getLength() - 1; i >= 0; --i) {
			this.map.removeNamedItem(this.map.item(i).getNodeName());
		}
	}

	@Override
	public boolean containsKey(final Object key) {

		return key == null
			? false
			: this.element.hasAttribute(key.toString());
	}

	@Override
	public boolean containsValue(final Object value) {

		for (int i = this.map.getLength() - 1; i >= 0; --i) {
			if (this.map.item(i).getNodeValue().equals(value)) {
				return true;
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
	public Object get(final Object key) {

		return key == null
			? null
			: this.element.hasAttribute(key.toString())
				? this.element.getAttribute(key.toString())
				: null;
	}

	@Override
	public boolean isEmpty() {

		return !this.element.hasAttributes();
	}

	@Override
	public final Set<String> keySet() {

		return this.keySet == null
			? this.keySet = new KeySet()
			: this.keySet;
	}

	@Override
	public Object put(final String key, final Object value) {

		if (key == null) {
			return null;
		}
		try {
			return this.get(key);
		} finally {
			this.element.setAttribute(key, value.toString());
		}
	}

	@Override
	public void putAll(final Map<? extends String, ? extends Object> map) {

		if (!map.isEmpty()) {
			for (final Map.Entry<? extends String, ? extends Object> current : map.entrySet()) {
				this.put(current.getKey(), current.getValue());
			}
		}
	}

	@Override
	public Object remove(final Object key) {

		if (key == null) {
			return null;
		}
		try {
			return this.get(key);
		} finally {
			this.element.removeAttribute(key.toString());
		}
	}

	@Override
	public int size() {

		return this.map.getLength();
	}

	@Override
	public final Collection<Object> values() {

		return this.valueCollection == null
			? this.valueCollection = new ValueCollection()
			: this.valueCollection;
	}
}
