/**
 *
 */
package ru.myx.vm_vliw32_2010;

import static ru.myx.ae3.exec.ModifierArguments.AA0RB;


import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ResultHandler;

/** @author myx */
final class IA1_AXXX_R_0_NN_NXT extends InstructionIA1I {

	private final VOFmtA1 operation;
	
	IA1_AXXX_R_0_NN_NXT(final VOFmtA1 operation) {

		this.operation = operation;
	}
	
	@Override
	public final ExecStateCode execCall(final ExecProcess process) {

		return this.operation.execute(process, process.ra0RB, 0, ResultHandler.FA_BNN_NXT);
	}
	
	@Override
	public final int getConstant() {

		return 0;
	}
	
	@Override
	public ModifierArgument getModifierA() {

		return AA0RB;
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
	public ResultHandler getStore() {

		return ResultHandler.FA_BNN_NXT;
	}
}
