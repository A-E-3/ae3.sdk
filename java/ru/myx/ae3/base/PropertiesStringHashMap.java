/**
 *
 */
package ru.myx.ae3.base;

import java.util.HashMap;
import java.util.Iterator;

import ru.myx.ae3.Engine;
import ru.myx.ae3.help.Format;

class PropertiesStringHashMap extends HashMap<String, BasePropertyData<String>> implements BaseProperties<String> {

	/** The default initial capacity - MUST be a power of two. */
	private static final int DEFAULT_INITIAL_CAPACITY = Engine.MODE_SPEED
		? 16
		: 8;

	/** The load factor used when none specified in constructor. */
	private static final float DEFAULT_LOAD_FACTOR = Engine.MODE_SIZE
		? 6f
		: Engine.MODE_SPEED
			? 2f
			: 4f;

	private static final long serialVersionUID = 2888363574028818360L;

	/** properties, only enumerable **/
	private volatile BasePropertyData<String> first;

	/** properties, only enumerable **/
	private volatile BasePropertyData<String> last;

	PropertiesStringHashMap() {

		super(PropertiesStringHashMap.DEFAULT_INITIAL_CAPACITY, PropertiesStringHashMap.DEFAULT_LOAD_FACTOR);
	}

	/** all properties passed, including non-enumerable **/
	PropertiesStringHashMap(final BasePropertyData<String> first) {

		super(PropertiesStringHashMap.DEFAULT_INITIAL_CAPACITY, PropertiesStringHashMap.DEFAULT_LOAD_FACTOR);

		for (BasePropertyData<String> current = first;;) {
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
			final BasePropertyData<String> next = current.next;
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

	/** @param property
	 * @return */
	private BaseProperties<String> internAppend(final BasePropertyData<String> property) {

		final BasePropertyData<String> last = this.last;
		if (last == null) {
			this.first = this.last = property;
			return this;
		}
		last.next = property;
		(this.last = property).prev = last;
		return this;
	}

	private final BaseProperties<String> internDelete(final BasePropertyData<String> removed) {

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
		
		final BasePropertyData<String> next = removed.next;
		final BasePropertyData<String> prev = removed.prev;

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
	private BaseProperties<String> internReplace(final BasePropertyData<String> property, final BasePropertyData<String> removed) {

		if (removed == this.first) {
			if (removed == this.last) {
				this.first = this.last = property;
				return this;
			}

			final BasePropertyData<String> next = property.next = removed.next;
			if (next != null) {
				next.prev = property;
			}
			this.first = property;
			return this;
		}
		
		final BasePropertyData<String> prev = property.prev = removed.prev;
		if (prev != null) {
			prev.next = property;
		}

		if (removed == this.last) {
			this.last = property;
			return this;
		}

		final BasePropertyData<String> next = property.next = removed.next;
		if (next != null) {
			next.prev = property;
		}
		
		return this;
	}

	@Override
	public BaseProperties<String> add(final BaseObject instance, final BasePrimitiveString name, final BaseProperty property, final short attributes) {

		return this.add(
				name,
				Base.createPropertyString(
						instance, //
						name.stringValue(),
						property,
						attributes));
	}

	@Override
	public final BaseProperties<String> add(final BaseObject instance, final String name, final BaseProperty property, final short attributes) {

		return this.add(
				name,
				Base.createPropertyString(
						instance, //
						name,
						property,
						attributes));
	}

	@Override
	public final BaseProperties<String> add(final BasePrimitiveString name, final BaseObject value, final short attributes) {

		return this.add(name, new BasePropertyHolderString(null, value, attributes));
	}

	@Override
	public BaseProperties<String> add(final BasePrimitiveString name, final BasePropertyData<String> property) {

		return this.add(name.stringValue(), property);
	}

	@Override
	public final BaseProperties<String> add(final String name, final BaseObject value, final short attributes) {

		return this.add(name, new BasePropertyHolderString(null, value, attributes));
	}
	
	@Override
	public BaseProperties<String> add(final String name, final BasePropertyData<String> property) {

		assert property != null : "NULL property";
		assert name != null : "NULL property name";
		assert property.next == null : "Property is already assigned!";

		if (property.name != null && !property.name.equals(name)) {
			throw new IllegalArgumentException("Property is already assigned with another name!");
		}

		property.name = name;
		final BasePropertyData<String> removed = this.put(name, property);

		/** removed was not enumerable **/
		if (removed == null || (removed.attributes & BaseProperty.ATTR_ENUMERABLE) == 0) {
			/** property is enumerable **/
			if ((property.attributes & BaseProperty.ATTR_ENUMERABLE) != 0) {
				return this.internAppend(property);
			}
			return this;
		}

		/** property is not enumerable, just removing removed **/
		if ((property.attributes & BaseProperty.ATTR_ENUMERABLE) == 0) {
			return this.internDelete(removed);
		}
		
		/** both (property and remove) are enumerable **/
		return this.internReplace(property, removed);
	}

	@Override
	public final void clear() {

		super.clear();
		this.first = this.last = null;
	}

	@Override
	public BaseProperties<String> delete(final BasePrimitiveString name) {

		final BasePropertyData<String> removed = this.remove(name.stringValue());

		if (removed == null || (removed.attributes & BaseProperty.ATTR_ENUMERABLE) == 0) {
			/** nothing changed **/
			return this;
		}

		return this.internDelete(removed);
	}

	@Override
	public BaseProperties<String> delete(final String name) {

		final BasePropertyData<String> removed = this.remove(name);

		if (removed == null || (removed.attributes & BaseProperty.ATTR_ENUMERABLE) == 0) {
			/** nothing changed **/
			return this;
		}

		return this.internDelete(removed);
	}

	@Override
	public BasePropertyData<String> find(final BasePrimitiveString name) {

		return this.get(name.stringValue());
	}

	@Override
	public BasePropertyData<String> find(final CharSequence name) {

		return this.get(name.toString());
	}

	@Override
	public BasePropertyData<String> find(final String name) {

		return this.get(name);
	}

	@Override
	public boolean hasEnumerableProperties() {

		return this.first != null;
	}

	/** COMMENTS:
	 *
	 * 1) put only enumerable properties to a linked chain, use this.keySet() for 'all'
	 *
	 * 2) use sequence - to put enumerable elements in the front. */
	@Override
	public Iterator<String> iteratorAll() {

		return this.first == null
			? this.keySet().iterator()
			: new IteratorSequenceString(this.iteratorEnumerableAsString(), this.keySet().iterator());
	}

	@Override
	public Iterator<BasePrimitiveString> iteratorAllAsPrimitive() {

		return this.first == null
			? new IteratorPrimitiveForString(this.keySet().iterator())
			: new IteratorSequencePrimitive(this.iteratorEnumerableAsPrimitive(), new IteratorPrimitiveForString(this.keySet().iterator()));
	}

	@Override
	public Iterator<String> iteratorAllAsString() {

		return this.iteratorAll();
	}

	@Override
	public Iterator<String> iteratorEnumerable() {

		/** this linked list contains only enumerable properties **/
		final BasePropertyData<String> first = this.first;
		return first == null
			? BaseObject.ITERATOR_EMPTY
			: new IteratorPropertiesAll<>(first);
	}

	@Override
	public Iterator<BasePrimitiveString> iteratorEnumerableAsPrimitive() {

		/** this linked list contains only enumerable properties **/
		final BasePropertyData<String> first = this.first;
		return first == null
			? BaseProperties.ITERATOR_EMPTY_PRIMITIVE_STRING
			: new IteratorPropertiesAllToPrimitive(first);
	}

	@Override
	public Iterator<String> iteratorEnumerableAsString() {

		return this.iteratorEnumerable();
	}

	/** important for debugging */
	@Override
	public String toString() {

		final StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName());
		for (final Iterator<String> iterator = this.iteratorAllAsString(); iterator.hasNext();) {
			final String key = iterator.next();
			builder.append(
					builder.length() == 0
						? ": "
						: ", ");
			final BasePropertyData<String> data = this.find(key);
			builder.append(
					data == null || data.name == null
						? key
						: data.name);
			builder.append(" : ");
			builder.append(
					data != null
						? Format.Describe.toEcmaSource(data.propertyGet(null, key), "")
						: "?");
		}
		return builder.append("#;").toString();
	}
}
