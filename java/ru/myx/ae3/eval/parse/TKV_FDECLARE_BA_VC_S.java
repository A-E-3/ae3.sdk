/*
 * Created on 29.10.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.ae3.eval.parse;

import static ru.myx.ae3.exec.ModifierArguments.AA0RB;
import static ru.myx.ae3.exec.ModifierArguments.AE21POP;


import ru.myx.ae3.base.BasePrimitiveString;
import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.OperationsA2X;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;
import ru.myx.ae3.exec.parse.expression.TokenInstruction;
import ru.myx.ae3.exec.parse.expression.TokenValue;

/**
 * @author myx
 *
 */
public final class TKV_FDECLARE_BA_VC_S extends TokenValue {
	
	private final TokenInstruction argumentB;

	private final BasePrimitiveString argumentA;

	/**
	 * @param argumentA
	 * @param argumentB
	 */
	public TKV_FDECLARE_BA_VC_S(final BasePrimitiveString argumentA, final TokenInstruction argumentB) {
		assert argumentB.assertStackValue();
		this.argumentA = argumentA;
		this.argumentB = argumentB.toExecDetachableResult();
	}

	@Override
	public final String getNotation() {
		
		return this.argumentA + " = " + this.argumentB.getNotation();
	}

	@Override
	public final InstructionResult getResultType() {
		
		return this.argumentB.getResultType();
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

		final ModifierArgument modifierA = this.argumentB.toDirectModifier();

		if (modifierA == AA0RB) {
			if (this.argumentB.toPreferStackResult()) {
				this.argumentB.toAssembly(assembly, null, null, ResultHandler.FB_BSN_NXT);
				assembly.addInstruction(OperationsA2X.XFDECLARE_D.instruction(this.argumentA, null, AE21POP, 0, store));
				return;
			}
			this.argumentB.toAssembly(assembly, null, null, ResultHandler.FA_BNN_NXT);
		}

		assembly.addInstruction(OperationsA2X.XFDECLARE_D.instruction(this.argumentA, null, modifierA, 0, store));
	}

	@Override
	public final String toCode() {
		
		return "FDECLARE_T\t2\tVC->S\tCONST('" + this.argumentA + " = " + this.argumentB + "');";
	}
}
