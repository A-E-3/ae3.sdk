/**
 *
 */
package ru.myx.vm_vliw32_2010;


import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;
import ru.myx.ae3.exec.Instruction;
import ru.myx.ae3.exec.InstructionEditable;
import ru.myx.ae3.exec.InstructionPlaceholder;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ResultHandler;

/** @author myx */
final class InstructionIA3E extends InstructionIA3A implements InstructionEditable {
	
	private final OperationA3X operation;
	
	private final ModifierArgument modifierA;
	
	private final ModifierArgument modifierB;
	
	private final ModifierArgument modifierC;
	
	private int constant;
	
	private final ResultHandler store;
	
	private Instruction finished;
	
	/** @param operation
	 * @param modifierA
	 * @param modifierB
	 * @param modifierC
	 * @param constant
	 * @param store
	 * @param state */
	InstructionIA3E(
			final OperationA3X operation,
			final ModifierArgument modifierA,
			final ModifierArgument modifierB,
			final ModifierArgument modifierC,
			final int constant,
			final ResultHandler store) {
		
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
	
	/** @return constant */
	@Override
	public final int getConstant() {
		
		return this.constant;
	}
	
	@Override
	public Instruction getFinalIfReady() {
		
		return this.finished;
	}
	
	@Override
	public ModifierArgument getModifierA() {
		
		return this.modifierA;
	}
	
	@Override
	public ModifierArgument getModifierB() {
		
		return this.modifierB;
	}
	
	@Override
	public ModifierArgument getModifierC() {
		
		return this.modifierC;
	}
	
	@Override
	public final int getOperandCount() {
		
		return this.operation.getStackInputCount(this.constant) + this.modifierA.argumentStackRead() + this.modifierB.argumentStackRead() + this.modifierC.argumentStackRead();
	}
	
	@Override
	public final OperationA3X getOperation() {
		
		return this.operation;
	}
	
	@Override
	public final int getResultCount() {
		
		return (this.store.isStackPush()
			? 1
			: 0) + this.modifierA.argumentStackWrite() + this.modifierB.argumentStackWrite() + this.modifierC.argumentStackWrite();
		
	}
	
	@Override
	public ResultHandler getStore() {
		
		return this.store;
	}

	/** @param constant */
	@Override
	public final InstructionEditable setConstant(final int constant) {
		
		this.constant = constant;
		this.finished = null;
		return this;
	}
	
	@Override
	public Instruction setFinished() {
		
		return this.finished = this.operation.instruction(this.modifierA, this.modifierB, this.modifierC, this.constant, this.store);
	}
	
	@Override
	public InstructionPlaceholder setInstruction(final Instruction instruction) {
		
		this.finished = instruction;
		return this;
	}
	
}
