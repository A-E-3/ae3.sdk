/**
 *
 */
package ru.myx.vm_vliw32_2010;

import com.sun.istack.internal.NotNull;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.eval.parse.ParseConstants;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ResultHandler;

/** @author myx */
final class IA1_AXXX_O_0_NN_NXT extends InstructionIA1I {
	
	private final VOFmtA1 operation;

	private final BaseObject argumentA;

	IA1_AXXX_O_0_NN_NXT(
			final VOFmtA1 operation, //
			final BaseObject argumentA) {
		
		this.operation = operation;
		this.argumentA = argumentA;
	}

	@Override
	public final ExecStateCode execCall(final ExecProcess process) {
		
		return this.operation.execute(process, this.argumentA, 0, ResultHandler.FA_BNN_NXT);
	}

	/** @return constant */
	@Override
	public final int getConstant() {
		
		return 0;
	}

	@Override
	@NotNull
	public ModifierArgument getModifierA() {
		
		return ParseConstants.getConstantValue(this.argumentA).toConstantModifier();
	}

	@Override
	public int getOperandCount() {
		
		return this.operation.getStackInputCount(0);
	}

	/** @return operation */
	@Override
	public final VOFmtA1 getOperation() {
		
		return this.operation;
	}

	@Override
	public int getResultCount() {
		
		return 0;
	}

	@Override
	@NotNull
	public ResultHandler getStore() {
		
		return ResultHandler.FA_BNN_NXT;
	}
}
