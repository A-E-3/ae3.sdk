/**
 *
 */
package ru.myx.ae3.exec;


import ru.myx.ae3.base.BasePrimitiveString;
import ru.myx.vm_vliw32_2010.InstructionIA1I;
import ru.myx.vm_vliw32_2010.OperationA10;

/** @author myx */
final class IA10_XLOAD_P_F_0_NN_RET extends InstructionIA1I {
	
	private final BasePrimitiveString argumentA;

	IA10_XLOAD_P_F_0_NN_RET(final BasePrimitiveString argumentA) {
		
		this.argumentA = argumentA;
	}

	@Override
	public final ExecStateCode execCall(final ExecProcess ctx) throws Exception {
		
		/** can't do - doesn't read scope hierarchy as needed ^^^^^ */
		// return process.vmPropertyRead(process, this.argumentA, UNDEFINED,
		// EFC_PNN_RET.INSTANCE);
		
		return EFC_PNN_RET.INSTANCE.execReturn(ctx, ctx.contextGetBindingValue(this.argumentA, false));

	}

	/** @return constant */
	@Override
	public final int getConstant() {
		
		return 0;
	}

	@Override
	public ModifierArgument getModifierA() {
		
		return new ModifierArgumentA32FVIMM(this.argumentA);
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
	public ResultHandler getStore() {
		
		return ResultHandler.FC_PNN_RET;
	}
}
