/**
 *
 */
package ru.myx.vm_vliw32_2010;

import com.sun.istack.internal.NotNull;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ResultHandler;

/** @author myx */
final class IA2_AXXX_AB_C_XX_XXX extends InstructionIA2I {

	private final int constant;
	
	private final ModifierArgument modifierA;
	
	private final ModifierArgument modifierB;
	
	private final OperationA2X operation;
	
	private final ResultHandler store;
	
	IA2_AXXX_AB_C_XX_XXX(
			final OperationA2X operation,
			final ModifierArgument modifierFilterA,
			final ModifierArgument modifierFilterB,
			final int constant,
			final ResultHandler store) {

		if (constant > VIFmtA21.CNST_MAX || constant < VIFmtA21.CNST_MIN) {
			throw new IllegalArgumentException("Constant out of range, value=" + constant + ", max=" + VIFmtA21.CNST_MAX + ", min=" + VIFmtA21.CNST_MIN);
		}
		this.operation = operation;
		this.modifierA = modifierFilterA;
		this.modifierB = modifierFilterB;
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
}
