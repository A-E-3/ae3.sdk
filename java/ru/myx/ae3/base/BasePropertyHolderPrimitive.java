package ru.myx.ae3.base;

import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;
import ru.myx.ae3.exec.ResultHandler;

/** 8.6.1 Property Attributes
 * <p>
 * A property can have zero or more attributes from the following set:<br< <b>ReadOnly</b> - The
 * property is a read-only property. Attempts by ECMAScript code to write to the property will be
 * ignored. (Note, however, that in some cases the value of a property with the ReadOnly attribute
 * may change over time because of actions taken by the host environment; therefore “ReadOnly” does
 * not mean “constant and unchanging”!)<br>
 * <b>DontEnum</b> - The property is not to be enumerated by a for-in enumeration (section 12.6.4).
 * <br>
 * <b>DontDelete</b> - Attempts to delete the property will be ignored. See the description of the
 * delete operator in section 11.4.1.<br>
 * <p>
 * Internal properties not mentioned here since they are implemented not as properties.
 * <p>
 *
 * @author myx */
final class BasePropertyHolderPrimitive extends BasePropertyDataPrimitive {
	
	BaseObject value;

	/** @param value
	 * @param attributes */
	public BasePropertyHolderPrimitive(final BaseObject value, final short attributes) {
		super(attributes);
		this.value = value;
	}

	/** @param name
	 * @param value
	 * @param attributes */
	public BasePropertyHolderPrimitive(final BasePrimitiveString name, final BaseObject value, final short attributes) {
		super(name, attributes);
		this.value = value;
	}

	/** @param name
	 * @param value
	 * @param attributes */
	public BasePropertyHolderPrimitive(final BasePrimitiveString name, final double value, final short attributes) {
		super(name, attributes);
		this.value = Base.forDouble(value);
	}

	/** @param name
	 * @param value
	 * @param attributes */
	public BasePropertyHolderPrimitive(final BasePrimitiveString name, final long value, final short attributes) {
		super(name, attributes);
		this.value = Base.forLong(value);
	}

	/** @param name
	 * @param value
	 * @param attributes */
	public BasePropertyHolderPrimitive(final BasePrimitiveString name, final String value, final short attributes) {
		super(name, attributes);
		this.value = Base.forString(value);
	}

	@Override
	public BaseObject propertyGet(final BaseObject instance, final BasePrimitiveString name) {
		
		// assert name != null : "Name is NULL";
		// assert name == this.name :
		// "Names must match for this type of property: (this.name="
		// + this.name
		// + ", name="
		// + name
		// + ")";
		return this.value;
	}

	@Override
	public BaseObject propertyGet(final BaseObject instance, final String name) {
		
		// assert name != null : "Name is NULL";
		// assert name.equals( this.name.toString() ) :
		// "Names must match for this type of property: (this.name="
		// + this.name
		// + ", name="
		// + name
		// + ")";
		return this.value;
	}

	@Override
	public BaseObject propertyGetAndSet(final BaseObject instance, final String name, final BaseObject value) {
		
		// assert name != null : "Name is NULL";
		// assert name.equals( this.name.toString() ) :
		// "Names must match for this type of property: (this.name="
		// + this.name
		// + ", name="
		// + name
		// + ")";
		try {
			return this.value;
		} finally {
			this.value = value;
		}
	}

	@Override
	public ExecStateCode propertyGetCtxResult(final ExecProcess ctx, final BaseObject instance, final BasePrimitive<?> name, final ResultHandler store) {
		
		// assert name != null : "Name is NULL";

		// assert name == this.name :
		// "Names must match for this type of property: (this.name="
		// + this.name
		// + ", name="
		// + name
		// + ")";
		return store.execReturn(ctx, this.value);
	}

	/** @param value
	 * @param attributes
	 * @return
	 * @throws IllegalArgumentException */
	@Override
	public final boolean propertySet(final BaseObject instance, final CharSequence name, final BaseObject value, final short attributes) throws IllegalArgumentException {
		
		// assert name != null : "Name is NULL";

		// assert name.equals(this.name.toString()) : "Names must match for this type of property:
		// (this.name=" + this.name + ", name=" + name + ")";
		
		/** Must not change attributes */
		this.value = value;
		return true;
	}
}
