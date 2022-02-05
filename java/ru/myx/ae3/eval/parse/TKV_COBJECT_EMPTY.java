package ru.myx.ae3.eval.parse;

import ru.myx.ae3.eval.tokens.TokenValue;
import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.Instructions;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.OperationsA00;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;

/**
 *
 * @author myx
 *
 */
public final class TKV_COBJECT_EMPTY extends TokenValue {
	
	/**
	 *
	 */
	public static final TKV_COBJECT_EMPTY INSTANCE = new TKV_COBJECT_EMPTY();
	
	private TKV_COBJECT_EMPTY() {
		// empty
	}
	
	@Override
	public final String getNotation() {
		
		return "{}";
	}
	
	@Override
	public final InstructionResult getResultType() {
		
		return InstructionResult.OBJECT;
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
		
		if (store == ResultHandler.FB_BSN_NXT) {
			assembly.addInstruction(Instructions.INSTR_COBJECT_0_0_SN_NEXT);
			return;
		}
		
		assembly.addInstruction(OperationsA00.XCOBJECT_N.instruction(0, store));
	}
	
	@Override
	public String toCode() {
		
		return "COBJECT_LITERAL [{}];";
		
	}
	
}
