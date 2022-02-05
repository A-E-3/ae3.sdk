/*
 * Created on 29.10.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.ae3.eval.parse;

import ru.myx.ae3.eval.tokens.TokenInstruction;
import ru.myx.ae3.eval.tokens.TokenOperator;
import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ModifierArguments;
import ru.myx.ae3.exec.OperationsA10;
import ru.myx.ae3.exec.OperationsA2X;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;

final class TKO_DELETE_BA_VS_S extends TokenOperator {
	
	private final TokenInstruction argumentB;

	/**
	 * @param argumentB
	 */
	public TKO_DELETE_BA_VS_S(final TokenInstruction argumentB) {
		assert argumentB.isStackValue();
		this.argumentB = argumentB;
	}

	@Override
	public final String getNotation() {
		
		return "delete " + this.argumentB.getNotation();
	}

	@Override
	public final int getOperandCount() {
		
		return 1;
	}

	@Override
	public final int getPriorityLeft() {
		
		return 500;
	}

	@Override
	public final int getPriorityRight() {
		
		return 500;
	}

	@Override
	public final InstructionResult getResultType() {
		
		return InstructionResult.BOOLEAN;
	}

	@Override
	public void toAssembly(final ProgramAssembly assembly, final ModifierArgument argumentA, final ModifierArgument argumentB, final ResultHandlerBasic store) {
		
		/**
		 * check operands
		 */
		assert argumentA != null;
		assert argumentB == null;

		/**
		 * valid store
		 */
		assert store != null;

		final ModifierArgument modifierB = this.argumentB.toDirectModifier();
		if (modifierB == ModifierArguments.AA0RB) {
			this.argumentB.toAssembly(assembly, null, null, ResultHandler.FA_BNN_NXT);
		}
		assembly.addInstruction(argumentA == ModifierArguments.AB7FV
			? OperationsA10.XFDELETE_N.instruction(modifierB, 0, store)
			: OperationsA2X.XADELETE.instruction(argumentA, modifierB, 0, store));
	}

	@Override
	public final String toCode() {
		
		return "DELETE\t2\tSV ->S\t[" + this.argumentB + "];";
	}

	@Override
	public TokenInstruction toStackValue(final ProgramAssembly assembly, final TokenInstruction argumentA, final boolean sideEffectsOnly) {
		
		return new TKV_DELETE_BA_VV_S(argumentA, this.argumentB);
	}
}
