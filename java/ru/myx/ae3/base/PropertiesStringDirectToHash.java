/**
 *
 */
package ru.myx.ae3.base;

import java.io.Serializable;
import java.util.Iterator;

import ru.myx.ae3.help.Format;

/** Used in OPTIMISE 'SIZE' */
final class PropertiesStringDirectToHash implements BaseProperties<String>, Serializable {

	private static final long serialVersionUID = 2888363574028818360L;

	/** Does equals() on elements - slower than strict equals comparisons.
	 *
	 * Normally 5 */
	// private static final short TRESHOLD = 100;
	// private static final short TRESHOLD = 1;
	private static final short TRESHOLD = 7;

	private BasePropertyData<String> first;

	private BasePropertyData<String> last;

	private short size;

	PropertiesStringDirectToHash(final BasePropertyData<String> property1, final String name2, final BasePropertyData<String> property2) {

		assert property1 != null : "NULL property";
		assert property1.name != null : "Property should be already assigned!";
		assert property2 != null : "NULL property";
		assert property2.name == null : "Property is already assigned!";
		property2.name = name2;
		this.first = property1;
		property1.prev = null;
		property1.next = property2;
		this.last = property2;
		property2.prev = property1;
		property2.next = null;
		this.size = 2;
	}

	private BaseProperties<String> internAppend(final BasePropertyData<String> property) {

		final BasePropertyData<String> last = this.last;
		(this.last = last.next = property).prev = last;
		
		if (++this.size >= PropertiesStringDirectToHash.TRESHOLD) {
			
			return new PropertiesStringHashMap(this.first);
		}

		return this;
	}

	private BaseProperties<String> internDelete(final BasePropertyData<String> removed) {

		this.size--;
		if (removed == this.first) {
			if (removed == this.last) {
				this.size = 0;
				assert false : "Shouldn't be here - should already switch to a single property itself!";
				return null;
			}
			(this.first = removed.next).prev = null;
			if (/* this.first.next == null || */ this.first == this.last) {
				return this.first;
			}
			return this;
		}
		if (removed == this.last) {
			(this.last = removed.prev).next = null;
			if ( /* this.last.prev == null || */ this.last == this.first) {
				return this.last;
			}
			return this;
		}
		if (removed.next != null) {
			removed.next.prev = removed.prev;
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
		if (removed.prev != null) {
			removed.prev.next = removed.next;
			removed.prev = null;
		}
		return this;
	}

	/** @param property
	 * @param replaced
	 * @return */
	private BaseProperties<String> internReplace(final BasePropertyData<String> property, final BasePropertyData<String> replaced) {

		final BasePropertyData<String> next = property.next = replaced.next;
		if (next != null) {
			next.prev = property;
		}
		final BasePropertyData<String> prev = property.prev = replaced.prev;
		if (prev != null) {
			prev.next = property;
		}
		return this;
	}

	@Override
	public BaseProperties<String> add(final BaseObject instance, final BasePrimitiveString name, final BaseProperty property, final short attributes) {

		return this.add(instance, name.stringValue(), property, attributes);
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

		return this.add(name.stringValue(), value, attributes);
	}

	@Override
	public BaseProperties<String> add(final BasePrimitiveString name, final BasePropertyData<String> property) {

		return this.add(name.stringValue(), property);
	}

	@Override
	public final BaseProperties<String> add(final String name, final BaseObject value, final short attributes) {

		assert value != null : "NULL value";

		final BasePropertyData<String> replaced = this.find(name);

		if (replaced == null) {
			return this.internAppend(new BasePropertyHolderString(name, value, attributes));
		}

		if (replaced.attributes == attributes && replaced instanceof BasePropertyHolderString) {
			((BasePropertyHolderString) replaced).value = value;
		}

		return this.internReplace(new BasePropertyHolderString(replaced.name, value, attributes), replaced);
	}

	@Override
	public BaseProperties<String> add(final String name, final BasePropertyData<String> property) {

		assert property != null : "NULL property";
		assert property.name == null : "Property is already assigned!";
		assert property.next == null : "Property is already assigned!";
		
		property.name = name;

		final BasePropertyData<String> replaced = this.find(name);

		if (replaced == null) {
			return this.internAppend(property);
		}

		return this.internReplace(property, replaced);
	}

	@Override
	public BaseProperties<String> delete(final BasePrimitiveString name) {

		final BasePropertyData<String> removed = this.find(name.stringValue());
		if (removed == null) {
			return this;
		}

		return this.internDelete(removed);
	}

	@Override
	public BaseProperties<String> delete(final String name) {

		final BasePropertyData<String> removed = this.find(name);
		if (removed == null) {
			return this;
		}

		return this.internDelete(removed);
	}

	@Override
	public BasePropertyData<String> find(final BasePrimitiveString name) {

		return this.find(name.stringValue());
	}

	@Override
	public BasePropertyData<String> find(final CharSequence name) {

		return this.find(name.toString());
	}

	@Override
	public BasePropertyData<String> find(final String name) {

		for (BasePropertyData<String> first = this.first;;) {
			final String check = first.name;
			if (check == name || check.equals(name)) {
				return first;
			}
			if (null == (first = first.next)) {
				return null;
			}
		}
	}

	@Override
	public boolean hasEnumerableProperties() {

		for (BasePropertyData<String> first = this.first;;) {
			if ((first.attributes & BaseProperty.ATTR_ENUMERABLE) != 0) {
				return true;
			}
			if (null == (first = first.next)) {
				return false;
			}
		}
	}

	@Override
	public Iterator<String> iteratorAll() {

		return new IteratorPropertiesAll<>(this.first);
	}

	@Override
	public final Iterator<BasePrimitiveString> iteratorAllAsPrimitive() {

		return new IteratorPropertiesAllToPrimitive(this.first);
	}

	@Override
	public final Iterator<String> iteratorAllAsString() {

		return new IteratorPropertiesAll<>(this.first);
	}

	/** all properties in chain */
	@Override
	public final Iterator<String> iteratorEnumerable() {

		for (BasePropertyData<String> first = this.first;;) {
			if ((first.attributes & BaseProperty.ATTR_ENUMERABLE) != 0) {
				return new IteratorPropertiesEnumerableString(first);
			}
			if (null == (first = first.next)) {
				return BaseObject.ITERATOR_EMPTY;
			}
		}
	}

	@Override
	public final Iterator<BasePrimitiveString> iteratorEnumerableAsPrimitive() {

		for (BasePropertyData<String> first = this.first;;) {
			if ((first.attributes & BaseProperty.ATTR_ENUMERABLE) != 0) {
				return new IteratorPropertiesEnumerableStringAsPrimitive(first);
			}
			if (null == (first = first.next)) {
				return BaseProperties.ITERATOR_EMPTY_PRIMITIVE_STRING;
			}
		}
	}

	@Override
	public Iterator<String> iteratorEnumerableAsString() {

		return this.iteratorEnumerable();
	}

	@Override
	public final int size() {

		return this.size;
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
			builder.append(key);
			builder.append(" : ");
			builder.append(
					data != null
						? Format.Describe.toEcmaSource(data.propertyGet(null, key), "")
						: "?");
		}
		return builder.append("#;").toString();
	}
}
