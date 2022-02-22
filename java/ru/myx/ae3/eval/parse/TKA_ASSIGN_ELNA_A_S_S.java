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
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandlerBasic;

final class TKA_ASSIGN_ELNA_A_S_S extends TokenAssignment {
	
	@Override
	public final String getNotation() {
		
		return "??= ";
	}
	
	@Override
	public final int getOperandCount() {
		
		return 2;
	}
	
	@Override
	public final InstructionResult getResultType() {
		
		return InstructionResult.OBJECT;
	}
	
	@Override
	public void toAssembly(final ProgramAssembly assembly, final ModifierArgument argumentA, final ModifierArgument argumentB, final ResultHandlerBasic store) {
		
		throw new UnsupportedOperationException();
	}
	
	@Override
	public final String toCode() {
		
		return "ELNA\t1\tS  ->S;";
	}
	
	@Override
	public TokenInstruction toStackValue(final ProgramAssembly assembly, final TokenInstruction argumentA, final TokenInstruction argumentB, final boolean sideEffectsOnly) {
		
		return new TKV_ASSIGN_ELNA(argumentA, argumentB);
	}
}
