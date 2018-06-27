package ru.myx.ae3.base;

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
abstract class BaseNativeAbstract extends BaseEditableAbstract implements BaseNative {

	final BaseObject prototype;

	/** primitive types are not accepted
	 *
	 * @param prototype
	 *            null to specify no prototype or prototype otherwise */
	BaseNativeAbstract(final BaseObject prototype) {

		assert prototype == null || !prototype.baseIsPrimitive() : "Primitive prototype: prototype=" + prototype;
		assert Base.checkPrototypeChainDuplicates(this, prototype) : "Duplicates in prototype chain!";
		this.prototype = prototype;
	}

	@Override
	public boolean baseIsExtensible() {

		return true;
	}

	@Override
	public final BaseObject basePrototype() {

		return this.prototype;
	}

	@Override
	public abstract boolean equals(final Object o);

	@Override
	public abstract int hashCode();

	@Override
	public String toString() {

		return "[object " + this.getClass().getSimpleName() + "]";
	}
}
