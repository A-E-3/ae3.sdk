/**
 * 
 */
package ru.myx.ae3.exec;

/**
 * @author myx
 * 
 */
final class IA00_AXXX_X_SN_NXT extends InstructionA00 {
	
	
	private final int constant;
	
	private final OperationA00 operation;
	
	/**
	 * @param operation
	 * @param constant
	 * @param store
	 * @param state
	 */
	IA00_AXXX_X_SN_NXT(final OperationA00 operation, final int constant) {
		if (constant > InstructionA00.CNST_MAX || constant < InstructionA00.CNST_MIN) {
			throw new IllegalArgumentException("Constant out of range, value=" + constant + ", max=" + InstructionA00.CNST_MAX + ", min=" + InstructionA00.CNST_MIN);
		}
		this.operation = operation;
		this.constant = constant;
	}
	
	@Override
	public final ExecStateCode execCall(final ExecProcess process) throws Exception {
		
		
		final ExecStateCode code = this.operation.execute(process, this.constant);
		
		if (code == null) {
			process.stackPush();
			return null;
		}
		
		return code;
	}
	
	/**
	 * @return constant
	 */
	@Override
	public final int getConstant() {
		
		
		return this.constant;
	}
	
	@Override
	public final int getOperandCount() {
		
		
		return this.operation.getStackInputCount(this.constant);
	}
	
	@Override
	public final OperationA00 getOperation() {
		
		
		return this.operation;
	}
	
	@Override
	public final int getResultCount() {
		
		
		return 1;
	}
	
	@Override
	public boolean isRelativeAddressInConstant() {
		
		
		return this.operation.isRelativeAddressInConstant();
	}
	
	@Override
	public final boolean isStackPush() {
		
		
		return true;
	}
	
	@Override
	public ResultHandler getStore() {
		
		
		return ResultHandler.FB_BSN_NXT;
	}
	
}
