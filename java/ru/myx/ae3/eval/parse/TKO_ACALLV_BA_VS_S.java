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
import ru.myx.ae3.exec.OperationsA2X;
import ru.myx.ae3.exec.OperationsS2X;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;

final class TKO_ACALLV_BA_VS_S extends TokenOperator {
	
	private final TokenInstruction accessProperty;
	
	TKO_ACALLV_BA_VS_S(final TokenInstruction accessProperty) {
		
		assert accessProperty.assertStackValue();
		this.accessProperty = accessProperty;
	}
	
	@Override
	public final String getNotation() {
		
		return this.accessProperty.getNotationAccess() + "()";
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
		
		return this.accessProperty.toDirectModifier() != ModifierArguments.AA0RB;
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
		
		final TokenInstruction accessProperty = this.accessProperty;
		final ModifierArgument modifierProperty = accessProperty.toDirectModifier();
		if (modifierProperty == ModifierArguments.AA0RB) {
			assert argumentA != ModifierArguments.AA0RB : "this.isDirectSupported: " + this.isDirectSupported();
			accessProperty.toAssembly(assembly, null, null, ResultHandler.FA_BNN_NXT);
		}
		assembly.addInstruction(
				(accessProperty.getResultType() == InstructionResult.STRING
					? OperationsS2X.VACALLS_XS
					: OperationsA2X.XACALLS)//
							.instruction(argumentA, modifierProperty, 0, store));
	}
	
	@Override
	public final String toCode() {
		
		return (this.accessProperty.getResultType() == InstructionResult.STRING
			? OperationsS2X.VACALLS_XS
			: OperationsA2X.XACALLS)//
				+ "\t1+0\tVS ->S;";
	}
	
	@Override
	public TokenInstruction toStackValue(final ProgramAssembly assembly, final TokenInstruction argumentA, final boolean sideEffectsOnly) {
		
		if (argumentA.toDirectModifier() == ModifierArguments.AB4CT) {
			return new TKV_ZTCALLV_A_V_S(this.accessProperty);
		}
		if (argumentA.toDirectModifier() == ModifierArguments.AB7FV) {
			return new TKV_FCALLV_A_V_S(this.accessProperty);
		}
		return new TKV_ACALLV_BA_VV_S(argumentA.toExecDetachableResult(), this.accessProperty);
	}
}
