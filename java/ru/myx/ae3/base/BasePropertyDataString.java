package ru.myx.ae3.base;

import java.util.Iterator;

import ru.myx.util.IteratorSingle;

/**
 *
 * 8.6.1 Property Attributes
 * <p>
 * A property can have zero or more attributes from the following set:<br<
 * <b>ReadOnly</b> - The property is a read-only property. Attempts by
 * ECMAScript code to write to the property will be ignored. (Note, however,
 * that in some cases the value of a property with the ReadOnly attribute may
 * change over time because of actions taken by the host environment; therefore
 * “ReadOnly” does not mean “constant and unchanging”!)<br>
 * <b>DontEnum</b> - The property is not to be enumerated by a for-in
 * enumeration (section 12.6.4). <br>
 * <b>DontDelete</b> - Attempts to delete the property will be ignored. See the
 * description of the delete operator in section 11.4.1.<br>
 * <p>
 * Internal properties not mentioned here since they are implemented not as
 * properties.
 * <p>
 *
 * @author myx
 *
 */
abstract class BasePropertyDataString extends BasePropertyData<String> {

	/**
	 * @param attributes
	 */
	public BasePropertyDataString(final int attributes) {
		this.name = null;
		this.attributes = (short) attributes;
	}
	
	/**
	 * @param name
	 * @param attributes
	 */
	public BasePropertyDataString(final String name, final short attributes) {
		this.name = name;
		this.attributes = attributes;
	}
	
	@Override
	public BaseProperties<String> add(final BaseObject instance, final BasePrimitiveString name, final BaseProperty property, final short attributes) {

		return this.add(instance, name.stringValue(), property, attributes);
	}
	
	@Override
	public final BaseProperties<String> add(final BaseObject instance, final String name, final BaseProperty property, final short attributes) {

		return this.add(name, Base.createPropertyString(
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
	public final BaseProperties<String> add(final BasePrimitiveString name, final BasePropertyData<String> property) {

		assert this.name != null : "Name should be established already!";
		return new PropertiesStringDirectToHash(this, name.stringValue(), property);
	}
	
	@Override
	public final BaseProperties<String> add(final String name, final BaseObject value, final short attributes) {

		return this.add(name, new BasePropertyHolderString(null, value, attributes));
	}
	
	@Override
	public final BaseProperties<String> add(final String name, final BasePropertyData<String> property) {

		assert this.name != null : "Name should be established already!";
		return new PropertiesStringDirectToHash(this, name, property);
	}
	
	@Override
	public final BasePropertyData<String> delete(final BasePrimitiveString name) {

		return name.stringEquals(this.name)
			? null
			: this;
	}
	
	@Override
	public final BasePropertyData<String> delete(final String name) {

		return this.name.equals(name)
			? null
			: this;
	}
	
	@Override
	public final BasePropertyDataString find(final BasePrimitiveString name) {

		// assert this.name != null : "Name should be established already!";
		return name.stringEquals(this.name)
			? this
			: null;
	}
	
	@Override
	public final BasePropertyDataString find(final CharSequence name) {

		// assert this.name != null : "Name should be established already!";
		return this.name.contentEquals(name)
			? this
			: null;
	}
	
	@Override
	public final BasePropertyDataString find(final String name) {

		// assert this.name != null : "Name should be established already!";
		return this.name.equals(name)
			? this
			: null;
	}
	
	@Override
	public final boolean hasEnumerableProperties() {

		return (this.attributes & BaseProperty.ATTR_ENUMERABLE) != 0;
	}
	
	@Override
	public final Iterator<String> iteratorAll() {

		return new IteratorSingle<>(this.name);
	}
	
	@Override
	public final Iterator<BasePrimitiveString> iteratorAllAsPrimitive() {

		return new IteratorSingle<>(Base.forString(this.name));
	}
	
	@Override
	public final Iterator<String> iteratorAllAsString() {

		return new IteratorSingle<>(this.name);
	}
	
	@Override
	public final Iterator<String> iteratorEnumerable() {

		return (this.attributes & BaseProperty.ATTR_ENUMERABLE) != 0
			? new IteratorSingle<>(this.name)
			: BaseObject.ITERATOR_EMPTY;
	}
	
	@Override
	public final Iterator<BasePrimitiveString> iteratorEnumerableAsPrimitive() {

		return (this.attributes & BaseProperty.ATTR_ENUMERABLE) != 0
			? new IteratorSingle<>(Base.forString(this.name))
			: BaseProperties.ITERATOR_EMPTY_PRIMITIVE_STRING;
	}
	
	@Override
	public final Iterator<String> iteratorEnumerableAsString() {

		return this.iteratorEnumerable();
	}
	
	@Override
	public final int size() {

		return 1;
	}
}
