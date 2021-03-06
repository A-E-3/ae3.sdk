/**
 *
 */
package ru.myx.vm_vliw32_2010;


import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BasePrimitiveString;
import ru.myx.ae3.eval.parse.ParseConstants;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.OperationsA2X;
import ru.myx.ae3.exec.ResultHandler;

/** @author myx */
class IA2_VFSTORE_SB_X_NN_NXT extends InstructionIA2A {

	private final BasePrimitiveString argumentA;
	private final ModifierArgument modifierB;
	
	IA2_VFSTORE_SB_X_NN_NXT(final BasePrimitiveString argumentA, final ModifierArgument modifierB) {

		this.argumentA = argumentA;
		this.modifierB = modifierB;
	}
	
	@Override
	public final ExecStateCode execCall(final ExecProcess ctx) throws Exception {

		final BaseObject argumentB = ExecProcess.vmEnsureDetached(ctx, this.modifierB.argumentRead(ctx));
		
		ctx.contextSetMutableBinding(this.argumentA, argumentB, false);
		ctx.ra0RB = argumentB;
		return null;
	}
	
	@Override
	public ModifierArgument getModifierA() {

		return ParseConstants.getConstantValue(this.argumentA).toConstantModifier();
	}
	
	@Override
	public ModifierArgument getModifierB() {

		return this.modifierB;
	}
	
	@Override
	public final int getOperandCount() {

		return this.getOperation().getStackInputCount(0) + this.modifierB.argumentStackRead();
	}
	
	@Override
	public final OperationA2X getOperation() {

		return OperationsA2X.XFSTORE_D;
	}
	
	@Override
	public int getResultCount() {

		return this.modifierB.argumentStackWrite();
	}
	
	@Override
	public ResultHandler getStore() {

		return ResultHandler.FA_BNN_NXT;
	}
	
	@Override
	public int hashCodeModifierA() {

		return this.argumentA.hashCode();
	}
	
}
