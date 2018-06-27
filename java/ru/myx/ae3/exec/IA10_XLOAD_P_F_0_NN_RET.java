/**
 *
 */
package ru.myx.ae3.exec;

import com.sun.istack.internal.NotNull;

import ru.myx.ae3.base.BasePrimitiveString;

/**
 * @author myx
 *
 */
final class IA10_XLOAD_P_F_0_NN_RET extends InstructionA10I {
	
	
	private final BasePrimitiveString argumentA;
	
	IA10_XLOAD_P_F_0_NN_RET(final BasePrimitiveString argumentA) {
		this.argumentA = argumentA;
	}
	
	@Override
	public final boolean equals(final Object o) {
		
		
		if (o == this) {
			return true;
		}
		if (o instanceof IA10_XLOAD_P_F_0_NN_RET) {
			final IA10_XLOAD_P_F_0_NN_RET x = (IA10_XLOAD_P_F_0_NN_RET) o;
			return this.argumentA == x.argumentA;
		}
		return false;
	}
	
	@Override
	public final ExecStateCode execCall(final ExecProcess ctx) throws Exception {
		
		
		/**
		 * can't do - doesn't read scope hierarchy as needed ^^^^^
		 */
		// return process.vmPropertyRead(process, this.argumentA, UNDEFINED,
		// EFC_PNN_RET.INSTANCE);

		return EFC_PNN_RET.INSTANCE.execReturn(ctx, ctx.contextGetBindingValue(this.argumentA, false));
		
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
		
		
		return OperationsA10.XFLOAD_P.hashCode() ^ this.argumentA.hashCode() ^ ResultHandler.FB_BNN_RET.hashCode();
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
		
		
		return new ModifierArgumentA32FVIMM(this.argumentA);
	}
}
