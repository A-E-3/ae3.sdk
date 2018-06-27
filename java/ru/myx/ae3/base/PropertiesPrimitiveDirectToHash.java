/**
 *
 */
package ru.myx.ae3.base;

import java.io.Serializable;
import java.util.Iterator;

import ru.myx.ae3.help.Format;

final class PropertiesPrimitiveDirectToHash implements BaseProperties<BasePrimitiveString>, Serializable {

	private static final long serialVersionUID = 2888363574028818360L;

	/** Normally 5 */
	// private static final short TRESHOLD = 100;
	// private static final short TRESHOLD = 1;
	private static final short TRESHOLD = 8;

	private BasePropertyData<BasePrimitiveString> first;

	private BasePropertyData<BasePrimitiveString> last;

	private short size;

	PropertiesPrimitiveDirectToHash(final BasePropertyData<BasePrimitiveString> property1, final BasePrimitiveString name2, final BasePropertyData<BasePrimitiveString> property2) {
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

	@Override
	public BaseProperties<BasePrimitiveString> add(final BaseObject instance, final BasePrimitiveString name, final BaseProperty property, final short attributes) {

		return this.add(name, Base.createPropertyPrimitive(
				instance, //
				name,
				property,
				attributes));
	}

	@Override
	public final BaseProperties<BasePrimitiveString> add(final BaseObject instance, final String name, final BaseProperty property, final short attributes) {

		return this.add(instance, Base.forString(name), property, attributes);
	}

	@Override
	public final BaseProperties<BasePrimitiveString> add(final BasePrimitiveString name, final BaseObject value, final short attributes) {

		return this.add(name, new BasePropertyHolderPrimitive(null, value, attributes));
	}

	@Override
	public final BaseProperties<BasePrimitiveString> add(final BasePrimitiveString name, final BasePropertyData<BasePrimitiveString> property) {

		assert property != null : "NULL property";
		assert property.name == null : "Property is already assigned!";
		assert property.next == null : "Property is already assigned!";
		property.name = name;
		final BasePropertyData<BasePrimitiveString> replaced = this.find(name);
		if (replaced == null) {
			if (++this.size >= PropertiesPrimitiveDirectToHash.TRESHOLD) {
				this.last.next = property;
				property.prev = this.last;
				this.last = property;
				return new PropertiesPrimitiveOwnIdentityMap(this.first, this.last);
				/** compare <code>
				return new PropertiesPrimitiveHashMap( this.first );
				 * </code> */
			}
			this.last.next = property;
			property.prev = this.last;
			this.last = property;
		} else {
			final BasePropertyData<BasePrimitiveString> next = replaced.next;
			if (next != null) {
				next.prev = property;
			}
			final BasePropertyData<BasePrimitiveString> prev = replaced.prev;
			if (prev != null) {
				prev.next = property;
			}
			property.next = next;
			property.prev = prev;
		}
		return this;
	}

	@Override
	public final BaseProperties<BasePrimitiveString> add(final String name, final BaseObject value, final short attributes) {

		return this.add(Base.forString(name), value, attributes);
	}

	@Override
	public final BaseProperties<BasePrimitiveString> add(final String name, final BasePropertyData<BasePrimitiveString> property) {

		return this.add(Base.forString(name), property);
	}

	@Override
	public final BaseProperties<BasePrimitiveString> delete(final BasePrimitiveString name) {

		return this.delete(this.find(name));
	}

	private BaseProperties<BasePrimitiveString> delete(final BasePropertyData<BasePrimitiveString> removed) {

		if (removed == null) {
			return this;
		}
		this.size--;
		if (removed == this.first) {
			if (removed == this.last) {
				assert false : "Shouldn't be here - should already switch to a single property itself!";
				return null;
			}
			this.first = removed.next;
			this.first.prev = null;
			if (this.first.next == null) {
				return this.first;
			}
			return this;
		}
		if (removed == this.last) {
			this.last = removed.prev;
			this.last.next = null;
			if (this.last.prev == null || this.last == this.first) {
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

	@Override
	public final BaseProperties<BasePrimitiveString> delete(final String name) {

		return this.delete(this.find(name));
	}

	@Override
	public final BasePropertyData<BasePrimitiveString> find(final BasePrimitiveString name) {

		for (BasePropertyData<BasePrimitiveString> first = this.first;;) {
			if (first.name == name) {
				return first;
			}
			first = first.next;
			if (first == null) {
				return null;
			}
		}
	}

	@Override
	public final BasePropertyData<BasePrimitiveString> find(final CharSequence name) {

		if (name instanceof BasePrimitiveString) {
			for (BasePropertyData<BasePrimitiveString> first = this.first;;) {
				if (first.name == name) {
					return first;
				}
				first = first.next;
				if (first == null) {
					return null;
				}
			}
		}
		{
			final String stringName = name instanceof String
				? (String) name
				: name.toString();
			for (BasePropertyData<BasePrimitiveString> first = this.first;;) {
				if (first.name.stringEquals(stringName)) {
					return first;
				}
				first = first.next;
				if (first == null) {
					return null;
				}
			}
		}
	}

	@Override
	public final BasePropertyData<BasePrimitiveString> find(final String name) {

		for (BasePropertyData<BasePrimitiveString> first = this.first;;) {
			if (first.name.stringEquals(name)) {
				return first;
			}
			first = first.next;
			if (first == null) {
				return null;
			}
		}
	}

	@Override
	public final boolean hasEnumerableProperties() {

		for (BasePropertyData<BasePrimitiveString> first = this.first;;) {
			/** now ^^^^^^ it is for sure we don't need to pass 'name' to 'isEnumerable' */
			if ((first.attributes & BaseProperty.ATTR_ENUMERABLE) != 0) {
				return true;
			}
			first = first.next;
			if (first == null) {
				return false;
			}
		}
	}

	@Override
	public final Iterator<BasePrimitiveString> iteratorAll() {

		return new IteratorPropertiesAll<>(this.first);
	}

	@Override
	public final Iterator<BasePrimitiveString> iteratorAllAsPrimitive() {

		return new IteratorPropertiesAll<>(this.first);
	}

	@Override
	public final Iterator<String> iteratorAllAsString() {

		return new IteratorPropertiesAllToString<>(this.first);
	}

	/** all properties in chain */
	@Override
	public final Iterator<BasePrimitiveString> iteratorEnumerable() {

		for (BasePropertyData<BasePrimitiveString> first = this.first;;) {
			/** now ^^^^^^ it is for sure we don't need to pass 'name' to 'isEnumerable' */
			if ((first.attributes & BaseProperty.ATTR_ENUMERABLE) != 0) {
				return new IteratorPropertiesEnumerablePrimitive(first);
			}
			first = first.next;
			if (first == null) {
				return BaseProperties.ITERATOR_EMPTY_PRIMITIVE_STRING;
			}
		}
	}

	@Override
	public final Iterator<BasePrimitiveString> iteratorEnumerableAsPrimitive() {

		return this.iteratorEnumerable();
	}

	/** all properties in chain */
	@Override
	public final Iterator<String> iteratorEnumerableAsString() {

		for (BasePropertyData<BasePrimitiveString> first = this.first;;) {
			if ((first.attributes & BaseProperty.ATTR_ENUMERABLE) != 0) {
				return new IteratorPropertiesEnumerablePrimitiveAsString<>(first);
			}
			first = first.next;
			if (first == null) {
				return BaseObject.ITERATOR_EMPTY;
			}
		}
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
		for (final Iterator<BasePrimitiveString> iterator = this.iteratorAllAsPrimitive(); iterator.hasNext();) {
			final BasePrimitiveString key = iterator.next();
			builder.append(builder.length() == 0
				? ": "
				: ", ");
			final BasePropertyData<BasePrimitiveString> data = this.find(key);
			builder.append(key);
			builder.append(" : ");
			builder.append(data != null
				? Format.Describe.toEcmaSource(data.propertyGet(null, key), "")
				: "?");
		}
		return builder.append("#;").toString();
	}
}
