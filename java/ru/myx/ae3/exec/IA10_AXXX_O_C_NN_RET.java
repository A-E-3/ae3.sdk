/**
 *
 */
package ru.myx.ae3.exec;

import com.sun.istack.internal.NotNull;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.eval.parse.ParseConstants;

/**
 * @author myx
 *
 */
class IA10_AXXX_O_C_NN_RET extends InstructionA10I {
	
	
	private final OperationA10 operation;

	private final BaseObject argumentA;

	private final int constant;

	IA10_AXXX_O_C_NN_RET(final OperationA10 operation, final BaseObject argumentA, final int constant) {
		if (constant > InstructionA10.CNST_MAX || constant < InstructionA10.CNST_MIN) {
			throw new IllegalArgumentException("Constant out of range, value=" + constant + ", max=" + InstructionA10.CNST_MAX + ", min=" + InstructionA10.CNST_MIN);
		}
		this.operation = operation;
		this.argumentA = argumentA;
		this.constant = constant;
	}

	@Override
	public final boolean equals(final Object o) {
		
		
		if (o == this) {
			return true;
		}
		if (o instanceof IA10_AXXX_O_C_NN_RET) {
			final IA10_AXXX_O_C_NN_RET x = (IA10_AXXX_O_C_NN_RET) o;
			return this.operation == x.operation && this.constant == x.constant && this.argumentA == x.argumentA;
		}
		return false;
	}

	@Override
	public final ExecStateCode execCall(final ExecProcess process) throws Exception {
		
		
		return this.operation.execute(process, this.argumentA, this.constant, ResultHandler.FC_PNN_RET);
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
	public final OperationA10 getOperation() {
		
		
		return this.operation;
	}

	@Override
	public int getResultCount() {
		
		
		return 0;
	}

	@Override
	public final int hashCode() {
		
		
		return this.operation.hashCode() //
				^ this.constant ^ ResultHandler.FB_BNN_RET.hashCode() ^ this.argumentA.hashCode();
	}

	@Override
	public boolean isStackPush() {
		
		
		return false;
	}

	@Override
	public ResultHandler getStore() {
		
		
		return ResultHandler.FC_PNN_RET;
	}

	@Override
	@NotNull
	public ModifierArgument getModifierA() {
		
		
		return ParseConstants.getConstantValue(this.argumentA).toConstantModifier();
	}
}
