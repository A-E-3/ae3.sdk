/*
 * Created on 11.05.2006
 */
package ru.myx.ae3.control.field;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseNativeObject;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.control.ControlBasic;
import ru.myx.ae3.reflect.Reflect;

/** @author myx
 * @param <F>
 * @param <J>
 *            Java value
 * @param <N>
 *            Native value */
public interface ControlFieldGeneric<F extends ControlFieldGeneric<?, J, N>, J extends Object, N extends BaseObject> extends ControlField, ControlBasic<F> {

	/**
	 *
	 */
	static BaseObject PROTOTYPE = new BaseNativeObject(Reflect.classToBasePrototype(ControlFieldGeneric.class));

	@Override
	default BaseObject basePrototype() {

		return ControlFieldGeneric.PROTOTYPE;
	}

	/** @return field */
	@Override
	F cloneField();
	
	/** @param object
	 *            value is persistent form to be converted to runtime value
	 * @param fieldsetContext
	 *            object with all fields, could be NULL easily
	 * @return */
	@Override
	N dataRetrieve(BaseObject object, BaseObject fieldsetContext);
	
	/** @param object
	 *            value is runtime form to be converted to persistent value
	 * @param fieldsetContext
	 *            object with all fields, could be NULL easily
	 * @return */
	@Override
	BaseObject dataStore(BaseObject object, BaseObject fieldsetContext);
	
	/** @param source
	 * @return string */
	@Override
	default String dataValidate(final BaseObject source) {
		
		return null;
	}
	
	/** @return boolean */
	@Override
	boolean isConstant();
	
	/** @param name
	 * @param value
	 * @return same object */
	@Override
	F setAttribute(String name, BaseObject value);
	
	/** @param name
	 * @param value
	 * @return same object */
	@Override
	default F setAttribute(final String name, final boolean value) {
		
		return this.setAttribute(
				name,
				value
					? BaseObject.TRUE
					: BaseObject.FALSE);
	}
	
	/** @param name
	 * @param value
	 * @return same object */
	@Override
	default F setAttribute(final String name, final double value) {
		
		return this.setAttribute(name, Base.forDouble(value));
	}
	
	/** @param name
	 * @param value
	 * @return same object */
	@Override
	default F setAttribute(final String name, final long value) {
		
		return this.setAttribute(name, Base.forLong(value));
	}
	
	/** @param name
	 * @param value
	 * @return same object */
	@Override
	default F setAttribute(final String name, final String value) {

		return this.setAttribute(
				name,
				value == null
					? null
					: Base.forString(value));
	}
	
	/** @param map
	 * @return same object */
	@Override
	F setAttributes(BaseObject map);
	
	/** Sets field's 'constant' attribute.
	 *
	 * @return field */
	@Override
	@SuppressWarnings("unchecked")
	default F setConstant() {

		this.setAttribute("constant", true);
		return (F) this;
	}
	
	/** Sets field's 'hint' attribute.
	 *
	 * @param hint
	 *
	 * @return field */
	@Override
	@SuppressWarnings("unchecked")
	default F setFieldHint(final BaseObject hint) {
		
		this.setAttribute("hint", hint);
		return (F) this;
	}
	
	/** Sets field's 'hint' attribute.
	 *
	 * @param hint
	 *
	 * @return field */
	@Override
	@SuppressWarnings("unchecked")
	default F setFieldHint(final String hint) {

		this.setAttribute("hint", hint);
		return (F) this;
	}
	
	/** Sets field's 'type' attribute.
	 *
	 * @param type
	 *
	 * @return field */
	@Override
	@SuppressWarnings("unchecked")
	default F setFieldType(final BaseObject type) {
		
		this.setAttribute("type", type);
		return (F) this;
	}
	
	/** Sets field's 'type' attribute.
	 *
	 * @param type
	 *
	 * @return field */
	@Override
	@SuppressWarnings("unchecked")
	default F setFieldType(final String type) {
		
		this.setAttribute("type", type);
		return (F) this;
	}
	
	/** FIXME: inline
	 *
	 * Sets field's 'variant' attribute.
	 *
	 * @param variant
	 *
	 * @return field */
	@Override
	@SuppressWarnings("unchecked")
	default F setFieldVariant(final BaseObject variant) {

		this.setAttribute("variant", variant);
		return (F) this;
	}
	
	/** FIXME: inline
	 *
	 * Sets field's 'variant' attribute.
	 *
	 * @param variant
	 *
	 * @return field */
	@Override
	@SuppressWarnings("unchecked")
	default F setFieldVariant(final String variant) {

		this.setAttribute("variant", variant);
		return (F) this;
	}
	
}
