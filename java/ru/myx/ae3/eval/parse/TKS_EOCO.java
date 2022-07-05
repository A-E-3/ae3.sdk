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
import ru.myx.ae3.eval.tokens.TokenValue;
import ru.myx.ae3.exec.ProgramAssembly;

/** @author myx */
final class TKS_EOCO extends TokenSyntax implements TokenSyntax.ConditionalStackValuable {

	@Override
	public final String getNotation() {

		return "?.";
	}

	@Override
	public final int getPriorityLeft() {

		return 950;
	}

	@Override
	public final int getPriorityRight() {

		return 950;
	}

	@Override
	public final ConditionType getSkipCondition() {

		return TokenInstruction.ConditionType.NULLISH_YES;
	}

	@Override
	public final boolean isConstantForArguments() {

		/** ??? constant for constant arguments ??? false maybe: it could be anything on right-hand
		 * side... */
		return true;
	}

	@Override
	public final TokenInstruction toStackValue(final ProgramAssembly assembly, final TokenInstruction argumentA, final TokenInstruction argumentB, final boolean sideEffectsOnly)
			throws Evaluate.CompilationException {

		if (argumentB instanceof TokenValue.SyntacticallyFrameAccess) {
			return new TKV_EOCO(argumentA.toExecDetachableResult(), ((TokenValue.SyntacticallyFrameAccess) argumentB).getDirectChainingAccessReplacement());
		}
		return new TKV_EOCO(argumentA.toExecDetachableResult(), argumentB);
	}
}
