/*
 * Created on 29.10.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.ae3.eval.parse;

import ru.myx.ae3.eval.tokens.TokenOperator;
import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ModifierArgumentA30IMM;
import ru.myx.ae3.exec.OperationsS2X;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandlerBasic;

final class TKO_MNOT_N_A_S_S extends TokenOperator {
	
	public static final TokenOperator INSTANCE = new TKO_MNOT_N_A_S_S();

	private TKO_MNOT_N_A_S_S() {
		//
	}

	@Override
	public final String getNotation() {

		return " ~";
	}

	@Override
	public final int getOperandCount() {

		return 1;
	}

	@Override
	public final int getPriorityLeft() {

		return +10000;
	}

	@Override
	public final int getPriorityRight() {

		return 500;
	}

	@Override
	public final InstructionResult getResultType() {

		return InstructionResult.INTEGER;
	}

	@Override
	public final boolean isConstantForArguments() {

		return true;
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

		assembly.addInstruction(OperationsS2X.VMBXOR_N.instruction(argumentA, ModifierArgumentA30IMM.MONE, 0, store));
	}

	@Override
	public final String toCode() {

		return "MNOT_N\t1\tS  ->S;";
	}
}
