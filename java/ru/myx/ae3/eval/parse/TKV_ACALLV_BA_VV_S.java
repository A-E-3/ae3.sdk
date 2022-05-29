/*
 * Created on 09.03.2004 To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ru.myx.ae3.eval.parse;

import ru.myx.ae3.eval.tokens.TokenInstruction;
import ru.myx.ae3.eval.tokens.TokenValue;
import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ModifierArguments;
import ru.myx.ae3.exec.OperationsA2X;
import ru.myx.ae3.exec.OperationsS2X;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;

final class TKV_ACALLV_BA_VV_S extends TokenValue {
	
	private final TokenInstruction accessObject;
	
	private final TokenInstruction argumentB;
	
	TKV_ACALLV_BA_VV_S(final TokenInstruction accessObject, final TokenInstruction argumentB) {
		
		assert accessObject.isStackValue();
		assert argumentB.isStackValue();
		this.accessObject = accessObject;
		this.argumentB = argumentB;
	}
	
	@Override
	public final String getNotation() {
		
		return this.accessObject.getNotationValue() + "[" + this.argumentB.getNotation() + "]()";
	}
	
	@Override
	public final InstructionResult getResultType() {
		
		return InstructionResult.OBJECT;
	}
	
	@Override
	public void toAssembly(final ProgramAssembly assembly, final ModifierArgument argumentA, final ModifierArgument argumentB, final ResultHandlerBasic store) {
		
		/** zero operands */
		assert argumentA == null;
		assert argumentB == null;
		
		final ModifierArgument modifierA = this.accessObject.toDirectModifier();
		final ModifierArgument modifierB = this.argumentB.toDirectModifier();
		final boolean directA = modifierA == ModifierArguments.AA0RB;
		final boolean directB = modifierB == ModifierArguments.AA0RB;
		if (directA) {
			this.accessObject.toAssembly(
					assembly,
					null,
					null,
					directB
						? ResultHandler.FB_BSN_NXT
						: ResultHandler.FA_BNN_NXT);
		}
		if (directB) {
			this.argumentB.toAssembly(assembly, null, null, ResultHandler.FA_BNN_NXT);
		}
		assembly.addInstruction(
				(this.argumentB.getResultType() == InstructionResult.STRING
					? OperationsS2X.VACALLS_XS
					: OperationsA2X.XACALLS)//
							.instruction(
									directA && directB
										? ModifierArguments.AE21POP
										: modifierA,
									modifierB,
									0,
									store));
	}
	
	@Override
	public final String toCode() {
		
		return (this.argumentB.getResultType() == InstructionResult.STRING
			? OperationsS2X.VACALLS_XS
			: OperationsA2X.XACALLS)//
				+ "\t0\tVV ->S\t[" + this.accessObject + ", " + this.argumentB + "];";
	}
}
