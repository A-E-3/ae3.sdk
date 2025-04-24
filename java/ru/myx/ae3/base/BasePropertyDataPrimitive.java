package ru.myx.ae3.base;

import java.util.Iterator;

import ru.myx.util.IteratorSingle;

abstract class BasePropertyDataPrimitive extends BasePropertyData<BasePrimitiveString> {

	/** @param name
	 * @param attributes */
	public BasePropertyDataPrimitive(final BasePrimitiveString name, final short attributes) {

		super(name, attributes);
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

		assert name != null : "NULL property name";
		assert this.name != null : "Name should be established already!";
		return new PropertiesPrimitiveDirectToHash(this, name, property);
	}

	@Override
	public final BaseProperties<BasePrimitiveString> add(final String name, final BaseObject value, final short attributes) {

		return this.add(Base.forString(name), value, attributes);
	}

	@Override
	public final BaseProperties<BasePrimitiveString> add(final String name, final BasePropertyData<BasePrimitiveString> property) {

		assert this.name != null : "Name should be established already!";
		return new PropertiesPrimitiveDirectToHash(this, Base.forString(name), property);
	}

	@Override
	public final BasePropertyData<BasePrimitiveString> delete(final BasePrimitiveString name) {

		// assert this.name != null : "Name should be established already!";
		return this.name == name
			? null
			: this;
	}

	@Override
	public final BaseProperties<BasePrimitiveString> delete(final String name) {

		// assert this.name != null : "Name should be established already!";
		return this.name.stringEquals(name)
			? null
			: this;
	}

	@Override
	public final BasePropertyData<BasePrimitiveString> find(final BasePrimitiveString name) {

		// assert this.name != null : "Name should be established already!";
		return this.name == name
			? this
			: null;
	}

	@Override
	public final BasePropertyData<BasePrimitiveString> find(final CharSequence name) {

		// assert this.name != null : "Name should be established already!";
		return this.name == name || this.name.equals(name)
			? this
			: null;
	}

	@Override
	public final BasePropertyData<BasePrimitiveString> find(final String name) {

		// assert this.name != null : "Name should be established already!";
		return this.name.stringValue().equals(name)
			? this
			: null;
	}

	@Override
	public final boolean hasEnumerableProperties() {

		return (this.attributes & BaseProperty.ATTR_ENUMERABLE) != 0;
	}

	@Override
	public final Iterator<BasePrimitiveString> iteratorAll() {

		return new IteratorSingle<>(this.name);
	}

	@Override
	public final Iterator<BasePrimitiveString> iteratorAllAsPrimitive() {

		return new IteratorSingle<>(this.name);
	}

	@Override
	public final Iterator<String> iteratorAllAsString() {

		return new IteratorSingle<>(this.name.stringValue());
	}

	@Override
	public final Iterator<BasePrimitiveString> iteratorEnumerable() {

		return (this.attributes & BaseProperty.ATTR_ENUMERABLE) != 0
			? new IteratorSingle<>(this.name)
			: BaseProperties.ITERATOR_EMPTY_PRIMITIVE_STRING;
	}

	@Override
	public final Iterator<BasePrimitiveString> iteratorEnumerableAsPrimitive() {

		return this.iteratorEnumerable();
	}

	@Override
	public final Iterator<String> iteratorEnumerableAsString() {

		return (this.attributes & BaseProperty.ATTR_ENUMERABLE) != 0
			? new IteratorSingle<>(this.name.stringValue())
			: BaseObject.ITERATOR_EMPTY;
	}

	@Override
	public final int size() {

		return 1;
	}
}
