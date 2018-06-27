/**
 *
 */
package ru.myx.ae3.exec;

/**
 * @author myx
 *
 */
class IA01_AXXX_C_XX_XXX extends InstructionA01 {
	
	
	private final int constant;

	private final OperationA01 operation;

	private final ResultHandler store;

	IA01_AXXX_C_XX_XXX(final OperationA01 operation, final int constant, final ResultHandler store) {
		this.operation = operation;
		this.constant = constant;
		this.store = store;
	}

	@Override
	public final ExecStateCode execCall(final ExecProcess process) throws Exception {
		
		
		return this.operation.execute(process, this.constant, this.store);
	}

	@Override
	public int getConstant() {
		
		
		return this.constant;
	}

	@Override
	public int getOperandCount() {
		
		
		return this.operation.getStackInputCount(this.constant);
	}

	@Override
	public final OperationA01 getOperation() {
		
		
		return this.operation;
	}

	@Override
	public int getResultCount() {
		
		
		return this.store.isStackPush()
			? 1
			: 0;
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
