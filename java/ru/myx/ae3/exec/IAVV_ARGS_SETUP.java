/**
 *
 */
package ru.myx.ae3.exec;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BasePrimitiveString;
import ru.myx.ae3.help.Text;

/** @author myx */
public final class IAVV_ARGS_SETUP implements Instruction {

	private final BasePrimitiveString calleeName;

	private final BasePrimitiveString[] arguments;

	/** @param calleeName
	 *            (NULL is unknown or hidden)
	 * @param arguments */
	public IAVV_ARGS_SETUP(final String calleeName, final BasePrimitiveString[] arguments) {
		assert arguments != null : "Use CSCOPE instruction for NULL arguments";
		assert calleeName != null || arguments.length > 0 : "Use CSCOPE instruction for zero arguments and no calleeName";
		this.calleeName = calleeName == null
			? null
			: Base.forString(calleeName);
		this.arguments = arguments;
	}

	/** @param calleeName
	 *            (NULL is unknown or hidden)
	 * @param arguments */
	public IAVV_ARGS_SETUP(final String calleeName, final String[] arguments) {
		assert arguments != null : "Use CSCOPE instruction for NULL arguments";
		assert calleeName != null || arguments.length > 0 : "Use CSCOPE instruction for zero arguments and no calleeName";
		this.calleeName = calleeName == null
			? null
			: Base.forString(calleeName);
		this.arguments = new BasePrimitiveString[arguments.length];
		for (int i = arguments.length - 1; i >= 0; --i) {
			this.arguments[i] = Base.forString(arguments[i]);
		}
	}

	@Override
	public final ExecStateCode execCall(final ExecProcess ctx) {

		ctx.vmScopeDeriveLocals();
		ctx.contextExecFARGS(this.calleeName, this.arguments);
		return null;
	}

	@Override
	public final int getOperandCount() {

		return 0;
	}

	@Override
	public final int getResultCount() {

		return 0;
	}

	@Override
	public final String toCode() {

		return "ARGS_SETUP\t0>" + this.arguments.length + "\tC  ->D\tCONST(" + (this.calleeName == null
			? ""
			: this.calleeName + ", ") + "'" + Text.join(this.arguments, "','") + "');";
	}

}
