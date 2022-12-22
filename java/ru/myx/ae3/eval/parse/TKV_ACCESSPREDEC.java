package ru.myx.ae3.eval.parse;

import ru.myx.ae3.eval.tokens.TokenInstruction;
import ru.myx.ae3.eval.tokens.TokenValue;
import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.Instructions;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ModifierArgumentA30IMM;
import ru.myx.ae3.exec.ModifierArguments;
import ru.myx.ae3.exec.OperationsS2X;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;

class TKV_ACCESSPREDEC extends TokenValue {

	private final TokenInstruction reference;
	
	public TKV_ACCESSPREDEC(final TokenInstruction reference) {
		
		assert reference.isAccessReference();
		assert reference.isStackValue();
		this.reference = reference;
	}
	
	@Override
	public String getNotation() {

		return " --" + this.reference.getNotation();
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
		assembly.addInstruction(//
				modifierReferenced == ModifierArguments.AA0RB
					? Instructions.INSTR_MSUB_D_BA_1R_V
					: OperationsS2X.VMSUB_D.instruction(modifierReferenced, ModifierArgumentA30IMM.ONE, 0, ResultHandler.FA_BNN_NXT));
		this.reference.toReferenceWriteAfterRead(assembly, null, null, ModifierArguments.AA0RB, false, store);
	}
	
	@Override
	public String toCode() {

		return "ACCESSPREDEC [" + this.reference + "];";
	}
}
