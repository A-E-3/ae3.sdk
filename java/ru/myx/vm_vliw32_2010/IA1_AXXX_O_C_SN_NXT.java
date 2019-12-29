/**
 *
 */
package ru.myx.vm_vliw32_2010;


import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.eval.parse.ParseConstants;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ResultHandler;

/** @author myx */
class IA1_AXXX_O_C_SN_NXT extends InstructionIA1I {

	private final VOFmtA1 operation;

	private final BaseObject argumentA;

	private final int constant;

	IA1_AXXX_O_C_SN_NXT(final VOFmtA1 operation, final BaseObject argumentA, final int constant) {

		if (constant > operation.getConstantMaxValue() || constant < operation.getConstantMinValue()) {
			throw new IllegalArgumentException(
					"Constant out of range, value=" + constant + ", max=" + operation.getConstantMaxValue() + ", min=" + operation.getConstantMinValue());
		}
		this.operation = operation.execStackResult();
		this.argumentA = argumentA;
		this.constant = constant;
	}

	@Override
	public final ExecStateCode execCall(final ExecProcess process) throws Exception {

		return this.operation.execute(process, this.argumentA, this.constant, ResultHandler.FB_BSN_NXT);
	}

	@Override
	public int getConstant() {

		return this.constant;
	}

	@Override
	public ModifierArgument getModifierA() {

		return ParseConstants.getConstantValue(this.argumentA).toConstantModifier();
	}

	@Override
	public int getOperandCount() {

		return this.operation.getStackInputCount(this.constant);
	}

	@Override
	public final VOFmtA1 getOperation() {

		return this.operation;
	}

	@Override
	public int getResultCount() {

		return 1;
	}

	@Override
	public ResultHandler getStore() {

		return ResultHandler.FB_BSN_NXT;
	}
}
