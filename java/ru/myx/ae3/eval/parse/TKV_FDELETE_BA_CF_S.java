/*
 * Created on 29.10.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.ae3.eval.parse;

import ru.myx.ae3.base.BasePrimitiveString;
import ru.myx.ae3.eval.tokens.TokenValue;
import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.OperationsA10;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandlerBasic;

final class TKV_FDELETE_BA_CF_S extends TokenValue {
	
	private final BasePrimitiveString argumentB;

	/** @param name */
	public TKV_FDELETE_BA_CF_S(final BasePrimitiveString name) {

		this.argumentB = name;
	}

	@Override
	public final String getNotation() {
		
		return "delete " + this.argumentB;
	}

	@Override
	public final InstructionResult getResultType() {
		
		return InstructionResult.BOOLEAN;
	}

	@Override
	public void toAssembly(final ProgramAssembly assembly, final ModifierArgument argumentA, final ModifierArgument argumentB, final ResultHandlerBasic store) {
		
		/** zero operands */
		assert argumentA == null;
		assert argumentB == null;

		/** valid store */
		assert store != null;

		assembly.addInstruction(OperationsA10.XFDELETE_N.instruction(this.argumentB, null, 0, store));
	}

	@Override
	public final String toCode() {
		
		return "FDELETE\t2\tFC ->S\tCONST('" + this.argumentB + "');";
	}
}
