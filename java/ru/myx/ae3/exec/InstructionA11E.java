/**
 *
 */
package ru.myx.ae3.exec;

import com.sun.istack.internal.NotNull;

/**
 * @author myx
 *
 */
final class InstructionA11E extends InstructionA11 implements InstructionEditable {
	
	
	private int constant;
	
	private Instruction finished;
	
	private final ModifierArgument modifierA;
	
	private final OperationA11 operation;
	
	private final ResultHandler store;
	
	InstructionA11E(final OperationA11 operation, final ModifierArgument modifierA, final int constant, final ResultHandler store) {
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
		return false;
	}
	
	@Override
	public final ExecStateCode execCall(final ExecProcess process) throws Exception {
		
		
		return this.operation.execute(process, this.modifierA.argumentRead(process), this.constant, this.store);
	}
	
	@Override
	public final int getConstant() {
		
		
		return this.constant;
	}
	
	@Override
	public final Instruction getFinalIfReady() {
		
		
		return this.finished;
	}
	
	@Override
	@NotNull
	public ModifierArgument getModifierA() {
		
		
		return this.modifierA;
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
	public ResultHandler getStore() {
		
		
		return this.store;
	}
	
	@Override
	public final int hashCode() {
		
		
		return this.operation.hashCode() //
				^ this.constant ^ this.store.hashCode() ^ this.modifierA.hashCode();
	}
	
	@Override
	public boolean isRelativeAddressInConstant() {
		
		
		return this.operation.isRelativeAddressInConstant();
	}
	
	@Override
	public boolean isStackPush() {
		
		
		return this.store.isStackPush();
	}
	
	@Override
	public final InstructionEditable setConstant(final int constant) {
		
		
		if (constant > InstructionA11.CNST_MAX || constant < InstructionA11.CNST_MIN) {
			throw new IllegalArgumentException("Constant out of range, value=" + constant + ", max=" + InstructionA11.CNST_MAX + ", min=" + InstructionA11.CNST_MIN);
		}
		this.constant = constant;
		this.finished = null;
		return this;
	}
	
	@Override
	public InstructionPlaceholder setInstruction(final Instruction instruction) {
		
		
		this.finished = instruction;
		return this;
	}
	
	@Override
	public final Instruction setFinished() {
		
		
		return this.finished = this.operation.instruction(this.modifierA, this.constant, this.store);
	}
}
