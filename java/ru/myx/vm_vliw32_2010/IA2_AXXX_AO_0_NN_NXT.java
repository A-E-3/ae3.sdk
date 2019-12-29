/**
 *
 */
package ru.myx.vm_vliw32_2010;


import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.eval.parse.ParseConstants;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ResultHandler;

/** @author myx */
final class IA2_AXXX_AO_0_NN_NXT extends IA2_AAAA_XX_X_NN_NXT {

	private final OperationA2X operation;
	
	private final ModifierArgument modifierA;
	
	private final BaseObject argumentB;
	
	IA2_AXXX_AO_0_NN_NXT(final OperationA2X operation, final ModifierArgument modifierA, final BaseObject argumentB) {

		this.operation = operation;
		this.modifierA = modifierA;
		this.argumentB = argumentB;
	}
	
	@Override
	public final ExecStateCode execCall(final ExecProcess process) throws Exception {

		final BaseObject argumentA = this.modifierA.argumentRead(process);
		return this.operation.execute(process, argumentA, this.argumentB, 0, ResultHandler.FA_BNN_NXT);
	}
	
	@Override
	public final int getConstant() {

		return 0;
	}
	
	@Override
	public ModifierArgument getModifierA() {

		return this.modifierA;
	}
	
	@Override
	public ModifierArgument getModifierB() {

		return ParseConstants.getConstantValue(this.argumentB).toConstantModifier();
	}
	
	@Override
	public final int getOperandCount() {

		return this.getOperation().getStackInputCount(0) + this.modifierA.argumentStackRead() + 0;
	}
	
	@Override
	public final OperationA2X getOperation() {

		return this.operation;
	}
	
	@Override
	public int getResultCount() {

		return this.modifierA.argumentStackWrite() + 0;
	}
	
	@Override
	public int hashCodeModifierB() {

		return this.argumentB.hashCode();
	}
	
}
