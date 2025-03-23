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
	
	private final TokenInstruction accessProperty;
	
	TKV_ACALLV_BA_VV_S(final TokenInstruction accessObject, final TokenInstruction accessProperty) {
		
		assert accessObject.isStackValue();
		assert accessProperty.isStackValue();
		this.accessObject = accessObject;
		this.accessProperty = accessProperty;
	}
	
	@Override
	public final String getNotation() {
		
		return this.accessObject.getNotationValue() + "[" + this.accessProperty.getNotation() + "]()";
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
		
		final ModifierArgument modifierObject = this.accessObject.toDirectModifier();
		final ModifierArgument modifierProperty = this.accessProperty.toDirectModifier();
		
		final boolean directObject = modifierObject == ModifierArguments.AA0RB;
		final boolean directProperty = modifierProperty == ModifierArguments.AA0RB;
		
		if (directObject) {
			this.accessObject.toAssembly(
					assembly,
					null,
					null,
					directProperty
						? ResultHandler.FB_BSN_NXT
						: ResultHandler.FA_BNN_NXT);
		}
		if (directProperty) {
			this.accessProperty.toAssembly(assembly, null, null, ResultHandler.FA_BNN_NXT);
		}
		assembly.addInstruction(
				(this.accessProperty.getResultType() == InstructionResult.STRING
					? OperationsS2X.VACALLS_XS
					: OperationsA2X.XACALLS)//
							.instruction(
									directObject && directProperty
										? ModifierArguments.AE21POP
										: modifierObject,
									modifierProperty,
									0,
									store));
	}
	
	@Override
	public final String toCode() {
		
		return (this.accessProperty.getResultType() == InstructionResult.STRING
			? OperationsS2X.VACALLS_XS
			: OperationsA2X.XACALLS)//
				+ "\t0\tVV ->S\t[" + this.accessObject + ", " + this.accessProperty + "];";
	}
}
