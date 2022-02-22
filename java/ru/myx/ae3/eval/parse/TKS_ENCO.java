/*
 * Created on 29.10.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.ae3.eval.parse;

import ru.myx.ae3.eval.Evaluate;
import ru.myx.ae3.eval.tokens.TokenInstruction;
import ru.myx.ae3.eval.tokens.TokenSyntax;
import ru.myx.ae3.exec.ProgramAssembly;

final class TKS_ENCO extends TokenSyntax implements TokenSyntax.ConditionalStackValuable {

	@Override
	public final String getNotation() {

		return "??";
	}

	@Override
	public final int getPriorityLeft() {

		return 241;
	}

	@Override
	public final int getPriorityRight() {

		return 241;
	}

	@Override
	public final TokenInstruction.ConditionType getSkipCondition() {

		return TokenInstruction.ConditionType.NULLISH_NOT;
	}

	@Override
	public final boolean isConstantForArguments() {

		return true;
	}
	
	@Override
	public final TokenInstruction toStackValue(final ProgramAssembly assembly, final TokenInstruction argumentA, final TokenInstruction argumentB, final boolean sideEffectsOnly)
			throws Evaluate.CompilationException {

		return new TKV_ENCO(argumentA, argumentB);
	}
}
