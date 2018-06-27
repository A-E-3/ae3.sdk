package ru.myx.ae3.base;

import java.util.Iterator;

import ru.myx.ae3.reflect.ReflectionDisable;

/**
 * 
 * Host object with no own properties
 * 
 * 1) BaseHostDirect's baseValue() method MUST return 'this'.
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
public abstract class BaseHostDirect implements BaseHost {
	
	@Override
	public final boolean baseDelete(final String name) {
		
		return false;
	}
	
	@Override
	public final BaseProperty baseGetOwnProperty(final BasePrimitiveString name) {
		
		return null;
	}
	
	@Override
	public final BaseProperty baseGetOwnProperty(final String name) {
		
		return null;
	}
	
	@Override
	public final boolean baseHasKeysOwn() {
		
		return false;
	}
	
	@Override
	public final boolean baseIsExtensible() {
		
		return false;
	}
	
	@Override
	public final Iterator<String> baseKeysOwn() {
		
		return BaseObject.ITERATOR_EMPTY;
	}
	
	@Override
	public final Iterator<? extends BasePrimitive<?>> baseKeysOwnPrimitive() {
		
		return BaseObject.ITERATOR_EMPTY_PRIMITIVE;
	}
}
