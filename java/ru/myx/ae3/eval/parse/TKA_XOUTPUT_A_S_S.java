/*
 * Created on 29.10.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.ae3.eval.parse;

import ru.myx.ae3.eval.tokens.TokenAssignment;
import ru.myx.ae3.eval.tokens.TokenInstruction;
import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.OperationsA10;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandlerBasic;

final class TKA_XOUTPUT_A_S_S extends TokenAssignment {
	
	@Override
	public final String getNotation() {
		
		return "= ";
	}
	
	@Override
	public final int getOperandCount() {
		
		return 1;
	}
	
	@Override
	public final InstructionResult getResultType() {
		
		return InstructionResult.OBJECT;
	}
	
	@Override
	public void toAssembly(final ProgramAssembly assembly, final ModifierArgument argumentA, final ModifierArgument argumentB, final ResultHandlerBasic store) {
		
		/**
		 * No optimizations and checks, toStackValue is used unless someone
		 * calls this method explicitly.
		 */
		final ResultHandlerBasic output = store.replaceDoOutput();
		if (output == null) {
			throw new IllegalStateException(this + " shound not be used unless complete stack value");
		}
		assembly.addInstruction(OperationsA10.XFLOAD_P.instruction(argumentA, 0, output));
	}
	
	@Override
	public final String toCode() {
		
		return "XOUTPUT\t1\tS  ->S;";
	}
	
	@Override
	public TokenInstruction toStackValue(final ProgramAssembly assembly, final TokenInstruction argumentA, final boolean sideEffectsOnly) {
		
		return new TKV_OUTPUT_A_V_S(argumentA);
	}
}
