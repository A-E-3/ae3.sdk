/*
 * Created on 29.10.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.ae3.eval.parse;

import ru.myx.ae3.eval.tokens.TokenInstruction;
import ru.myx.ae3.eval.tokens.TokenValue;
import ru.myx.ae3.exec.Instruction;
import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ModifierArguments;
import ru.myx.ae3.exec.OperationsA3X;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;

final class TKV_ACALLM_CBA_AVM_S extends TokenValue {

	private final ModifierArgument accessObjectModifier;

	private final TokenInstruction accessProperty;

	private final TokenInstruction argument;

	private final Instruction carguments;

	TKV_ACALLM_CBA_AVM_S(final ModifierArgument accessObjectModifier, final TokenInstruction accessProperty, final TokenInstruction argument, final Instruction carguments) {

		assert carguments != null;
		assert accessObjectModifier != null;
		assert accessProperty.assertStackValue();
		assert argument.assertZeroStackOperands();
		assert argument.getResultCount() == carguments.getOperandCount();
		assert accessObjectModifier != ModifierArguments.AE21POP && accessObjectModifier != ModifierArguments.AA0RB;
		this.accessObjectModifier = accessObjectModifier;
		this.accessProperty = accessProperty;
		this.argument = argument;
		this.carguments = carguments;
	}

	@Override
	public final String getNotation() {

		return "" + this.accessObjectModifier + "." + this.accessProperty.getNotation() + "( " + this.argument.getNotation() + " )";
	}

	@Override
	public final InstructionResult getResultType() {

		return InstructionResult.OBJECT;
	}

	@Override
	public void toAssembly(final ProgramAssembly assembly, final ModifierArgument argumentA, final ModifierArgument argumentB, final ResultHandlerBasic store) {

		assert argumentA == null;
		assert argumentB == null;

		final ModifierArgument modifierProperty = this.accessProperty.toDirectModifier();
		final boolean directProperty = modifierProperty == ModifierArguments.AA0RB;
		if (directProperty) {
			this.accessProperty.toAssembly(assembly, null, null, ResultHandler.FB_BSN_NXT);
		}

		this.argument.toAssembly(assembly, null, null, ResultHandler.FB_BSN_NXT);
		assembly.addInstruction(this.carguments);
		assembly.addInstruction(
				OperationsA3X.XACALLM//
						.instruction(
								this.accessObjectModifier, //
								directProperty
									? ModifierArguments.AE21POP
									: modifierProperty,
								ModifierArguments.AA0RB,
								0,
								store));
	}

	@Override
	public final String toCode() {

		return OperationsA3X.XACALLM + "\t0\tAVM->S;";
	}
}
