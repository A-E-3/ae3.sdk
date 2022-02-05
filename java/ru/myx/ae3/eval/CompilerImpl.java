/*
 * Created on 29.10.2003
 * 
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.ae3.eval;

import ru.myx.ae3.eval.parse.ExpressionParser;
import ru.myx.ae3.eval.tokens.TokenInstruction;
import ru.myx.ae3.exec.ProgramAssembly;

final class CompilerImpl implements Precompiler {
	@Override
	public TokenInstruction parse(final ProgramAssembly assembly, final String expression, final BalanceType balanceType) {
		return ExpressionParser.parseExpression( assembly, expression.trim(), balanceType );
	}
}
