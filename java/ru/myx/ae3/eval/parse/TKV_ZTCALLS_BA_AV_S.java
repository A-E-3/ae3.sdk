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

final class TKV_ZTCALLS_BA_AV_S extends TokenValue {

	private final TokenInstruction accessProperty;

	private final TokenInstruction arguments;

	private final int constant;

	TKV_ZTCALLS_BA_AV_S(final TokenInstruction accessProperty, final TokenInstruction arguments, final int constant) {
		
		assert accessProperty.assertStackValue();
		assert arguments.assertZeroStackOperands();
		assert arguments.getResultCount() == constant;
		assert constant > 1 : constant == 0
			? "Use " + TKV_ZTCALLV_A_V_S.class.getSimpleName() + " then"
			: "Use " + TKV_ZTCALLO_BA_AV_S.class.getSimpleName() + " then";
		this.accessProperty = accessProperty;
		this.arguments = arguments;
		this.constant = constant;
	}

	@Override
	public final String getNotation() {

		return "this." + this.accessProperty.getNotation() + "( " + this.arguments.getNotation() + " )";
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

		/** Anyway in stack, constant is expected to be more than zero */
		this.arguments.toAssembly(assembly, null, null, ResultHandler.FB_BSN_NXT);

		final ModifierArgument modifierProperty = this.accessProperty.toDirectModifier();
		final boolean directProperty = modifierProperty == ModifierArguments.AA0RB;
		if (directProperty) {
			this.accessProperty.toAssembly(
					assembly, //
					null,
					null,
					ResultHandler.FA_BNN_NXT);
		}

		assembly.addInstruction(
				OperationsA10.ZTCALLS //
						.instruction(modifierProperty, this.constant, store));
	}

	@Override
	public final String toCode() {

		return OperationsA10.ZTCALLS + "\t0+" + this.constant + "\tAV->S;";
	}
}
