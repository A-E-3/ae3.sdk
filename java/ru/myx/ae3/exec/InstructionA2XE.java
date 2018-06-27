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
final class InstructionA2XE extends InstructionA2X implements InstructionEditable {
	
	
	private final OperationA2X operation;

	private final ModifierArgument modifierA;

	private final ModifierArgument modifierB;

	private int constant;

	private final ResultHandler store;

	InstructionA2XE(final OperationA2X operation, final ModifierArgument modifierA, final ModifierArgument modifierB, final int constant, final ResultHandler store) {
		if (constant > InstructionA2X.CNST_MAX || constant < InstructionA2X.CNST_MIN) {
			throw new IllegalArgumentException("Constant out of range, value=" + constant + ", max=" + InstructionA2X.CNST_MAX + ", min=" + InstructionA2X.CNST_MIN);
		}
		this.operation = operation;
		this.modifierA = modifierA;
		this.modifierB = modifierB;
		this.constant = constant;
		this.store = store;
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
		
		
		if (constant > InstructionA2X.CNST_MAX || constant < InstructionA2X.CNST_MIN) {
			throw new IllegalArgumentException("Constant out of range, value=" + constant + ", max=" + InstructionA2X.CNST_MAX + ", min=" + InstructionA2X.CNST_MIN);
		}
		this.constant = constant;
		this.finished = null;
		return this;
	}
	@Override
	public final ExecStateCode execCall(final ExecProcess process) throws Exception {
		
		
		final BaseObject argumentB = this.modifierB.argumentRead(process);
		final BaseObject argumentA = this.modifierA.argumentRead(process);
		return this.operation.execute(process, argumentA, argumentB, this.constant, this.store);
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

	private Instruction finished;

	@Override
	public final Instruction setFinished() {
		
		
		return this.finished = this.operation.instruction(this.modifierA, this.modifierB, this.constant, this.store);
	}
	
	@Override
	public InstructionPlaceholder setInstruction(final Instruction instruction) {
		
		
		this.finished = instruction;
		return this;
	}

	@Override
	public final Instruction getFinalIfReady() {
		
		
		return this.finished;
	}
}
