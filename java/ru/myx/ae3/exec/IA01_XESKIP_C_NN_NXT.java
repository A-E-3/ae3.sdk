/**
 *
 */
package ru.myx.ae3.exec;

import ru.myx.vm_vliw32_2010.OperationA01;

/** @author myx */
final class IA01_XESKIP_C_NN_NXT extends IA01_AAAA_C_NN_NXT {
	
	IA01_XESKIP_C_NN_NXT(final int constant) {
		
		super(constant);
	}

	@Override
	public final ExecStateCode execCall(final ExecProcess process) {
		
		process.ri08IP += this.constant;
		return null;
	}

	@Override
	public final int getOperandCount() {
		
		return 0;
	}

	@Override
	public final OperationA01 getOperation() {
		
		return OperationsA01.XESKIP_P;
	}
}
