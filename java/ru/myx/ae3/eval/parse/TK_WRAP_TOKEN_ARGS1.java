/*
 * Created on 11.03.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ru.myx.ae3.eval.parse;

import ru.myx.ae3.e4.parse.TokenType;
import ru.myx.ae3.eval.tokens.TokenInstruction;
import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ModifierArguments;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandlerBasic;

/**
 * @author myx
 *
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public final class TK_WRAP_TOKEN_ARGS1 implements TokenInstruction {
	
	private final ModifierArgument modifierA;

	private TokenInstruction token;

	/**
	 * @param token
	 * @param modifierA
	 */
	public TK_WRAP_TOKEN_ARGS1(final TokenInstruction token, final ModifierArgument modifierA) {
		assert token.getOperandCount() == 1 : "Operand count must be 1, count=" + token.getOperandCount() + ", class=" + token.getClass().getName();
		assert modifierA != null;
		assert modifierA != ModifierArguments.AA0RB;
		assert modifierA != ModifierArguments.AE21POP;
		this.token = token;
		this.modifierA = modifierA;
	}

	@Override
	public final String getNotation() {
		
		return "" //
				+ this.token.getNotation() + (this.modifierA == null
					? ""
					: this.modifierA + " ")
				+ "";
	}

	@Override
	public final int getOperandCount() {
		
		return 0;
	}

	@Override
	public int getPriorityLeft() {
		
		return this.token.getPriorityLeft();
	}

	@Override
	public int getPriorityRight() {
		
		return this.token.getPriorityRight();
	}

	@Override
	public final int getResultCount() {
		
		return this.token.getResultCount();
	}

	@Override
	public InstructionResult getResultType() {
		
		return this.token.getResultType();
	}

	@Override
	public TokenType getTokenType() {
		
		return TokenType.VALUE;
	}

	@Override
	public boolean isDirectSupported() {
		
		return this.modifierA != ModifierArguments.AA0RB && this.token.isDirectSupported();
	}

	@Override
	public void toAssembly(final ProgramAssembly assembly, final ModifierArgument argumentA, final ModifierArgument argumentB, final ResultHandlerBasic store) {
		
		/**
		 * zero operands (one operand are already embedded in this token)
		 */
		assert argumentA == null;
		assert argumentB == null;

		/**
		 * valid store
		 */
		assert store != null;

		this.token.toAssembly(assembly, this.modifierA, null, store);
	}

	@Override
	public String toCode() {
		
		return "ARGS1 {" + this.token + " " + this.modifierA + "};";
	}

	@Override
	public TokenInstruction toExecDetachableResult() {
		
		this.token = this.token.toExecDetachableResult();
		return this;
	}

	@Override
	public TokenInstruction toExecNativeResult() {
		
		this.token = this.token.toExecNativeResult();
		return this;
	}
}
