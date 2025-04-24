/**
 *
 */
package ru.myx.ae3.base;

import java.io.Serializable;
import java.util.Iterator;

import ru.myx.ae3.help.Format;

/** Used in OPTIMISE 'SPEED' */
final class PropertiesPrimitiveDirectToHash implements BaseProperties<BasePrimitiveString>, Serializable {
	
	private static final long serialVersionUID = 2888363574028818360L;
	
	/** Does strict equality comparisons - faster than equals().
	 *
	 * Normally 8 */
	// private static final short TRESHOLD = 100;
	// private static final short TRESHOLD = 1;
	private static final short TRESHOLD = 9;
	
	/** all properties, including non-enumerable **/
	private BasePropertyData<BasePrimitiveString> first;
	
	/** all properties, including non-enumerable **/
	private BasePropertyData<BasePrimitiveString> last;
	
	private short size;
	
	PropertiesPrimitiveDirectToHash(final BasePropertyData<BasePrimitiveString> property1, final BasePrimitiveString name2, final BasePropertyData<BasePrimitiveString> property2) {
		
		assert property1 != null : "NULL property";
		assert property1.name != null : "Property should be already assigned!";
		assert property2 != null : "NULL property";
		assert property2.name == null : "Property is already assigned!";
		
		assert name2 != null : "NULL property name";
		property2.name = name2;

		this.first = property1;
		property1.prev = null;
		property1.next = property2;
		this.last = property2;
		property2.prev = property1;
		property2.next = null;
		this.size = 2;
	}
	
	private BaseProperties<BasePrimitiveString> internAppend(final BasePropertyData<BasePrimitiveString> property) {
		
		final BasePropertyData<BasePrimitiveString> last = this.last;
		(this.last = last.next = property).prev = last;
		
		if (++this.size >= PropertiesPrimitiveDirectToHash.TRESHOLD) {
			
			return new PropertiesPrimitiveOwnIdentityMap(this.first);
			// return new PropertiesPrimitiveOwnIdentityMap2(this.first, property);
			
		}
		
		return this;
	}
	
	private BaseProperties<BasePrimitiveString> internDelete(final BasePropertyData<BasePrimitiveString> removed) {
		
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
	private BaseProperties<BasePrimitiveString> internReplace(final BasePropertyData<BasePrimitiveString> property, final BasePropertyData<BasePrimitiveString> replaced) {
		
		final BasePropertyData<BasePrimitiveString> next = property.next = replaced.next;
		if (next != null) {
			next.prev = property;
		}
		final BasePropertyData<BasePrimitiveString> prev = property.prev = replaced.prev;
		if (prev != null) {
			prev.next = property;
		}
		return this;
	}
	
	@Override
	public BaseProperties<BasePrimitiveString> add(final BaseObject instance, final BasePrimitiveString name, final BaseProperty propertyUnknown, final short attributes) {
		
		assert name != null : "NULL property name";
		assert propertyUnknown != null : "NULL property";
		
		final BasePropertyData<BasePrimitiveString> property = Base.createPropertyPrimitive(
				instance, //
				name,
				propertyUnknown,
				attributes);
		
		final BasePropertyData<BasePrimitiveString> replaced = this.find(name);
		
		if (replaced == null) {
			property.name = name;
			return this.internAppend(property);
		}
		
		if (replaced.attributes == attributes && replaced instanceof BasePropertyHolderPrimitive && property instanceof BasePropertyHolderPrimitive) {
			((BasePropertyHolderPrimitive) replaced).value = ((BasePropertyHolderPrimitive) property).value;
			return this;
		}
		
		property.name = name;
		return this.internReplace(property, replaced);
	}
	
	@Override
	public final BaseProperties<BasePrimitiveString> add(final BaseObject instance, final String name, final BaseProperty propertyUnknown, final short attributes) {
		
		return this.add(instance, Base.forString(name), propertyUnknown, attributes);
	}
	
	@Override
	public final BaseProperties<BasePrimitiveString> add(final BasePrimitiveString name, final BaseObject value, final short attributes) {
		
		assert value != null : "NULL value";
		assert name != null : "NULL property name";
		
		final BasePropertyData<BasePrimitiveString> replaced = this.find(name);
		
		if (replaced == null) {
			return this.internAppend(new BasePropertyHolderPrimitive(name, value, attributes));
		}
		
		if (replaced.attributes == attributes && replaced instanceof BasePropertyHolderPrimitive) {
			((BasePropertyHolderPrimitive) replaced).value = value;
			return this;
		}
		
		return this.internReplace(new BasePropertyHolderPrimitive(name, value, attributes), replaced);
	}
	
	@Override
	public final BaseProperties<BasePrimitiveString> add(final BasePrimitiveString name, final BasePropertyData<BasePrimitiveString> property) {
		
		assert name != null : "NULL property name";
		assert property != null : "NULL property";
		assert property.name == null : "Property is already assigned!";
		assert property.next == null : "Property is already assigned!";
		
		property.name = name;
		
		final BasePropertyData<BasePrimitiveString> replaced = this.find(name);
		
		if (replaced == null) {
			return this.internAppend(property);
		}
		
		return this.internReplace(property, replaced);
	}
	
	@Override
	public final BaseProperties<BasePrimitiveString> add(final String name, final BaseObject value, final short attributes) {
		
		assert value != null : "NULL value";
		
		final BasePropertyData<BasePrimitiveString> replaced = this.find(name);
		
		if (replaced == null) {
			return this.internAppend(new BasePropertyHolderPrimitive(Base.forString(name), value, attributes));
		}
		
		if (replaced.attributes == attributes && replaced instanceof BasePropertyHolderPrimitive) {
			((BasePropertyHolderPrimitive) replaced).value = value;
		}
		
		return this.internReplace(new BasePropertyHolderPrimitive(replaced.name, value, attributes), replaced);
	}
	
	@Override
	public final BaseProperties<BasePrimitiveString> add(final String name, final BasePropertyData<BasePrimitiveString> property) {
		
		assert property != null : "NULL property";
		assert property.name == null : "Property is already assigned!";
		assert property.next == null : "Property is already assigned!";
		
		final BasePropertyData<BasePrimitiveString> replaced = this.find(name);
		
		if (replaced == null) {
			property.name = Base.forString(name);
			return this.internAppend(property);
		}
		
		property.name = replaced.name;
		return this.internReplace(property, replaced);
	}
	
	@Override
	public final BaseProperties<BasePrimitiveString> delete(final BasePrimitiveString name) {
		
		final BasePropertyData<BasePrimitiveString> removed = this.find(name);
		if (removed == null) {
			return this;
		}
		
		return this.internDelete(removed);
	}
	
	@Override
	public final BaseProperties<BasePrimitiveString> delete(final String name) {
		
		final BasePropertyData<BasePrimitiveString> removed = this.find(name);
		if (removed == null) {
			return this;
		}
		
		return this.internDelete(removed);
	}
	
	@Override
	public final BasePropertyData<BasePrimitiveString> find(final BasePrimitiveString name) {
		
		for (BasePropertyData<BasePrimitiveString> first = this.first;;) {
			if (first.name == name) {
				return first;
			}
			if (null == (first = first.next)) {
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
				if (null == (first = first.next)) {
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
				if (null == (first = first.next)) {
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
			if (null == (first = first.next)) {
				return null;
			}
		}
	}
	
	@Override
	public final boolean hasEnumerableProperties() {
		
		for (BasePropertyData<BasePrimitiveString> first = this.first;;) {
			if ((first.attributes & BaseProperty.ATTR_ENUMERABLE) != 0) {
				return true;
			}
			if (null == (first = first.next)) {
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
			if (null == (first = first.next)) {
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
			if (null == (first = first.next)) {
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
