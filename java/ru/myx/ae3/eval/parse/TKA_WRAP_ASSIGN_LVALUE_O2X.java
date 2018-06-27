package ru.myx.ae3.eval.parse;

import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandlerBasic;
import ru.myx.ae3.exec.parse.expression.TokenAssignment;
import ru.myx.ae3.exec.parse.expression.TokenInstruction;

class TKA_WRAP_ASSIGN_LVALUE_O2X extends TokenAssignment {
	
	private final TokenInstruction operation;

	public TKA_WRAP_ASSIGN_LVALUE_O2X(final TokenInstruction operation) {
		/**
		 * check some
		 */
		assert operation.getOperandCount() == 2;
		assert operation.getResultCount() == 1;
		/**
		 * actual constructor code
		 */
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
		
		/**
		 * check operands
		 */
		assert argumentA != null;
		assert argumentB != null;
		
		/**
		 * valid store
		 */
		assert store != null;

		this.operation.toAssembly(assembly, argumentA, argumentB, store);
	}

	@Override
	public String toCode() {
		
		return "ACCESS ASSIGN_T_O2X " + this.operation + "=;";
	}
}
