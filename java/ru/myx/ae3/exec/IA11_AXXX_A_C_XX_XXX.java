/**
 *
 */
package ru.myx.ae3.exec;

import com.sun.istack.internal.NotNull;

/**
 * @author myx
 *
 */
final class IA11_AXXX_A_C_XX_XXX extends InstructionA11I {
	
	
	private final int constant;
	
	private final ModifierArgument modifierA;
	
	private final OperationA11 operation;
	
	private final ResultHandler store;
	
	IA11_AXXX_A_C_XX_XXX(final OperationA11 operation, final ModifierArgument modifierA, final int constant, final ResultHandler store) {
		if (constant > InstructionA11.CNST_MAX || constant < InstructionA11.CNST_MIN) {
			throw new IllegalArgumentException("Constant out of range, value=" + constant + ", max=" + InstructionA11.CNST_MAX + ", min=" + InstructionA11.CNST_MIN);
		}
		this.operation = operation;
		this.modifierA = modifierA;
		this.constant = constant;
		this.store = store;
	}
	
	@Override
	public final boolean equals(final Object o) {
		
		
		if (o == this) {
			return true;
		}
		if (o instanceof IA11_AXXX_A_C_XX_XXX) {
			final IA11_AXXX_A_C_XX_XXX x = (IA11_AXXX_A_C_XX_XXX) o;
			return this.operation == x.operation && this.constant == x.constant && this.store == x.store && this.modifierA == x.modifierA;
		}
		return false;
	}
	
	@Override
	public final ExecStateCode execCall(final ExecProcess process) throws Exception {
		
		
		return this.operation.execute(process, this.modifierA.argumentRead(process), this.constant, this.store);
	}
	
	@Override
	public int getConstant() {
		
		
		return this.constant;
	}
	
	@Override
	public int getOperandCount() {
		
		
		return this.operation.getStackInputCount(this.constant) + this.modifierA.argumentStackRead();
	}
	
	@Override
	public final OperationA11 getOperation() {
		
		
		return this.operation;
	}
	
	@Override
	public int getResultCount() {
		
		
		return (this.store.isStackPush()
			? 1
			: 0) + this.modifierA.argumentStackWrite();
	}
	
	@Override
	public final int hashCode() {
		
		
		return this.operation.hashCode() //
				^ this.constant ^ this.store.hashCode() ^ this.modifierA.hashCode();
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
}
