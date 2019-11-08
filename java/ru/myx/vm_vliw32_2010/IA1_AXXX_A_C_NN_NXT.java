/**
 *
 */
package ru.myx.vm_vliw32_2010;

import com.sun.istack.internal.NotNull;

import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ResultHandler;

/** @author myx */
final class IA1_AXXX_A_C_NN_NXT extends InstructionIA1I {
	
	private final VOFmtA1 operation;

	private final ModifierArgument modifierA;

	private final int constant;

	IA1_AXXX_A_C_NN_NXT(final VOFmtA1 operation, final ModifierArgument modifierA, final int constant) {
		
		if (constant > operation.getConstantMaxValue() || constant < operation.getConstantMinValue()) {
			throw new IllegalArgumentException(
					"Constant out of range, value=" + constant + ", max=" + operation.getConstantMaxValue() + ", min=" + operation.getConstantMinValue());
		}
		this.operation = operation;
		this.modifierA = modifierA;
		this.constant = constant;
	}

	@Override
	public final ExecStateCode execCall(final ExecProcess process) throws Exception {
		
		return this.operation.execute(process, this.modifierA.argumentRead(process), this.constant, ResultHandler.FA_BNN_NXT);
	}

	@Override
	public int getConstant() {
		
		return this.constant;
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
		
		return this.modifierA.argumentStackWrite();
	}

	@Override
	@NotNull
	public ResultHandler getStore() {
		
		return ResultHandler.FA_BNN_NXT;
	}
}
