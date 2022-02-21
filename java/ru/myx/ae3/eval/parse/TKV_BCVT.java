/*
 * Created on 29.10.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.ae3.eval.parse;

import ru.myx.ae3.eval.tokens.TokenInstruction;
import ru.myx.ae3.eval.tokens.TokenValue;
import ru.myx.ae3.exec.InstructionEditable;
import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.Instructions;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ModifierArguments;
import ru.myx.ae3.exec.OperationsA10;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;
import ru.myx.ae3.exec.ResultHandlerDirect;

final class TKV_BCVT extends TokenValue {
	
	private final TokenInstruction argumentA;
	
	TKV_BCVT(final TokenInstruction argumentA) {
		
		assert argumentA.toConstantModifier() == null : "Reduce then!";
		this.argumentA = argumentA;
	}
	
	@Override
	public final String getNotation() {
		
		return "(!!" + this.argumentA.getNotationValue() + ')';
	}
	
	@Override
	public final InstructionResult getResultType() {
		
		return InstructionResult.BOOLEAN;
	}
	
	@Override
	public void toAssembly(final ProgramAssembly assembly, final ModifierArgument argumentA, final ModifierArgument argumentB, final ResultHandlerBasic store) {
		
		/** zero operands (one operand is already embedded in this token) */
		assert argumentA == null;
		assert argumentB == null;
		
		/** valid store */
		assert store != null;
		
		/** flush all values to assembly */
		final ModifierArgument modifierA = this.argumentA.toDirectModifier();
		if (modifierA == ModifierArguments.AA0RB) {
			this.argumentA.toAssembly(assembly, null, null, ResultHandler.FA_BNN_NXT);
			if (store == ResultHandler.FA_BNN_NXT) {
				assembly.addInstruction(Instructions.INSTR_BCVT_1_R_NN_NEXT);
				return;
			}
			if (store == ResultHandler.FB_BSN_NXT) {
				assembly.addInstruction(Instructions.INSTR_BCVT_1_R_SN_NEXT);
				return;
			}
		} else //
		if (modifierA == ModifierArguments.AE21POP) {
			if (store == ResultHandler.FB_BSN_NXT) {
				assembly.addInstruction(Instructions.INSTR_BCVT_1_S_SN_NEXT);
				return;
			}
		}
		assembly.addInstruction(OperationsA10.XBCVT_N.instruction(modifierA, 0, store));
	}
	
	@Override
	public final String toCode() {
		
		return "BCVT\t1\tV [" + this.argumentA + "]  ->S;";
	}
	
	@Override
	public InstructionEditable toConditionalSkipEditable(final ProgramAssembly assembly, final int start, final TokenInstruction.ConditionType compare, final ResultHandler store) {
		
		if (store != ResultHandler.FU_BNN_NXT) {
			// super.toBooleanConditionalSkip(assembly, start, compare, store);
			// return;
			final ModifierArgument modifierA = this.argumentA.toDirectModifier();
			if (modifierA != ModifierArguments.AA0RB) {
				assembly.addInstruction(OperationsA10.XBCVT_N.instruction(modifierA, 0, ResultHandler.FA_BNN_NXT));
			} else {
				this.argumentA.toAssembly(assembly, null, null, ResultHandler.FA_BNN_NXT);
				assembly.addInstruction(Instructions.INSTR_BCVT_1_R_NN_NEXT);
			}
			/* normal boolean order */
			final InstructionEditable editable = compare.createEditableDirect(store);
			assembly.addInstruction(editable);
			return editable;
		}
		
		final ResultHandlerDirect direct = store.execDirectTransportType().transportForBooleanCheck().handlerForStoreNext();

		final ModifierArgument modifierA = this.argumentA.toDirectModifier();
		if (modifierA != ModifierArguments.AA0RB) {
			/* normal boolean order */
			final InstructionEditable editable = compare.createEditable(modifierA, store);
			assembly.addInstruction(editable);
			return editable;
		}

		this.argumentA.toAssembly(assembly, null, null, direct);

		/* normal boolean order */
		final InstructionEditable editable = compare.createEditable(direct, store);
		assembly.addInstruction(editable);
		return editable;
	}
	
	@Override
	public void toConditionalSkipSingleton(final ProgramAssembly assembly, final TokenInstruction.ConditionType compare, final int constant, final ResultHandler store) {

		if (store != ResultHandler.FU_BNN_NXT) {
			// super.toBooleanConditionalSkip(assembly, compare, constant, store);
			// return;
			final ModifierArgument modifierA = this.argumentA.toDirectModifier();
			if (modifierA != ModifierArguments.AA0RB) {
				assembly.addInstruction(OperationsA10.XBCVT_N.instruction(modifierA, 0, ResultHandler.FA_BNN_NXT));
			} else {
				this.argumentA.toAssembly(assembly, null, null, ResultHandler.FA_BNN_NXT);
				assembly.addInstruction(Instructions.INSTR_BCVT_1_R_NN_NEXT);
			}
			/* normal boolean order */
			assembly.addInstruction(compare.createSingletonDirect(constant, store));
			return;
		}

		final ModifierArgument modifierA = this.argumentA.toDirectModifier();
		if (modifierA != ModifierArguments.AA0RB) {
			/* normal boolean order */
			assembly.addInstruction(compare.createSingleton(modifierA, constant, store));
			return;
		}

		final ResultHandlerDirect direct = store.execDirectTransportType().transportForBooleanCheck().handlerForStoreNext();
		this.argumentA.toAssembly(assembly, null, null, direct);

		/* normal boolean order */
		assembly.addInstruction(compare.createSinglton(direct, constant, store));
	}
}
