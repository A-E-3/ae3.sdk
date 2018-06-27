package ru.myx.ae3.control;

import ru.myx.ae3.access.AccessPermissions;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.control.command.ControlCommand;
import ru.myx.ae3.control.command.ControlCommandset;

/**
 * @author myx
 * @param <T>
 * 			
 */
public abstract class AbstractActor<T extends AbstractActor<?>> extends AbstractBasic<T> implements ControlActor<T> {
	
	@Override
	public AccessPermissions getCommandPermissions() {
		
		return null;
	}
	
	@Override
	public Object getCommandResult(final ControlCommand<?> command, final BaseObject arguments) throws Exception {
		
		if (command == null) {
			throw new IllegalArgumentException("No command!");
		}
		throw new IllegalArgumentException("Unknown command: " + command.getKey());
	}
	
	@Override
	public ControlCommandset getCommands() {
		
		return null;
	}
}
