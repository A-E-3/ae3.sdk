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
import ru.myx.ae3.exec.ModifierArguments;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandlerBasic;
import ru.myx.ae3.exec.parse.expression.TokenInstruction;

/**
 * @author myx
 *
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public final class TK_WRAP_TOKEN_ARGS2 implements TokenInstruction {
	
	private final ModifierArgument modifierA;

	private final ModifierArgument modifierB;

	private TokenInstruction token;

	private final int count;

	/**
	 * @param token
	 * @param modifierA
	 * @param modifierB
	 */
	public TK_WRAP_TOKEN_ARGS2(final TokenInstruction token, final ModifierArgument modifierA, final ModifierArgument modifierB) {
		assert token.getOperandCount() == 2 : "Operand count must be 2, count=" + token.getOperandCount() + ", class=" + token.getClass().getName();
		assert modifierA != ModifierArguments.AA0RB;
		assert modifierA != ModifierArguments.AE21POP;
		assert modifierB != ModifierArguments.AA0RB;
		assert modifierB != ModifierArguments.AE21POP;
		this.token = token;
		this.modifierA = modifierA;
		this.modifierB = modifierB;
		this.count = 0
				//
				+ (modifierA == null
					? 0
					: 1)
				//
				+ (modifierB == null
					? 0
					: 1)
		//
		;
		assert this.count > 0 : "Use this only to insert some non-default arguments!";
		assert token.getOperandCount() >= this.count;
	}

	@Override
	public final String getNotation() {
		
		return "" //
				+ (this.modifierA == null
					? ""
					: this.modifierA + " ")
				+ this.token.getNotation() + (this.modifierB == null
					? ""
					: " " + this.modifierB)
				+ "";
	}

	@Override
	public final int getOperandCount() {
		
		return 2 - this.count;
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
		
		return 2 == this.count
			? TokenType.VALUE
			: this.token.getTokenType();
	}

	@Override
	public boolean isDirectSupported() {
		
		return this.modifierA != ModifierArguments.AA0RB && this.modifierB != ModifierArguments.AA0RB && this.token.isDirectSupported();
	}

	@Override
	public void toAssembly(final ProgramAssembly assembly, final ModifierArgument argumentA, final ModifierArgument argumentB, final ResultHandlerBasic store) {
		
		final int operandCount = this.getOperandCount();
		/**
		 * check operands (some operands are already embedded in this token)
		 */
		assert argumentA == null == operandCount < 1;
		assert argumentB == null == operandCount < 2;
		/**
		 * valid store
		 */
		assert store != null;

		switch (operandCount) {
			case 0 :
				this.token.toAssembly(assembly, this.modifierA, this.modifierB, store);
				return;
			case 1 : {
				if (this.modifierA == null) {
					this.token.toAssembly(assembly, argumentA, this.modifierB, store);
					return;
				}
				if (this.modifierB == null) {
					this.token.toAssembly(assembly, this.modifierA, argumentA, store);
					return;
				}
				throw new IllegalStateException("At least one of two nested arguments must be defined!");
			}
			default :
				throw new IllegalArgumentException(
						"Unsupported TOKENS_ARGS2 operand count: count=" + operandCount + ", class=" + this.token.getClass().getName() + ", text=" + this.token);
		}
	}

	@Override
	public String toCode() {
		
		return "ARGS2 {" + this.token + " " + this.modifierA + ", " + this.modifierB + "};";
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
