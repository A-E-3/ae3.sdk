/*
 * Created on 29.10.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.ae3.eval.parse;

import ru.myx.ae3.eval.tokens.TokenInstruction;
import ru.myx.ae3.eval.tokens.TokenSyntax;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandlerBasic;

final class TKS_QUESTION_MARK extends TokenSyntax {

	@Override
	public final String getNotation() {
		
		return "?";
	}

	@Override
	public final int getPriorityLeft() {
		
		return 170;
	}

	@Override
	public final int getPriorityRight() {
		
		return 80;
	}

	@Override
	public void toAssembly(final ProgramAssembly assembly, final ModifierArgument argumentA, final ModifierArgument argumentB, final ResultHandlerBasic store) {
		
		ParseConstants.TKV_ERROR_UNMATCHED_QUESTION_MARK.toAssembly(assembly, argumentA, argumentB, store);
	}

	@Override
	public TokenInstruction toStackValue(final ProgramAssembly programAssembly, final boolean sideEffectsOnly) {
		
		return ParseConstants.TKV_ERROR_UNMATCHED_QUESTION_MARK;
	}
}
