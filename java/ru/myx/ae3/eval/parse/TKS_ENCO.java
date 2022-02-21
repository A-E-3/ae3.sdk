/*
 * Created on 29.10.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.ae3.eval.parse;

import ru.myx.ae3.eval.tokens.TokenSyntax;

final class TKS_ENCO extends TokenSyntax {

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
	public final boolean isConstantForArguments() {

		return true;
	}
}
