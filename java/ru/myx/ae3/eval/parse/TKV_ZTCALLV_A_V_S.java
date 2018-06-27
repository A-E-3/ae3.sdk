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
import ru.myx.ae3.exec.OperationsA10;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;
import ru.myx.ae3.exec.parse.expression.TokenInstruction;
import ru.myx.ae3.exec.parse.expression.TokenValue;

final class TKV_ZTCALLV_A_V_S extends TokenValue {
	
	private final TokenInstruction argumentA;
	
	TKV_ZTCALLV_A_V_S(final TokenInstruction argumentA) {
		assert argumentA.assertStackValue();
		this.argumentA = argumentA;
	}
	
	@Override
	public final String getNotation() {
		
		return "this." + this.argumentA.getNotation() + "()";
	}
	
	@Override
	public final InstructionResult getResultType() {
		
		return InstructionResult.OBJECT;
	}
	
	@Override
	public void toAssembly(final ProgramAssembly assembly, final ModifierArgument argumentA, final ModifierArgument argumentB, final ResultHandlerBasic store) {
		
		/**
		 * zero operands (the only operand is already embedded in this token)
		 */
		assert argumentA == null;
		assert argumentB == null;
		
		/**
		 * valid store
		 */
		assert store != null;
		
		final ModifierArgument modifierB = this.argumentA.toDirectModifier();
		if (modifierB == ModifierArguments.AA0RB) {
			this.argumentA.toAssembly(assembly, null, null, ResultHandler.FA_BNN_NXT);
		}
		assembly.addInstruction(OperationsA10.ZTCALLS //
				.instruction(modifierB, 0, store));
	}
	
	@Override
	public final String toCode() {
		
		return OperationsA10.ZTCALLS + "\t0\tV ->S;";
	}
}
