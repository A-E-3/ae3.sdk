/**
 *
 */
package ru.myx.ae3.exec;

import com.sun.istack.internal.NotNull;

/**
 * @author myx
 *
 */
final class InstructionA10E extends InstructionA10 implements InstructionEditable {
	
	
	private final OperationA10 operation;
	
	private final ModifierArgument modifierA;
	
	private final ResultHandler store;
	
	private int constant;
	
	InstructionA10E(final OperationA10 operation, final ModifierArgument modifierA, final int constant, final ResultHandler store) {
		if (constant > InstructionA10.CNST_MAX || constant < InstructionA10.CNST_MIN) {
			throw new IllegalArgumentException("Constant out of range, value=" + constant + ", max=" + InstructionA10.CNST_MAX + ", min=" + InstructionA10.CNST_MIN);
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
	public final int getConstant() {
		
		
		return this.constant;
	}
	
	/**
	 * @param constant
	 */
	@Override
	public final InstructionEditable setConstant(final int constant) {
		
		
		if (constant > InstructionA10.CNST_MAX || constant < InstructionA10.CNST_MIN) {
			throw new IllegalArgumentException("Constant out of range, value=" + constant + ", max=" + InstructionA10.CNST_MAX + ", min=" + InstructionA10.CNST_MIN);
		}
		this.constant = constant;
		this.finished = null;
		return this;
	}
	@Override
	public final ExecStateCode execCall(final ExecProcess process) throws Exception {
		
		
		return this.operation.execute(process, this.modifierA.argumentRead(process), this.constant, this.store);
	}
	
	@Override
	public int getOperandCount() {
		
		
		return this.operation.getStackInputCount(this.constant) + this.modifierA.argumentStackRead();
	}
	
	@Override
	public final OperationA10 getOperation() {
		
		
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
	public boolean isRelativeAddressInConstant() {
		
		
		return this.operation.isRelativeAddressInConstant();
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
	
	private Instruction finished;
	
	@Override
	public InstructionPlaceholder setInstruction(final Instruction instruction) {
		
		
		this.finished = instruction;
		return this;
	}
	
	@Override
	public final Instruction setFinished() {
		
		
		return this.finished = this.operation.instruction(this.modifierA, this.constant, this.store);
	}
	
	@Override
	public final Instruction getFinalIfReady() {
		
		
		return this.finished;
	}
}
