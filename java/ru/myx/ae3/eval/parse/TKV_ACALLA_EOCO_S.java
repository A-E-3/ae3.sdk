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
import ru.myx.ae3.exec.Instruction;
import ru.myx.ae3.exec.InstructionEditable;
import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ModifierArguments;
import ru.myx.ae3.exec.OperationsA01;
import ru.myx.ae3.exec.OperationsA11;
import ru.myx.ae3.exec.OperationsA2X;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;

final class TKV_ACALLA_EOCO_S extends TokenValue {
	
	private final TokenInstruction accessObject;
	
	private final TokenInstruction accessProperty;
	
	private final TokenInstruction arguments;
	
	private final int constant;
	
	private final Instruction carguments;
	
	final TokenInstruction tokenRight;
	
	TKV_ACALLA_EOCO_S(//
			final TokenInstruction accessObject,
			final TokenInstruction accessProperty,
			final TokenInstruction arguments,
			final int constant,
			final Instruction carguments,
			final TokenInstruction tokenRight) {
		
		assert accessObject != ParseConstants.TKV_FRAME : "Use " + TKV_FCALLA_EOCO_S.class.getSimpleName() + " then!";
		
		assert arguments == null || arguments.assertZeroStackOperands();
		assert arguments == null
			? 0 == constant
			: arguments.getResultCount() == constant;
		
		assert carguments == null || arguments != null;
		
		this.accessObject = accessObject;
		this.accessProperty = accessProperty;
		this.arguments = arguments;
		this.constant = constant;
		this.carguments = carguments;
		
		this.tokenRight = tokenRight;
	}
	
	/** @param assembly
	 * @param store
	 * @throws CompilationException */
	private void encodeCALL(final ProgramAssembly assembly, final ResultHandlerBasic store) throws CompilationException {
		
		if (this.arguments != null) {
			this.arguments.toAssembly(assembly, null, null, ResultHandler.FB_BSN_NXT);
		}
		
		if (this.carguments == null) {
			if (this.tokenRight == null || this.tokenRight == ParseConstants.TKV_DIRECT) {
				assembly.addInstruction(OperationsA01.XRCALLA.instruction(this.constant, store));
			} else {
				assembly.addInstruction(OperationsA01.XRCALLA.instruction(this.constant, ResultHandler.FA_BNN_NXT));
				this.tokenRight.toAssembly(assembly, null, null, store);
			}
		} else {
			assembly.addInstruction(this.carguments);
			/** TODO: XRCALLM **/
			assembly.addInstruction(OperationsA01.XRCALLA.instruction(this.constant, store));
		}
	}
	
	@Override
	public final String getNotation() {
		
		if (this.arguments == null) {
			return this.accessObject.getNotation() + this.accessProperty.getNotationAccess() + "?.()";
		}
		return this.accessObject.getNotation() + this.accessProperty.getNotationAccess() + "?.(" + this.arguments.getNotation() + ")";
	}
	
	@Override
	public final InstructionResult getResultType() {
		
		return InstructionResult.OBJECT;
	}

	@Override
	public void toAssembly(final ProgramAssembly assembly, final ModifierArgument argumentA, final ModifierArgument argumentB, final ResultHandlerBasic store) {
		
		assert argumentA == null;
		assert argumentB == null;
		
		final TokenInstruction accessObject = this.accessObject;
		
		final TokenInstruction accessProperty = this.accessProperty;
		final ModifierArgument modifierProperty = accessProperty.toDirectModifier();
		final boolean directProperty = modifierProperty == ModifierArguments.AA0RB;
		
		if (store == ResultHandler.FA_BNN_NXT || store == ResultHandler.FU_BNN_NXT) {
			/** get thisValue to stack **/
			accessObject.toAssembly(assembly, null, null, ResultHandler.FB_BSN_NXT);
			
			if (directProperty) {
				accessProperty.toAssembly(assembly, null, null, ResultHandler.FA_BNN_NXT);
			}
			
			final InstructionEditable skip = OperationsA11.XNCALLSKIP.instructionCreate(
					directProperty
						? ModifierArguments.AA0RB
						: modifierProperty,
					0,
					ResultHandler.FB_BSN_NXT);
			assembly.addInstruction(skip);
			final int rightStart = assembly.size();
			
			this.encodeCALL(assembly, store);
			
			skip.setConstant(assembly.getInstructionCount(rightStart)).setFinished();
			return;
		}
		
		if (store instanceof ResultHandlerBasic.ExecutionContinue) {
			/** get thisValue to stack **/
			accessObject.toAssembly(assembly, null, null, ResultHandler.FB_BSN_NXT);
			
			if (directProperty) {
				accessProperty.toAssembly(assembly, null, null, ResultHandler.FA_BNN_NXT);
			}
			
			final InstructionEditable skip = OperationsA11.XNCALLSKIP.instructionCreate(
					directProperty
						? ModifierArguments.AA0RB
						: modifierProperty,
					0,
					ResultHandler.FB_BSN_NXT);
			assembly.addInstruction(skip);
			final int rightStart = assembly.size();
			
			this.encodeCALL(assembly, ResultHandler.FA_BNN_NXT);
			
			skip.setConstant(assembly.getInstructionCount(rightStart)).setFinished();
			
			ParseConstants.TKV_DIRECT.toAssembly(assembly, null, null, store);
			return;
		}
		
		{
			final ModifierArgument modifierObject = accessObject.toDirectModifier();
			final boolean directObject = modifierObject == ModifierArguments.AA0RB;
			if (directObject) {
				accessObject.toAssembly(
						assembly,
						null,
						null,
						directProperty
							? ResultHandler.FB_BSN_NXT
							: ResultHandler.FA_BNN_NXT);
			}
			if (directProperty) {
				accessProperty.toAssembly(assembly, null, null, ResultHandler.FA_BNN_NXT);
			}
			assembly.addInstruction(
					OperationsA2X.XNCALLPREP.instruction(
							directObject && directProperty
								? ModifierArguments.AE21POP
								: modifierObject,
							modifierProperty,
							0,
							store)//
			);
			
			this.encodeCALL(assembly, store);
			return;
		}
	}
	
	@Override
	public final String toCode() {
		
		return "XACALLA_EOCO\t0+" + this.constant + "\tVS ->S;";
	}
}
