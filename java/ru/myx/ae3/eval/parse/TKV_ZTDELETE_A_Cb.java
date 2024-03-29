/*
 * Created on 29.10.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.ae3.eval.parse;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.ecma.Ecma;
import ru.myx.ae3.eval.tokens.TokenValue;
import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.OperationsA10;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandlerBasic;

final class TKV_ZTDELETE_A_Cb extends TokenValue {
	
	private final BaseObject argumentB;

	/**
	 * @param name
	 */
	public TKV_ZTDELETE_A_Cb(final BaseObject name) {
		this.argumentB = name;
	}

	@Override
	public final String getNotation() {
		
		return "delete this[" + Ecma.toEcmaSourceCompact(this.argumentB) + ']';
	}

	@Override
	public final InstructionResult getResultType() {
		
		return InstructionResult.BOOLEAN;
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

		assembly.addInstruction(OperationsA10.ZTDELETE_N.instruction(this.argumentB, null, 0, store));
	}

	@Override
	public final String toCode() {
		
		return "DELETE\t2\tTC ->S\tCONST('" + this.argumentB + "');";
	}
}
