/**
 * 
 */
package ru.myx.ae3.exec;

import com.sun.istack.internal.NotNull;

import ru.myx.ae3.base.BaseObject;

/**
 * @author myx
 * 
 */
final class IA2X_AXXX_AB_0_NN_RET extends InstructionA2XI {
	
	
	private final ModifierArgument modifierA;
	
	private final ModifierArgument modifierB;
	
	private final OperationA2X operation;
	
	IA2X_AXXX_AB_0_NN_RET(final OperationA2X operation, final ModifierArgument modifierFilterA, final ModifierArgument modifierFilterB) {
		this.operation = operation;
		this.modifierA = modifierFilterA;
		this.modifierB = modifierFilterB;
	}
	
	@Override
	public final boolean equals(final Object o) {
		
		
		if (o == this) {
			return true;
		}
		if (o instanceof IA2X_AXXX_AB_0_NN_RET) {
			final IA2X_AXXX_AB_0_NN_RET x = (IA2X_AXXX_AB_0_NN_RET) o;
			return this.operation == x.operation && this.modifierB == x.modifierB && this.modifierA == x.modifierA;
		}
		return false;
	}
	
	@Override
	public final ExecStateCode execCall(final ExecProcess process) throws Exception {
		
		
		final BaseObject argumentB = this.modifierB.argumentRead(process);
		final BaseObject argumentA = this.modifierA.argumentRead(process);
		return this.operation.execute(process, argumentA, argumentB, 0, ResultHandler.FC_PNN_RET);
	}
	
	@Override
	public final int getConstant() {
		
		
		return 0;
	}
	
	@Override
	public int getOperandCount() {
		
		
		return this.operation.getStackInputCount(0) + this.modifierA.argumentStackRead() + this.modifierB.argumentStackRead();
	}
	
	@Override
	public final OperationA2X getOperation() {
		
		
		return this.operation;
	}
	
	@Override
	public int getResultCount() {
		
		
		return 0 + this.modifierA.argumentStackWrite() + this.modifierB.argumentStackWrite();
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
		
		
		return this.modifierA;
	}
	
	@Override
	@NotNull
	public ModifierArgument getModifierB() {
		
		
		return this.modifierB;
	}
}
