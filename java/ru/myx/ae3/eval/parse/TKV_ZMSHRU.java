/*
 * Created on 29.10.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.ae3.eval.parse;

import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ModifierArguments;
import ru.myx.ae3.exec.OperationsA10;
import ru.myx.ae3.exec.OperationsS10;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;
import ru.myx.ae3.exec.parse.expression.TokenInstruction;
import ru.myx.ae3.exec.parse.expression.TokenValue;

final class TKV_ZMSHRU extends TokenValue {

	private final TokenInstruction argumentA;

	private final int argumentB;

	private int visibility;

	TKV_ZMSHRU(final TokenInstruction argumentA, final int argumentB, final int visibility) {
		// assert argumentA.toConstantModifier() == null : "Reduce then!";
		this.argumentA = argumentA;
		this.argumentB = argumentB;
		this.visibility = visibility;
	}

	@Override
	public final String getNotation() {

		return this.argumentA.getNotationValue() + " >>> " + this.argumentB;
	}

	@Override
	public final InstructionResult getResultType() {

		return InstructionResult.INTEGER;
	}

	@Override
	public void toAssembly(final ProgramAssembly assembly, final ModifierArgument argumentA, final ModifierArgument argumentB, final ResultHandlerBasic store) {

		/**
		 * zero operands (one operand is already embedded in this token)
		 */
		assert argumentA == null;
		assert argumentB == null;

		/**
		 * valid store
		 */
		assert store != null;

		/**
		 * flush all values to assembly
		 */
		final ModifierArgument modifierA = this.argumentA.toDirectModifier();
		if (modifierA == ModifierArguments.AA0RB) {
			this.argumentA.toAssembly(assembly, null, null, ResultHandler.FA_BNN_NXT);
		}
		assembly.addInstruction((this.visibility == 2
			? OperationsS10.VMBSHRU_N
			: this.visibility == 1
				? OperationsS10.VMBSHRU_D
				: OperationsA10.ZMSHRU_T)//
						.instruction(modifierA, this.argumentB, store));
	}

	@Override
	public final String toCode() {

		return (this.visibility == 2
			? OperationsS10.VMBSHRU_N
			: this.visibility == 1
				? OperationsS10.VMBSHRU_D
				: OperationsA10.ZMSHRU_T) + "\t1\tV [" + this.argumentA + ", " + this.argumentB + "]  ->S;";
	}

	@Override
	public TokenInstruction toExecDetachableResult() {

		this.visibility = 1;
		return this;
	}

	@Override
	public TokenInstruction toExecNativeResult() {

		this.visibility = 2;
		return this;
	}
}
