/*
 * Created on 11.05.2006
 */
package ru.myx.ae3.control;

import ru.myx.ae3.base.BaseArray;
import ru.myx.ae3.base.BaseNativeObject;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.control.command.ControlCommandset;
import ru.myx.ae3.reflect.Reflect;

/**
 * @author myx
 * @param <T>
 * 			
 */
public interface ControlContainer<T extends ControlContainer<?>> extends ControlActor<T> {
	
	/**
	 * 
	 */
	public static final BaseObject PROTOTYPE = new BaseNativeObject(Reflect.classToBasePrototype(ControlContainer.class));
	
	@Override
	default BaseObject basePrototype() {
		
		return ControlContainer.PROTOTYPE;
	}
	
	/**
	 * @param key
	 * @return commandset
	 */
	public ControlCommandset getContentCommands(final String key);
	
	/**
	 * @param keys
	 * @return commandset
	 */
	public ControlCommandset getContentMultipleCommands(final BaseArray keys);
}
