package ru.myx.ae3.eval.parse;

import ru.myx.ae3.eval.tokens.TokenInstruction;
import ru.myx.ae3.eval.tokens.TokenValue;
import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.Instructions;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ModifierArgumentA30IMM;
import ru.myx.ae3.exec.ModifierArguments;
import ru.myx.ae3.exec.OperationsA2X;
import ru.myx.ae3.exec.OperationsS2X;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;

class TKV_ACCESSPOSTINC extends TokenValue {
	
	private final TokenInstruction reference;

	private int visibility;

	public TKV_ACCESSPOSTINC(final TokenInstruction reference, final int visibility) {

		assert reference.isAccessReference();
		assert reference.isStackValue();
		this.reference = reference;
		this.visibility = visibility;
	}

	@Override
	public String getNotation() {
		
		return this.reference.getNotation() + "++ ";
	}

	@Override
	public final InstructionResult getResultType() {
		
		return InstructionResult.NUMBER;
	}

	@Override
	public void toAssembly(final ProgramAssembly assembly, final ModifierArgument argumentA, final ModifierArgument argumentB, final ResultHandlerBasic store) {
		
		/** zero operands */
		assert argumentA == null;
		assert argumentB == null;

		/** valid store */
		assert store != null;

		final ModifierArgument modifierReferenced = this.reference.toReferenceReadBeforeWrite(assembly, null, null, true, true, false);
		assembly.addInstruction(
				modifierReferenced == ModifierArguments.AA0RB
					? Instructions.INSTR_MADDN_D_BA_1R_V
					: OperationsS2X.VMADDN_N.instruction(modifierReferenced, ModifierArgumentA30IMM.ONE, 0, ResultHandler.FA_BNN_NXT));
		this.reference.toReferenceWriteAfterRead(assembly, null, null, ModifierArguments.AA0RB, false, ResultHandler.FA_BNN_NXT);
		/** FIXME fucking cheat */
		assembly.addInstruction( //
				store == ResultHandler.FA_BNN_NXT
					? this.visibility == 2
						? Instructions.INSTR_MSUB_N_BA_1R_V
						: this.visibility == 1
							? Instructions.INSTR_MSUB_D_BA_1R_V
							: Instructions.INSTR_MSUB_T_BA_1R_V
					: (this.visibility == 2
						? OperationsS2X.VMSUB_N
						: this.visibility == 2
							? OperationsS2X.VMSUB_D
							: OperationsA2X.XMSUB_T).instruction(ModifierArguments.AA0RB, ModifierArgumentA30IMM.ONE, 0, store));
	}

	@Override
	public String toCode() {
		
		return "ACCESSPOSTINC [" + this.reference + "];";
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
