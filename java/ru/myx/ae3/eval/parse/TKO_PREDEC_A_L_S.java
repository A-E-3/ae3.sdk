package ru.myx.ae3.eval.parse;

import ru.myx.ae3.base.BasePrimitiveString;
import ru.myx.ae3.eval.tokens.TokenInstruction;
import ru.myx.ae3.eval.tokens.TokenOperator;
import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandlerBasic;

class TKO_PREDEC_A_L_S extends TokenOperator {
	@Override
	public String getNotation() {
		return " --";
	}
	
	@Override
	public int getOperandCount() {
		return 1;
	}
	
	@Override
	public final int getPriorityLeft() {
		return +10000;
	}
	
	@Override
	public int getPriorityRight() {
		return 900;
	}
	
	@Override
	public final InstructionResult getResultType() {
		return InstructionResult.NUMBER;
	}
	
	@Override
	public void toAssembly(
			final ProgramAssembly assembly,
			final ModifierArgument argumentA,
			final ModifierArgument argumentB,
			final ResultHandlerBasic store) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String toCode() {
		return "PREDEC;";
	}
	
	@Override
	public TokenInstruction toStackValue(
			final ProgramAssembly assembly,
			final TokenInstruction argumentA,
			final boolean sideEffectsOnly) {
		assert argumentA.assertAccessReference();
		final BasePrimitiveString name = argumentA.toContextPropertyName();
		return name != null
				? new TKV_FRAMEPREDEC( name )
				: new TKV_ACCESSPREDEC( argumentA );
	}
}
