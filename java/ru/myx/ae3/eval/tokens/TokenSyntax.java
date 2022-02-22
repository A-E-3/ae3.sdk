/*
 * Created on 29.10.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.ae3.eval.tokens;

import ru.myx.ae3.e4.parse.TokenType;
import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandlerBasic;

/** @author myx */
public abstract class TokenSyntax implements TokenInstruction {

	/** @author myx */
	public static interface ConditionalStackValuable {

		/** condition to skip execution of right-hand side
		 *
		 * @return */
		public TokenInstruction.ConditionType getSkipCondition();
	}

	@Override
	public final int getOperandCount() {

		return 0;
	}

	@Override
	public int getPriorityLeft() {

		throw new IllegalStateException("Given (" + this + ") syntax token should not be used in compiled expression!");
	}

	@Override
	public int getPriorityRight() {

		throw new IllegalStateException("Given (" + this + ") syntax token should not be used in compiled expression!");
	}

	@Override
	public final int getResultCount() {

		return 0;
	}

	@Override
	public final InstructionResult getResultType() {

		return InstructionResult.NEVER;
	}

	@Override
	public final TokenType getTokenType() {

		return TokenType.SYNTAX;
	}

	@Override
	public void toAssembly(final ProgramAssembly assembly, final ModifierArgument argumentA, final ModifierArgument argumentB, final ResultHandlerBasic store) {
		
		throw new IllegalStateException("Syntax tokens are not for execution!");
	}

	@Override
	public final String toCode() {

		return "SYNTAX: " + this.getNotation();
	}

	@Override
	public final String toString() {
		
		return this.getNotation();
	}
}
