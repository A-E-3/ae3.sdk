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
final class IA10_XLOAD_P_O_0_NN_RET extends InstructionA10I {
	
	
	private final BaseObject argumentA;
	
	IA10_XLOAD_P_O_0_NN_RET(final BaseObject argumentA) {
		this.argumentA = argumentA;
	}
	
	@Override
	public final boolean equals(final Object o) {
		
		
		if (o == this) {
			return true;
		}
		if (o instanceof IA10_XLOAD_P_O_0_NN_RET) {
			final IA10_XLOAD_P_O_0_NN_RET x = (IA10_XLOAD_P_O_0_NN_RET) o;
			return this.argumentA == x.argumentA;
		}
		return false;
	}
	
	@Override
	public final ExecStateCode execCall(final ExecProcess ctx) {
		
		
		return EFC_PNN_RET.INSTANCE.execReturn(ctx, this.argumentA);
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
		
		
		return 0;
	}
	
	/**
	 * @return operation
	 */
	@Override
	public final OperationA10 getOperation() {
		
		
		return OperationsA10.XFLOAD_P;
	}
	
	@Override
	public int getResultCount() {
		
		
		return 0;
	}
	
	@Override
	public final int hashCode() {
		
		
		return OperationsA10.XFLOAD_P.hashCode() ^ this.argumentA.hashCode();
	}
	
	@Override
	public boolean isStackPush() {
		
		
		return false;
	}
	
	@Override
	@NotNull
	public ResultHandler getStore() {
		
		
		return ResultHandler.FC_PNN_RET;
	}
	
	@Override
	@NotNull
	public ModifierArgument getModifierA() {
		
		
		return ParseConstants.getConstantValue(this.argumentA).toConstantModifier();
	}
}
