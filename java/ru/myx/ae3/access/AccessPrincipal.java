/*
 * Created on 10.05.2006
 */
package ru.myx.ae3.access;

import ru.myx.ae3.base.BaseNativeObject;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.reflect.Reflect;

/**
 * @author myx
 * @param <T>
 * 			
 */
public interface AccessPrincipal<T extends AccessPrincipal<?>> extends BaseObject, Comparable<T> {
	
	/**
	 * 
	 */
	public static final BaseObject PROTOTYPE = new BaseNativeObject(Reflect.classToBasePrototype(AccessPrincipal.class));
	
	@Override
	default BaseObject basePrototype() {
		
		return AccessPrincipal.PROTOTYPE;
	}
	
	/**
	 * @return key
	 */
	public String getKey();
	
	/**
	 * @return boolean
	 */
	public boolean isGroup();
	
	/**
	 * @return boolean
	 */
	public boolean isPerson();
}
