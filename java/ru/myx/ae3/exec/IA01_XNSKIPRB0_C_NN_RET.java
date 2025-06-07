/**
 *
 */
package ru.myx.ae3.exec;

import ru.myx.ae3.base.BaseObject;
import ru.myx.vm_vliw32_2010.OperationA01;

/** @author myx */
final class IA01_XNSKIPRB0_C_NN_RET extends IA01_AAAA_C_NN_XXX {
	
	IA01_XNSKIPRB0_C_NN_RET(final int constant) {
		
		super(constant);
	}

	@Override
	public final ExecStateCode execCall(final ExecProcess ctx) {
		
		final BaseObject candidateValue = ctx.ra0RB;
		final Object replacementValue = candidateValue.baseValue();
		/* strict ?? false */
		if (replacementValue == null) {
			ctx.ri08IP += this.constant;
			/** return NULL - no VLIW command parts to skip! */
			return null;
		}
		/* strict ?? true */
		if (replacementValue == candidateValue) {
			return EFC_PNN_RET.INSTANCE.execReturn(ctx);
		}
		/* implicit reference (References and Promises) */
		if (replacementValue instanceof final BaseObject baseValue) {
			/* reference ?? false */
			if (baseValue.baseValue() == null) {
				ctx.ri08IP += this.constant;
				/** return NULL - no VLIW command parts to skip! */
				return null;
			}
			/* reference ?? true */
			ctx.ra0RB = baseValue;
			return EFC_PNN_RET.INSTANCE.execReturn(ctx);
		}
		/* failover ?? true */
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
