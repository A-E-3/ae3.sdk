/*
 * Created on 29.10.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.ae3.eval.parse;

import ru.myx.ae3.base.BasePrimitiveString;
import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.OperationsA10;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandlerBasic;
import ru.myx.ae3.exec.parse.expression.TokenValue;

/**
 * @author myx
 *
 */
public final class TKV_FRAMEPOSTINC extends TokenValue {
	
	private final BasePrimitiveString name;

	/**
	 * @param name
	 */
	public TKV_FRAMEPOSTINC(final BasePrimitiveString name) {
		this.name = name;
	}

	@Override
	public final String getNotation() {
		
		return this.name + "++";
	}

	@Override
	public final InstructionResult getResultType() {
		
		return InstructionResult.NUMBER;
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

		assembly.addInstruction(OperationsA10.XFSGETADD_N.instruction(ParseConstants.getConstantValue(this.name).toConstantModifier(), +1, store));
	}

	@Override
	public String toCode() {
		
		return "INCPOST\tCONT\tS\tD--\t$" + this.name;
	}
}
