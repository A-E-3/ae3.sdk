/*
 * Created on 11.05.2006
 */
package ru.myx.ae3.control;

import ru.myx.ae3.access.AccessPermissions;
import ru.myx.ae3.base.BaseNativeObject;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.control.command.ControlCommand;
import ru.myx.ae3.control.command.ControlCommandset;
import ru.myx.ae3.reflect.Reflect;

/**
 * @author myx
 * @param <T>
 * 			
 */
public interface ControlActor<T extends ControlActor<?>> extends ControlBasic<T> {
	
	/**
	 * 
	 */
	static BaseObject PROTOTYPE = new BaseNativeObject(Reflect.classToBasePrototype(ControlActor.class));
	
	@Override
	default BaseObject basePrototype() {
		
		return ControlActor.PROTOTYPE;
	}
	
	/**
	 * @return permissions
	 */
	AccessPermissions getCommandPermissions();
	
	/**
	 * @param command
	 * @param arguments
	 * @return result
	 * @throws Exception
	 */
	Object getCommandResult(ControlCommand<?> command, BaseObject arguments) throws Exception;
	
	/**
	 * @return commands
	 */
	ControlCommandset getCommands();
}
