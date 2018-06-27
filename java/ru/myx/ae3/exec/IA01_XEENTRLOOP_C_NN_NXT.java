/**
 * 
 */
package ru.myx.ae3.exec;

/**
 * @author myx
 * 
 */
final class IA01_XEENTRLOOP_C_NN_NXT extends IA01_AAAA_C_NN_NXT {
	
	
	IA01_XEENTRLOOP_C_NN_NXT(final int constant) {
		super(constant);
		if (constant <= 0) {
			throw new IllegalArgumentException("Incorrect frame size: " + constant);
		}
	}
	
	@Override
	public final ExecStateCode execCall(final ExecProcess process) throws Exception {
		
		
		return process.vmFrameEntryOpCtrlNewVars(process.ri08IP + 1 + this.constant);
	}
	
	@Override
	public final int getOperandCount() {
		
		
		return 0;
	}
	
	@Override
	public final OperationA01 getOperation() {
		
		
		return OperationsA01.XEENTRLOOP_P;
	}
}
