/**
 *
 */
package ru.myx.ae3.exec;

import com.sun.istack.internal.NotNull;

/**
 * @author myx
 *
 */
class IA10_AXXX_A_C_SN_NXT extends InstructionA10I {
	
	
	private final int constant;

	private final ModifierArgument modifierA;

	private final OperationA10 operation;

	IA10_AXXX_A_C_SN_NXT(final OperationA10 operation, final ModifierArgument modifierA, final int constant) {
		if (constant > InstructionA10.CNST_MAX || constant < InstructionA10.CNST_MIN) {
			throw new IllegalArgumentException("Constant out of range, value=" + constant + ", max=" + InstructionA10.CNST_MAX + ", min=" + InstructionA10.CNST_MIN);
		}
		this.operation = operation.execStackResult();
		this.modifierA = modifierA;
		this.constant = constant;
	}

	@Override
	public final boolean equals(final Object o) {
		
		
		if (o == this) {
			return true;
		}
		if (o instanceof IA10_AXXX_A_C_SN_NXT) {
			final IA10_AXXX_A_C_SN_NXT x = (IA10_AXXX_A_C_SN_NXT) o;
			return this.operation == x.operation && this.constant == x.constant && this.modifierA == x.modifierA;
		}
		return false;
	}

	@Override
	public final ExecStateCode execCall(final ExecProcess process) throws Exception {
		
		
		return this.operation.execute(process, this.modifierA.argumentRead(process), this.constant, ResultHandler.FB_BSN_NXT);
	}

	@Override
	public int getConstant() {
		
		
		return this.constant;
	}

	@Override
	public int getOperandCount() {
		
		
		return this.operation.getStackInputCount(this.constant) + this.modifierA.argumentStackRead();
	}

	@Override
	public final OperationA10 getOperation() {
		
		
		return this.operation;
	}

	@Override
	public int getResultCount() {
		
		
		return 1 + this.modifierA.argumentStackWrite();
	}

	@Override
	public final int hashCode() {
		
		
		return this.operation.hashCode() //
				^ this.constant ^ ResultHandler.FB_BSN_NXT.hashCode() ^ 0 ^ this.modifierA.hashCode();
	}

	@Override
	public boolean isStackPush() {
		
		
		return true;
	}

	@Override
	public ResultHandler getStore() {
		
		
		return ResultHandler.FB_BSN_NXT;
	}

	@Override
	@NotNull
	public ModifierArgument getModifierA() {
		
		
		return this.modifierA;
	}
}
