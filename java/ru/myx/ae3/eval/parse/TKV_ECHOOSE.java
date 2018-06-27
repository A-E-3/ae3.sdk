/*
 * Created on 11.03.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ru.myx.ae3.eval.parse;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.OperationsA01;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ProgramPart;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;
import ru.myx.ae3.exec.parse.expression.TokenInstruction;
import ru.myx.ae3.exec.parse.expression.TokenValue;

/** @author myx
 *
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments */
public final class TKV_ECHOOSE extends TokenValue {

	private final TokenInstruction condition;

	private TokenInstruction tokenLeft;

	private TokenInstruction tokenRight;

	/** @param condition
	 * @param tokenLeft
	 * @param tokenRight */
	public TKV_ECHOOSE(final TokenInstruction condition, final TokenInstruction tokenLeft, final TokenInstruction tokenRight) {
		assert condition.assertStackValue();
		assert tokenLeft.assertStackValue();
		assert tokenRight.assertStackValue();
		this.condition = condition;
		this.tokenLeft = tokenLeft;
		this.tokenRight = tokenRight;
	}

	@Override
	public final String getNotation() {

		return "(" + this.condition.getNotation() + " ? " + this.tokenLeft.getNotation() + " : " + this.tokenRight.getNotation() + ")";
	}

	@Override
	public final InstructionResult getResultType() {

		return this.tokenLeft.getResultType().merge(this.tokenRight.getResultType());
	}

	@Override
	public void toAssembly(final ProgramAssembly assembly, final ModifierArgument argumentA, final ModifierArgument argumentB, final ResultHandlerBasic store) {

		/** zoro operands */
		assert argumentA == null;
		assert argumentB == null;

		/** valid store */
		assert store != null;

		{
			final BaseObject constant = this.condition.toConstantValue();
			if (constant != null) {
				if (constant.baseToBoolean() == BaseObject.TRUE) {
					this.tokenLeft.toAssembly(assembly, null, null, store);
					return;
				}
				this.tokenRight.toAssembly(assembly, null, null, store);
				return;
			}
		}

		final int initialOffset = assembly.size();

		this.tokenLeft.toAssembly(assembly, null, null, store);
		final ProgramPart tokenLeft = assembly.toProgram(initialOffset);

		this.tokenRight.toAssembly(assembly, null, null, store);
		final ProgramPart tokenRight = assembly.toProgram(initialOffset);

		if (store instanceof ResultHandlerBasic.ExecutionContinue) {
			this.condition.toBooleanConditionalSkip(assembly, false, tokenLeft.length() + 1, ResultHandler.FU_BNN_NXT);
			assembly.addInstruction(tokenLeft);
			assembly.addInstruction(OperationsA01.XESKIP_P.instruction(tokenRight.length(), ResultHandler.FA_BNN_NXT));
			assembly.addInstruction(tokenRight);
			return;
		}

		this.condition.toBooleanConditionalSkip(assembly, false, tokenLeft.length(), ResultHandler.FU_BNN_NXT);
		assembly.addInstruction(tokenLeft);
		assembly.addInstruction(tokenRight);
		return;
	}

	@Override
	public String toCode() {

		return "ECHOOSE [" + this.condition + "?" + this.tokenLeft + " : " + this.tokenRight + "];";
	}

	@Override
	public TokenInstruction toExecDetachableResult() {

		this.tokenLeft = this.tokenLeft.toExecDetachableResult();
		this.tokenRight = this.tokenRight.toExecDetachableResult();
		return this;
	}

	@Override
	public TokenInstruction toExecNativeResult() {

		this.tokenLeft = this.tokenLeft.toExecNativeResult();
		this.tokenRight = this.tokenRight.toExecNativeResult();
		return this;
	}
}
