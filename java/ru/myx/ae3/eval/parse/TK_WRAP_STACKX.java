/*
 * Created on 11.03.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ru.myx.ae3.eval.parse;

import java.util.Arrays;

import ru.myx.ae3.e4.parse.TokenType;
import ru.myx.ae3.exec.Instruction;
import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;
import ru.myx.ae3.exec.parse.expression.TokenInstruction;

/**
 * @author myx
 *
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public final class TK_WRAP_STACKX implements TokenInstruction {

	private final TokenInstruction[] tokens;

	/**
	 * @param tokens
	 */
	public TK_WRAP_STACKX(final TokenInstruction[] tokens) {
		this.tokens = tokens;
		assert tokens.length > 0 : "STACK0? no way!";
		assert tokens.length > 1 : "STACK1? no way!";
	}

	@Override
	public final String getNotation() {

		return "STACKX[" + Arrays.asList(this.tokens) + "]";
	}

	@Override
	public final int getOperandCount() {

		int balance = 0;
		final TokenInstruction[] code = this.tokens;
		for (int i = code.length - 1; i >= 0; --i) {
			final Instruction current = code[i];
			balance += current.getOperandCount();
		}
		return balance;
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

		int balance = 0;
		final TokenInstruction[] code = this.tokens;
		for (int i = code.length - 1; i >= 0; --i) {
			final Instruction current = code[i];
			balance += current.getResultCount();
		}
		return balance;
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
		final int count = this.tokens.length;
		for (int i = 0; i < count; ++i) {
			this.tokens[i].toExecDetachableResult().toAssembly(assembly, null, null, ResultHandler.FB_BSN_NXT);
		}
	}

	@Override
	public String toCode() {

		return "NONE/STACKX " + Arrays.asList(this.tokens) + ";";
	}
}
