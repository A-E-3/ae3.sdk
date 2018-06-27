/*
 * Created on 11.03.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ru.myx.ae3.eval.parse;

import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ModifierArguments;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;
import ru.myx.ae3.exec.parse.expression.TokenInstruction;
import ru.myx.ae3.exec.parse.expression.TokenValue;

/**
 * @author myx
 *
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public final class TKV_WRAP_TOKENS2 extends TokenValue {
	
	private final TokenInstruction token1;

	/**
	 * not 'final' for being able to replace by toDetachableResult() method.
	 */
	private TokenInstruction token2;

	/**
	 * @param token1
	 * @param token2
	 */
	public TKV_WRAP_TOKENS2(final TokenInstruction token1, final TokenInstruction token2) {
		assert token1.assertStackValue();
		// assertStackZeroBalance();
		assert token2.getResultCount() - token2.getOperandCount() == 0;
		// assert token2.assertStackOperatorMoreThanZeroOperands();
		assert token1.toDirectModifier() == ModifierArguments.AA0RB : "Reduce next token instead!";
		this.token1 = token1;
		this.token2 = token2;
	}

	@Override
	public final String getNotation() {
		
		return "(" + this.token1.getNotation() + " " + this.token2.getNotation() + ")";
	}

	@Override
	public final InstructionResult getResultType() {
		
		return this.token2.getResultType();
	}

	@Override
	public boolean isAccessReference() {
		
		return this.token2.isAccessReference();
	}

	@Override
	public void toAssembly(final ProgramAssembly assembly, final ModifierArgument argumentA, final ModifierArgument argumentB, final ResultHandlerBasic store) {
		
		/**
		 * zero operands (one operand is already embedded in this token)
		 */
		assert argumentA == null;
		assert argumentB == null;

		/**
		 * valid store
		 */
		assert store != null;

		final boolean directO = this.token2.isDirectSupported();
		this.token1.toAssembly(assembly, null, null, directO
			? ResultHandler.FA_BNN_NXT
			: ResultHandler.FB_BSN_NXT);
		this.token2.toAssembly(
				assembly,
				directO
					? ModifierArguments.AA0RB
					: ModifierArguments.AE21POP,
				null, //
				store);
		return;
	}

	@Override
	public String toCode() {
		
		return "TOKENS2 [" + this.token1 + ", " + this.token2 + "];";
	}

	@Override
	public final TokenInstruction toExecDetachableResult() {
		
		this.token2 = this.token2.toExecDetachableResult();
		return this;
	}

	@Override
	public final TokenInstruction toExecNativeResult() {
		
		this.token2 = this.token2.toExecNativeResult();
		return this;
	}

	@Override
	public ModifierArgument toReferenceReadBeforeWrite(final ProgramAssembly assembly,
			final ModifierArgument argumentA,
			final ModifierArgument argumentB,
			final boolean needRead,
			final boolean directAllowed) {
		
		/**
		 * zero operands (one operand is already embedded in this token)
		 */
		assert argumentA == null;
		assert argumentB == null;

		final boolean directO = this.token2.isDirectSupported() && false;
		this.token1.toAssembly(assembly, null, null, directO
			? ResultHandler.FA_BNN_NXT
			: ResultHandler.FB_BSN_NXT);
		return this.token2.toReferenceReadBeforeWrite(
				assembly,
				directO
					? ModifierArguments.AA0RB
					: ModifierArguments.AE21POP,
				null, //
				needRead,
				directAllowed);
	}

	@Override
	public void toReferenceWriteAfterRead(final ProgramAssembly assembly,
			final ModifierArgument argumentA,
			final ModifierArgument argumentB,
			final ModifierArgument modifierValue,
			final ResultHandler store) {
		
		/**
		 * zero operands (one operand is already embedded in this token)
		 */
		assert argumentA == null;
		assert argumentB == null;
		/**
		 * valid store
		 */
		assert store != null;

		final boolean directO = this.token2.isDirectSupported() && false;
		this.token2.toReferenceWriteAfterRead(
				assembly, //
				directO
					? ModifierArguments.AA0RB
					: ModifierArguments.AE21POP,
				null,
				modifierValue,
				store);
		return;
	}
}
