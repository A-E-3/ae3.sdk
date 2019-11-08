/**
 *
 */
package ru.myx.ae3.exec;

import com.sun.istack.internal.NotNull;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.eval.parse.ParseConstants;
import ru.myx.vm_vliw32_2010.InstructionIA1I;
import ru.myx.vm_vliw32_2010.OperationA10;

/** @author myx */
final class IA10_XLOAD_P_O_0_NN_NXT extends InstructionIA1I {

	private final BaseObject argumentA;
	
	IA10_XLOAD_P_O_0_NN_NXT(final BaseObject argumentA) {

		this.argumentA = argumentA;
	}
	
	@Override
	public final ExecStateCode execCall(final ExecProcess ctx) {

		ctx.ra0RB = this.argumentA;
		return null;
	}
	
	/** @return constant */
	@Override
	public final int getConstant() {

		return 0;
	}
	
	@Override
	@NotNull
	public ModifierArgument getModifierA() {

		return ParseConstants.getConstantValue(this.argumentA).toConstantModifier();
	}
	
	@Override
	public int getOperandCount() {

		return 0;
	}
	
	/** @return operation */
	@Override
	public final OperationA10 getOperation() {

		return OperationsA10.XFLOAD_P;
	}
	
	@Override
	public int getResultCount() {

		return 0;
	}
	
	@Override
	@NotNull
	public ResultHandler getStore() {

		return ResultHandler.FA_BNN_NXT;
	}
}
