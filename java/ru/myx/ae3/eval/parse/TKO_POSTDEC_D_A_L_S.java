package ru.myx.ae3.eval.parse;

import ru.myx.ae3.base.BasePrimitiveString;
import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandlerBasic;
import ru.myx.ae3.exec.parse.expression.TokenInstruction;
import ru.myx.ae3.exec.parse.expression.TokenOperator;

class TKO_POSTDEC_D_A_L_S extends TokenOperator {
	public static final TokenOperator	INSTANCE	= new TKO_POSTDEC_D_A_L_S();
	
	private TKO_POSTDEC_D_A_L_S() {
		//
	}
	
	@Override
	public String getNotation() {
		return "-- ";
	}
	
	@Override
	public int getOperandCount() {
		return 1;
	}
	
	@Override
	public final int getPriorityLeft() {
		return 900;
		// return 100;
	}
	
	@Override
	public int getPriorityRight() {
		return +10000;
		// return 100;
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
		return "POSTDEC_D;";
	}
	
	@Override
	public TokenInstruction toExecNativeResult() {
		return TKO_POSTDEC_N_A_L_S.INSTANCE;
	}
	
	@Override
	public TokenInstruction toStackValue(
			final ProgramAssembly assembly,
			final TokenInstruction argumentA,
			final boolean sideEffectsOnly) {
		assert argumentA.assertAccessReference();
		final BasePrimitiveString name = argumentA.toContextPropertyName();
		return name != null
				? new TKV_FRAMEPOSTDEC( name )
				: new TKV_ACCESSPOSTDEC( argumentA, 1 );
	}
}
