/**
 * 
 */
package ru.myx.ae3.exec;

import static ru.myx.ae3.exec.ModifierArguments.AA0RB;

import com.sun.istack.internal.NotNull;

import ru.myx.ae3.base.BaseObject;

/**
 * @author myx
 * 
 */
final class IA2X_AXXX_AR_0_NN_NXT extends IA2X_AAAA_XX_X_NN_NXT {
	
	
	private final OperationA2X operation;
	
	private final ModifierArgument modifierA;
	
	IA2X_AXXX_AR_0_NN_NXT(final OperationA2X operation, final ModifierArgument modifierA) {
		this.operation = operation;
		this.modifierA = modifierA;
	}
	
	@Override
	public final int getConstant() {
		
		
		return 0;
	}
	
	@Override
	public final int getOperandCount() {
		
		
		return this.getOperation().getStackInputCount(0) + this.modifierA.argumentStackRead() + 0;
	}
	
	@Override
	@NotNull
	public ModifierArgument getModifierA() {
		
		
		return this.modifierA;
	}
	
	@Override
	@NotNull
	public ModifierArgument getModifierB() {
		
		
		return AA0RB;
	}
	
	@Override
	public final boolean equals(final Object o) {
		
		
		if (o == this) {
			return true;
		}
		if (o instanceof IA2X_AXXX_AR_0_NN_NXT) {
			final IA2X_AXXX_AR_0_NN_NXT x = (IA2X_AXXX_AR_0_NN_NXT) o;
			return this.operation == x.operation && this.modifierA == x.modifierA;
		}
		return false;
	}
	
	@Override
	public final ExecStateCode execCall(final ExecProcess process) throws Exception {
		
		
		final BaseObject argumentB = process.ra0RB;
		final BaseObject argumentA = this.modifierA.argumentRead(process);
		return this.operation.execute(process, argumentA, argumentB, 0, ResultHandler.FA_BNN_NXT);
	}
	
	@Override
	public final OperationA2X getOperation() {
		
		
		return this.operation;
	}
	
	@Override
	public int getResultCount() {
		
		
		return this.modifierA.argumentStackWrite() + 0;
	}
}
