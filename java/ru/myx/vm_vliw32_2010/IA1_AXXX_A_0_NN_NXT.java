/**
 *
 */
package ru.myx.vm_vliw32_2010;


import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ResultHandler;

/** @author myx */
final class IA1_AXXX_A_0_NN_NXT extends InstructionIA1I {

	private final VOFmtA1 operation;
	
	private final ModifierArgument modifierA;
	
	IA1_AXXX_A_0_NN_NXT(
			final VOFmtA1 operation, //
			final ModifierArgument modifierFilterA) {

		this.operation = operation;
		this.modifierA = modifierFilterA;
	}
	
	@Override
	public final ExecStateCode execCall(final ExecProcess process) {

		return this.operation.execute(process, this.modifierA.argumentRead(process), 0, ResultHandler.FA_BNN_NXT);
	}
	
	/** @return constant */
	@Override
	public final int getConstant() {

		return 0;
	}
	
	@Override
	public ModifierArgument getModifierA() {

		return this.modifierA;
	}
	
	@Override
	public int getOperandCount() {

		return this.operation.getStackInputCount(0) + this.modifierA.argumentStackRead();
	}
	
	/** @return operation */
	@Override
	public final VOFmtA1 getOperation() {

		return this.operation;
	}
	
	@Override
	public int getResultCount() {

		return 0 + this.modifierA.argumentStackWrite();
	}
	
	@Override
	public ResultHandler getStore() {

		return ResultHandler.FA_BNN_NXT;
	}
}
