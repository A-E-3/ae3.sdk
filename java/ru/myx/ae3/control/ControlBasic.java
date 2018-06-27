/*
 * Created on 11.05.2006
 */
package ru.myx.ae3.control;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseNativeObject;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.common.HasAttributes;
import ru.myx.ae3.reflect.Reflect;

/** @author myx
 * @param <T>
 */
public interface ControlBasic<T extends ControlBasic<?>> extends BaseObject, HasAttributes {
	
	/**
	 *
	 */
	static BaseObject PROTOTYPE = new BaseNativeObject(Reflect.classToBasePrototype(ControlBasic.class));

	@Override
	default BaseObject basePrototype() {
		
		return ControlBasic.PROTOTYPE;
	}

	/** returns some data with additional object description (if any).
	 *
	 * @return all object data */
	BaseObject getData();

	/** Returns an icon notation or null when no icon available
	 *
	 * @return icon type name */
	String getIcon();

	/** Returns a key or null
	 *
	 * @return key */
	String getKey();

	/** Returns a title or null when no title available
	 *
	 * @return title */
	String getTitle();

	/** @param name
	 * @param value
	 * @return same object */
	T setAttribute(String name, BaseObject value);

	/** @param name
	 * @param value
	 * @return same object */
	default T setAttribute(final String name, final boolean value) {

		return this.setAttribute(
				name,
				value
					? BaseObject.TRUE
					: BaseObject.FALSE);
	}

	/** @param name
	 * @param value
	 * @return same object */
	default T setAttribute(final String name, final double value) {

		return this.setAttribute(name, Base.forDouble(value));
	}

	/** @param name
	 * @param value
	 * @return same object */
	default T setAttribute(final String name, final long value) {

		return this.setAttribute(name, Base.forLong(value));
	}

	/** @param name
	 * @param value
	 * @return same object */
	default T setAttribute(final String name, final String value) {
		
		return this.setAttribute(
				name,
				value == null
					? null
					: Base.forString(value));
	}

	/** @param map
	 * @return same object */
	T setAttributes(BaseObject map);
}
