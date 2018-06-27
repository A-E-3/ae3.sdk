/**
 * 
 */
package ru.myx.ae3.exec;

import ru.myx.ae3.base.BaseObject;

/**
 * @author myx
 * 
 */
class IA2X_AXXX_AB_C_NN_NXT extends IA2X_AAAA_AB_C_NN_NXT {
	
	
	private final OperationA2X operation;
	
	IA2X_AXXX_AB_C_NN_NXT(final OperationA2X operation, final ModifierArgument modifierA, final ModifierArgument modifierB, final int constant) {
		super(modifierA, modifierB, constant);
		this.operation = operation;
	}
	
	@Override
	public final boolean equals(final Object o) {
		
		
		if (o == this) {
			return true;
		}
		if (o instanceof IA2X_AXXX_AB_C_NN_NXT) {
			final IA2X_AXXX_AB_C_NN_NXT x = (IA2X_AXXX_AB_C_NN_NXT) o;
			return this.operation == x.operation && this.constant == x.constant && this.modifierB == x.modifierB && this.modifierA == x.modifierA;
		}
		return false;
	}
	
	@Override
	public final ExecStateCode execCall(final ExecProcess process) throws Exception {
		
		
		final BaseObject argumentB = this.modifierB.argumentRead(process);
		final BaseObject argumentA = this.modifierA.argumentRead(process);
		return this.operation.execute(process, argumentA, argumentB, this.constant, ResultHandler.FA_BNN_NXT);
	}
	
	@Override
	public final OperationA2X getOperation() {
		
		
		return this.operation;
	}
	
	@Override
	public int getResultCount() {
		
		
		return this.modifierA.argumentStackWrite() + this.modifierB.argumentStackWrite();
	}
}
