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
import ru.myx.ae3.exec.Instructions;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ModifierArguments;
import ru.myx.ae3.exec.OperationsA10;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;

/**
 * @author myx
 *
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public final class TKV_OUTPUT_A_V_S extends TokenValue {
	
	private final TokenInstruction value;

	/**
	 * @param value
	 */
	public TKV_OUTPUT_A_V_S(final TokenInstruction value) {
		assert value.assertStackValue();
		this.value = value.toExecDetachableResult();
	}

	@Override
	public final String getNotation() {
		
		return "= " + this.value.getNotation() + " /* OUT */";
	}

	@Override
	public final InstructionResult getResultType() {
		
		return this.value.getResultType();
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

		/**
		 * flush all values to assembly
		 */
		final ModifierArgument embeddedArgument = this.value.toDirectModifier();
		assert embeddedArgument != null;

		final ResultHandlerBasic output = store.replaceDoOutput();
		if (output == null) {
			/**
			 * need two outputs total anyway %)
			 */
			this.value.toAssembly(assembly, null, null, ResultHandler.FB_BNO_NXT);
			assembly.addInstruction(store == ResultHandler.FB_BNO_NXT
				? Instructions.INSTR_LOAD_1_R_NO_NEXT
				: OperationsA10.XFLOAD_P.instruction(ModifierArguments.AA0RB, 0, store));
			return;
		}

		if (embeddedArgument == ModifierArguments.AA0RB) {
			this.value.toAssembly(assembly, null, null, output);
			return;
		}
		assembly.addInstruction(OperationsA10.XFLOAD_P.instruction(embeddedArgument, 0, output));
	}

	@Override
	public String toCode() {
		
		return "OUTPUT [" + this.value + "];";
	}
}
