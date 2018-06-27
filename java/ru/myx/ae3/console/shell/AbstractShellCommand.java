package ru.myx.ae3.console.shell;

import ru.myx.ae3.base.BaseFunctionAbstract;
import ru.myx.ae3.exec.ExecCallableFull;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;

/**
 * @author myx
 *
 */
public abstract class AbstractShellCommand //
		extends
			BaseFunctionAbstract //
		implements
			ExecCallableFull,
			ShellCommand //
{

	@Override
	public String toString() {

		return "[Function ShellCommand('" + this.getName() + "')]";
	}

	@Override
	public abstract AbstractShellCommand clone();

	@Override
	public abstract ExecStateCode execCallImpl(final ExecProcess ctx);

}
