/**
 *
 * ARMW stands for "Access-Read-Modify-Write"
 *
 * Created on 29.10.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.ae3.eval.parse;

import ru.myx.ae3.eval.Evaluate.CompilationException;
import ru.myx.ae3.eval.tokens.TokenAssignment;
import ru.myx.ae3.eval.tokens.TokenInstruction;
import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandlerBasic;

class TKA_ASSIGN_ARMW_O2X extends TokenAssignment {

	private final TokenInstruction operation;
	
	public TKA_ASSIGN_ARMW_O2X(final TokenInstruction operation) {
		
		/** check some */
		assert operation.getOperandCount() == 2;
		assert operation.getResultCount() == 1;
		/** actual constructor code */
		this.operation = operation;
	}
	
	@Override
	public String getNotation() {

		return "" + this.operation.getNotation() + "=";
	}
	
	@Override
	public int getOperandCount() {

		return 2;
	}
	
	@Override
	public final InstructionResult getResultType() {

		return this.operation.getResultType();
	}
	
	@Override
	public boolean isDirectSupported() {

		return this.operation.isDirectSupported();
	}
	
	@Override
	public void toAssembly(final ProgramAssembly assembly, final ModifierArgument argumentA, final ModifierArgument argumentB, final ResultHandlerBasic store) {

		/** check operands */
		assert argumentA != null;
		assert argumentB != null;

		/** valid store */
		assert store != null;
		
		this.operation.toAssembly(assembly, argumentA, argumentB, store);
	}

	@Override
	public String toCode() {

		return "ASSIGN ARMW_T_O2X " + this.operation + "=;";
	}

	@Override
	public TokenInstruction toStackValue(final ProgramAssembly assembly, final TokenInstruction argumentA, final TokenInstruction argumentB, final boolean sideEffectsOnly)
			throws CompilationException {
		
		return new TKV_ASSIGN_ARMW(argumentA, this, argumentB);
	}
}
