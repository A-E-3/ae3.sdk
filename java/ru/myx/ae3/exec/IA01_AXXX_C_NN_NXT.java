/**
 *
 */
package ru.myx.ae3.exec;

/**
 * @author myx
 *
 */
final class IA01_AXXX_C_NN_NXT extends IA01_AAAA_X_NN_NXT {
	
	
	private final OperationA01 operation;

	private final int constant;

	private final int operandCount;

	/**
	 * @param operation
	 * @param constant
	 * @param store
	 * @param state
	 */
	IA01_AXXX_C_NN_NXT(final OperationA01 operation, final int constant) {
		if (constant > InstructionA01.CNST_MAX || constant < InstructionA01.CNST_MIN) {
			throw new IllegalArgumentException("Constant out of range, value=" + constant + ", max=" + InstructionA01.CNST_MAX + ", min=" + InstructionA01.CNST_MIN);
		}
		this.operation = operation;
		this.constant = constant;
		this.operandCount = operation.getStackInputCount(constant);
	}

	@Override
	public final ExecStateCode execCall(final ExecProcess process) throws Exception {
		
		
		return this.operation.execute(process, this.constant, ResultHandler.FA_BNN_NXT);
	}

	@Override
	public final int getConstant() {
		
		
		return this.constant;
	}

	@Override
	public final int getOperandCount() {
		
		
		return this.operandCount;
	}

	@Override
	public final OperationA01 getOperation() {
		
		
		return this.operation;
	}
}
