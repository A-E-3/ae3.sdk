/**
 *
 */
package ru.myx.vm_vliw32_2010;

import static ru.myx.ae3.exec.ModifierArguments.AA0RB;

import com.sun.istack.internal.NotNull;

import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ResultHandler;

/** @author myx */
final class IA1_AXXX_R_C_XX_XXX extends InstructionIA1I {

	private final VOFmtA1 operation;
	
	private final int constant;
	
	private final ResultHandler store;
	
	IA1_AXXX_R_C_XX_XXX(final VOFmtA1 operation, final int constant, final ResultHandler store) {

		if (constant > operation.getConstantMaxValue() || constant < operation.getConstantMinValue()) {
			throw new IllegalArgumentException(
					"Constant out of range, value=" + constant + ", max=" + operation.getConstantMaxValue() + ", min=" + operation.getConstantMinValue());
		}
		this.operation = operation;
		this.constant = constant;
		this.store = store;
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
	@NotNull
	public ModifierArgument getModifierA() {

		return AA0RB;
	}
	
	@Override
	public final int getOperandCount() {

		return this.operation.getStackInputCount(this.constant);
	}
	
	@Override
	public final VOFmtA1 getOperation() {

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
}