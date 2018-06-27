/*
 * Created on 25.04.2006
 */
package ru.myx.ae3.control;

import ru.myx.ae3.access.AccessPermissions;
import ru.myx.ae3.base.BaseArray;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.control.command.ControlCommand;
import ru.myx.ae3.control.command.ControlCommandset;

/**
 * @author myx
 * @param <T>
 * @param <P>
 * 
 */
public class FilterContainer<T extends FilterContainer<?, ?>, P extends ControlContainer<?>> extends FilterBasic<T, P>
		implements ControlContainer<T> {
	/**
	 * @param handler
	 */
	protected FilterContainer(final P handler) {
		super( handler );
	}
	
	@Override
	public AccessPermissions getCommandPermissions() {
		return this.parent.getCommandPermissions();
	}
	
	@Override
	public Object getCommandResult(final ControlCommand<?> command, final BaseObject arguments) throws Exception {
		return this.parent.getCommandResult( command, arguments );
	}
	
	@Override
	public ControlCommandset getCommands() {
		return this.parent.getCommands();
	}
	
	@Override
	public ControlCommandset getContentCommands(final String key) {
		return this.parent.getContentCommands( key );
	}
	
	@Override
	public ControlCommandset getContentMultipleCommands(final BaseArray keys) {
		return this.parent.getContentMultipleCommands( keys );
	}
}
