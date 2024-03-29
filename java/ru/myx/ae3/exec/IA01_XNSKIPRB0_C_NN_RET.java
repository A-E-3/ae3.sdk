/**
 *
 */
package ru.myx.ae3.exec;

import ru.myx.vm_vliw32_2010.OperationA01;

/** @author myx */
final class IA01_XNSKIPRB0_C_NN_RET extends IA01_AAAA_C_NN_XXX {

	IA01_XNSKIPRB0_C_NN_RET(final int constant) {

		super(constant);
	}
	
	@Override
	public final ExecStateCode execCall(final ExecProcess ctx) {

		if (ctx.ra0RB.baseValue() == null) {
			ctx.ri08IP += this.constant;
			/** return NULL - no VLIW command parts to skip! */
			return null;
		}
		return EFC_PNN_RET.INSTANCE.execReturn(ctx);
	}
	
	@Override
	public final int getOperandCount() {

		return 0;
	}
	
	@Override
	public final OperationA01 getOperation() {

		return OperationsA01.XNSKIPRB0_P;
	}
	
	@Override
	public ResultHandler getStore() {

		return ResultHandler.FC_PNN_RET;
	}
	
}
