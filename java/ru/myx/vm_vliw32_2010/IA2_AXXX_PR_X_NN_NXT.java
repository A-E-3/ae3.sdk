/**
 *
 */
package ru.myx.vm_vliw32_2010;

import static ru.myx.ae3.exec.ModifierArguments.AA0RB;
import static ru.myx.ae3.exec.ModifierArguments.AE21POP;

import com.sun.istack.internal.NotNull;

import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ResultHandler;

/** @author myx */
final class IA2_AXXX_PR_X_NN_NXT extends InstructionIA2I {

	/**
	 *
	 */
	private final OperationA2X operation;
	
	private final int constant;
	
	/** @param operation
	 * @param constant */
	IA2_AXXX_PR_X_NN_NXT(final OperationA2X operation, final int constant) {

		this.operation = operation;
		this.constant = constant;
	}
	
	@Override
	public final ExecStateCode execCall(final ExecProcess process) {

		return this.operation.execute(process, process.stackPop(), process.ra0RB, this.constant, ResultHandler.FA_BNN_NXT);
	}
	
	@Override
	public final int getConstant() {

		return this.constant;
	}
	
	@Override
	@NotNull
	public ModifierArgument getModifierA() {

		return AE21POP;
	}
	
	@Override
	@NotNull
	public ModifierArgument getModifierB() {

		return AA0RB;
	}
	
	@Override
	public int getOperandCount() {

		return this.operation.getStackInputCount(this.constant) + 1;
	}
	
	@Override
	public final OperationA2X getOperation() {

		return this.operation;
	}
	
	@Override
	public int getResultCount() {

		return 0 + 0;
	}
	
	@Override
	@NotNull
	public ResultHandler getStore() {

		return ResultHandler.FA_BNN_NXT;
	}
}
