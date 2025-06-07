/**
 *
 */
package ru.myx.ae3.exec;

import ru.myx.ae3.base.BaseObject;
import ru.myx.vm_vliw32_2010.InstructionIA1I;
import ru.myx.vm_vliw32_2010.OperationA11;
import ru.myx.vm_vliw32_2010.VIFmtA11;

/** @author myx */
final class IA11_XNSKIP0A_A_C_FU_NN_NXT extends InstructionIA1I {

	private final ModifierArgument modifierA;
	
	private final int constant;
	
	IA11_XNSKIP0A_A_C_FU_NN_NXT(final ModifierArgument modifierA, final int constant) {

		if (constant > VIFmtA11.CNST_MAX || constant < VIFmtA11.CNST_MIN) {
			throw new IllegalArgumentException("Constant out of range, value=" + constant + ", max=" + VIFmtA11.CNST_MAX + ", min=" + VIFmtA11.CNST_MIN);
		}
		this.modifierA = modifierA;
		this.constant = constant;
	}
	
	@Override
	public final ExecStateCode execCall(final ExecProcess ctx) throws Exception {
		
		final BaseObject candidateValue = /* ctx.ra0RB = */ this.modifierA.argumentRead(ctx);
		final Object replacementValue = candidateValue.baseValue();
		/* strict ?? false */
		if (replacementValue == null) {
			ctx.ri08IP += this.constant;
			return null;
		}
		/* strict ?? true */
		if (replacementValue == candidateValue) {
			return null;
		}
		/* implicit reference (References and Promises) */
		if (replacementValue instanceof final BaseObject baseValue) {
			/* reference ?? false */
			if (baseValue.baseValue() == null) {
				ctx.ri08IP += this.constant;
				/** return NULL - no VLIW command parts to skip! */
				// return ExecStateCode.NEXT;
				return null;
			}
			/* reference ?? true */
			// ctx.ra0RB = baseValue;
			return null;
		}
		/* failover ?? true */
		return null;
	}
	
	@Override
	public int getConstant() {

		return this.constant;
	}
	
	@Override
	public ModifierArgument getModifierA() {

		return this.modifierA;
	}
	
	@Override
	public int getOperandCount() {

		return OperationsA11.XNSKIP0A_P.getStackInputCount(this.constant) + this.modifierA.argumentStackRead();
	}
	
	@Override
	public final OperationA11 getOperation() {

		return OperationsA11.XNSKIP0A_P;
	}
	
	@Override
	public int getResultCount() {

		return this.modifierA.argumentStackWrite();
	}
	
	@Override
	public ResultHandler getStore() {

		return ResultHandler.FA_BNN_NXT;
	}
	
}
