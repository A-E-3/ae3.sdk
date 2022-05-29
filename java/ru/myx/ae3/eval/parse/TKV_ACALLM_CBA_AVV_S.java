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

final class TKV_ACALLM_CBA_AVV_S extends TokenValue {
	
	private final TokenInstruction accessObject;
	
	private final TokenInstruction accessProperty;
	
	private final TokenInstruction argument;
	
	private final Instruction carguments;
	
	TKV_ACALLM_CBA_AVV_S(final TokenInstruction accessObject, final TokenInstruction accessProperty, final TokenInstruction argument, final Instruction carguments) {
		
		assert carguments != null;
		assert accessObject != null;
		assert accessProperty.assertStackValue();
		assert argument.assertZeroStackOperands();
		assert argument.getResultCount() == carguments.getOperandCount();
		assert accessObject != ModifierArguments.AE21POP && accessObject != ModifierArguments.AA0RB;
		this.accessObject = accessObject;
		this.accessProperty = accessProperty;
		this.argument = argument;
		this.carguments = carguments;
	}
	
	@Override
	public final String getNotation() {
		
		return "" + this.accessObject + "." + this.accessProperty.getNotation() + "( " + this.argument.getNotation() + " )";
	}
	
	@Override
	public final InstructionResult getResultType() {
		
		return InstructionResult.OBJECT;
	}
	
	@Override
	public void toAssembly(final ProgramAssembly assembly, final ModifierArgument argumentA, final ModifierArgument argumentB, final ResultHandlerBasic store) {
		
		assert argumentA == null;
		assert argumentB == null;
		
		final ModifierArgument modifierB = this.accessProperty.toDirectModifier();
		final ModifierArgument modifierA = this.accessObject.toDirectModifier();
		final boolean directB = modifierB == ModifierArguments.AA0RB;
		final boolean directA = modifierA == ModifierArguments.AA0RB;
		if (directA) {
			this.accessObject.toAssembly(
					assembly, //
					null,
					null,
					ResultHandler.FB_BSN_NXT);
		}
		if (directB) {
			this.accessProperty.toAssembly(
					assembly, //
					null,
					null,
					ResultHandler.FB_BSN_NXT);
		}

		this.argument.toAssembly(assembly, null, null, ResultHandler.FB_BSN_NXT);
		assembly.addInstruction(this.carguments);
		
		assembly.addInstruction(
				OperationsA3X.XACALLM//
						.instruction(
								directA
									? ModifierArguments.AE21POP
									: modifierA, //
								directB
									? ModifierArguments.AE21POP
									: modifierB,
									ModifierArguments.AA0RB,
								0,
								store));
	}
	
	@Override
	public final String toCode() {
		
		return OperationsA3X.XACALLM + "\t0\tAVV->S;";
	}
}
