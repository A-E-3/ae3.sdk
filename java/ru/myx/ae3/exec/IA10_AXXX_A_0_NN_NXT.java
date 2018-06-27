/**
 * 
 */
package ru.myx.ae3.exec;

import com.sun.istack.internal.NotNull;

/**
 * @author myx
 * 
 */
final class IA10_AXXX_A_0_NN_NXT extends InstructionA10I {
	
	
	private final OperationA10 operation;
	
	private final ModifierArgument modifierA;
	
	IA10_AXXX_A_0_NN_NXT(
			final OperationA10 operation, //
			final ModifierArgument modifierFilterA) {
		this.operation = operation;
		this.modifierA = modifierFilterA;
	}
	
	@Override
	public final boolean equals(final Object o) {
		
		
		if (o == this) {
			return true;
		}
		if (o instanceof InstructionA2X) {
			final IA10_AXXX_A_0_NN_NXT x = (IA10_AXXX_A_0_NN_NXT) o;
			return this.operation == x.operation && this.modifierA == x.modifierA;
		}
		return false;
	}
	
	@Override
	public final ExecStateCode execCall(final ExecProcess process) {
		
		
		return this.operation.execute(process, this.modifierA.argumentRead(process), 0, ResultHandler.FA_BNN_NXT);
	}
	
	/**
	 * @return constant
	 */
	@Override
	public final int getConstant() {
		
		
		return 0;
	}
	
	@Override
	public int getOperandCount() {
		
		
		return this.operation.getStackInputCount(0) + this.modifierA.argumentStackRead();
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
		
		
		return 0 + this.modifierA.argumentStackWrite();
	}
	
	@Override
	public final int hashCode() {
		
		
		return this.operation.hashCode() ^ this.modifierA.hashCode();
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
		
		
		return this.modifierA;
	}
}
