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
final class IA2_AXXX_OB_0_SN_NXT extends InstructionIA2I {
	
	private final BaseObject argumentA;

	private final ModifierArgument modifierB;

	private final OperationA2X operation;

	IA2_AXXX_OB_0_SN_NXT(final OperationA2X operation, final BaseObject argumentA, final ModifierArgument modifierB) {
		
		this.operation = operation.execStackResult();
		this.argumentA = argumentA;
		this.modifierB = modifierB;
	}

	@Override
	public final ExecStateCode execCall(final ExecProcess process) throws Exception {
		
		final BaseObject argumentB = this.modifierB.argumentRead(process);

		return this.operation.execute(process, this.argumentA, argumentB, 0, ResultHandler.FB_BSN_NXT);
	}

	@Override
	public final int getConstant() {
		
		return 0;
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
	public int getOperandCount() {
		
		return this.operation.getStackInputCount(0) + 0 + this.modifierB.argumentStackRead();
	}

	@Override
	public final OperationA2X getOperation() {
		
		return this.operation;
	}

	@Override
	public int getResultCount() {
		
		return 1 + 0 + this.modifierB.argumentStackWrite();
	}

	@Override
	public ResultHandler getStore() {
		
		return ResultHandler.FB_BSN_NXT;
	}

	@Override
	public int hashCodeModifierA() {
		
		return this.argumentA.hashCode();
	}

}
