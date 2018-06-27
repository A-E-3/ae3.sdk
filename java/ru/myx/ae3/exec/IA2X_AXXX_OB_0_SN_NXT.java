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
final class IA2X_AXXX_OB_0_SN_NXT extends InstructionA2XI {
	
	
	private final BaseObject argumentA;
	
	private final ModifierArgument modifierB;
	
	private final OperationA2X operation;
	
	IA2X_AXXX_OB_0_SN_NXT(final OperationA2X operation, final BaseObject argumentA, final ModifierArgument modifierB) {
		this.operation = operation.execStackResult();
		this.argumentA = argumentA;
		this.modifierB = modifierB;
	}
	
	@Override
	public final boolean equals(final Object o) {
		
		
		if (o == this) {
			return true;
		}
		if (o instanceof IA2X_AXXX_OB_0_SN_NXT) {
			final IA2X_AXXX_OB_0_SN_NXT x = (IA2X_AXXX_OB_0_SN_NXT) o;
			return this.operation == x.operation && this.modifierB == x.modifierB && this.argumentA == x.argumentA;
		}
		return false;
	}
	
	@Override
	public final ExecStateCode execCall(final ExecProcess process) throws Exception {
		
		
		final BaseObject argumentB = this.modifierB.argumentRead(process);
		
		return this.operation.execute(process, this.argumentA, argumentB, 0, ResultHandler.FB_BSN_NXT);
	}
	
	@Override
	public final int getConstant() {
		
		
		return 0;
	}
	
	@Override
	public int getOperandCount() {
		
		
		return this.operation.getStackInputCount(0) + 0 + this.modifierB.argumentStackRead();
	}
	
	@Override
	public final OperationA2X getOperation() {
		
		
		return this.operation;
	}
	
	@Override
	public int getResultCount() {
		
		
		return 1 + 0 + this.modifierB.argumentStackWrite();
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
		
		
		return ParseConstants.getConstantValue(this.argumentA).toConstantModifier();
	}
	
	@Override
	@NotNull
	public ModifierArgument getModifierB() {
		
		
		return this.modifierB;
	}
	
	@Override
	int hashCodeModifierA() {
		
		
		return this.argumentA.hashCode();
	}
	
}
