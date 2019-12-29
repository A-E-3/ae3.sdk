/**
 *
 */
package ru.myx.vm_vliw32_2010;


import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ResultHandler;

/** @author myx */
class IA1_AXXX_A_C_XX_XXX extends InstructionIA1I {

	private final int constant;
	
	private final ModifierArgument modifierA;
	
	private final VOFmtA1 operation;
	
	private final ResultHandler store;
	
	IA1_AXXX_A_C_XX_XXX(final VOFmtA1 operation, final ModifierArgument modifierA, final int constant, final ResultHandler store) {

		if (constant > operation.getConstantMaxValue() || constant < operation.getConstantMinValue()) {
			throw new IllegalArgumentException(
					"Constant out of range, value=" + constant + ", max=" + operation.getConstantMaxValue() + ", min=" + operation.getConstantMinValue());
		}
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
	public int getConstant() {

		return this.constant;
	}
	
	@Override
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
}
