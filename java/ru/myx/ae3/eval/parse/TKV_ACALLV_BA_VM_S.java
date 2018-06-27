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
import ru.myx.ae3.exec.parse.expression.TokenValue;

final class TKV_ACALLV_BA_VM_S extends TokenValue {
	
	private final ModifierArgument argumentA;
	
	private final TokenInstruction argumentB;
	
	TKV_ACALLV_BA_VM_S(final ModifierArgument argumentA, final TokenInstruction argumentB) {
		assert argumentA != null;
		assert argumentB.assertStackValue();
		assert argumentA != ModifierArguments.AE21POP && argumentA != ModifierArguments.AA0RB;
		this.argumentA = argumentA;
		this.argumentB = argumentB;
	}
	
	@Override
	public final String getNotation() {
		
		return this.argumentA + "." + this.argumentB.getNotation() + "()";
	}
	
	@Override
	public final InstructionResult getResultType() {
		
		return InstructionResult.OBJECT;
	}
	
	@Override
	public void toAssembly(final ProgramAssembly assembly, final ModifierArgument argumentA, final ModifierArgument argumentB, final ResultHandlerBasic store) {
		
		assert argumentA == null;
		assert argumentB == null;
		
		final ModifierArgument modifierB = this.argumentB.toDirectModifier();
		if (modifierB == ModifierArguments.AA0RB) {
			this.argumentB.toAssembly(assembly, null, null, ResultHandler.FA_BNN_NXT);
		}
		assembly.addInstruction((this.argumentB.getResultType() == InstructionResult.STRING
			? OperationsS2X.VACALLS_XS
			: OperationsA2X.XACALLS)//
					.instruction(this.argumentA, modifierB, 0, store));
	}
	
	@Override
	public final String toCode() {
		
		return (this.argumentB.getResultType() == InstructionResult.STRING
			? OperationsS2X.VACALLS_XS
			: OperationsA2X.XACALLS)//
				+ "\t0\tVM ->S;";
	}
}
