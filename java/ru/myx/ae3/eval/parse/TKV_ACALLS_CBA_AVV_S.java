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
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ModifierArguments;
import ru.myx.ae3.exec.OperationsA2X;
import ru.myx.ae3.exec.OperationsS2X;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;

final class TKV_ACALLS_CBA_AVV_S extends TokenValue {
	
	private final TokenInstruction accessObject;
	
	private final TokenInstruction accessProperty;
	
	private final TokenInstruction arguments;
	
	private final int constant;
	
	TKV_ACALLS_CBA_AVV_S(final TokenInstruction argumentA, final TokenInstruction argumentB, final TokenInstruction argument, final int constant) {
		assert argumentA.assertStackValue();
		assert argumentB.assertStackValue();
		assert argument.assertZeroStackOperands();
		assert argument.getResultCount() == constant;
		assert constant > 1 : constant == 0
			? "Use " + TKV_ACALLV_BA_VM_S.class.getSimpleName() + " then"
			: "Use " + TKV_ACALLO_CBA_AVM_S.class.getSimpleName() + " then";
		this.accessObject = argumentA;
		this.accessProperty = argumentB;
		this.arguments = argument;
		this.constant = constant;
	}
	
	@Override
	public final String getNotation() {
		
		return "" + this.accessObject + "." + this.accessProperty.getNotation() + "( " + this.arguments.getNotation() + " )";
	}
	
	@Override
	public final InstructionResult getResultType() {
		
		return InstructionResult.OBJECT;
	}
	
	@Override
	public void toAssembly(final ProgramAssembly assembly, final ModifierArgument argumentA, final ModifierArgument argumentB, final ResultHandlerBasic store) {
		
		assert argumentA == null;
		assert argumentB == null;
		
		final ModifierArgument modifierA = this.accessObject.toDirectModifier();
		final boolean directA = modifierA == ModifierArguments.AA0RB;
		
		/**
		 * arguments will go first for ACALLS
		 */
		/**
		 * Anyway in stack, constant is expected to be more than zero
		 */
		this.arguments.toAssembly(assembly, null, null, ResultHandler.FB_BSN_NXT);
		
		if (directA) {
			this.accessObject.toAssembly(
					assembly, //
					null,
					null,
					ResultHandler.FB_BSN_NXT);
		}
		
		final ModifierArgument modifierB = this.accessProperty.toDirectModifier();
		final boolean directB = modifierB == ModifierArguments.AA0RB;
		if (directB) {
			this.accessProperty.toAssembly(
					assembly, //
					null,
					null,
					ResultHandler.FA_BNN_NXT);
		}
		
		assembly.addInstruction((this.accessProperty.getResultType() == InstructionResult.STRING
			? OperationsS2X.VACALLS_XS
			: OperationsA2X.XACALLS)//
					.instruction(
							directA
								? ModifierArguments.AE21POP
								: modifierA, //
							modifierB,
							this.constant,
							store));
	}
	
	@Override
	public final String toCode() {
		
		return (this.accessProperty.getResultType() == InstructionResult.STRING
			? OperationsS2X.VACALLS_XS
			: OperationsA2X.XACALLS)//
				+ "\t0+" + this.constant + "\tAVM->S;";
	}
}
