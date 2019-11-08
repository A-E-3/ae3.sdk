/**
 *
 */
package ru.myx.vm_vliw32_2010;

import com.sun.istack.internal.NotNull;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;
import ru.myx.ae3.exec.Instruction;
import ru.myx.ae3.exec.InstructionEditable;
import ru.myx.ae3.exec.InstructionPlaceholder;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ResultHandler;

/** @author myx */
final class InstructionIA2E extends InstructionIA2A implements InstructionEditable {
	
	private final OperationA2X operation;
	
	private final ModifierArgument modifierA;
	
	private final ModifierArgument modifierB;
	
	private int constant;
	
	private final ResultHandler store;
	
	private Instruction finished;
	
	InstructionIA2E(final OperationA2X operation, final ModifierArgument modifierA, final ModifierArgument modifierB, final int constant, final ResultHandler store) {
		
		this.operation = operation;
		this.modifierA = modifierA;
		this.modifierB = modifierB;
		this.constant = constant;
		this.store = store;
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
	public final Instruction getFinalIfReady() {
		
		return this.finished;
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
	
	/** @param constant */
	@Override
	public final InstructionEditable setConstant(final int constant) {
		
		this.constant = constant;
		this.finished = null;
		return this;
	}

	@Override
	public final Instruction setFinished() {
		
		return this.finished = this.operation.instruction(this.modifierA, this.modifierB, this.constant, this.store);
	}
	
	@Override
	public InstructionPlaceholder setInstruction(final Instruction instruction) {
		
		this.finished = instruction;
		return this;
	}
}
