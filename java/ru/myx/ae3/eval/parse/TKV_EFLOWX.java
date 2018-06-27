/*
 * Created on 11.03.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ru.myx.ae3.eval.parse;

import java.util.Arrays;

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
public final class TKV_EFLOWX extends TokenValue {
	
	private final TokenInstruction[] tokens;

	/**
	 * @param tokens
	 */
	public TKV_EFLOWX(final TokenInstruction[] tokens) {
		assert tokens.length > 3 : "Invalid flow item count (" + this.getClass().getSimpleName() + ", more than 3 expected): " + tokens.length;
		assert TokenValue.assertAllValues(tokens);
		this.tokens = tokens;
	}

	@Override
	public final String getNotation() {
		
		return "FLOWX(" + Arrays.asList(this.tokens) + ")";
	}

	@Override
	public final InstructionResult getResultType() {
		
		return this.tokens[this.tokens.length - 1].getResultType();
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
		for (int i = 0; i < count;) {
			final int index = i++;
			if (i == count) {
				/**
				 * last one! apply Store, State and stop
				 */
				this.tokens[index].toAssembly(assembly, null, null, store);
				break;
			}
			assert this.tokens[index].assertStackValue();
			this.tokens[index].toAssembly(assembly, null, null, ResultHandler.FA_BNN_NXT);
		}
	}

	@Override
	public String toCode() {
		
		return "FLOWX " + Arrays.asList(this.tokens) + ";";
	}

	@Override
	public TokenInstruction toExecDetachableResult() {
		
		final int index = this.tokens.length - 1;
		this.tokens[index] = this.tokens[index].toExecDetachableResult();
		return this;
	}

	@Override
	public TokenInstruction toExecNativeResult() {
		
		final int index = this.tokens.length - 1;
		this.tokens[index] = this.tokens[index].toExecNativeResult();
		return this;
	}
}
