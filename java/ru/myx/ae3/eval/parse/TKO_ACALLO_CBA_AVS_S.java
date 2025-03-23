/*
 * Created on 29.10.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.ae3.eval.parse;

import ru.myx.ae3.eval.tokens.TokenInstruction;
import ru.myx.ae3.eval.tokens.TokenOperator;
import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ModifierArguments;
import ru.myx.ae3.exec.OperationsA3X;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;

final class TKO_ACALLO_CBA_AVS_S extends TokenOperator {
	
	private final TokenInstruction accessProperty;
	
	private final TokenInstruction argument;
	
	TKO_ACALLO_CBA_AVS_S(final TokenInstruction accessProperty, final TokenInstruction argument) {

		assert accessProperty.assertStackValue();
		assert argument.assertStackValue();
		this.accessProperty = accessProperty;
		this.argument = argument.toExecDetachableResult();
	}
	
	@Override
	public final String getNotation() {
		
		return this.accessProperty.getNotationAccess() + "( " + this.argument.getNotation() + " )";
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
		
		return this.argument.toDirectModifier() != ModifierArguments.AA0RB && this.accessProperty.toDirectModifier() != ModifierArguments.AA0RB;
	}
	
	@Override
	public boolean isParseValueRight() {
		
		return true;
	}
	
	@Override
	public void toAssembly(final ProgramAssembly assembly, final ModifierArgument argumentA, final ModifierArgument argumentB, final ResultHandlerBasic store) {
		
		/** argumentA is access object */
		assert argumentA != null;
		assert argumentB == null;

		final ModifierArgument modifierArgument = this.argument.toDirectModifier();
		final ModifierArgument modifierProperty = this.accessProperty.toDirectModifier();
		final boolean directArgument = modifierArgument == ModifierArguments.AA0RB;
		final boolean directProperty = modifierProperty == ModifierArguments.AA0RB;
		if (directProperty) {
			assert argumentA != ModifierArguments.AA0RB : "this.isDirectSupported: " + this.isDirectSupported();
			this.accessProperty.toAssembly(
					assembly, //
					null,
					null,
					directArgument
						? ResultHandler.FB_BSN_NXT
						: ResultHandler.FA_BNN_NXT);
		}
		if (directArgument) {
			assert argumentA != ModifierArguments.AA0RB : "this.isDirectSupported: " + this.isDirectSupported();
			this.argument.toAssembly(assembly, null, null, ResultHandler.FA_BNN_NXT);
		}
		
		assembly.addInstruction(
				OperationsA3X.XACALLO//
						.instruction(
								argumentA, //
								directProperty && directArgument
									? ModifierArguments.AE21POP
									: modifierProperty,
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
			return new TKV_ZTCALLO_BA_AV_S(this.accessProperty, this.argument);
		}
		if (argumentA.toDirectModifier() == ModifierArguments.AB7FV) {
			return new TKV_FCALLO_BA_AV_S(this.accessProperty, this.argument);
		}
		return new TKV_ACALLO_CBA_AVV_S(argumentA.toExecDetachableResult(), this.accessProperty, this.argument);
	}
}
