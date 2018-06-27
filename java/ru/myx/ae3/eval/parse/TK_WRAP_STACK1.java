/*
 * Created on 11.03.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ru.myx.ae3.eval.parse;

import ru.myx.ae3.e4.parse.TokenType;
import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;
import ru.myx.ae3.exec.parse.expression.TokenInstruction;

/**
 *
 * Enforce SS
 *
 * @author myx
 *
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public final class TK_WRAP_STACK1 implements TokenInstruction {

	private final TokenInstruction token;
	
	/**
	 * @param token
	 */
	public TK_WRAP_STACK1(final TokenInstruction token) {
		this.token = token;
		assert token != null : "STACK0? no way!";
	}
	
	@Override
	public final String getNotation() {

		return "STACK1[" + this.token + "]";
	}
	
	@Override
	public final int getOperandCount() {

		return this.token.getOperandCount();
	}
	
	@Override
	public int getPriorityLeft() {

		return +10000;
	}
	
	@Override
	public int getPriorityRight() {

		return +10000;
	}
	
	@Override
	public final int getResultCount() {

		return this.token.getResultCount();
	}
	
	@Override
	public InstructionResult getResultType() {

		return null;
	}
	
	@Override
	public final TokenType getTokenType() {

		return TokenType.OPERATOR;
	}
	
	@Override
	public boolean isDirectSupported() {

		return false;
	}
	
	@Override
	public void toAssembly(final ProgramAssembly assembly, final ModifierArgument argumentA, final ModifierArgument argumentB, final ResultHandlerBasic store) {

		/**
		 * zero operands
		 */
		assert argumentA == null;
		assert argumentB == null;
		
		/**
		 * valid store
		 */
		assert store != null;
		
		/**
		 * flush all values to assembly
		 */
		this.token.toExecDetachableResult().toAssembly(assembly, null, null, ResultHandler.FB_BSN_NXT);
	}
	
	@Override
	public String toCode() {

		return "NONE/STACK1 " + this.token + ";";
	}
}
