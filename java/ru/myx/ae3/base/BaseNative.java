package ru.myx.ae3.base;

import ru.myx.ae3.reflect.ReflectionDisable;

/**
 * 4.3.6 Native Object
 * <p>
 * A native object is any object supplied by an ECMAScript implementation
 * independent of the host environment. Standard native objects are defined in
 * this specification. Some native objects are built-in; others may be
 * constructed during the course of execution of an ECMAScript program.
 * <p>
 *
 * @author myx
 */
@ReflectionDisable
public interface BaseNative extends BaseObject, BaseEditable {
	//
}
