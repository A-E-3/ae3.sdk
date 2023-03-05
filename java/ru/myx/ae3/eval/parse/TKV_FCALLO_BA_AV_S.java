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
import ru.myx.vm_vliw32_2010.OperationA2X;

final class TKV_FCALLO_BA_AV_S extends TokenValue {

	private final TokenInstruction argumentA;

	private final TokenInstruction argumentB;

	TKV_FCALLO_BA_AV_S(final TokenInstruction argumentA, final TokenInstruction argumentB) {

		assert argumentB.assertStackValue();
		assert argumentA.assertStackValue();
		this.argumentA = argumentA;
		this.argumentB = argumentB.toExecDetachableResult();
	}

	@Override
	public final String getNotation() {

		return this.argumentA.getNotation() + "( " + this.argumentB.getNotation() + " )";
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

		final ModifierArgument modifierArgument = this.argumentB.toDirectModifier();
		final ModifierArgument modifierB = this.argumentA.toDirectModifier();
		final boolean directArgument = modifierArgument == ModifierArguments.AA0RB;
		final boolean directB = modifierB == ModifierArguments.AA0RB;
		if (directB) {
			this.argumentA.toAssembly(
					assembly, //
					null,
					null,
					directArgument
						? ResultHandler.FB_BSN_NXT
						: ResultHandler.FA_BNN_NXT);
		}
		if (directArgument) {
			this.argumentB.toAssembly(assembly, null, null, ResultHandler.FA_BNN_NXT);
		}

		assembly.addInstruction(
				((OperationA2X) OperationsA2X.XFCALLO).instruction(
						directB && directArgument
							? ModifierArguments.AE21POP
							: modifierB,
						modifierArgument,
						0,
						store));
	}

	@Override
	public final String toCode() {

		return OperationsA2X.XFCALLO + "\t0\tAV->S;";
	}
}
