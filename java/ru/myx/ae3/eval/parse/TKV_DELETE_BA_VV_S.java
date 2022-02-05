/*
 * Created on 29.10.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.ae3.eval.parse;

import ru.myx.ae3.eval.tokens.TokenInstruction;
import ru.myx.ae3.eval.tokens.TokenValue;
import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ModifierArguments;
import ru.myx.ae3.exec.OperationsA10;
import ru.myx.ae3.exec.OperationsA2X;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;

final class TKV_DELETE_BA_VV_S extends TokenValue {
	
	private final TokenInstruction argumentA;

	private final TokenInstruction argumentB;

	/**
	 * @param argumentA
	 * @param argumentB
	 */
	public TKV_DELETE_BA_VV_S(final TokenInstruction argumentA, final TokenInstruction argumentB) {
		assert argumentA.isStackValue();
		assert argumentB.isStackValue();
		this.argumentA = argumentA;
		this.argumentB = argumentB;
	}

	@Override
	public final String getNotation() {
		
		return "delete " + this.argumentA.getNotation() + "[" + this.argumentB.getNotation() + "]";
	}

	@Override
	public final InstructionResult getResultType() {
		
		return InstructionResult.BOOLEAN;
	}

	@Override
	public void toAssembly(final ProgramAssembly assembly, final ModifierArgument argumentA, final ModifierArgument argumentB, final ResultHandlerBasic store) {
		
		/**
		 * zero operands
		 */
		assert argumentA == null;
		assert argumentB == null;

		/**
		 * valid store
		 */
		assert store != null;

		final ModifierArgument modifierA = this.argumentA.toDirectModifier();
		final ModifierArgument modifierB = this.argumentB.toDirectModifier();
		final boolean directA = modifierA == ModifierArguments.AA0RB;
		final boolean directB = modifierB == ModifierArguments.AA0RB;
		if (directA) {
			this.argumentA.toAssembly(assembly, null, null, directB
				? ResultHandler.FB_BSN_NXT
				: ResultHandler.FA_BNN_NXT);
		}
		if (directB) {
			this.argumentB.toAssembly(assembly, null, null, ResultHandler.FA_BNN_NXT);
		}
		assembly.addInstruction(modifierA == ModifierArguments.AB7FV
			? OperationsA10.XFDELETE_N.instruction(modifierB, 0, store)
			: modifierA == ModifierArguments.AB4CT
				? OperationsA10.ZTDELETE_N.instruction(modifierB, 0, store)
				: OperationsA2X.XADELETE.instruction(
						directA && directB
							? ModifierArguments.AE21POP
							: modifierA, //
						modifierB,
						0,
						store));
	}

	@Override
	public final String toCode() {
		
		return "DELETE\t2\tVV ->S\t[" + this.argumentA + " " + this.argumentB + "];";
	}
}
