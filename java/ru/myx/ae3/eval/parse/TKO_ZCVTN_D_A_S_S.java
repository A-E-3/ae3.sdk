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
import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.Instructions;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ModifierArguments;
import ru.myx.ae3.exec.OperationsA10;
import ru.myx.ae3.exec.OperationsS10;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;

final class TKO_ZCVTN_D_A_S_S extends TokenOperator {
	
	public static final TokenOperator INSTANCE = new TKO_ZCVTN_D_A_S_S();

	private TKO_ZCVTN_D_A_S_S() {
		//
	}

	@Override
	public final String getNotation() {
		
		return " +";
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

		final BaseObject constantArgument = argumentA.argumentConstantValue();
		if (constantArgument != null) {
			assembly.addInstruction(OperationsA10.XFLOAD_P.instruction(ParseConstants.getConstantValue(constantArgument.baseToNumber()).toConstantModifier(), 0, store));
			return;
		}

		if (argumentA == ModifierArguments.AA0RB) {
			if (store == ResultHandler.FA_BNN_NXT) {
				assembly.addInstruction(Instructions.INSTR_CVTN_D_1_R_NN_NEXT);
				return;
			}
			if (store == ResultHandler.FB_BSN_NXT) {
				assembly.addInstruction(Instructions.INSTR_CVTN_D_1_R_SN_NEXT);
				return;
			}
		} else //
		if (argumentA == ModifierArguments.AE21POP) {
			if (store == ResultHandler.FB_BSN_NXT) {
				assembly.addInstruction(Instructions.INSTR_CVTN_D_1_S_SN_NEXT);
				return;
			}
		}

		if (store == ResultHandler.FA_BNN_NXT) {
			assembly.addInstruction(OperationsS10.VCVTN_D.instruction(argumentA, 0, ResultHandler.FA_BNN_NXT));
			return;
		}

		assembly.addInstruction(OperationsS10.VCVTN_D.instruction(argumentA, 0, store));
	}

	@Override
	public final String toCode() {
		
		return OperationsS10.VCVTN_D + "\t1\tS  ->S;";
	}

	@Override
	public TokenInstruction toExecNativeResult() {
		
		return TKO_ZCVTN_N_A_S_S.INSTANCE;
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	public TokenInstruction toStackValue(final ProgramAssembly assembly, final TokenInstruction argumentA, final boolean sideEffectsOnly) {
		
		final BaseObject value = argumentA.toConstantValue();
		if (value != null && (sideEffectsOnly || value.baseIsPrimitiveNumber())) {
			/**
			 * known to be a constant already
			 */
			return argumentA;
		}
		switch (argumentA.getResultType()) {
			case INTEGER :
			case NUMBER :
				return argumentA;
		}
		return new TKV_ZCVTN(argumentA, 1);
	}
}
