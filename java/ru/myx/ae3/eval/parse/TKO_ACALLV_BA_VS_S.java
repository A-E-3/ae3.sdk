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
import ru.myx.ae3.exec.OperationsA2X;
import ru.myx.ae3.exec.OperationsS2X;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;
import ru.myx.ae3.exec.parse.expression.TokenInstruction;
import ru.myx.ae3.exec.parse.expression.TokenOperator;

final class TKO_ACALLV_BA_VS_S extends TokenOperator {
	
	private final TokenInstruction callPropertyName;
	
	TKO_ACALLV_BA_VS_S(final TokenInstruction callPropertyName) {
		assert callPropertyName.assertStackValue();
		this.callPropertyName = callPropertyName;
	}
	
	@Override
	public final String getNotation() {
		
		return "[" + this.callPropertyName.getNotation() + "]()";
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
		
		return this.callPropertyName.toDirectModifier() != ModifierArguments.AA0RB;
	}
	
	@Override
	public boolean isParseValueRight() {
		
		return true;
	}
	
	@Override
	public void toAssembly(final ProgramAssembly assembly, final ModifierArgument argumentA, final ModifierArgument argumentB, final ResultHandlerBasic store) {
		
		assert argumentA != null;
		assert argumentB == null;
		
		final ModifierArgument modifierB = this.callPropertyName.toDirectModifier();
		if (modifierB == ModifierArguments.AA0RB) {
			assert argumentA != ModifierArguments.AA0RB;
			this.callPropertyName.toAssembly(assembly, null, null, ResultHandler.FA_BNN_NXT);
		}
		assembly.addInstruction((this.callPropertyName.getResultType() == InstructionResult.STRING
			? OperationsS2X.VACALLS_XS
			: OperationsA2X.XACALLS)//
					.instruction(argumentA, modifierB, 0, store));
	}
	
	@Override
	public final String toCode() {
		
		return (this.callPropertyName.getResultType() == InstructionResult.STRING
			? OperationsS2X.VACALLS_XS
			: OperationsA2X.XACALLS)//
				+ "\t1+0\tVS ->S;";
	}
	
	@Override
	public TokenInstruction toStackValue(final ProgramAssembly assembly, final TokenInstruction argumentA, final boolean sideEffectsOnly) {
		
		if (argumentA.toDirectModifier() == ModifierArguments.AB4CT) {
			return new TKV_ZTCALLV_A_V_S(this.callPropertyName);
		}
		if (argumentA.toDirectModifier() == ModifierArguments.AB7FV) {
			return new TKV_FCALLV_A_V_S(this.callPropertyName);
		}
		return new TKV_ACALLV_BA_VV_S(argumentA.toExecDetachableResult(), this.callPropertyName);
	}
}
