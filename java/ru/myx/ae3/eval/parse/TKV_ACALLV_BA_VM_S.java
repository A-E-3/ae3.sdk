/*
 * Created on 29.10.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.ae3.eval.parse;

import ru.myx.ae3.eval.tokens.TokenInstruction;
import ru.myx.ae3.eval.tokens.TokenValue;
import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.Instructions;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ModifierArguments;
import ru.myx.ae3.exec.OperationsA2X;
import ru.myx.ae3.exec.OperationsS2X;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;

final class TKV_ACALLV_BA_VM_S extends TokenValue {
	
	private final ModifierArgument accessObjectModifier;
	
	private final TokenInstruction accessProperty;
	
	TKV_ACALLV_BA_VM_S(final ModifierArgument accessObjectModifier, final TokenInstruction accessProperty) {
		
		assert accessObjectModifier != null;
		assert accessProperty.assertStackValue();
		assert accessObjectModifier != ModifierArguments.AE21POP && accessObjectModifier != ModifierArguments.AA0RB;
		this.accessObjectModifier = accessObjectModifier;
		this.accessProperty = accessProperty;
	}
	
	@Override
	public final String getNotation() {
		
		return this.accessObjectModifier + "." + this.accessProperty.getNotation() + "()";
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
		
		final ModifierArgument modifierObject = this.accessObjectModifier;
		final boolean directObject = modifierObject == ModifierArguments.AA0RB;

		final ModifierArgument modifierProperty = this.accessProperty.toDirectModifier();
		final boolean directProperty = modifierProperty == ModifierArguments.AA0RB;
		
		if (directObject && directProperty) {
			assembly.addInstruction(Instructions.INSTR_LOAD_1_R_SN_NEXT);
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
				+ "\t0\tVM ->S;";
	}
}
