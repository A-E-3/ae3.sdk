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
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;

final class TKO_BCVT_A_S_S extends TokenOperator {
	
	@Override
	public final String getNotation() {
		
		return "!!";
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
		
		return InstructionResult.BOOLEAN;
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
			if (store == ResultHandler.FA_BNN_NXT) {
				assembly.addInstruction(constantArgument.baseToJavaBoolean()
					? Instructions.INSTR_LOAD_TRUE_NN_NEXT
					: Instructions.INSTR_LOAD_FALSE_NN_NEXT);
				return;
			}
			if (store == ResultHandler.FB_BSN_NXT) {
				assembly.addInstruction(constantArgument.baseToJavaBoolean()
					? Instructions.INSTR_LOAD_TRUE_SN_NEXT
					: Instructions.INSTR_LOAD_FALSE_SN_NEXT);
				return;
			}
			assembly.addInstruction(OperationsA10.XFLOAD_P.instruction(constantArgument.baseToBoolean(), (constantArgument.baseToJavaBoolean()
				? ParseConstants.TKV_BOOLEAN_TRUE
				: ParseConstants.TKV_BOOLEAN_FALSE).toConstantModifier(), 0, store));
			return;
		}

		if (argumentA == ModifierArguments.AA0RB) {
			if (store == ResultHandler.FA_BNN_NXT) {
				assembly.addInstruction(Instructions.INSTR_BCVT_1_R_NN_NEXT);
				return;
			}
			if (store == ResultHandler.FB_BSN_NXT) {
				assembly.addInstruction(Instructions.INSTR_BCVT_1_R_SN_NEXT);
				return;
			}
		} else //
		if (argumentA == ModifierArguments.AE21POP) {
			if (store == ResultHandler.FB_BSN_NXT) {
				assembly.addInstruction(Instructions.INSTR_BCVT_1_S_SN_NEXT);
				return;
			}
		}

		assembly.addInstruction(OperationsA10.XBCVT_N.instruction(argumentA, 0, store));
	}

	@Override
	public final String toCode() {
		
		return "CVTB\t1\tS  ->S;";
	}

	@Override
	public TokenInstruction toStackValue(final ProgramAssembly assembly, final TokenInstruction argumentA, final boolean sideEffectsOnly) {
		
		final BaseObject value = argumentA.toConstantValue();
		if (value != null) {
			return value.baseToJavaBoolean()
				? ParseConstants.TKV_BOOLEAN_TRUE
				: ParseConstants.TKV_BOOLEAN_FALSE;
		}
		if (argumentA.getResultType() == InstructionResult.BOOLEAN) {
			return argumentA;
		}
		return new TKV_BCVT(argumentA);
	}
}
