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
final class IA3X_AXXX_ABC_C_XX_XXX extends InstructionA3XI {
	
	
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
	
	private final ResultHandler store;
	
	/**
	 * @param operation
	 * @param modifierA
	 * @param modifierB
	 * @param modifierC
	 * @param constant
	 * @param store
	 * @param state
	 */
	IA3X_AXXX_ABC_C_XX_XXX(
			final OperationA3X operation,
			final ModifierArgument modifierA,
			final ModifierArgument modifierB,
			final ModifierArgument modifierC,
			final int constant,
			final ResultHandler store) {
		if (constant > InstructionA3X.CNST_MAX || constant < InstructionA3X.CNST_MIN) {
			throw new IllegalArgumentException("Constant out of range, value=" + constant + ", max=" + InstructionA3X.CNST_MAX + ", min=" + InstructionA3X.CNST_MIN);
		}
		this.operation = operation;
		this.modifierA = modifierA;
		this.modifierB = modifierB;
		this.modifierC = modifierC;
		this.constant = constant;
		this.store = store;
	}
	
	@Override
	public final ExecStateCode execCall(final ExecProcess process) throws Exception {
		
		
		final BaseObject argumentC = this.modifierC.argumentRead(process);
		final BaseObject argumentB = this.modifierB.argumentRead(process);
		final BaseObject argumentA = this.modifierA.argumentRead(process);
		return this.operation.execute(process, argumentA, argumentB, argumentC, this.constant, this.store);
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
	
	/**
	 * @return operation
	 */
	@Override
	public final OperationA3X getOperation() {
		
		
		return this.operation;
	}
	
	@Override
	public int getResultCount() {
		
		
		return (this.store.isStackPush()
			? 1
			: 0) + this.modifierA.argumentStackWrite() + this.modifierB.argumentStackWrite() + this.modifierC.argumentStackWrite();
		
	}
	
	@Override
	public boolean isStackPush() {
		
		
		return this.store.isStackPush();
	}
	
	@Override
	public ResultHandler getStore() {
		
		
		return this.store;
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
