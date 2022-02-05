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
import ru.myx.ae3.exec.ModifierArguments;
import ru.myx.ae3.exec.OperationsA2X;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;

final class TKO_DELETE_A_L_S extends TokenOperator {
	
	@Override
	public final String getNotation() {

		return "delete ";
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
		assert argumentB != null;

		/**
		 * valid store
		 */
		assert store != null;

		if (store == ResultHandler.FB_BSN_NXT) {
			if (argumentA == argumentB && argumentA == ModifierArguments.AE21POP) {
				assembly.addInstruction(Instructions.INSTR_DELETE_A_SS_S);
				return;
			}
		}

		assembly.addInstruction(OperationsA2X.XADELETE.instruction(argumentA, argumentB, 0, store));
	}

	@Override
	public final String toCode() {

		return "DELETE\t1\tSS  ->S;";
	}

	@Override
	public TokenInstruction toStackValue(final ProgramAssembly assembly, final TokenInstruction argumentA, final boolean sideEffectsOnly) {

		assert argumentA.assertAccessReference();
		return argumentA.toReferenceDelete();
	}
}
