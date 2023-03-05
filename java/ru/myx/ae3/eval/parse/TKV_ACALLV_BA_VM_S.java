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
	
	private final TokenInstruction argumentB;
	
	TKV_ACALLV_BA_VM_S(final ModifierArgument accessObjectModifier, final TokenInstruction argumentB) {
		
		assert accessObjectModifier != null;
		assert argumentB.assertStackValue();
		assert accessObjectModifier != ModifierArguments.AE21POP && accessObjectModifier != ModifierArguments.AA0RB;
		this.accessObjectModifier = accessObjectModifier;
		this.argumentB = argumentB;
	}
	
	@Override
	public final String getNotation() {
		
		return this.accessObjectModifier + "." + this.argumentB.getNotation() + "()";
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
		
		final ModifierArgument modifierA = this.accessObjectModifier;
		final boolean directA = modifierA == ModifierArguments.AA0RB;

		final ModifierArgument modifierB = this.argumentB.toDirectModifier();
		final boolean directB = modifierB == ModifierArguments.AA0RB;
		
		if (directA && directB) {
			assembly.addInstruction(Instructions.INSTR_LOAD_1_R_SN_NEXT);
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
				+ "\t0\tVM ->S;";
	}
}
