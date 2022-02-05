/*
 * Created on 29.10.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.ae3.eval.parse;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.ecma.Ecma;
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

final class TKO_TYPEOF_A_S_S extends TokenOperator {

	private static final ModifierArgument MODIFIER_FN_TYPEOF = ModifierArgumentFunctionTypeof.INSTANCE;

	private static final Instruction INSTR_TYPEOF_A_R_S;

	private static final Instruction INSTR_TYPEOF_A_R_V;

	private static final Instruction INSTR_TYPEOF_A_S_S;

	private static final Instruction INSTR_TYPEOF_A_S_V;

	static {
		INSTR_TYPEOF_A_R_S = //
				OperationsA2X.XFCALLO.instruction(TKO_TYPEOF_A_S_S.MODIFIER_FN_TYPEOF, ModifierArguments.AA0RB, 0, ResultHandler.FB_BSN_NXT);

		INSTR_TYPEOF_A_R_V = //
				OperationsA2X.XFCALLO.instruction(TKO_TYPEOF_A_S_S.MODIFIER_FN_TYPEOF, ModifierArguments.AA0RB, 0, ResultHandler.FA_BNN_NXT);

		INSTR_TYPEOF_A_S_S = //
				OperationsA2X.XFCALLO.instruction(TKO_TYPEOF_A_S_S.MODIFIER_FN_TYPEOF, ModifierArguments.AE21POP, 0, ResultHandler.FB_BSN_NXT);

		INSTR_TYPEOF_A_S_V = //
				OperationsA2X.XFCALLO.instruction(TKO_TYPEOF_A_S_S.MODIFIER_FN_TYPEOF, ModifierArguments.AE21POP, 0, ResultHandler.FA_BNN_NXT);

	}

	@Override
	public final String getNotation() {

		return "typeof";
	}

	@Override
	public final String getNotationValue() {

		return "typeof ";
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

		return InstructionResult.STRING;
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
				assembly.addInstruction(TKO_TYPEOF_A_S_S.INSTR_TYPEOF_A_R_V);
				return;
			}
			if (argumentA == ModifierArguments.AE21POP) {
				assembly.addInstruction(TKO_TYPEOF_A_S_S.INSTR_TYPEOF_A_S_V);
				return;
			}
			assembly.addInstruction(
					OperationsA2X.XFCALLO.instruction(
							TKO_TYPEOF_A_S_S.MODIFIER_FN_TYPEOF, //
							argumentA,
							0,
							store));
			return;
		}
		if (store == ResultHandler.FB_BSN_NXT) {
			if (argumentA == ModifierArguments.AA0RB) {
				assembly.addInstruction(TKO_TYPEOF_A_S_S.INSTR_TYPEOF_A_R_S);
				return;
			}
			if (argumentA == ModifierArguments.AE21POP) {
				assembly.addInstruction(TKO_TYPEOF_A_S_S.INSTR_TYPEOF_A_S_S);
				return;
			}
		}

		assembly.addInstruction(OperationsA2X.XFCALLO.instruction(TKO_TYPEOF_A_S_S.MODIFIER_FN_TYPEOF, argumentA, 0, store));
	}

	@Override
	public final String toCode() {

		return "TYPEOF\t1\tS  ->S;";
	}

	@Override
	public TokenInstruction toStackValue(final ProgramAssembly assembly, final TokenInstruction argumentA, final boolean sideEffectsOnly) {

		final BaseObject value = argumentA.toConstantValue();
		if (value != null) {
			/** FIXME: ecmaTypeOf is not the same as BaseObject.baseClass() */
			return ParseConstants.getConstantValue(Base.forString(Ecma.ecmaTypeOf(value)));
		}
		return super.toStackValue(assembly, argumentA, sideEffectsOnly);
	}
}
