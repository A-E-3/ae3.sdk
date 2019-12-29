/**
 *
 */
package ru.myx.vm_vliw32_2010;


import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.eval.parse.ParseConstants;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ResultHandler;

/** @author myx */
final class IA3_AXXX_AOC_C_NN_RET extends InstructionIA3I {
	
	private final OperationA3X operation;

	private final ModifierArgument modifierA;

	private final BaseObject argumentB;

	private final ModifierArgument modifierB;

	private final ModifierArgument modifierC;

	private final int constant;

	IA3_AXXX_AOC_C_NN_RET(
			final OperationA3X operation,
			final ModifierArgument modifierA,
			final BaseObject argumentB,
			final ModifierArgument modifierB,
			final ModifierArgument modifierC,
			final int constant) {
		
		if (constant > VIFmtA31.CNST_MAX || constant < VIFmtA31.CNST_MIN) {
			throw new IllegalArgumentException("Constant out of range, value=" + constant + ", max=" + VIFmtA31.CNST_MAX + ", min=" + VIFmtA31.CNST_MIN);
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

	/** @return constant */
	@Override
	public final int getConstant() {
		
		return this.constant;
	}

	@Override
	public ModifierArgument getModifierA() {
		
		return this.modifierA;
	}

	@Override
	public ModifierArgument getModifierB() {
		
		return this.modifierB == null
			? ParseConstants.getConstantValue(this.argumentB).toConstantModifier()
			: this.modifierB;
	}

	@Override
	public ModifierArgument getModifierC() {
		
		return this.modifierC;
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
	public ResultHandler getStore() {
		
		return ResultHandler.FC_PNN_RET;
	}

	@Override
	public int hashCodeModifierB() {
		
		return this.argumentB.hashCode();
	}
}
