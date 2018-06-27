package ru.myx.ae3.base;

import ru.myx.ae3.reflect.ReflectionDisable;

/**
 * 
 * Host object with no own properties
 * 
 * 1) BaseHostEmpty's baseValue() method MUST return 'this'.
 * 
 * 2) Abstract 'host' object with NO own properties by design, see
 * BaseHostObject for one WITH own properties.
 * 
 * Only 1 abstract method: baseToString.
 * 
 * @author myx
 * 		
 */
@ReflectionDisable
public abstract class BaseHostSealed implements BaseHost, BaseObjectNotWritable {
	//
}
