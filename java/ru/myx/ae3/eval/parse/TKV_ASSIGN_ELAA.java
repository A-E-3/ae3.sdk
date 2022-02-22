/*
 * Created on 11.03.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ru.myx.ae3.eval.parse;

import ru.myx.ae3.eval.tokens.TokenInstruction;
import ru.myx.ae3.eval.tokens.TokenValue;
import ru.myx.ae3.exec.Instruction;
import ru.myx.ae3.exec.InstructionEditable;
import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ModifierArguments;
import ru.myx.ae3.exec.OperationsA01;
import ru.myx.ae3.exec.OperationsA11;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;
import ru.myx.ae3.exec.ResultHandlerDirect;

/** @author myx
 *
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments */
public final class TKV_ASSIGN_ELAA extends TokenValue {
	
	private final TokenInstruction tokenLeft;
	
	private final TokenInstruction tokenRight;
	
	/** @param reference
	 * @param value */
	public TKV_ASSIGN_ELAA(final TokenInstruction reference, final TokenInstruction value) {
		
		assert reference.assertStackValue();
		assert value.assertStackValue();
		this.tokenLeft = reference;
		this.tokenRight = value;
	}
	
	@Override
	public final String getNotation() {
		
		return "(" + this.tokenLeft.getNotation() + " &&= " + this.tokenRight.getNotation() + ")";
	}
	
	@Override
	public final InstructionResult getResultType() {
		
		return this.tokenLeft.getResultType().merge(this.tokenRight.getResultType());
	}
	
	@Override
	public void toAssembly(//
			final ProgramAssembly assembly,
			final ModifierArgument argumentA,
			final ModifierArgument argumentB,
			final ResultHandlerBasic store//
	) {
		
		/** zero operands (two operand is already embedded in this token) */
		assert argumentA == null;
		assert argumentB == null;
		/** valid store */
		assert store != null;
		
		if (store == ResultHandler.FA_BNN_NXT || store == ResultHandler.FU_BNN_NXT) {
			final ModifierArgument modifierReferenced = this.tokenLeft.toReferenceReadBeforeWrite(assembly, null, null, true, true);
			final InstructionEditable skip = modifierReferenced == ModifierArguments.AA0RB
				? OperationsA01.XESKIPRB0_P.instructionCreate(0, ResultHandler.FA_BNN_NXT)
				: OperationsA11.XESKIP0A_P.instructionCreate(modifierReferenced, 0, ResultHandler.FA_BNN_NXT);
			assembly.addInstruction(skip);
			final int rightStart = assembly.size();
			final ModifierArgument modifierValue = this.tokenRight.toDirectModifier();
			if (modifierValue == ModifierArguments.AA0RB) {
				this.tokenRight.toAssembly(assembly, null, null, ResultHandler.FA_BNN_NXT);
			}
			this.tokenLeft.toReferenceWriteAfterRead(assembly, argumentA, argumentB, modifierValue, store);
			final Instruction cleanupOnSkip = this.tokenLeft.toReferenceWriteSkipAfterRead(assembly, argumentA, argumentB, store);
			if (cleanupOnSkip == null) {
				skip.setConstant(assembly.getInstructionCount(rightStart)).setFinished();
				return;
			}
			{
				assembly.addInstruction(OperationsA01.XESKIP_P.instruction(1, ResultHandler.FA_BNN_NXT));
				skip.setConstant(assembly.getInstructionCount(rightStart)).setFinished();
				assembly.addInstruction(cleanupOnSkip);
			}
			return;
		}
		
		if (store instanceof ResultHandlerBasic.ExecutionContinue) {
			final ModifierArgument modifierReferenced = this.tokenLeft.toReferenceReadBeforeWrite(assembly, null, null, true, true);
			
			final ResultHandlerDirect direct = store.execDirectTransportType().handlerForStoreNext();
			
			final ResultHandlerBasic onlyEffects = ((ResultHandlerBasic.ExecutionContinue) store).replaceEffectsOnly();
			if (onlyEffects != null) {
				assembly.addInstruction(
						modifierReferenced == ModifierArguments.AA0RB
							? OperationsA01.XESKIPRB1_P.instruction(0, onlyEffects)
							: OperationsA11.XESKIP1A_P.instruction(modifierReferenced, 0, onlyEffects));
			}
			final InstructionEditable skip = modifierReferenced == ModifierArguments.AA0RB
				? OperationsA01.XESKIPRB0_P.instructionCreate(0, direct)
				: OperationsA11.XESKIP0A_P.instructionCreate(modifierReferenced, 0, direct);
			assembly.addInstruction(skip);
			final int rightStart = assembly.size();
			final ModifierArgument modifierValue = this.tokenRight.toDirectModifier();
			if (modifierValue == ModifierArguments.AA0RB) {
				this.tokenRight.toAssembly(assembly, null, null, ResultHandler.FA_BNN_NXT);
			}
			this.tokenLeft.toReferenceWriteAfterRead(assembly, argumentA, argumentB, modifierValue, store);
			final Instruction cleanupOnSkip = this.tokenLeft.toReferenceWriteSkipAfterRead(assembly, argumentA, argumentB, store);
			if (cleanupOnSkip == null) {
				skip.setConstant(assembly.getInstructionCount(rightStart)).setFinished();
				return;
			}
			{
				assembly.addInstruction(OperationsA01.XESKIP_P.instruction(1, ResultHandler.FA_BNN_NXT));
				skip.setConstant(assembly.getInstructionCount(rightStart)).setFinished();
				assembly.addInstruction(cleanupOnSkip);
			}
			return;
		}
		
		{
			final ModifierArgument modifierReferenced = this.tokenLeft.toReferenceReadBeforeWrite(assembly, null, null, true, true);
			final Instruction cleanupOnSkip = this.tokenLeft.toReferenceWriteSkipAfterRead(assembly, argumentA, argumentB, store);
			if (cleanupOnSkip == null) {
				final InstructionEditable skip = modifierReferenced == ModifierArguments.AA0RB
					? OperationsA01.XESKIPRB0_P.instructionCreate(0, store)
					: OperationsA11.XESKIP0A_P.instructionCreate(modifierReferenced, 0, store);
				assembly.addInstruction(skip);
				final int rightStart = assembly.size();
				final ModifierArgument modifierValue = this.tokenRight.toDirectModifier();
				if (modifierValue == ModifierArguments.AA0RB) {
					this.tokenRight.toAssembly(assembly, null, null, ResultHandler.FA_BNN_NXT);
				}
				this.tokenLeft.toReferenceWriteAfterRead(assembly, argumentA, argumentB, modifierValue, store);
				skip.setConstant(assembly.getInstructionCount(rightStart)).setFinished();
				return;
			}
			{
				final InstructionEditable skip = modifierReferenced == ModifierArguments.AA0RB
					? OperationsA01.XESKIPRB0_P.instructionCreate(0, ResultHandler.FA_BNN_NXT)
					: OperationsA11.XESKIP0A_P.instructionCreate(modifierReferenced, 0, ResultHandler.FA_BNN_NXT);
				assembly.addInstruction(skip);
				final int rightStart = assembly.size();
				final ModifierArgument modifierValue = this.tokenRight.toDirectModifier();
				if (modifierValue == ModifierArguments.AA0RB) {
					this.tokenRight.toAssembly(assembly, null, null, ResultHandler.FA_BNN_NXT);
				}
				this.tokenLeft.toReferenceWriteAfterRead(assembly, argumentA, argumentB, modifierValue, store);
				skip.setConstant(assembly.getInstructionCount(rightStart)).setFinished();
				assembly.addInstruction(cleanupOnSkip);
			}
			return;
		}
		
		/** <code>
		this.tokenLeft.toConditionalSkipSingleton(assembly, TokenInstruction.ConditionType.TRUISH_YES, 0, store);
		this.tokenRight.toAssembly(assembly, null, null, store);
		
		this.operation.toAssembly(
				assembly, //
				modifierReferenced,
				directValue && !directSupport
					? ModifierArguments.AE21POP
					: modifierValue,
				ResultHandler.FA_BNN_NXT);
		
		this.tokenLeft.toReferenceWriteAfterRead(assembly, argumentA, argumentB, ModifierArguments.AA0RB, store);
		</code> */
	}
	
	@Override
	public String toCode() {
		
		return "ASSIGN_ELAA [" + this.tokenLeft + " &&= " + this.tokenRight + "];";
	}
}
