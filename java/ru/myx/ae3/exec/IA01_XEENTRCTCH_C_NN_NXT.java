/**
 *
 */
package ru.myx.ae3.exec;

import ru.myx.vm_vliw32_2010.OperationA01;

/** @author myx */
final class IA01_XEENTRCTCH_C_NN_NXT extends IA01_AAAA_C_NN_NXT {
	
	IA01_XEENTRCTCH_C_NN_NXT(final int constant) {
		
		super(constant);
		if (constant <= 0) {
			throw new IllegalArgumentException("Incorrect frame size: " + constant);
		}
	}

	@Override
	public final ExecStateCode execCall(final ExecProcess process) throws Exception {
		
		return process.vmFrameEntryOpCatch(process.ri08IP + 1 + this.constant);
	}

	@Override
	public final int getOperandCount() {
		
		return 0;
	}

	@Override
	public final OperationA01 getOperation() {
		
		return OperationsA01.XEENTRCTCH_P;
	}
}
