package ru.myx.ae3.common;

import ru.myx.ae3.base.BaseFutureCharSequence;
import ru.myx.ae3.reflect.Reflect;
import ru.myx.ae3.reflect.ReflectionExplicit;
import ru.myx.ae3.reflect.ReflectionManual;

/**
 * @author myx
 * 		
 * @param <V>
 * 			
 * 			
 */
@ReflectionManual
public class FutureSimpleCharSequence<V extends CharSequence> extends FutureSimpleUnknown<V> implements BaseFutureCharSequence<V> {
	
	static {
		Reflect.classToBasePrototype(FutureSimpleCharSequence.class);
	}
	
	/**
	 * 
	 */
	@ReflectionExplicit
	public FutureSimpleCharSequence() {
		//
	}
	
}
