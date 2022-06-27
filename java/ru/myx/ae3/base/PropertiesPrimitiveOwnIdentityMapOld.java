/**
 *
 */
package ru.myx.ae3.base;

import java.io.Serializable;
import java.util.Iterator;

import ru.myx.ae3.help.Format;

final class PropertiesPrimitiveOwnIdentityMapOld //
		extends
			HashMapPrimitiveIdentity<BasePrimitiveString, BasePropertyData<BasePrimitiveString>> //
		implements
			BaseProperties<BasePrimitiveString>,
			Serializable //
{

	private static final long serialVersionUID = 2888363574028818360L;

	private BasePropertyData<BasePrimitiveString> first;

	private BasePropertyData<BasePrimitiveString> last;

	PropertiesPrimitiveOwnIdentityMapOld(final BasePropertyData<BasePrimitiveString> first) {

		super(16, 4.0f);
		this.first = first;
		this.last = first;
		for (BasePropertyData<BasePrimitiveString> current = first;;) {
			this.put(current.name, current);
			current = current.next;
			if (current == null) {
				break;
			}
		}
	}

	PropertiesPrimitiveOwnIdentityMapOld(final BasePropertyData<BasePrimitiveString> first, final BasePropertyData<BasePrimitiveString> last) {

		super(16, 4.0f);
		this.first = first;
		this.last = last;
		for (BasePropertyData<BasePrimitiveString> current = first;;) {
			this.put(current.name, current);
			current = current.next;
			if (current == null) {
				break;
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

		return this.add(instance, Base.forString(name), property, attributes);
	}

	@Override
	public final BaseProperties<BasePrimitiveString> add(final BasePrimitiveString name, final BaseObject value, final short attributes) {

		return this.add(name, new BasePropertyHolderPrimitive(null, value, attributes));
	}

	@Override
	public final BaseProperties<BasePrimitiveString> add(final BasePrimitiveString name, final BasePropertyData<BasePrimitiveString> property) {

		assert property != null : "NULL property";
		assert name != null : "NULL property name";
		assert property.name == null || property.name.equals(name) : "Property is already assigned!";
		assert property.next == null : "Property is already assigned!";
		property.name = name;
		final BasePropertyData<BasePrimitiveString> replaced = this.put(name, property);
		if (replaced == null) {
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

		return this.deleteImpl(this.remove(name));
	}

	@Override
	public final BaseProperties<BasePrimitiveString> delete(final String name) {

		final BasePropertyData<BasePrimitiveString> found = this.find(name);
		return found == null
			? this
			: this.deleteImpl(this.remove(found.name));
	}

	private final BaseProperties<BasePrimitiveString> deleteImpl(final BasePropertyData<BasePrimitiveString> removed) {

		if (removed == null) {
			return this;
		}
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

	@Override
	public final boolean hasEnumerableProperties() {

		for (BasePropertyData<BasePrimitiveString> first = this.first;;) {
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
}
