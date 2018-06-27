/**
 * 
 */
package ru.myx.ae3.exec;

import com.sun.istack.internal.NotNull;

import ru.myx.ae3.base.BaseObject;

/**
 * @author myx
 * 
 */
final class IA3X_AXXX_ABC_C_NN_NXT extends InstructionA3XI {
	
	
	/**
	 * 
	 */
	private final OperationA3X operation;
	
	/**
	 * 
	 */
	private final ModifierArgument modifierA;
	
	/**
	 * 
	 */
	private final ModifierArgument modifierB;
	
	/**
	 * 
	 */
	private final ModifierArgument modifierC;
	
	/**
	 * 
	 */
	private final int constant;
	
	/**
	 * @param operation
	 * @param modifierFilterA
	 * @param modifierFilterB
	 * @param modifierFilterC
	 * @param constant
	 * @param store
	 * @param state
	 */
	IA3X_AXXX_ABC_C_NN_NXT(
			final OperationA3X operation,
			final ModifierArgument modifierFilterA,
			final ModifierArgument modifierFilterB,
			final ModifierArgument modifierFilterC,
			final int constant) {
		if (constant > InstructionA3X.CNST_MAX || constant < InstructionA3X.CNST_MIN) {
			throw new IllegalArgumentException("Constant out of range, value=" + constant + ", max=" + InstructionA3X.CNST_MAX + ", min=" + InstructionA3X.CNST_MIN);
		}
		this.operation = operation;
		this.modifierA = modifierFilterA;
		this.modifierB = modifierFilterB;
		this.modifierC = modifierFilterC;
		this.constant = constant;
	}
	
	@Override
	public final ExecStateCode execCall(final ExecProcess process) throws Exception {
		
		
		final BaseObject argumentC = this.modifierC.argumentRead(process);
		final BaseObject argumentB = this.modifierB.argumentRead(process);
		final BaseObject argumentA = this.modifierA.argumentRead(process);
		return this.operation.execute(process, argumentA, argumentB, argumentC, this.constant, ResultHandler.FA_BNN_NXT);
	}
	
	/**
	 * @return constant
	 */
	@Override
	public final int getConstant() {
		
		
		return this.constant;
	}
	
	@Override
	public int getOperandCount() {
		
		
		return this.operation.getStackInputCount(this.constant) + this.modifierA.argumentStackRead() + this.modifierB.argumentStackRead() + this.modifierC.argumentStackRead();
	}
	
	@Override
	public final OperationA3X getOperation() {
		
		
		return this.operation;
	}
	
	@Override
	public int getResultCount() {
		
		
		return 0 //
				+ this.modifierA.argumentStackWrite() + this.modifierB.argumentStackWrite() + this.modifierC.argumentStackWrite();
		
	}
	
	@Override
	public final boolean isStackPush() {
		
		
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
	
	@Override
	@NotNull
	public ModifierArgument getModifierB() {
		
		
		return this.modifierB;
	}
	
	@Override
	@NotNull
	public ModifierArgument getModifierC() {
		
		
		return this.modifierC;
	}
}
