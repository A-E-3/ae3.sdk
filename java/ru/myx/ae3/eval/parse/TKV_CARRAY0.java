/*
 * Created on 11.03.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ru.myx.ae3.eval.parse;

import ru.myx.ae3.eval.tokens.TokenValue;
import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.OperationsA00;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandlerBasic;

/**
 * @author myx
 *
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public final class TKV_CARRAY0 extends TokenValue {
	
	/**
	 *
	 */
	public static final TKV_CARRAY0 INSTANCE = new TKV_CARRAY0();
	
	/**
	 * @param token
	 */
	private TKV_CARRAY0() {
		//
	}
	
	@Override
	public final String getNotation() {
		
		return "[]";
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
		assembly.addInstruction(OperationsA00.XCARRAY_N.instruction(0, store));
	}
	
	@Override
	public String toCode() {
		
		return "ARRAY0;";
	}
}
