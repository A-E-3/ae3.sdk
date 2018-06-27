package ru.myx.ae3.console.shell;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseArray;
import ru.myx.ae3.base.BaseFunction;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BaseString;
import ru.myx.ae3.eval.Evaluate;
import ru.myx.ae3.exec.Exec;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;
import ru.myx.ae3.exec.ResultHandler;

final class ShellJsCommand extends AbstractShellCommand {
	
	private final String name;
	
	private final String reference;
	
	private final BaseString<?> referenceBase;
	
	ShellJsCommand(final String name, final String reference) {
		System.out.println(">>>>>> shell js command: reference: " + reference);
		
		this.name = name;
		this.reference = reference;
		this.referenceBase = Base.forString(reference);
	}
	
	@Override
	public ShellJsCommand clone() {
		
		return this;
	}
	
	@Override
	public ExecStateCode execCallImpl(final ExecProcess ctx) {
		
		ctx.contextExecFDEBUG("shell to require bridge, command: " + this.name);
		
		/** Needs to be detached to be still actual when used */
		final BaseArray arguments = ctx.contextGetArguments();

		final BaseObject object;
		{
			final BaseFunction require = ctx.baseGet("require", BaseObject.UNDEFINED).baseCall();
			if (require == null) {
				return ctx.vmRaise("'require' facility is not present");
			}

			// ctx is this
			object = require.callNJ1(ctx, this.referenceBase);
		}
		{
			if (object == null || object == BaseObject.UNDEFINED) {
				return ctx.vmRaise("'" + this.reference + "' library is not found");
			}
			final BaseFunction function = object.baseGet("run", BaseObject.UNDEFINED).baseCall();
			return function != null
				? function.execCallPrepare(ctx, object, ResultHandler.FA_BNN_NXT, false, arguments)
				: ctx.vmRaise("not 'run' method");
		}
	}
	
	@Override
	public String getDescription() {
		
		final ExecProcess sub = Exec.createProcess(null, "shell to require bridge, command: " + this.name);
		sub.baseDefine("reference", this.reference);
		
		try {
			final BaseObject result = Evaluate.evaluateObject("require(reference).description", sub, null);
			return result == BaseObject.UNDEFINED || result == BaseObject.NULL || result == BaseObject.FALSE
				? null
				: result.baseToJavaString();
		} catch (final Throwable t) {
			// Report.exception( "SHELL", "While running: " + this.name +
			// ", method: getDescription", t );
			return "ERROR: " + t.getMessage();
		}
	}
	
	@Override
	public String getName() {
		
		return this.name;
	}
	
}
