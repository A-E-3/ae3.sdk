/**
 *
 */
package ru.myx.ae3.exec;

/**
 * @author myx
 *
 *
 */
final class IA01_XESKIPRL1_C_NN_NXT extends IA01_AAAA_C_NN_NXT {
	
	
	IA01_XESKIPRL1_C_NN_NXT(final int constant) {
		super(constant);
	}
	
	@Override
	public final ExecStateCode execCall(final ExecProcess ctx) {
		
		
		if (ctx.ra1RL != 0) {
			ctx.ri08IP += this.constant;
			/**
			 * return NULL - no VLIW command parts to skip!
			 */
			// return ExecStateCode.NEXT;
		}
		return null;
	}
	
	@Override
	public final int getOperandCount() {
		
		
		return 0;
	}
	
	@Override
	public final OperationA01 getOperation() {
		
		
		return OperationsA01.XESKIPRL1_P;
	}
}
