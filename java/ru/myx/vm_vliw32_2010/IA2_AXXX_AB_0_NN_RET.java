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
final class IA2_AXXX_AB_0_NN_RET extends InstructionIA2I {

	private final ModifierArgument modifierA;
	
	private final ModifierArgument modifierB;
	
	private final OperationA2X operation;
	
	IA2_AXXX_AB_0_NN_RET(final OperationA2X operation, final ModifierArgument modifierFilterA, final ModifierArgument modifierFilterB) {

		this.operation = operation;
		this.modifierA = modifierFilterA;
		this.modifierB = modifierFilterB;
	}
	
	@Override
	public final ExecStateCode execCall(final ExecProcess process) throws Exception {

		final BaseObject argumentB = this.modifierB.argumentRead(process);
		final BaseObject argumentA = this.modifierA.argumentRead(process);
		return this.operation.execute(process, argumentA, argumentB, 0, ResultHandler.FC_PNN_RET);
	}
	
	@Override
	public final int getConstant() {

		return 0;
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

		return this.operation.getStackInputCount(0) + this.modifierA.argumentStackRead() + this.modifierB.argumentStackRead();
	}
	
	@Override
	public final OperationA2X getOperation() {

		return this.operation;
	}
	
	@Override
	public int getResultCount() {

		return 0 + this.modifierA.argumentStackWrite() + this.modifierB.argumentStackWrite();
	}
	
	@Override
	public ResultHandler getStore() {

		return ResultHandler.FC_PNN_RET;
	}
}
