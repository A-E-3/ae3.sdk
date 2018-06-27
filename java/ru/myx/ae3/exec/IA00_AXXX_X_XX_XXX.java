/**
 * 
 */
package ru.myx.ae3.exec;

/**
 * @author myx
 * 
 */
final class IA00_AXXX_X_XX_XXX extends InstructionA00 {
	
	
	private final int constant;
	
	private final OperationA00 operation;
	
	private final ResultHandler store;
	
	/**
	 * @param operation
	 * @param constant
	 * @param store
	 */
	IA00_AXXX_X_XX_XXX(final OperationA00 operation, final int constant, final ResultHandler store) {
		assert store != null;
		assert store != ResultHandler.FA_BNN_NXT;
		if (constant > InstructionA00.CNST_MAX || constant < InstructionA00.CNST_MIN) {
			throw new IllegalArgumentException("Constant out of range, value=" + constant + ", max=" + InstructionA00.CNST_MAX + ", min=" + InstructionA00.CNST_MIN);
		}
		this.operation = operation;
		this.constant = constant;
		this.store = store;
	}
	
	@Override
	public final ExecStateCode execCall(final ExecProcess process) throws Exception {
		
		
		final ExecStateCode code = this.operation.execute(process, this.constant);
		return code == null
			? this.store.execReturn(process)
			: code;
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
		
		
		return this.store.isStackPush()
			? 1
			: 0;
	}
	
	@Override
	public boolean isRelativeAddressInConstant() {
		
		
		return this.operation.isRelativeAddressInConstant();
	}
	
	@Override
	public final boolean isStackPush() {
		
		
		return this.store.isStackPush();
	}
	
	@Override
	public ResultHandler getStore() {
		
		
		return this.store;
	}
	
}
