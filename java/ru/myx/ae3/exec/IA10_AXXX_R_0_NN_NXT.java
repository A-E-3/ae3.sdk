/**
 * 
 */
package ru.myx.ae3.exec;

import static ru.myx.ae3.exec.ModifierArguments.AA0RB;

import com.sun.istack.internal.NotNull;

/**
 * @author myx
 * 
 */
final class IA10_AXXX_R_0_NN_NXT extends InstructionA10I {
	
	
	private final OperationA10 operation;
	
	IA10_AXXX_R_0_NN_NXT(final OperationA10 operation) {
		this.operation = operation;
	}
	
	@Override
	public final boolean equals(final Object o) {
		
		
		if (o == this) {
			return true;
		}
		if (o instanceof InstructionA2X) {
			final IA10_AXXX_R_0_NN_NXT x = (IA10_AXXX_R_0_NN_NXT) o;
			return this.operation == x.operation;
		}
		return false;
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
	public int getOperandCount() {
		
		
		return this.operation.getStackInputCount(0);
	}
	
	/**
	 * @return operation
	 */
	@Override
	public final OperationA10 getOperation() {
		
		
		return this.operation;
	}
	
	@Override
	public int getResultCount() {
		
		
		return 0;
	}
	
	@Override
	public final int hashCode() {
		
		
		return this.operation.hashCode();
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
		
		
		return AA0RB;
	}
}
