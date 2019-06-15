package ru.myx.ae3.base;

import java.util.Iterator;

import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.reflect.ReflectionDisable;

/** 4.3.6 Native Object
 * <p>
 * A native object is any object supplied by an ECMAScript implementation independent of the host
 * environment. Standard native objects are defined in this specification. Some native objects are
 * built-in; others may be constructed during the course of execution of an ECMAScript program.
 * <p>
 *
 * @author myx */
@ReflectionDisable
public interface BaseNative extends /* already: BaseObject, */BaseEditable {
	
	/** @param object
	 * @param propertiesObject
	 * @return */
	public static BaseObject defineProperties(final BaseObject object, final BaseObject propertiesObject) {
		
		if (object.baseIsPrimitive()) {
			throw new BaseNativeErrorTypeError("Object is primitive!");
		}
		if (propertiesObject == BaseObject.UNDEFINED || propertiesObject == BaseObject.NULL) {
			return object;
		}
		if (propertiesObject.baseIsPrimitive()) {
			throw new BaseNativeErrorTypeError("TypeError: primitive type, " + propertiesObject);
		}
		/** 15.2.3.7 Object.defineProperties ( O, Properties ) The defineProperties function is used
		 * to add own properties and/or update the attributes of existing own properties of an
		 * object.
		 *
		 * When the defineProperties function is called, the following steps are taken: <code>
				1.	If Type(O) is not Object throw a TypeError exception.
				2.	Let props be ToObject(Properties).
				3.	Let names be an internal list containing the names of each enumerable own property of props.
				4.	Let descriptors be an empty internal List.
				5.	For each element P of names in list order,
					a. Let descObj be the result of calling the [[Get]] internal method of props with P as the argument.
					b. Let desc be the result of calling ToPropertyDescriptor with descObj as the argument.
					c. Append desc to the end of descriptors.
				6.	For each element desc of descriptors in list order, a.	Call the [[DefineOwnProperty]] internal method of O with arguments P, desc, and true.
				7.	Return O. If an implementation defines a specific order of enumeration for the for-in statement, that same enumeration
				order must be used to order the list elements in step 3 of this algorithm.
			 * </code> */
		/** <code>
		final BaseArray propertiesArray = propertiesObject.baseArray();
		if (propertiesArray != null) {
			throw new BaseNativeErrorTypeError("Not yet! Array?");
		}
		</code> */
		for (final Iterator<? extends BasePrimitive<?>> i = propertiesObject.baseKeysOwnPrimitive(); i.hasNext();) {
			final BasePrimitive<?> name = i.next();
			final BaseObject descriptor = propertiesObject.baseGet(name, BaseObject.UNDEFINED);
			BaseNative.defineProperty(object, name.baseToString(), descriptor);
		}
		return object;
	}
	
	//
	/** @param object
	 * @param name
	 * @param description
	 * @return */
	public static BaseObject defineProperty(final BaseObject object, final BasePrimitiveString name, final BaseObject description) {

		if (object.baseIsPrimitive()) {
			throw new BaseNativeErrorTypeError("TypeError");
		}
		if (description == BaseObject.UNDEFINED || description == BaseObject.NULL) {
			throw new BaseNativeErrorTypeError("Property descriptor is required");
		}
		if (description.baseIsPrimitive()) {
			throw new BaseNativeErrorTypeError("TypeError: primitive type, " + description);
		}
		/** 15.2.3.6 Object.defineProperty ( O, P, Attributes ).
		 *
		 * The defineProperty function is used to add an own property and/or update the attributes
		 * of an existing own property of an object.
		 *
		 * When the defineProperty function is called, the following steps are taken:: <code>
			1.	If Type(O) is not Object throw a TypeError exception.
			2.	Let name be ToString(P).
			3.	Let desc be the result of calling ToPropertyDescriptor with Attributes as the argument.
			4.	Call the [[DefineOwnProperty]] internal method of O with arguments name, desc, and true.
			5.	Return O.
			</code> */

		final BaseObject value = Base.get(description, "value", BaseObject.UNDEFINED, BaseObject.PROTOTYPE);
		final BaseObject getterObject = Base.get(description, "get", BaseObject.UNDEFINED, BaseObject.PROTOTYPE);
		final BaseObject setterObject = Base.get(description, "set", BaseObject.UNDEFINED, BaseObject.PROTOTYPE);

		final short attributes = BaseProperty.propertyAttributesFromDescriptor(description);

		/** none */
		if (getterObject == BaseObject.UNDEFINED && setterObject == BaseObject.UNDEFINED) {
			object.baseDefine(name, value, attributes);
			return object;
		}
		if (value != BaseObject.UNDEFINED) {
			throw new BaseNativeErrorTypeError("Cannot specify a value, either data descriptor, either procedural one, not both!");
		}
		if (!(object instanceof BaseEditable)) {
			throw new BaseNativeErrorTypeError("Not BaseEditable, cannot define setter or getter, class: " + object.getClass().getSimpleName());
		}

		final BaseProperty property;
		/** both */
		if (getterObject != BaseObject.UNDEFINED && setterObject != BaseObject.UNDEFINED) {
			final BaseFunction getter = getterObject.baseCall();
			if (getter == null) {
				throw new BaseNativeErrorTypeError("getter is not a function, class: " + getterObject.getClass().getSimpleName());
			}
			final BaseFunction setter = setterObject.baseCall();
			if (setter == null) {
				throw new BaseNativeErrorTypeError("setter is not a function, class: " + setterObject.getClass().getSimpleName());
			}
			property = new BasePropertyGetterAndSetter(getter, setter);
		} else //
		/** getter only */
		if (getterObject != BaseObject.UNDEFINED) {
			final BaseFunction getter = getterObject.baseCall();
			if (getter == null) {
				throw new BaseNativeErrorTypeError("getter is not a function, class: " + getterObject.getClass().getSimpleName());
			}
			property = new BasePropertyGetterNoSetter(getter);
		} else //
		/** setter only */
		{
			final BaseFunction setter = setterObject.baseCall();
			if (setter == null) {
				throw new BaseNativeErrorTypeError("setter is not a function, class: " + setterObject.getClass().getSimpleName());
			}
			property = new BasePropertySetterNoGetter(setter);
		}

		((BaseEditable) object).setOwnProperty(name, property, attributes);
		return object;
	}

	/** @param ctx
	 * @param store
	 * @param object
	 * @param propertiesObject
	 * @return */
	public static ExecStateCode vmDefineProperties(//
			final ExecProcess ctx,
			final ResultHandler store,
			final BaseObject object,
			final BaseObject propertiesObject) {

		if (object.baseIsPrimitive()) {
			return ctx.vmRaiseType("Object is primitive!");
		}
		if (propertiesObject == BaseObject.UNDEFINED || propertiesObject == BaseObject.NULL) {
			return store.execReturn(ctx, object);
		}
		if (propertiesObject.baseIsPrimitive()) {
			return ctx.vmRaise("TypeError: primitive type, " + propertiesObject);
		}
		/** 15.2.3.7 Object.defineProperties ( O, Properties ) The defineProperties function is used
		 * to add own properties and/or update the attributes of existing own properties of an
		 * object.
		 *
		 * When the defineProperties function is called, the following steps are taken: <code>
				1.	If Type(O) is not Object throw a TypeError exception.
				2.	Let props be ToObject(Properties).
				3.	Let names be an internal list containing the names of each enumerable own property of props.
				4.	Let descriptors be an empty internal List.
				5.	For each element P of names in list order,
					a. Let descObj be the result of calling the [[Get]] internal method of props with P as the argument.
					b. Let desc be the result of calling ToPropertyDescriptor with descObj as the argument.
					c. Append desc to the end of descriptors.
				6.	For each element desc of descriptors in list order, a.	Call the [[DefineOwnProperty]] internal method of O with arguments P, desc, and true.
				7.	Return O. If an implementation defines a specific order of enumeration for the for-in statement, that same enumeration
				order must be used to order the list elements in step 3 of this algorithm.
			 * </code> */
		/** <code>
		final BaseArray propertiesArray = propertiesObject.baseArray();
		if (propertiesArray != null) {
			return ctx.vmRaise("Not yet! Array?");
		}
		</code> */
		for (final Iterator<? extends BasePrimitive<?>> i = propertiesObject.baseKeysOwnPrimitive(); i.hasNext();) {
			final BasePrimitive<?> name = i.next();
			final BaseObject descriptor = propertiesObject.baseGet(name, BaseObject.UNDEFINED);
			final ExecStateCode code = BaseNative.vmDefineProperty(ctx, ResultHandler.FA_BNN_NXT, object, name.baseToString(), descriptor);
			if (code != null) {
				return code;
			}
		}
		return store.execReturn(ctx, object);
	}

	/** @param ctx
	 * @param store
	 * @param object
	 * @param name
	 * @param description
	 * @return */
	public static ExecStateCode vmDefineProperty(//
			final ExecProcess ctx,
			final ResultHandler store,
			final BaseObject object,
			final BasePrimitiveString name,
			final BaseObject description) {

		if (object.baseIsPrimitive()) {
			return ctx.vmRaiseType("Object is primitive!");
		}
		if (description == BaseObject.UNDEFINED || description == BaseObject.NULL) {
			return ctx.vmRaiseType("Property descriptor is required!");
		}
		if (description.baseIsPrimitive()) {
			return ctx.vmRaiseType("Property descriptor is primitive, " + description);
		}
		/** 15.2.3.6 Object.defineProperty ( O, P, Attributes ).
		 *
		 * The defineProperty function is used to add an own property and/or update the attributes
		 * of an existing own property of an object.
		 *
		 * When the defineProperty function is called, the following steps are taken:: <code>
			1.	If Type(O) is not Object throw a TypeError exception.
			2.	Let name be ToString(P).
			3.	Let desc be the result of calling ToPropertyDescriptor with Attributes as the argument.
			4.	Call the [[DefineOwnProperty]] internal method of O with arguments name, desc, and true.
			5.	Return O.
			</code> */

		final BaseObject value = Base.get(description, "value", BaseObject.UNDEFINED, BaseObject.PROTOTYPE);
		final BaseObject getterObject = Base.get(description, "get", BaseObject.UNDEFINED, BaseObject.PROTOTYPE);
		final BaseObject setterObject = Base.get(description, "set", BaseObject.UNDEFINED, BaseObject.PROTOTYPE);

		final short attributes = BaseProperty.propertyAttributesFromDescriptor(description);

		/** none */
		if (getterObject == BaseObject.UNDEFINED && setterObject == BaseObject.UNDEFINED) {
			object.baseDefine(name, value, attributes);
			return store.execReturn(ctx, object);
		}
		if (value != BaseObject.UNDEFINED) {
			return ctx.vmRaiseType("Cannot specify a value, either data descriptor, either procedural one, not both!");
		}
		if (!(object instanceof BaseEditable)) {
			return ctx.vmRaiseType("Not BaseEditable, cannot define setter or getter, class: " + object.getClass().getSimpleName());
		}

		final BaseProperty property;
		/** both */
		if (getterObject != BaseObject.UNDEFINED && setterObject != BaseObject.UNDEFINED) {
			final BaseFunction getter = getterObject.baseCall();
			if (getter == null) {
				return ctx.vmRaiseType("getter is not a function, class: " + getterObject.getClass().getSimpleName());
			}
			final BaseFunction setter = setterObject.baseCall();
			if (setter == null) {
				return ctx.vmRaiseType("setter is not a function, class: " + setterObject.getClass().getSimpleName());
			}
			property = new BasePropertyGetterAndSetter(getter, setter);
		} else //
		/** getter only */
		if (getterObject != BaseObject.UNDEFINED) {
			final BaseFunction getter = getterObject.baseCall();
			if (getter == null) {
				return ctx.vmRaiseType("getter is not a function, class: " + getterObject.getClass().getSimpleName());
			}
			property = new BasePropertyGetterNoSetter(getter);
		} else //
		/** setter only */
		{
			final BaseFunction setter = setterObject.baseCall();
			if (setter == null) {
				return ctx.vmRaiseType("setter is not a function, class: " + setterObject.getClass().getSimpleName());
			}
			property = new BasePropertySetterNoGetter(setter);
		}

		((BaseEditable) object).setOwnProperty(name, property, attributes);
		return store.execReturn(ctx, object);
	}

}
