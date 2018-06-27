/**
 * 
 */
package ru.myx.ae3.exec;

import com.sun.istack.internal.NotNull;

/**
 * @author myx
 * 
 */
final class IA11_XESKIP1A_A_C_NN_NXT extends InstructionA11I {
	
	
	private final ModifierArgument modifierA;
	
	private final int constant;
	
	IA11_XESKIP1A_A_C_NN_NXT(final ModifierArgument modifierA, final int constant) {
		if (constant > InstructionA11.CNST_MAX || constant < InstructionA11.CNST_MIN) {
			throw new IllegalArgumentException("Constant out of range, value=" + constant + ", max=" + InstructionA11.CNST_MAX + ", min=" + InstructionA11.CNST_MIN);
		}
		this.modifierA = modifierA;
		this.constant = constant;
	}
	
	@Override
	public final boolean equals(final Object o) {
		
		
		if (o == this) {
			return true;
		}
		if (o instanceof IA11_XESKIP1A_A_C_NN_NXT) {
			final IA11_XESKIP1A_A_C_NN_NXT x = (IA11_XESKIP1A_A_C_NN_NXT) o;
			return this.constant == x.constant && this.modifierA == x.modifierA;
		}
		return false;
	}
	
	@Override
	public final ExecStateCode execCall(final ExecProcess process) throws Exception {
		
		
		if ((process.ra0RB = this.modifierA.argumentRead(process)).baseToBoolean() == TRUE) {
			process.ri08IP += this.constant;
		}
		return null;
	}
	
	@Override
	public int getConstant() {
		
		
		return this.constant;
	}
	
	@Override
	public int getOperandCount() {
		
		
		return OperationsA11.XESKIP1A_P.getStackInputCount(this.constant) + this.modifierA.argumentStackRead();
	}
	
	@Override
	public final OperationA11 getOperation() {
		
		
		return OperationsA11.XESKIP1A_P;
	}
	
	@Override
	public int getResultCount() {
		
		
		return this.modifierA.argumentStackWrite();
	}
	
	@Override
	public final int hashCode() {
		
		
		return OperationsA11.XESKIP1A_P.hashCode() //
				^ this.constant ^ this.modifierA.hashCode();
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
		
		
		return this.modifierA;
	}
}
