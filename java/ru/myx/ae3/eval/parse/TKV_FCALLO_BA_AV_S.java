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
import ru.myx.ae3.exec.OperationsA2X;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;

final class TKV_FCALLO_BA_AV_S extends TokenValue {

	private final TokenInstruction accessProperty;

	private final TokenInstruction argument;

	TKV_FCALLO_BA_AV_S(final TokenInstruction accessProperty, final TokenInstruction argument) {

		assert argument.assertStackValue();
		assert accessProperty.assertStackValue();
		this.accessProperty = accessProperty;
		this.argument = argument.toExecDetachableResult();
	}

	@Override
	public final String getNotation() {

		return this.accessProperty.getNotation() + "( " + this.argument.getNotation() + " )";
	}

	@Override
	public final InstructionResult getResultType() {

		return InstructionResult.OBJECT;
	}

	@Override
	public void toAssembly(final ProgramAssembly assembly, final ModifierArgument argumentA, final ModifierArgument argumentB, final ResultHandlerBasic store) {

		/** zero operands (both operands are already embedded in this token) */
		assert argumentA == null;
		assert argumentB == null;

		/** valid store */
		assert store != null;

		final ModifierArgument modifierArgument = this.argument.toDirectModifier();
		final ModifierArgument modifierProperty = this.accessProperty.toDirectModifier();
		final boolean directArgument = modifierArgument == ModifierArguments.AA0RB;
		final boolean directProperty = modifierProperty == ModifierArguments.AA0RB;
		if (directProperty) {
			this.accessProperty.toAssembly(
					assembly, //
					null,
					null,
					directArgument
						? ResultHandler.FB_BSN_NXT
						: ResultHandler.FA_BNN_NXT);
		}
		if (directArgument) {
			this.argument.toAssembly(assembly, null, null, ResultHandler.FA_BNN_NXT);
		}

		assembly.addInstruction(
				OperationsA2X.XFCALLO.instruction(
						directProperty && directArgument
							? ModifierArguments.AE21POP
							: modifierProperty,
						modifierArgument,
						0,
						store));
	}

	@Override
	public final String toCode() {

		return OperationsA2X.XFCALLO + "\t0\tAV->S;";
	}
}
