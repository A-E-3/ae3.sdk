/*
 * Created on 11.05.2006
 */
package ru.myx.ae3.control;

import ru.myx.ae3.base.BaseNativeObject;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.control.fieldset.ControlFieldset;
import ru.myx.ae3.reflect.Reflect;

/**
 * @author myx
 * @param <T>
 * 			
 */
public interface ControlForm<T extends ControlForm<?>> extends ControlActor<T> {
	
	/**
	 * 
	 */
	public static final BaseObject PROTOTYPE = new BaseNativeObject(Reflect.classToBasePrototype(ControlForm.class));
	
	@Override
	default BaseObject basePrototype() {
		
		return ControlForm.PROTOTYPE;
	}
	
	/**
	 * @return fieldset
	 */
	public ControlFieldset<?> getFieldset();
	
	/**
	 * @param data
	 * @return data
	 */
	public BaseObject setData(final BaseObject data);
}
