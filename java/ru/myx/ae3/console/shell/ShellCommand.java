/**
 *
 */
package ru.myx.ae3.console.shell;

import ru.myx.ae3.base.BaseFunction;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.reflect.ReflectionHidden;

/**
 * @author myx
 *
 *         TODO: extends BaseFunction<ShellCommand>
 *
 */
public interface ShellCommand extends BaseFunction {

	/**
	 * @return description
	 */
	public String getDescription();

	/**
	 * @return name
	 */
	public String getName();

	@Override
	@ReflectionHidden
	default BaseObject execScope() {

		/**
		 * executes in real current scope
		 */
		return ExecProcess.GLOBAL;
	}

	/**
	 *
	 * @return
	 */
	ShellCommand clone();

	/**
	 * @return
	 */
	default String getManual() {

		return null;
	}
}
