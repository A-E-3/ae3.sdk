/*
 * Created on 29.10.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.ae3.eval.parse;

import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandlerBasic;
import ru.myx.ae3.exec.parse.expression.TokenValue;
import ru.myx.ae3.help.Format;

/**
 * ERROR is not a 'throw' statement, it is only for compilation errors!
 *
 * @author myx
 *
 */
public final class TKV_ERROR_A_C_E extends TokenValue {
	
	private final String value;

	/**
	 * @param value
	 */
	public TKV_ERROR_A_C_E(final String value) {
		assert value != null : "NULL java value";
		this.value = value;
	}

	@Override
	public final String getNotation() {
		
		return "throw " + Format.Ecma.string(this.value);
	}

	@Override
	public final InstructionResult getResultType() {
		
		return InstructionResult.NEVER;
	}

	@Override
	public void toAssembly(final ProgramAssembly assembly, final ModifierArgument argumentA, final ModifierArgument argumentB, final ResultHandlerBasic store) {
		
		/**
		 * zero operands
		 */
		assert argumentA == null;
		assert argumentB == null;

		/**
		 * check store
		 */
		assert store != null;

		assembly.addError(this.value);
	}

	@Override
	public final String toCode() {
		
		return "ERROR\t1\tC  ->S\tCONST('" + this.value + "');";
	}
}
