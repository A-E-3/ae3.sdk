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
final class IA10_AXXX_R_C_NN_NXT extends InstructionA10I {
	
	
	private final OperationA10 operation;
	
	private final int constant;
	
	IA10_AXXX_R_C_NN_NXT(final OperationA10 operation, final int constant) {
		if (constant > InstructionA10.CNST_MAX || constant < InstructionA10.CNST_MIN) {
			throw new IllegalArgumentException("Constant out of range, value=" + constant + ", max=" + InstructionA10.CNST_MAX + ", min=" + InstructionA10.CNST_MIN);
		}
		this.operation = operation;
		this.constant = constant;
	}
	
	@Override
	public final boolean equals(final Object o) {
		
		
		if (o == this) {
			return true;
		}
		if (o instanceof IA10_AXXX_R_C_NN_NXT) {
			final IA10_AXXX_R_C_NN_NXT x = (IA10_AXXX_R_C_NN_NXT) o;
			return this.operation == x.operation && this.constant == x.constant;
		}
		return false;
	}
	
	@Override
	public final ExecStateCode execCall(final ExecProcess process) throws Exception {
		
		
		return this.operation.execute(process, process.ra0RB, this.constant, ResultHandler.FA_BNN_NXT);
	}
	
	@Override
	public int getConstant() {
		
		
		return this.constant;
	}
	
	@Override
	public int getOperandCount() {
		
		
		return this.operation.getStackInputCount(this.constant);
	}
	
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
		
		
		return this.operation.hashCode() //
				^ this.constant;
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
