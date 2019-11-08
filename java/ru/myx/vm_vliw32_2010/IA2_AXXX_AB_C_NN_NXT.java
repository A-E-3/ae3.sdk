/**
 *
 */
package ru.myx.vm_vliw32_2010;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ResultHandler;

/** @author myx */
class IA2_AXXX_AB_C_NN_NXT extends IA2_AAAA_AB_C_NN_NXT {
	
	private final OperationA2X operation;

	IA2_AXXX_AB_C_NN_NXT(final OperationA2X operation, final ModifierArgument modifierA, final ModifierArgument modifierB, final int constant) {
		
		super(modifierA, modifierB, constant);
		this.operation = operation;
	}

	@Override
	public final ExecStateCode execCall(final ExecProcess process) throws Exception {
		
		final BaseObject argumentB = this.modifierB.argumentRead(process);
		final BaseObject argumentA = this.modifierA.argumentRead(process);
		return this.operation.execute(process, argumentA, argumentB, this.constant, ResultHandler.FA_BNN_NXT);
	}

	@Override
	public final OperationA2X getOperation() {
		
		return this.operation;
	}

	@Override
	public int getResultCount() {
		
		return this.modifierA.argumentStackWrite() + this.modifierB.argumentStackWrite();
	}
}
