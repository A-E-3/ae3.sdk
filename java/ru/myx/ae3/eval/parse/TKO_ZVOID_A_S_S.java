/*
 * Created on 29.10.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.ae3.eval.parse;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.eval.tokens.TokenInstruction;
import ru.myx.ae3.eval.tokens.TokenOperator;
import ru.myx.ae3.exec.Instruction;
import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ModifierArguments;
import ru.myx.ae3.exec.OperationsA2X;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;

final class TKO_ZVOID_A_S_S extends TokenOperator {

	public static final TokenOperator INSTANCE = new TKO_ZVOID_A_S_S();

	private static final ModifierArgument MODIFIER_FN_VOID = ModifierArgumentFunctionVoid.INSTANCE;
	
	private static final Instruction INSTR_ZVOID_A_R_S;

	private static final Instruction INSTR_ZVOID_A_R_V;

	private static final Instruction INSTR_ZVOID_A_S_S;

	private static final Instruction INSTR_ZVOID_A_S_V;

	static {
		INSTR_ZVOID_A_R_S = //
				OperationsA2X.XFCALLO.instruction(TKO_ZVOID_A_S_S.MODIFIER_FN_VOID, ModifierArguments.AA0RB, 0, ResultHandler.FB_BSN_NXT);

		INSTR_ZVOID_A_R_V = //
				OperationsA2X.XFCALLO.instruction(TKO_ZVOID_A_S_S.MODIFIER_FN_VOID, ModifierArguments.AA0RB, 0, ResultHandler.FA_BNN_NXT);

		INSTR_ZVOID_A_S_S = //
				OperationsA2X.XFCALLO.instruction(TKO_ZVOID_A_S_S.MODIFIER_FN_VOID, ModifierArguments.AE21POP, 0, ResultHandler.FB_BSN_NXT);

		INSTR_ZVOID_A_S_V = //
				OperationsA2X.XFCALLO.instruction(TKO_ZVOID_A_S_S.MODIFIER_FN_VOID, ModifierArguments.AE21POP, 0, ResultHandler.FA_BNN_NXT);

	}

	private TKO_ZVOID_A_S_S() {

		// prevent
	}

	@Override
	public final String getNotation() {

		return "void";
	}

	@Override
	public final String getNotationValue() {

		return "void ";
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

		return InstructionResult.UNDEFINED;
	}

	@Override
	public final boolean isConstantForArguments() {

		return true;
	}

	@Override
	public void toAssembly(final ProgramAssembly assembly, final ModifierArgument argumentA, final ModifierArgument argumentB, final ResultHandlerBasic store) {

		/** check operands */
		assert argumentA != null;
		assert argumentB == null;

		/** valid store */
		assert store != null;

		if (store == ResultHandler.FA_BNN_NXT) {
			if (argumentA == ModifierArguments.AA0RB) {
				assembly.addInstruction(TKO_ZVOID_A_S_S.INSTR_ZVOID_A_R_V);
				return;
			}
			if (argumentA == ModifierArguments.AE21POP) {
				assembly.addInstruction(TKO_ZVOID_A_S_S.INSTR_ZVOID_A_S_V);
				return;
			}
			assembly.addInstruction(
					OperationsA2X.XFCALLO.instruction(
							TKO_ZVOID_A_S_S.MODIFIER_FN_VOID, //
							argumentA,
							0,
							store));
			return;
		}
		if (store == ResultHandler.FB_BSN_NXT) {
			if (argumentA == ModifierArguments.AA0RB) {
				assembly.addInstruction(TKO_ZVOID_A_S_S.INSTR_ZVOID_A_R_S);
				return;
			}
			if (argumentA == ModifierArguments.AE21POP) {
				assembly.addInstruction(TKO_ZVOID_A_S_S.INSTR_ZVOID_A_S_S);
				return;
			}
		}

		assembly.addInstruction(OperationsA2X.XFCALLO.instruction(TKO_ZVOID_A_S_S.MODIFIER_FN_VOID, argumentA, 0, store));
	}

	@Override
	public final String toCode() {

		return "ZVOID\t1\tS  ->S;";
	}

	@Override
	public TokenInstruction toStackValue(final ProgramAssembly assembly, final TokenInstruction argumentA, final boolean sideEffectsOnly) {

		final BaseObject value = argumentA.toConstantValue();
		if (value != null) {
			return ParseConstants.TKV_UNDEFINED;
		}
		return super.toStackValue(assembly, argumentA, sideEffectsOnly);
	}
}
