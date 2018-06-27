/*
 * Created on 11.05.2006
 */
package ru.myx.ae3.control.field;

import java.util.Set;

import ru.myx.ae3.base.BaseNativeObject;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.common.HasAttributes;
import ru.myx.ae3.control.fieldset.ControlFieldset;
import ru.myx.ae3.reflect.ControlType;
import ru.myx.ae3.reflect.Reflect;

/** @author myx */
public interface ControlField extends BaseObject, HasAttributes {
	
	/**
	 *
	 */
	static BaseObject PROTOTYPE = new BaseNativeObject(Reflect.classToBasePrototype(ControlField.class));
	
	@Override
	default BaseObject basePrototype() {
		
		return ControlField.PROTOTYPE;
	}
	
	/** @return field */
	ControlField cloneField();
	
	/** @param object
	 *            value is persistent form to be converted to runtime value
	 * @param fieldsetContext
	 *            object with all fields, could be NULL easily
	 * @return */
	BaseObject dataRetrieve(BaseObject object, BaseObject fieldsetContext);
	
	/** @param object
	 *            value is runtime form to be converted to persistent value
	 * @param fieldsetContext
	 *            object with all fields, could be NULL easily
	 * @return */
	BaseObject dataStore(BaseObject object, BaseObject fieldsetContext);
	
	/** @param source
	 * @return string */
	String dataValidate(BaseObject source);
	
	/** Fills inner field named to a set.
	 *
	 * @param target
	 */
	default void fillFields(final Set<String> target) {

		if (target == null) {
			throw new NullPointerException("Target Set MUST not be null!");
		}
		target.add(this.getKey());
	}
	
	/** returns a fieldset with all field attributes available. Should never return <b>null </b>
	 * when getFieldClass() returns non-null value.
	 *
	 * @return fieldset */
	default ControlFieldset<?> getFieldAttributesFieldset() {

		return null;
	}
	
	/** identity for a factory to recreate a field. null for a non-serializable field definition.
	 *
	 * @return string */
	default String getFieldClass() {

		return null;
	}
	
	/** title name for a field class
	 *
	 * @return object */
	default String getFieldClassTitle() {

		return this.getFieldClass();
	}
	
	/** Returns one of generic field data types.
	 *
	 * @return type instance */
	default ControlType<?, ?> getFieldDataType() {
		
		return Reflect.CONTROL_TYPE_EXACT_BASE_OBJECT;
	}
	
	/** @return fieldset */
	default ControlFieldset<?> getFieldset() {
		
		return null;
	}
	
	/** returns <b>true </b> when this field can be edited via type's editor.
	 *
	 * @param type
	 * @return boolean */
	default boolean getFieldTypeAvailability(final String type) {
		
		return false;
	}
	
	/** Returns a key or null
	 *
	 * @return key */
	String getKey();
	
	/** Returns a title or null when no title available
	 *
	 * @return title */
	String getTitle();
	
	/** @return boolean */
	boolean isConstant();
	
	/** @return boolean */
	default boolean isFieldset() {
		
		return false;
	}
	
	/** @param name
	 * @param value
	 * @return same object */
	ControlField setAttribute(String name, BaseObject value);
	
	/** @param name
	 * @param value
	 * @return same object */
	ControlField setAttribute(String name, boolean value);
	
	/** @param name
	 * @param value
	 * @return same object */
	ControlField setAttribute(String name, double value);
	
	/** @param name
	 * @param value
	 * @return same object */
	ControlField setAttribute(String name, long value);
	
	/** @param name
	 * @param value
	 * @return same object */
	ControlField setAttribute(String name, String value);
	
	/** @param map
	 * @return same object */
	ControlField setAttributes(BaseObject map);
	
	/** Sets field's 'constant' attribute.
	 *
	 * @return field */
	ControlField setConstant();
	
	/** Sets field's 'hint' attribute.
	 *
	 * @param hint
	 *
	 * @return field */
	ControlField setFieldHint(BaseObject hint);
	
	/** Sets field's 'hint' attribute.
	 *
	 * @param hint
	 *
	 * @return field */
	ControlField setFieldHint(String hint);
	
	/** Sets field's 'type' attribute.
	 *
	 * @param type
	 *
	 * @return field */
	ControlField setFieldType(BaseObject type);
	
	/** Sets field's 'type' attribute.
	 *
	 * @param type
	 *
	 * @return field */
	ControlField setFieldType(String type);
	
	/** FIXME: inline
	 *
	 * Sets field's 'variant' attribute.
	 *
	 * @param variant
	 *
	 * @return field */
	ControlField setFieldVariant(BaseObject variant);
	
	/** FIXME: inline
	 *
	 * Sets field's 'variant' attribute.
	 *
	 * @param variant
	 *
	 * @return field */
	default ControlField setFieldVariant(final String variant) {

		this.setAttribute("variant", variant);
		return this;
	}
	
}
