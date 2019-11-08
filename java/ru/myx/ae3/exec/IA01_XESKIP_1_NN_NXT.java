/**
 *
 */
package ru.myx.ae3.exec;

import ru.myx.vm_vliw32_2010.OperationA01;

/** @author myx */
final class IA01_XESKIP_1_NN_NXT extends IA01_AAAA_X_NN_NXT {
	
	static final InstructionA01 INSTANCE = new IA01_XESKIP_1_NN_NXT();

	private IA01_XESKIP_1_NN_NXT() {
		
		//
	}
	@Override
	public final ExecStateCode execCall(final ExecProcess process) {
		
		++process.ri08IP;
		return null;
	}

	@Override
	public final int getConstant() {
		
		return 1;
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
