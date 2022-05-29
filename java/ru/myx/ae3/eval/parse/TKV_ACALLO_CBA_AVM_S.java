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
import ru.myx.ae3.exec.OperationsA3X;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;

final class TKV_ACALLO_CBA_AVM_S extends TokenValue {
	
	private final ModifierArgument accessObjectModifier;
	
	private final TokenInstruction argumentB;
	
	private final TokenInstruction argument;
	
	TKV_ACALLO_CBA_AVM_S(final ModifierArgument accessObjectModifier, final TokenInstruction argumentB, final TokenInstruction argument) {
		assert argument.assertStackValue();
		assert accessObjectModifier != null;
		assert argumentB.assertStackValue();
		assert accessObjectModifier != ModifierArguments.AE21POP && accessObjectModifier != ModifierArguments.AA0RB;
		this.accessObjectModifier = accessObjectModifier;
		this.argumentB = argumentB;
		this.argument = argument.toExecDetachableResult();
	}
	
	@Override
	public final String getNotation() {
		
		return "" + this.accessObjectModifier + "." + this.argumentB.getNotation() + "( " + this.argument.getNotation() + " )";
	}
	
	@Override
	public final InstructionResult getResultType() {
		
		return InstructionResult.OBJECT;
	}
	
	@Override
	public void toAssembly(final ProgramAssembly assembly, final ModifierArgument argumentA, final ModifierArgument argumentB, final ResultHandlerBasic store) {
		
		assert argumentA == null;
		assert argumentB == null;
		
		final ModifierArgument modifierArgument = this.argument.toDirectModifier();
		final ModifierArgument modifierB = this.argumentB.toDirectModifier();
		final boolean directArgument = modifierArgument == ModifierArguments.AA0RB;
		final boolean directB = modifierB == ModifierArguments.AA0RB;
		if (directB) {
			this.argumentB.toAssembly(
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
		
		assembly.addInstruction(OperationsA3X.XACALLO//
				.instruction(
						this.accessObjectModifier, //
						directB && directArgument
							? ModifierArguments.AE21POP
							: modifierB,
						modifierArgument,
						0,
						store));
	}
	
	@Override
	public final String toCode() {
		
		return OperationsA3X.XACALLO + "\t0\tAVM->S;";
	}
}
