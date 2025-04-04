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
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;

final class TKV_FCALLV_A_V_S extends TokenValue {

	private final TokenInstruction accessProperty;

	TKV_FCALLV_A_V_S(final TokenInstruction accessProperty) {

		assert accessProperty.assertStackValue();
		this.accessProperty = accessProperty;
	}

	@Override
	public final String getNotation() {

		return this.accessProperty.getNotation() + "()";
	}

	@Override
	public final InstructionResult getResultType() {

		return InstructionResult.OBJECT;
	}

	@Override
	public void toAssembly(final ProgramAssembly assembly, final ModifierArgument argumentA, final ModifierArgument argumentB, final ResultHandlerBasic store) {

		/** zero operands (the only operand is already embedded in this token) */
		assert argumentA == null;
		assert argumentB == null;

		/** valid store */
		assert store != null;

		final ModifierArgument modifierProperty = this.accessProperty.toDirectModifier();
		if (modifierProperty == ModifierArguments.AA0RB) {
			this.accessProperty.toAssembly(assembly, null, null, ResultHandler.FA_BNN_NXT);
		}
		assembly.addInstruction(
				OperationsA10.XFCALLS//
						.instruction(modifierProperty, 0, store));
	}

	@Override
	public final String toCode() {

		return OperationsA10.XFCALLS + "\t0\tV ->S;";
	}
}
