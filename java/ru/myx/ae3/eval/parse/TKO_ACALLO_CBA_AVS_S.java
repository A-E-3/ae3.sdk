/*
 * Created on 29.10.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.ae3.eval.parse;

import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ModifierArguments;
import ru.myx.ae3.exec.OperationsA3X;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;
import ru.myx.ae3.exec.parse.expression.TokenInstruction;
import ru.myx.ae3.exec.parse.expression.TokenOperator;

final class TKO_ACALLO_CBA_AVS_S extends TokenOperator {
	
	private final TokenInstruction callPropertyName;
	
	private final TokenInstruction argument;
	
	TKO_ACALLO_CBA_AVS_S(final TokenInstruction callPropertyName, final TokenInstruction argument) {
		assert callPropertyName.assertStackValue();
		assert argument.assertStackValue();
		this.callPropertyName = callPropertyName;
		this.argument = argument.toExecDetachableResult();
	}
	
	@Override
	public final String getNotation() {
		
		return "[" + this.callPropertyName.getNotation() + "]( " + this.argument.getNotation() + " )";
	}
	
	@Override
	public final int getOperandCount() {
		
		return 1;
	}
	
	@Override
	public final int getPriorityLeft() {
		
		return 999;
	}
	
	@Override
	public final int getPriorityRight() {
		
		return 999;
	}
	
	@Override
	public final InstructionResult getResultType() {
		
		return InstructionResult.OBJECT;
	}
	
	@Override
	public boolean isDirectSupported() {
		
		return this.argument.toDirectModifier() != ModifierArguments.AA0RB && this.callPropertyName.toDirectModifier() != ModifierArguments.AA0RB;
	}
	
	@Override
	public boolean isParseValueRight() {
		
		return true;
	}
	
	@Override
	public void toAssembly(final ProgramAssembly assembly, final ModifierArgument argumentA, final ModifierArgument argumentB, final ResultHandlerBasic store) {
		
		/**
		 * argumentA is access source, this.argumentB is access key
		 */
		assert argumentA != null;
		assert argumentB == null;
		
		final ModifierArgument modifierArgument = this.argument.toDirectModifier();
		final ModifierArgument modifierB = this.callPropertyName.toDirectModifier();
		final boolean directArgument = modifierArgument == ModifierArguments.AA0RB;
		final boolean directB = modifierB == ModifierArguments.AA0RB;
		if (directB) {
			this.callPropertyName.toAssembly(
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
						argumentA, //
						directB && directArgument
							? ModifierArguments.AE21POP
							: modifierB,
						modifierArgument,
						0,
						store));
	}
	
	@Override
	public final String toCode() {
		
		return OperationsA3X.XACALLO + "\t1\tAMS->S;";
	}
	
	@Override
	public TokenInstruction toStackValue(final ProgramAssembly assembly, final TokenInstruction argumentA, final boolean sideEffectsOnly) {
		
		if (argumentA.toDirectModifier() == ModifierArguments.AB4CT) {
			return new TKV_ZTCALLO_BA_AV_S(this.callPropertyName, this.argument);
		}
		if (argumentA.toDirectModifier() == ModifierArguments.AB7FV) {
			return new TKV_FCALLO_BA_AV_S(this.callPropertyName, this.argument);
		}
		return new TKV_ACALLO_CBA_AVV_S(argumentA.toExecDetachableResult(), this.callPropertyName, this.argument);
	}
}
