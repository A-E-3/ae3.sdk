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
final class IA3X_AXXX_AOC_C_NN_RET extends InstructionA3XI {
	
	
	private final OperationA3X operation;
	
	private final ModifierArgument modifierA;
	
	private final BaseObject argumentB;
	
	private final ModifierArgument modifierB;
	
	private final ModifierArgument modifierC;
	
	private final int constant;
	
	IA3X_AXXX_AOC_C_NN_RET(
			final OperationA3X operation,
			final ModifierArgument modifierA,
			final BaseObject argumentB,
			final ModifierArgument modifierB,
			final ModifierArgument modifierC,
			final int constant) {
		if (constant > InstructionA3X.CNST_MAX || constant < InstructionA3X.CNST_MIN) {
			throw new IllegalArgumentException("Constant out of range, value=" + constant + ", max=" + InstructionA3X.CNST_MAX + ", min=" + InstructionA3X.CNST_MIN);
		}
		this.operation = operation;
		this.modifierA = modifierA;
		this.argumentB = argumentB;
		this.modifierB = modifierB;
		this.modifierC = modifierC;
		this.constant = constant;
	}
	
	@Override
	public final ExecStateCode execCall(final ExecProcess process) throws Exception {
		
		
		final BaseObject argumentC = this.modifierC.argumentRead(process);
		final BaseObject argumentA = this.modifierA.argumentRead(process);
		
		return this.operation.execute(process, argumentA, this.argumentB, argumentC, this.constant, ResultHandler.FC_PNN_RET);
	}
	
	/**
	 * @return constant
	 */
	@Override
	public final int getConstant() {
		
		
		return this.constant;
	}
	
	@Override
	public int getOperandCount() {
		
		
		return this.operation.getStackInputCount(this.constant) + this.modifierA.argumentStackRead() + 0 + this.modifierC.argumentStackRead();
	}
	
	@Override
	public final OperationA3X getOperation() {
		
		
		return this.operation;
	}
	
	@Override
	public int getResultCount() {
		
		
		return 0 + this.modifierA.argumentStackWrite() + 0 + this.modifierC.argumentStackWrite();
	}
	
	@Override
	public final boolean isStackPush() {
		
		
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
		
		
		return this.modifierA;
	}
	
	@Override
	@NotNull
	public ModifierArgument getModifierB() {
		
		
		return this.modifierB == null
			? ParseConstants.getConstantValue(this.argumentB).toConstantModifier()
			: this.modifierB;
	}
	
	@Override
	@NotNull
	public ModifierArgument getModifierC() {
		
		
		return this.modifierC;
	}
	
	@Override
	int hashCodeModifierB() {
		
		
		return this.argumentB.hashCode();
	}
}
