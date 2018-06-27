/**
 * 
 */
package ru.myx.ae3.exec;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BasePrimitiveString;
import ru.myx.ae3.eval.parse.ParseConstants;

/**
 * @author myx
 * 
 */
final class IA2X_VACCESSDS_AS_0_SN_NXT extends InstructionA2XI {
	
	
	private final ModifierArgument modifierA;
	
	private final BasePrimitiveString argumentB;
	
	IA2X_VACCESSDS_AS_0_SN_NXT(final ModifierArgument modifierA, final BaseObject argumentB) {
		this.modifierA = modifierA;
		this.argumentB = argumentB.baseToString();
	}
	
	@Override
	public final int getConstant() {
		
		
		return 0;
	}
	
	@Override
	public final int getOperandCount() {
		
		
		return OperationsS2X.VACCESS_DS.getStackInputCount(0) + this.modifierA.argumentStackRead() + 0;
	}
	
	@Override
	@NotNull
	public ModifierArgument getModifierA() {
		
		
		return this.modifierA;
	}
	
	@Override
	@NotNull
	public ModifierArgument getModifierB() {
		
		
		return ParseConstants.getConstantValue(this.argumentB).toConstantModifier();
	}
	
	@Override
	int hashCodeModifierB() {
		
		
		return this.argumentB.hashCode();
	}
	
	@Override
	public final boolean equals(final Object o) {
		
		
		if (o == this) {
			return true;
		}
		if (o instanceof IA2X_VACCESSDS_AS_0_SN_NXT) {
			final IA2X_VACCESSDS_AS_0_SN_NXT x = (IA2X_VACCESSDS_AS_0_SN_NXT) o;
			return this.argumentB == x.argumentB && this.modifierA == x.modifierA;
		}
		return false;
	}
	
	@Override
	@Nullable
	public final ExecStateCode execCall(@NotNull final ExecProcess process) throws Exception {
		
		
		final BaseObject argumentA = this.modifierA.argumentRead(process);
		
		return argumentA.vmPropertyRead(process, this.argumentB, BaseObject.UNDEFINED, ResultHandler.FB_BSN_NXT);
	}
	
	@Override
	@NotNull
	public final OperationA2X getOperation() {
		
		
		return OperationsS2X.VACCESS_DS;
	}
	
	@Override
	public int getResultCount() {
		
		
		return this.modifierA.argumentStackWrite() + 1;
	}
	
	@Override
	public boolean isStackPush() {
		
		
		return true;
	}
	
	@Override
	public ResultHandler getStore() {
		
		
		return ResultHandler.FB_BSN_NXT;
	}
}
