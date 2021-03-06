/**
 *
 */
package ru.myx.ae3.exec;



import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BasePrimitiveString;
import ru.myx.ae3.eval.parse.ParseConstants;
import ru.myx.vm_vliw32_2010.InstructionIA2I;
import ru.myx.vm_vliw32_2010.OperationA2X;

/** @author myx */
final class IA2_VACCESSDS_AS_0_SN_NXT extends InstructionIA2I {

	private final ModifierArgument modifierA;
	
	private final BasePrimitiveString argumentB;
	
	IA2_VACCESSDS_AS_0_SN_NXT(final ModifierArgument modifierA, final BaseObject argumentB) {

		this.modifierA = modifierA;
		this.argumentB = argumentB.baseToString();
	}
	
	@Override
	public final ExecStateCode execCall(final ExecProcess process) throws Exception {

		final BaseObject argumentA = this.modifierA.argumentRead(process);
		
		return argumentA.vmPropertyRead(process, this.argumentB, BaseObject.UNDEFINED, ResultHandler.FB_BSN_NXT);
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

		return this.modifierA.argumentStackWrite() + 1;
	}
	
	@Override
	public ResultHandler getStore() {

		return ResultHandler.FB_BSN_NXT;
	}
	
	@Override
	public int hashCodeModifierB() {

		return this.argumentB.hashCode();
	}
}
