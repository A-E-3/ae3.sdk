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
import ru.myx.ae3.exec.OperationsA10;
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
public final class TKV_CARRAY1 extends TokenValue {
	
	private final TokenInstruction token;

	/**
	 * @param token
	 */
	public TKV_CARRAY1(final TokenInstruction token) {
		assert token.assertStackValue();
		this.token = token.toExecNativeResult();
	}

	@Override
	public final String getNotation() {
		
		return "[" + this.token + "]";
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
		final ModifierArgument modifier = this.token.toDirectModifier();
		if (modifier == ModifierArguments.AA0RB) {
			this.token.toAssembly(assembly, null, null, ResultHandler.FA_BNN_NXT);
		}
		assembly.addInstruction(OperationsA10.XCARRAYX_N.instruction(modifier, 1, store));
	}

	@Override
	public String toCode() {
		
		return "ARRAY1 " + this.token + ";";
	}
}
