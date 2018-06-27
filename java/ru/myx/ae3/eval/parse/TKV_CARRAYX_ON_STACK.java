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
import ru.myx.ae3.exec.OperationsA00;
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
public final class TKV_CARRAYX_ON_STACK extends TokenValue {
	
	private final TokenInstruction[] tokens;

	/**
	 * @param tokens
	 */
	public TKV_CARRAYX_ON_STACK(final TokenInstruction[] tokens) {
		assert TokenValue.assertAllValues(tokens);
		assert tokens.length > 1;
		this.tokens = tokens;
	}

	@Override
	public final String getNotation() {
		
		return "[" + Arrays.asList(this.tokens) + "]";
	}

	@Override
	public InstructionResult getResultType() {
		
		return InstructionResult.ARRAY;
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
			this.tokens[i].toAssembly(assembly, null, null, ResultHandler.FB_BSN_NXT);
		}
		assembly.addInstruction(OperationsA00.XCARRAY_N.instruction(count, store));
	}

	@Override
	public String toCode() {
		
		return "ARRAY " + Arrays.asList(this.tokens) + ";";
	}
}
