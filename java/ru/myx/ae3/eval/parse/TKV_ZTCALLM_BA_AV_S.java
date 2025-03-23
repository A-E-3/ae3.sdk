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
import ru.myx.ae3.exec.OperationsA2X;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;

final class TKV_ZTCALLM_BA_AV_S extends TokenValue {
	
	private final TokenInstruction accessProperty;

	private final TokenInstruction argument;

	private final Instruction carguments;

	TKV_ZTCALLM_BA_AV_S(final TokenInstruction accessProperty, final TokenInstruction argument, final Instruction carguments) {

		/** function itself */
		assert accessProperty.assertStackValue();
		/** not really a value, could have more than one result */
		assert argument.assertZeroStackOperands();
		/** CARGUMENTS should consume them all */
		assert argument.getResultCount() == carguments.getOperandCount();
		/**
		 *
		 */
		this.accessProperty = accessProperty;
		this.argument = argument;
		this.carguments = carguments;
	}

	@Override
	public final String getNotation() {
		
		return "this." + this.accessProperty.getNotation() + "( " + this.argument.getNotation() + " )";
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

		final ModifierArgument modifierProperty = this.accessProperty.toDirectModifier();
		final boolean directProperty = modifierProperty == ModifierArguments.AA0RB;
		if (directProperty) {
			this.accessProperty.toAssembly(assembly, null, null, ResultHandler.FB_BSN_NXT);
		}

		this.argument.toAssembly(assembly, null, null, ResultHandler.FB_BSN_NXT);
		assembly.addInstruction(this.carguments);
		assembly.addInstruction(
				OperationsA2X.ZTCALLM.instruction(
						directProperty
							? ModifierArguments.AE21POP
							: modifierProperty,
						ModifierArguments.AA0RB,
						0,
						store));
	}

	@Override
	public final String toCode() {
		
		return OperationsA2X.ZTCALLM + "\t0\tAV->S;";
	}
}
