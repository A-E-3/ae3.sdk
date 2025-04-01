package ru.myx.ae3.base;

import ru.myx.ae3.reflect.ReflectionDisable;

/** TODO: is there a need for separate class? There is a constructor and there may be a need for API
 * factory
 *
 *
 * @author myx */
@ReflectionDisable
public class BaseNativeObjectCaseInsencetive extends BaseNativeObject {
	
	/**
	 *
	 */
	public BaseNativeObjectCaseInsencetive() {
		
		super();
		// this.properties = new PropertiesStringHashMapCaseInsencetive();
		this.properties = new PropertiesPrimitiveOwnIdentityMapInsencetive();
	}
}
