/**
 *
 */
package ru.myx.ae3.exec;

/**
 * @author myx
 *
 */
final class InstructionA01E extends InstructionA01 implements InstructionEditable {
	
	
	private final OperationA01 operation;
	
	private int constant;
	
	private final ResultHandler store;
	
	/**
	 * @param constant
	 */
	InstructionA01E(final OperationA01 operation, final int constant, final ResultHandler store) {
		if (constant > InstructionA01.CNST_MAX || constant < InstructionA01.CNST_MIN) {
			throw new IllegalArgumentException("Constant out of range, value=" + constant + ", max=" + InstructionA01.CNST_MAX + ", min=" + InstructionA01.CNST_MIN);
		}
		this.operation = operation;
		this.constant = constant;
		this.store = store;
	}
	
	@Override
	public final int getConstant() {
		
		
		return this.constant;
	}
	
	@Override
	public final ExecStateCode execCall(final ExecProcess process) throws Exception {
		
		
		return this.operation.execute(process, this.constant, this.store);
	}
	
	/**
	 * @param constant
	 */
	@Override
	public final InstructionEditable setConstant(final int constant) {
		
		
		if (constant > InstructionA01.CNST_MAX || constant < InstructionA01.CNST_MIN) {
			throw new IllegalArgumentException("Constant out of range, value=" + constant + ", max=" + InstructionA01.CNST_MAX + ", min=" + InstructionA01.CNST_MIN);
		}
		this.constant = constant;
		return this;
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
	public final int getResultCount() {
		
		
		return this.store.isStackPush()
			? 1
			: 0;
	}
	
	@Override
	public final boolean isStackPush() {
		
		
		return this.store.isStackPush();
	}
	
	@Override
	public final ResultHandler getStore() {
		
		
		return this.store;
	}
	
	private Instruction finished;
	
	@Override
	public InstructionPlaceholder setInstruction(final Instruction instruction) {
		
		
		this.finished = instruction;
		return this;
	}
	
	@Override
	public final Instruction setFinished() {
		
		
		return this.finished = this.operation.instruction(this.constant, this.store);
	}
	
	@Override
	public final Instruction getFinalIfReady() {
		
		
		return this.finished;
	}
}
