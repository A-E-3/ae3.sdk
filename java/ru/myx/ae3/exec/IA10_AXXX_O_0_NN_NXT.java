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
final class IA10_AXXX_O_0_NN_NXT extends InstructionA10I {
	
	
	private final OperationA10 operation;
	
	private final BaseObject argumentA;
	
	IA10_AXXX_O_0_NN_NXT(
			final OperationA10 operation, //
			final BaseObject argumentA) {
		this.operation = operation;
		this.argumentA = argumentA;
	}
	
	@Override
	public final boolean equals(final Object o) {
		
		
		if (o == this) {
			return true;
		}
		if (o instanceof InstructionA2X) {
			final IA10_AXXX_O_0_NN_NXT x = (IA10_AXXX_O_0_NN_NXT) o;
			return this.operation == x.operation && this.argumentA == x.argumentA;
		}
		return false;
	}
	
	@Override
	public final ExecStateCode execCall(final ExecProcess process) {
		
		
		return this.operation.execute(process, this.argumentA, 0, ResultHandler.FA_BNN_NXT);
	}
	
	/**
	 * @return constant
	 */
	@Override
	public final int getConstant() {
		
		
		return 0;
	}
	
	@Override
	public int getOperandCount() {
		
		
		return this.operation.getStackInputCount(0);
	}
	
	/**
	 * @return operation
	 */
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
		
		
		return this.operation.hashCode() ^ this.argumentA.hashCode();
	}
	
	@Override
	public boolean isStackPush() {
		
		
		return false;
	}
	
	@Override
	@NotNull
	public ResultHandler getStore() {
		
		
		return ResultHandler.FA_BNN_NXT;
	}
	
	@Override
	@NotNull
	public ModifierArgument getModifierA() {
		
		
		return ParseConstants.getConstantValue(this.argumentA).toConstantModifier();
	}
}
