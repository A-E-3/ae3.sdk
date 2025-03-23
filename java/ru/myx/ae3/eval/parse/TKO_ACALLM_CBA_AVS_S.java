/*
 * Created on 29.10.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.ae3.eval.parse;

import ru.myx.ae3.eval.tokens.TokenInstruction;
import ru.myx.ae3.eval.tokens.TokenOperator;
import ru.myx.ae3.exec.Instruction;
import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ModifierArguments;
import ru.myx.ae3.exec.OperationsA3X;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;

final class TKO_ACALLM_CBA_AVS_S extends TokenOperator {

	private final TokenInstruction accessProperty;
	
	private final TokenInstruction arguments;
	
	private final Instruction carguments;
	
	TKO_ACALLM_CBA_AVS_S(final TokenInstruction accessProperty, final TokenInstruction arguments, final Instruction carguments) {
		
		assert carguments != null;
		assert arguments.assertZeroStackOperands();
		assert arguments.getResultCount() == carguments.getOperandCount();
		assert accessProperty.assertStackValue();
		this.accessProperty = accessProperty;
		this.arguments = arguments;
		this.carguments = carguments;
	}
	
	@Override
	public final String getNotation() {

		return this.accessProperty.getNotationAccess() + "( " + this.arguments.getNotation() + " )";
	}
	
	@Override
	public final int getOperandCount() {

		return 1;
	}
	
	@Override
	public final int getPriorityLeft() {

		return 999;
	}
	
	@Override
	public final int getPriorityRight() {

		return 999;
	}
	
	@Override
	public final InstructionResult getResultType() {

		return InstructionResult.OBJECT;
	}
	
	@Override
	public boolean isDirectSupported() {

		return false;
	}
	
	@Override
	public boolean isParseValueRight() {

		return true;
	}
	
	@Override
	public void toAssembly(final ProgramAssembly assembly, final ModifierArgument argumentA, final ModifierArgument argumentB, final ResultHandlerBasic store) {

		/** argumentA is access object */
		assert argumentA != null;
		assert argumentB == null;
		
		assert argumentA != ModifierArguments.AA0RB : "this.isDirectSupported: " + this.isDirectSupported();

		final ModifierArgument modifierB = this.accessProperty.toDirectModifier();
		final boolean directB = modifierB == ModifierArguments.AA0RB;
		if (directB) {
			this.accessProperty.toAssembly(assembly, null, null, ResultHandler.FB_BSN_NXT);
		}
		
		this.arguments.toAssembly(assembly, null, null, ResultHandler.FB_BSN_NXT);
		assembly.addInstruction(this.carguments);
		assembly.addInstruction(
				OperationsA3X.XACALLM//
						.instruction(
								argumentA, //
								directB
									? ModifierArguments.AE21POP
									: modifierB,
								ModifierArguments.AA0RB,
								0,
								store));
		// assert false : "this=" + assembly.toProgram( start ).toCode();
	}
	
	@Override
	public final String toCode() {

		return OperationsA3X.XACALLM + "\t1\tAVS->S;";
	}
	
	@Override
	public TokenInstruction toStackValue(final ProgramAssembly assembly, final TokenInstruction argumentA, final boolean sideEffectsOnly) {
		
		if (argumentA.toDirectModifier() == ModifierArguments.AB4CT) {
			return new TKV_ZTCALLM_BA_AV_S(this.accessProperty, this.arguments, this.carguments);
		}
		if (argumentA.toDirectModifier() == ModifierArguments.AB7FV) {
			return new TKV_FCALLM_BA_AV_S(this.accessProperty, this.arguments, this.carguments);
		}
		return new TKV_ACALLM_CBA_AVV_S(argumentA.toExecDetachableResult(), this.accessProperty, this.arguments, this.carguments);
	}
}
