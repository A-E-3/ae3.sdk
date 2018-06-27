/*
 * Created on 29.10.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.ae3.eval.parse;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.Instructions;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ModifierArguments;
import ru.myx.ae3.exec.OperationsA2X;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;
import ru.myx.ae3.exec.parse.expression.TokenInstruction;
import ru.myx.ae3.exec.parse.expression.TokenOperator;

final class TKO_MSHRU_T_BA_SS_S extends TokenOperator {

	public static final TokenOperator INSTANCE = new TKO_MSHRU_T_BA_SS_S();

	private TKO_MSHRU_T_BA_SS_S() {
		// ignore
	}

	@Override
	public final String getNotation() {

		return ">>>";
	}

	@Override
	public final int getOperandCount() {

		return 2;
	}

	@Override
	public final int getPriorityLeft() {

		return 275;
	}

	@Override
	public final int getPriorityRight() {

		return 275;
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
		assert argumentB != null;

		/**
		 * valid store
		 */
		assert store != null;

		if (store == ResultHandler.FA_BNN_NXT) {
			if (argumentA == argumentB && argumentA == ModifierArguments.AE21POP) {
				assembly.addInstruction(Instructions.INSTR_MSHRU_T_BA_SS_V);
				return;
			}
		} else //
		if (store == ResultHandler.FB_BSN_NXT) {
			if (argumentA == argumentB && argumentA == ModifierArguments.AE21POP) {
				assembly.addInstruction(Instructions.INSTR_MSHRU_D_BA_SS_S);
				return;
			}
		}

		assembly.addInstruction(OperationsA2X.XMBSHRU_T.instruction(argumentA, argumentB, 0, store));
	}

	@Override
	public final String toCode() {

		return OperationsA2X.XMBSHRU_T + "\t2\tSS ->S;";
	}

	@Override
	public TokenInstruction toExecDetachableResult() {

		return TKO_MSHRU_D_BA_SS_S.INSTANCE;
	}

	@Override
	public TokenInstruction toExecNativeResult() {

		return TKO_MSHRU_N_BA_SS_S.INSTANCE;
	}

	@Override
	public TokenInstruction toStackValue(final ProgramAssembly assembly, final TokenInstruction argumentA, final TokenInstruction argumentB, final boolean sideEffectsOnly) {

		final BaseObject constantB = argumentB.toConstantValue();
		if (constantB != null) {
			return new TKV_ZMSHRU(argumentA, constantB.baseToJavaInteger(), 0);
		}
		return super.toStackValue(assembly, argumentA, argumentB, sideEffectsOnly);
	}
}
