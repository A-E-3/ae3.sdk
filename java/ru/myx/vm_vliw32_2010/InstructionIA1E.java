/**
 *
 */
package ru.myx.vm_vliw32_2010;

import com.sun.istack.internal.NotNull;

import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;
import ru.myx.ae3.exec.Instruction;
import ru.myx.ae3.exec.InstructionEditable;
import ru.myx.ae3.exec.InstructionPlaceholder;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ResultHandler;

/** @author myx */
final class InstructionIA1E extends InstructionIA1A implements InstructionEditable {
	
	private final VOFmtA1 operation;

	private final ModifierArgument modifierA;

	private final ResultHandler store;

	private int constant;

	private Instruction finished;

	InstructionIA1E(final VOFmtA1 operation, final ModifierArgument modifierA, final int constant, final ResultHandler store) {
		
		this.operation = operation;
		this.modifierA = modifierA;
		this.constant = constant;
		this.store = store;
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
	public final VOFmtA1 getOperation() {
		
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
	public boolean isRelativeAddressInConstant() {
		
		return this.operation.isRelativeAddressInConstant();
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
		
		return this.finished = this.operation.instruction(this.modifierA, this.constant, this.store);
	}

	@Override
	public InstructionPlaceholder setInstruction(final Instruction instruction) {
		
		this.finished = instruction;
		return this;
	}
}
