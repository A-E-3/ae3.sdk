/*
 * Created on 29.10.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.ae3.eval.parse;

import ru.myx.ae3.eval.Evaluate.CompilationException;
import ru.myx.ae3.eval.tokens.TokenInstruction;
import ru.myx.ae3.eval.tokens.TokenValue;
import ru.myx.ae3.exec.InstructionEditable;
import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ModifierArguments;
import ru.myx.ae3.exec.OperationsA01;
import ru.myx.ae3.exec.OperationsA10;
import ru.myx.ae3.exec.OperationsA11;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;

final class TKV_FCALLV_EOCO_S extends TokenValue {

	private final TokenInstruction access;

	final TokenInstruction tokenRight;

	TKV_FCALLV_EOCO_S(//
			final TokenInstruction access,
			final TokenInstruction tokenRight) {

		assert access.assertStackValue();
		assert access.isAccessReference();
		assert ParseConstants.TKV_FRAME == access.toReferenceObject();

		this.access = access;

		this.tokenRight = tokenRight;
	}

	/** @param assembly
	 * @param store
	 * @throws CompilationException */
	private void encodeCALL(final ProgramAssembly assembly, final ResultHandlerBasic store) throws CompilationException {

		if (this.tokenRight == null || this.tokenRight == ParseConstants.TKV_DIRECT) {
			/** TODO: change for FCALL callee no stack **/
			assembly.addInstruction(OperationsA01.XFCALLA.instruction(0, store));
		} else {
			/** TODO: change for FCALL callee no stack **/
			assembly.addInstruction(OperationsA01.XFCALLA.instruction(0, ResultHandler.FA_BNN_NXT));
			this.tokenRight.toAssembly(assembly, ModifierArguments.AA0RB, null, store);
		}
	}

	@Override
	public final String getNotation() {

		return this.access.getNotation() + "?.()";
	}

	@Override
	public final InstructionResult getResultType() {

		return InstructionResult.OBJECT;
	}
	
	@Override
	public void toAssembly(final ProgramAssembly assembly, final ModifierArgument argumentA, final ModifierArgument argumentB, final ResultHandlerBasic store) {

		assert argumentA == null;
		assert argumentB == null;

		/** get thisValue to stack **/
		/** current 'thisValue' instead of FV **/
		// assembly.addInstruction(Instructions.INSTR_LOAD_1_T_SN_NEXT);

		final TokenInstruction accessProperty = this.access;
		final ModifierArgument modifierProperty = accessProperty.toDirectModifier();
		final boolean directProperty = modifierProperty == ModifierArguments.AA0RB;

		if (directProperty) {
			accessProperty.toAssembly(assembly, null, null, ResultHandler.FA_BNN_NXT);
		}

		if (store == ResultHandler.FA_BNN_NXT || store == ResultHandler.FU_BNN_NXT) {
			final InstructionEditable skip = OperationsA11.XNSKIP0A_P.instructionCreate(modifierProperty, 0, ResultHandler.FB_BSN_NXT);
			assembly.addInstruction(skip);
			final int rightStart = assembly.size();

			this.encodeCALL(assembly, store);

			skip.setConstant(assembly.getInstructionCount(rightStart)).setFinished();
			return;
		}

		if (store instanceof ResultHandlerBasic.ExecutionContinue) {
			final InstructionEditable skip = OperationsA11.XNSKIP0A_P.instructionCreate(modifierProperty, 0, ResultHandler.FB_BSN_NXT);
			assembly.addInstruction(skip);
			final int rightStart = assembly.size();

			this.encodeCALL(assembly, ResultHandler.FA_BNN_NXT);

			skip.setConstant(assembly.getInstructionCount(rightStart)).setFinished();
			
			ParseConstants.TKV_DIRECT.toAssembly(assembly, null, null, store);
			return;
		}
		
		{
			assembly.addInstruction(
					OperationsA10.XFCALLPREP.instruction(
							directProperty
								? ModifierArguments.AA0RB
								: modifierProperty,
							0,
							store)//
			);
			
			this.encodeCALL(assembly, store);
		}
	}

	@Override
	public final String toCode() {

		return "XFCALLV_OCO\t0+1\tVS ->S;";
	}
}
