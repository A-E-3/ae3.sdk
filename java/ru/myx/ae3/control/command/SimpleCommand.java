/*
 * Created on 25.04.2006
 */
package ru.myx.ae3.control.command;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseFunction;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.control.AbstractBasic;
import ru.myx.ae3.exec.ExecCallable;
import ru.myx.ae3.exec.ExecCallableWrapper;

/** @author myx */
public class SimpleCommand extends AbstractBasic<SimpleCommand> implements ControlCommand<SimpleCommand>, ExecCallableWrapper {
	
	/**
	 *
	 */
	public SimpleCommand() {
		//
	}

	/** @param name
	 * @param title */
	public SimpleCommand(final String name, final Object title) {
		this.setAttributeIntern("id", name);
		this.setAttributeIntern("title", Base.forUnknown(title));
		this.recalculate();
	}

	@Override
	public BaseObject baseConstructPrototype() {
		
		return null;
	}

	@Override
	public boolean baseHasInstance(final BaseObject value) {
		
		return false;
	}

	// TODO: try returning 'FunctionReturnUndefined'?
	@Override
	public ExecCallable function() {
		
		final BaseFunction function = this.getAttributes().baseGet("function", BaseObject.UNDEFINED).baseCall();
		return function == null
			? null
			: function;
	}

	@Override
	public String toString() {
		
		return "[object " + this.baseClass() + "(" + this.getKey() + ")]";
	}
}
