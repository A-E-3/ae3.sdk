/*
 * Created on 11.03.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ru.myx.ae3.eval.parse;

import ru.myx.ae3.eval.tokens.TokenInstruction;
import ru.myx.ae3.eval.tokens.TokenValue;
import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ModifierArguments;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;

/**
 * @author myx
 *
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public final class TKV_WRAP_TOKENS3 extends TokenValue {
	
	private final TokenInstruction token1;

	private final TokenInstruction token2;

	/**
	 * not 'final' for being able to replace by toDetachableResult() method.
	 */
	private TokenInstruction token3;

	/**
	 * @param token1
	 * @param token2
	 * @param token3
	 */
	public TKV_WRAP_TOKENS3(final TokenInstruction token1, final TokenInstruction token2, final TokenInstruction token3) {
		assert token1.assertStackValue();
		assert token2.assertStackValue();
		assert token3.getOperandCount() == 2 && token3.getResultCount() == 1;
		assert token1.toDirectModifier() == ModifierArguments.AA0RB : "Reduce next token instead!";
		assert token2.toDirectModifier() == ModifierArguments.AA0RB : "Reduce next token instead!";
		this.token1 = token1;
		this.token2 = token2;
		this.token3 = token3;
	}

	@Override
	public final String getNotation() {
		
		return "(" + this.token1.getNotation() + " " + this.token2.getNotation() + " " + this.token3.getNotation() + ")";
	}

	@Override
	public final InstructionResult getResultType() {
		
		return this.token3.getResultType();
	}

	@Override
	public boolean isAccessReference() {
		
		return this.token3.isAccessReference();
	}

	@Override
	public boolean isDirectSupported() {
		
		return false;
	}

	@Override
	public void toAssembly(final ProgramAssembly assembly, final ModifierArgument argumentA, final ModifierArgument argumentB, final ResultHandlerBasic store) {
		
		/**
		 * zero operands (two operands are already embedded in this token)
		 */
		assert argumentA == null;
		assert argumentB == null;

		/**
		 * valid store
		 */
		assert store != null;

		final boolean directO = this.token3.isDirectSupported();
		this.token1.toAssembly(assembly, null, null, ResultHandler.FB_BSN_NXT);
		this.token2.toAssembly(assembly, null, null, directO
			? ResultHandler.FA_BNN_NXT
			: ResultHandler.FB_BSN_NXT);
		this.token3.toAssembly(
				assembly,
				ModifierArguments.AE21POP,
				directO
					? ModifierArguments.AA0RB
					: ModifierArguments.AE21POP, //
				store);
	}

	@Override
	public String toCode() {
		
		return "TOKENS3 [" + this.token1 + ", " + this.token2 + ", " + this.token3 + "];";
	}

	@Override
	public final TokenInstruction toExecDetachableResult() {
		
		this.token3 = this.token3.toExecDetachableResult();
		return this;
	}

	@Override
	public final TokenInstruction toExecNativeResult() {
		
		this.token3 = this.token3.toExecNativeResult();
		return this;
	}

	@Override
	public ModifierArgument toReferenceReadBeforeWrite(final ProgramAssembly assembly,
			final ModifierArgument argumentA,
			final ModifierArgument argumentB,
			final boolean needRead,
			final boolean directAllowed) {
		
		/**
		 * zero operands (two operands are already embedded in this token)
		 */
		assert argumentA == null;
		assert argumentB == null;

		final boolean directO = this.token3.isDirectSupported() && false;
		this.token1.toAssembly(
				assembly, //
				null,
				null,
				ResultHandler.FB_BSN_NXT);
		this.token2.toAssembly(
				assembly, //
				null,
				null,
				directO
					? ResultHandler.FA_BNN_NXT
					: ResultHandler.FB_BSN_NXT);
		return this.token3.toReferenceReadBeforeWrite(
				assembly, //
				ModifierArguments.AE21POP,
				directO
					? ModifierArguments.AA0RB
					: ModifierArguments.AE21POP,
				needRead, //
				directAllowed);
	}

	@Override
	public void toReferenceWriteAfterRead(final ProgramAssembly assembly,
			final ModifierArgument argumentA,
			final ModifierArgument argumentB,
			final ModifierArgument modifierValue,
			final ResultHandler store) {
		
		/**
		 * zero operands (two operands are already embedded in this token)
		 */
		assert argumentA == null;
		assert argumentB == null;
		/**
		 * valid store
		 */
		assert store != null;

		final boolean directO = this.token3.isDirectSupported() && false;
		this.token3.toReferenceWriteAfterRead(
				assembly, //
				ModifierArguments.AE21POP,
				directO
					? ModifierArguments.AA0RB
					: ModifierArguments.AE21POP,
				modifierValue,
				store);
	}
}
