/**
 *
 */
package ru.myx.ae3.exec;

import static ru.myx.ae3.exec.ModifierArguments.AA0RB;

import com.sun.istack.internal.NotNull;

/**
 * @author myx
 *
 */
final class IA11_AXXX_R_C_NN_NXT extends InstructionA11I {
	
	
	private final OperationA11 operation;

	private final int constant;

	IA11_AXXX_R_C_NN_NXT(final OperationA11 operation, final int constant) {
		if (constant > InstructionA11.CNST_MAX || constant < InstructionA11.CNST_MIN) {
			throw new IllegalArgumentException("Constant out of range, value=" + constant + ", max=" + InstructionA11.CNST_MAX + ", min=" + InstructionA11.CNST_MIN);
		}
		this.operation = operation;
		this.constant = constant;
	}

	@Override
	public final boolean equals(final Object o) {
		
		
		if (o == this) {
			return true;
		}
		if (o instanceof IA11_AXXX_R_C_NN_NXT) {
			final IA11_AXXX_R_C_NN_NXT x = (IA11_AXXX_R_C_NN_NXT) o;
			return this.operation == x.operation && this.constant == x.constant;
		}
		return false;
	}

	@Override
	public final ExecStateCode execCall(final ExecProcess process) throws Exception {
		
		
		return this.operation.execute(process, process.ra0RB, this.constant, ResultHandler.FA_BNN_NXT);
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
	public final OperationA11 getOperation() {
		
		
		return this.operation;
	}

	@Override
	public final int getResultCount() {
		
		
		return 0;
	}

	@Override
	public final int hashCode() {
		
		
		return this.operation.hashCode() //
				^ this.constant;
	}

	@Override
	public final boolean isStackPush() {
		
		
		return false;
	}

	@Override
	@NotNull
	public final ResultHandler getStore() {
		
		
		return ResultHandler.FA_BNN_NXT;
	}

	@Override
	@NotNull
	public final ModifierArgument getModifierA() {
		
		
		return AA0RB;
	}
}
