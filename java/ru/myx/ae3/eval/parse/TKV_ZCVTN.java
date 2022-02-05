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
import ru.myx.ae3.exec.OperationsA10;
import ru.myx.ae3.exec.OperationsS10;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;

final class TKV_ZCVTN extends TokenValue {
	
	private final TokenInstruction argumentA;

	private int visibility;

	TKV_ZCVTN(final TokenInstruction argumentA, final int visibility) {
		assert argumentA.toConstantModifier() == null : "Reduce then!";
		this.argumentA = argumentA;
		this.visibility = visibility;
	}

	@Override
	public final String getNotation() {
		
		return "+" + this.argumentA.getNotationValue();
	}

	@Override
	public final InstructionResult getResultType() {
		
		return this.argumentA.getResultType() == InstructionResult.INTEGER
			? InstructionResult.INTEGER
			: InstructionResult.NUMBER;
	}

	@Override
	public void toAssembly(final ProgramAssembly assembly, final ModifierArgument argumentA, final ModifierArgument argumentB, final ResultHandlerBasic store) {
		
		/**
		 * zero operands (one operand is already embedded in this token)
		 */
		assert argumentA == null;
		assert argumentB == null;

		/**
		 * valid store
		 */
		assert store != null;

		/**
		 * flush all values to assembly
		 */
		final ModifierArgument modifierA = this.argumentA.toDirectModifier();
		if (modifierA == ModifierArguments.AA0RB) {
			this.argumentA.toAssembly(assembly, null, null, ResultHandler.FA_BNN_NXT);
			if (store == ResultHandler.FA_BNN_NXT) {
				assembly.addInstruction(this.visibility == 2
					? Instructions.INSTR_CVTN_N_1_R_NN_NEXT
					: this.visibility == 1
						? Instructions.INSTR_CVTN_D_1_R_NN_NEXT
						: Instructions.INSTR_CVTN_T_1_R_NN_NEXT);
				return;
			}
			if (store == ResultHandler.FB_BSN_NXT) {
				assembly.addInstruction(this.visibility == 2
					? Instructions.INSTR_CVTN_N_1_R_SN_NEXT
					: Instructions.INSTR_CVTN_D_1_R_SN_NEXT);
				return;
			}
		} else //
		if (modifierA == ModifierArguments.AE21POP) {
			if (store == ResultHandler.FB_BSN_NXT) {
				assembly.addInstruction(this.visibility == 2
					? Instructions.INSTR_CVTN_N_1_S_SN_NEXT
					: Instructions.INSTR_CVTN_D_1_S_SN_NEXT);
				return;
			}
		}
		assembly.addInstruction((this.visibility == 2
			? OperationsS10.VCVTN_N
			: this.visibility == 1
				? OperationsS10.VCVTN_D
				: OperationsA10.ZCVTN_T)//
						.instruction(modifierA, 0, store));
	}

	@Override
	public final String toCode() {
		
		return (this.visibility == 2
			? OperationsS10.VCVTN_N
			: this.visibility == 1
				? OperationsS10.VCVTN_D
				: OperationsA10.ZCVTN_T) + "\t1\tV [" + this.argumentA + "]  ->S;";
	}

	@Override
	public TokenInstruction toExecDetachableResult() {
		
		this.visibility = 1;
		return this;
	}

	@Override
	public TokenInstruction toExecNativeResult() {
		
		this.visibility = 2;
		return this;
	}
}
