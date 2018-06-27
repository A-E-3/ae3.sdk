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
final class IA2X_AXXX_AB_C_XX_XXX extends InstructionA2XI {
	
	
	private final int constant;
	
	private final ModifierArgument modifierA;
	
	private final ModifierArgument modifierB;
	
	private final OperationA2X operation;
	
	private final ResultHandler store;
	
	IA2X_AXXX_AB_C_XX_XXX(
			final OperationA2X operation,
			final ModifierArgument modifierFilterA,
			final ModifierArgument modifierFilterB,
			final int constant,
			final ResultHandler store) {
		if (constant > InstructionA2X.CNST_MAX || constant < InstructionA2X.CNST_MIN) {
			throw new IllegalArgumentException("Constant out of range, value=" + constant + ", max=" + InstructionA2X.CNST_MAX + ", min=" + InstructionA2X.CNST_MIN);
		}
		this.operation = operation;
		this.modifierA = modifierFilterA;
		this.modifierB = modifierFilterB;
		this.constant = constant;
		this.store = store;
	}
	
	@Override
	public final boolean equals(final Object o) {
		
		
		if (o == this) {
			return true;
		}
		if (o instanceof IA2X_AXXX_AB_C_XX_XXX) {
			final IA2X_AXXX_AB_C_XX_XXX x = (IA2X_AXXX_AB_C_XX_XXX) o;
			return this.operation == x.operation && this.constant == x.constant && this.store == x.store && this.modifierB == x.modifierB && this.modifierA == x.modifierA;
		}
		return false;
	}
	
	@Override
	public final ExecStateCode execCall(final ExecProcess process) throws Exception {
		
		
		final BaseObject argumentB = this.modifierB.argumentRead(process);
		final BaseObject argumentA = this.modifierA.argumentRead(process);
		return this.operation.execute(process, argumentA, argumentB, this.constant, this.store);
	}
	
	@Override
	public final int getConstant() {
		
		
		return this.constant;
	}
	
	@Override
	public int getOperandCount() {
		
		
		return this.operation.getStackInputCount(this.constant) + this.modifierA.argumentStackRead() + this.modifierB.argumentStackRead();
	}
	
	@Override
	public final OperationA2X getOperation() {
		
		
		return this.operation;
	}
	
	@Override
	public int getResultCount() {
		
		
		return (this.store.isStackPush()
			? 1
			: 0) + this.modifierA.argumentStackWrite() + this.modifierB.argumentStackWrite();
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
}
