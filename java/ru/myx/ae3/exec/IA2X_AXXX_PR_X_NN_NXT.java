/**
 * 
 */
package ru.myx.ae3.exec;

import static ru.myx.ae3.exec.ModifierArguments.AA0RB;
import static ru.myx.ae3.exec.ModifierArguments.AE21POP;

import com.sun.istack.internal.NotNull;

/**
 * @author myx
 * 
 */
final class IA2X_AXXX_PR_X_NN_NXT extends InstructionA2XI {
	
	
	/**
	 * 
	 */
	private final OperationA2X operation;
	
	private final int constant;
	
	/**
	 * @param operation
	 * @param constant
	 */
	IA2X_AXXX_PR_X_NN_NXT(final OperationA2X operation, final int constant) {
		this.operation = operation;
		this.constant = constant;
	}
	
	@Override
	public final boolean equals(final Object o) {
		
		
		if (o == this) {
			return true;
		}
		if (o instanceof IA2X_AXXX_PR_X_NN_NXT) {
			final IA2X_AXXX_PR_X_NN_NXT x = (IA2X_AXXX_PR_X_NN_NXT) o;
			return this.operation == x.operation && this.constant == x.constant;
		}
		return false;
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
	public boolean isStackPush() {
		
		
		return false;
	}
	
	@Override
	@NotNull
	public ResultHandler getStore() {
		
		
		return ResultHandler.FA_BNN_NXT;
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
}
