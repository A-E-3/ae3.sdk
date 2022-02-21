/**
 *
 */
package ru.myx.ae3.exec;

import ru.myx.vm_vliw32_2010.InstructionIA1I;
import ru.myx.vm_vliw32_2010.OperationA11;
import ru.myx.vm_vliw32_2010.VIFmtA11;

/** @author myx */
final class IA11_XNSKIP0A_A_C_FA_NN_NXT extends InstructionIA1I {
	
	private final ModifierArgument modifierA;

	private final int constant;

	IA11_XNSKIP0A_A_C_FA_NN_NXT(final ModifierArgument modifierA, final int constant) {
		
		if (constant > VIFmtA11.CNST_MAX || constant < VIFmtA11.CNST_MIN) {
			throw new IllegalArgumentException("Constant out of range, value=" + constant + ", max=" + VIFmtA11.CNST_MAX + ", min=" + VIFmtA11.CNST_MIN);
		}
		this.modifierA = modifierA;
		this.constant = constant;
	}

	@Override
	public final ExecStateCode execCall(final ExecProcess process) throws Exception {
		
		if ((process.ra0RB = this.modifierA.argumentRead(process)).baseValue() == null) {
			process.ri08IP += this.constant;
		}
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
