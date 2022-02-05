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
import ru.myx.ae3.exec.Instructions;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ModifierArgumentA30IMM;
import ru.myx.ae3.exec.ModifierArguments;
import ru.myx.ae3.exec.OperationsA2X;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;

/**
 * @author myx
 *
 */
final class TKO_MSUB_T_BA_S0_S extends TokenOperator {

	public static final TokenOperator INSTANCE = new TKO_MSUB_T_BA_S0_S();

	private TKO_MSUB_T_BA_S0_S() {
		//
	}

	@Override
	public final String getNotation() {

		return "0-";
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

		return 600;
	}

	@Override
	public final InstructionResult getResultType() {

		return InstructionResult.NUMBER;
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

		if (store == ResultHandler.FA_BNN_NXT) {
			if (argumentA == ModifierArguments.AA0RB) {
				assembly.addInstruction(Instructions.INSTR_MSUB_T_BA_R0_V);
				return;
			}
			if (argumentA == ModifierArguments.AE21POP) {
				assembly.addInstruction(Instructions.INSTR_MSUB_T_BA_S0_V);
				return;
			}
		} else //
		if (store == ResultHandler.FB_BSN_NXT) {
			if (argumentA == ModifierArguments.AA0RB) {
				assembly.addInstruction(Instructions.INSTR_MSUB_D_BA_R0_S);
				return;
			}
			if (argumentA == ModifierArguments.AE21POP) {
				assembly.addInstruction(Instructions.INSTR_MSUB_D_BA_S0_S);
				return;
			}
		}

		assembly.addInstruction(OperationsA2X.XMSUB_T.instruction(ModifierArgumentA30IMM.ZERO, argumentA, 0, store));
	}

	@Override
	public final String toCode() {

		return OperationsA2X.XMSUB_T + "\t2\tCS ->S\tCONST(0);";
	}

	@Override
	public TokenInstruction toExecDetachableResult() {

		return TKO_MSUB_D_BA_S0_S.INSTANCE;
	}

	@Override
	public TokenInstruction toExecNativeResult() {

		return TKO_MSUB_N_BA_S0_S.INSTANCE;
	}
}
