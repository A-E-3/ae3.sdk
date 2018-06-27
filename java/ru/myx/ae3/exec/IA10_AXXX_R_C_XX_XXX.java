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
final class IA10_AXXX_R_C_XX_XXX extends InstructionA10I {
	
	
	private final OperationA10 operation;
	
	private final int constant;
	
	private final ResultHandler store;
	
	IA10_AXXX_R_C_XX_XXX(final OperationA10 operation, final int constant, final ResultHandler store) {
		if (constant > InstructionA10.CNST_MAX || constant < InstructionA10.CNST_MIN) {
			throw new IllegalArgumentException("Constant out of range, value=" + constant + ", max=" + InstructionA10.CNST_MAX + ", min=" + InstructionA10.CNST_MIN);
		}
		this.operation = operation;
		this.constant = constant;
		this.store = store;
	}
	
	@Override
	public final boolean equals(final Object o) {
		
		
		if (o == this) {
			return true;
		}
		if (o instanceof IA10_AXXX_R_C_XX_XXX) {
			final IA10_AXXX_R_C_XX_XXX x = (IA10_AXXX_R_C_XX_XXX) o;
			return this.operation == x.operation && this.constant == x.constant && this.store == x.store;
		}
		return false;
	}
	
	@Override
	public final ExecStateCode execCall(final ExecProcess process) throws Exception {
		
		
		return this.operation.execute(process, process.ra0RB, this.constant, this.store);
	}
	
	@Override
	public int getConstant() {
		
		
		return this.constant;
	}
	
	@Override
	public final int getOperandCount() {
		
		
		return this.operation.getStackInputCount(this.constant);
	}
	
	@Override
	public final OperationA10 getOperation() {
		
		
		return this.operation;
	}
	
	@Override
	public final int getResultCount() {
		
		
		return this.store.isStackPush()
			? 1
			: 0;
	}
	
	@Override
	public final int hashCode() {
		
		
		return this.operation.hashCode() //
				^ this.constant ^ this.store.hashCode();
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
		
		
		return AA0RB;
	}
}
