/**
 * 
 */
package ru.myx.ae3.exec;

/**
 * @author myx
 * 
 */
final class IA01_XELEAVE_C_NN_NXT extends IA01_AAAA_C_NN_NXT {
	
	
	IA01_XELEAVE_C_NN_NXT(final int constant) {
		super(constant);
	}
	
	@Override
	public final ExecStateCode execCall(final ExecProcess process) throws Exception {
		
		
		return process.ri0BSB == process.ri0ASP
			? process.vmFrameLeave()
			: process.vmRaise("Stack disbalance on frame leave!");
	}
	
	@Override
	public final int getOperandCount() {
		
		
		return 0;
	}
	
	@Override
	public final OperationA01 getOperation() {
		
		
		return OperationsA01.XELEAVE_P;
	}
}
