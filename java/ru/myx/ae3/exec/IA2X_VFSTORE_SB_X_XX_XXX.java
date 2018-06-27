/**
 * 
 */
package ru.myx.ae3.exec;

import com.sun.istack.internal.NotNull;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BasePrimitiveString;
import ru.myx.ae3.eval.parse.ParseConstants;

/**
 * @author myx
 * 
 */
class IA2X_VFSTORE_SB_X_XX_XXX extends InstructionA2X {
	
	
	private final BasePrimitiveString argumentA;
	
	private final ModifierArgument modifierB;
	
	private ResultHandler store;
	
	IA2X_VFSTORE_SB_X_XX_XXX(final BasePrimitiveString argumentA, final ModifierArgument modifierB, final ResultHandler store) {
		this.argumentA = argumentA;
		this.modifierB = modifierB;
		this.store = store;
	}
	
	@Override
	public final ExecStateCode execCall(final ExecProcess ctx) throws Exception {
		
		
		@NotNull
		final BaseObject argumentB = ExecProcess.vmEnsureDetached(ctx, this.modifierB.argumentRead(ctx));
		
		ctx.contextSetMutableBinding(this.argumentA, argumentB, false);
		
		return this.store.execReturn(ctx, argumentB);
	}
	
	@Override
	public final OperationA2X getOperation() {
		
		
		return OperationsA2X.XFSTORE_D;
	}
	
	@Override
	public final int getOperandCount() {
		
		
		return this.getOperation().getStackInputCount(0) + this.modifierB.argumentStackRead();
	}
	
	@Override
	public int getResultCount() {
		
		
		return (this.store.isStackPush()
			? 1
			: 0) + this.modifierB.argumentStackWrite();
	}
	
	@Override
	public ResultHandler getStore() {
		
		
		return this.store;
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
