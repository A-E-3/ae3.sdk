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
import ru.myx.ae3.exec.OperationsA10;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;
import ru.myx.ae3.exec.parse.expression.TokenInstruction;
import ru.myx.ae3.exec.parse.expression.TokenOperator;

final class TKO_ZCVTS_T_A_S_S extends TokenOperator {
	
	public static final TokenOperator INSTANCE = new TKO_ZCVTS_T_A_S_S();

	private TKO_ZCVTS_T_A_S_S() {
		//
	}

	@Override
	public final String getNotation() {
		
		return " ''+";
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
		
		/**
		 * check operands
		 */
		assert argumentA != null;
		assert argumentB == null;

		/**
		 * valid store
		 */
		assert store != null;

		final BaseObject constantArgument = argumentA.argumentConstantValue();
		if (constantArgument != null) {
			assembly.addInstruction(OperationsA10.XFLOAD_P.instruction(ParseConstants.getConstantValue(constantArgument.baseToString()).toConstantModifier(), 0, store));
			return;
		}

		if (argumentA == ModifierArguments.AA0RB) {
			if (store == ResultHandler.FA_BNN_NXT) {
				assembly.addInstruction(Instructions.INSTR_CVTS_D_1_R_NN_NEXT);
				return;
			}
			if (store == ResultHandler.FB_BSN_NXT) {
				assembly.addInstruction(Instructions.INSTR_CVTS_D_1_R_SN_NEXT);
				return;
			}
		} else//
		if (argumentA == ModifierArguments.AE21POP) {
			if (store == ResultHandler.FB_BSN_NXT) {
				assembly.addInstruction(Instructions.INSTR_CVTS_D_1_S_SN_NEXT);
				return;
			}
		}

		assembly.addInstruction(OperationsA10.ZCVTS_T.instruction(argumentA, 0, store));
	}

	@Override
	public final String toCode() {
		
		return OperationsA10.ZCVTS_T + "\t1\tS  ->S;";
	}

	@Override
	public TokenInstruction toExecDetachableResult() {
		
		return TKO_ZCVTS_D_A_S_S.INSTANCE;
	}

	@Override
	public TokenInstruction toExecNativeResult() {
		
		return TKO_ZCVTS_N_A_S_S.INSTANCE;
	}

	@Override
	public TokenInstruction toStackValue(final ProgramAssembly assembly, final TokenInstruction argumentA, final boolean sideEffectsOnly) {
		
		final BaseObject value = argumentA.toConstantValue();
		if (value != null && (sideEffectsOnly || value.baseIsPrimitiveString())) {
			/**
			 * known to be a constant already
			 */
			return argumentA;
		}
		if (argumentA.getResultType() == InstructionResult.STRING) {
			return argumentA;
		}
		return new TKV_ZCVTS(argumentA, 0);
	}
}
