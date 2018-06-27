/**
 * 
 */
package ru.myx.ae3.exec;

/**
 * @author myx
 * 
 */
final class IA00_AXXX_X_NN_NXT extends InstructionA00 {
	
	
	private final int constant;
	
	private final OperationA00 operation;
	
	/**
	 * @param operation
	 * @param constant
	 * @param store
	 * @param state
	 */
	IA00_AXXX_X_NN_NXT(final OperationA00 operation, final int constant) {
		this.operation = operation;
		this.constant = constant;
	}
	
	@Override
	public final ExecStateCode execCall(final ExecProcess process) throws Exception {
		
		
		return this.operation.execute(process, this.constant);
	}
	
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
		
		
		return 0;
	}
	
	@Override
	public boolean isRelativeAddressInConstant() {
		
		
		return this.operation.isRelativeAddressInConstant();
	}
	
	@Override
	public final boolean isStackPush() {
		
		
		return false;
	}
	
	@Override
	public ResultHandler getStore() {
		
		
		return ResultHandler.FA_BNN_NXT;
	}
}
