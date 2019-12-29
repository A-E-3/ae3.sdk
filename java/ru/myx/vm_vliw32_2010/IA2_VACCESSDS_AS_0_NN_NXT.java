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
import ru.myx.ae3.exec.OperationsS2X;
import ru.myx.ae3.exec.ResultHandler;

/** @author myx */
final class IA2_VACCESSDS_AS_0_NN_NXT extends IA2_AAAA_XX_X_NN_NXT {

	private final ModifierArgument modifierA;
	
	private final BasePrimitiveString argumentB;
	
	IA2_VACCESSDS_AS_0_NN_NXT(final ModifierArgument modifierA, final BaseObject argumentB) {

		this.modifierA = modifierA;
		this.argumentB = argumentB.baseToString();
	}
	
	@Override
	public final ExecStateCode execCall(final ExecProcess process) throws Exception {

		final BaseObject argumentA = this.modifierA.argumentRead(process);
		
		return argumentA.vmPropertyRead(process, this.argumentB, BaseObject.UNDEFINED, ResultHandler.FA_BNN_NXT);
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

		return OperationsS2X.VACCESS_DS.getStackInputCount(0) + this.modifierA.argumentStackRead() + 0;
	}
	
	@Override
	public final OperationA2X getOperation() {

		return OperationsS2X.VACCESS_DS;
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
