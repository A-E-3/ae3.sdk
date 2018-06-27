/*
 * Created on 11.03.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ru.myx.ae3.eval.parse;

import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.ModifierArgument;
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
public final class TKV_EFLOW2 extends TokenValue {

	private final TokenInstruction token1;

	private TokenInstruction token2;

	/**
	 * @param token1
	 * @param token2
	 */
	public TKV_EFLOW2(final TokenInstruction token1, final TokenInstruction token2) {
		assert token1.assertStackValue();
		assert token2.assertStackValue();
		this.token1 = token1;
		this.token2 = token2;
	}

	@Override
	public final String getNotation() {

		return "(" + this.token1.getNotation() + ", " + this.token2.getNotation() + ")";
	}

	@Override
	public final InstructionResult getResultType() {

		return this.token2.getResultType();
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

		this.token1.toAssembly(assembly, null, null, ResultHandler.FA_BNN_NXT);
		this.token2.toAssembly(assembly, null, null, store);
	}

	@Override
	public String toCode() {

		return "FLOW2 [" + this.token1 + ", " + this.token2 + "];";
	}

	@Override
	public TokenInstruction toExecDetachableResult() {

		this.token2 = this.token2.toExecDetachableResult();
		return this;
	}

	@Override
	public TokenInstruction toExecNativeResult() {

		this.token2 = this.token2.toExecNativeResult();
		return this;
	}
}
