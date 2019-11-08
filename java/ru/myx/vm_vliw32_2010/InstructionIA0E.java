/**
 *
 */
package ru.myx.vm_vliw32_2010;

import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;
import ru.myx.ae3.exec.Instruction;
import ru.myx.ae3.exec.InstructionEditable;
import ru.myx.ae3.exec.InstructionPlaceholder;
import ru.myx.ae3.exec.ResultHandler;

/** @author myx */
final class InstructionIA0E extends InstructionIA0A implements InstructionEditable {

	private int constant;

	private final VOFmtA0 operation;

	private final ResultHandler store;

	private Instruction finished;

	InstructionIA0E(final VOFmtA0 operation, final int constant, final ResultHandler store) {

		this.operation = operation;
		this.constant = constant;
		this.store = store;
	}

	@Override
	public final ExecStateCode execCall(final ExecProcess process) throws Exception {

		return this.operation.execute(process, this.constant, this.store);
	}

	@Override
	public final int getConstant() {

		return this.constant;
	}

	@Override
	public Instruction getFinalIfReady() {

		return this.finished;
	}

	@Override
	public final int getOperandCount() {

		return this.operation.getStackInputCount(this.constant);
	}

	@Override
	public final VOFmtA0 getOperation() {

		return this.operation;
	}

	@Override
	public final int getResultCount() {

		return this.store.isStackPush()
			? 1
			: 0;
	}

	@Override
	public ResultHandler getStore() {

		return this.store;
	}

	@Override
	public boolean isRelativeAddressInConstant() {

		return this.operation.isRelativeAddressInConstant();
	}
	
	@Override
	public InstructionEditable setConstant(final int constant) {

		this.constant = constant;
		this.finished = null;
		return this;
	}

	@Override
	public Instruction setFinished() {

		return this.finished = this.operation.instruction(this.constant, this.store);
	}

	@Override
	public InstructionPlaceholder setInstruction(final Instruction instruction) {

		this.finished = instruction;
		return this;
	}
}
