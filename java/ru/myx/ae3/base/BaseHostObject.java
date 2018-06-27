package ru.myx.ae3.base;

import java.util.Iterator;

import ru.myx.ae3.reflect.Reflect;
import ru.myx.ae3.reflect.ReflectionDisable;

/**
 * 
 * Host object with some own properties
 * 
 * 1) BaseHostObject's baseValue() method MUST return 'this'.
 * 
 * 2) Abstract 'host' object WITH own properties by design, see BaseHostEmpty
 * for one with NO own properties.
 * 
 * Only 1 abstract method: baseToString.
 * 
 * @author myx
 * 
 */
@ReflectionDisable
public abstract class BaseHostObject extends BaseEditableAbstract implements BaseHost {
	
	/**
	 * 
	 */
	protected BaseObject prototype;
	
	/**
	 * BaseObject.PROTOTYPE
	 */
	public BaseHostObject() {
		
		this.prototype = Reflect.classToBasePrototype(getClass());
	}
	
	/**
	 * @param prototype
	 */
	public BaseHostObject(final BaseObject prototype) {
		
		this.prototype = prototype;
	}
	
	/**
	 * overrides default iterator implementation.
	 */
	@Override
	public void baseClear() {
		
		this.properties = null;
	}
	
	@Override
	public boolean baseIsExtensible() {
		
		return true;
	}
	
	@Override
	public final BaseObject basePrototype() {
		
		return this.prototype;
	}
	
}
