/*
 * Created on 29.10.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.ae3.eval.parse;

import ru.myx.ae3.exec.InstructionEditable;
import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.Instructions;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ModifierArguments;
import ru.myx.ae3.exec.OperationsA01;
import ru.myx.ae3.exec.OperationsA10;
import ru.myx.ae3.exec.OperationsA11;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;
import ru.myx.ae3.exec.ResultHandlerDirect;
import ru.myx.ae3.exec.parse.expression.TokenInstruction;
import ru.myx.ae3.exec.parse.expression.TokenValue;

final class TKV_BNOT extends TokenValue {

	private final TokenInstruction argumentA;

	TKV_BNOT(final TokenInstruction argumentA) {

		assert argumentA.toConstantModifier() == null : "Reduce then!";
		this.argumentA = argumentA;
	}

	@Override
	public final String getNotation() {

		return "(!" + this.argumentA.getNotation() + ')';
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
				assembly.addInstruction(Instructions.INSTR_BNOT_1_R_NN_NEXT);
				return;
			}
			if (store == ResultHandler.FB_BSN_NXT) {
				assembly.addInstruction(Instructions.INSTR_BNOT_1_R_SN_NEXT);
				return;
			}
		} else //
		if (modifierA == ModifierArguments.AE21POP) {
			if (store == ResultHandler.FB_BSN_NXT) {
				assembly.addInstruction(Instructions.INSTR_BNOT_1_S_SN_NEXT);
				return;
			}
		}
		assembly.addInstruction(OperationsA10.XBNOT_N.instruction(modifierA, 0, store));
	}

	@Override
	public void toBooleanConditionalSkip(final ProgramAssembly assembly, final boolean compare, final int constant, final ResultHandler store) {

		if (store != ResultHandler.FU_BNN_NXT) {
			// super.toBooleanConditionalSkip(assembly, compare, constant, store);
			// return;
			final ModifierArgument modifierA = this.argumentA.toDirectModifier();
			if (modifierA != ModifierArguments.AA0RB) {
				assembly.addInstruction(OperationsA10.XBNOT_N.instruction(modifierA, 0, ResultHandler.FA_BNN_NXT));
			} else {
				this.argumentA.toAssembly(assembly, null, null, ResultHandler.FA_BNN_NXT);
				assembly.addInstruction(Instructions.INSTR_BNOT_1_R_NN_NEXT);
			}
			assembly.addInstruction(
					(compare
						/* normal boolean order */
						? OperationsA01.XESKIPRB1_P
						: OperationsA01.XESKIPRB0_P).instruction(constant, store));
			return;
		}

		final ModifierArgument modifierA = this.argumentA.toDirectModifier();
		if (modifierA != ModifierArguments.AA0RB) {
			assembly.addInstruction(
					(compare
						/* inverted order */
						? OperationsA11.XESKIP0A_P
						: OperationsA11.XESKIP1A_P).instruction(modifierA, constant, store));
			return;
		}

		final ResultHandlerDirect direct = store.execDirectTransportType().transportForBooleanCheck().handlerForStoreNext();
		this.argumentA.toAssembly(assembly, null, null, direct);

		if (direct == ResultHandler.FA_BNN_NXT) {
			assembly.addInstruction(
					(compare
						/* inverted order */
						? OperationsA01.XESKIPRB0_P
						: OperationsA01.XESKIPRB1_P).instruction(constant, store));
			return;
		}

		assembly.addInstruction(
				(compare
					/* inverted order */
					? OperationsA11.XESKIP0A_P
					: OperationsA11.XESKIP1A_P).instruction(ModifierArgument.forStore(direct), constant, store));

	}

	@Override
	public InstructionEditable toBooleanConditionalSkip(final ProgramAssembly assembly, final int start, final boolean compare, final ResultHandler store) {

		if (store != ResultHandler.FU_BNN_NXT) {
			// super.toBooleanConditionalSkip(assembly, start, compare, store);
			// return;
			final ModifierArgument modifierA = this.argumentA.toDirectModifier();
			if (modifierA != ModifierArguments.AA0RB) {
				assembly.addInstruction(OperationsA10.XBNOT_N.instruction(modifierA, 0, ResultHandler.FA_BNN_NXT));
			} else {
				this.argumentA.toAssembly(assembly, null, null, ResultHandler.FA_BNN_NXT);
				assembly.addInstruction(Instructions.INSTR_BNOT_1_R_NN_NEXT);
			}
			final InstructionEditable editable = (compare
				/* normal boolean order */
				? OperationsA01.XESKIPRB1_P
				: OperationsA01.XESKIPRB0_P).instructionCreate(0, store);
			assembly.addInstruction(editable);
			return editable;
		}

		final ResultHandlerDirect direct = store.execDirectTransportType().transportForBooleanCheck().handlerForStoreNext();

		final ModifierArgument modifierA = this.argumentA.toDirectModifier();
		if (modifierA != ModifierArguments.AA0RB) {
			final InstructionEditable editable = (compare
				/* inverted order */
				? OperationsA11.XESKIP0A_P
				: OperationsA11.XESKIP1A_P).instructionCreate(modifierA, 0, store);
			assembly.addInstruction(editable);
			return editable;
		}

		this.argumentA.toAssembly(assembly, null, null, direct);

		if (direct == ResultHandler.FA_BNN_NXT) {
			final InstructionEditable editable = (compare
				/* inverted order */
				? OperationsA01.XESKIPRB0_P
				: OperationsA01.XESKIPRB1_P).instructionCreate(0, store);
			assembly.addInstruction(editable);
			return editable;
		}

		final InstructionEditable editable = (compare
			/* inverted order */
			? OperationsA11.XESKIP0A_P
			: OperationsA11.XESKIP1A_P).instructionCreate(ModifierArgument.forStore(direct), 0, store);
		assembly.addInstruction(editable);
		return editable;
	}

	@Override
	public final String toCode() {

		return "BNOT\t1\tV [" + this.argumentA + "]  ->S;";
	}
}
