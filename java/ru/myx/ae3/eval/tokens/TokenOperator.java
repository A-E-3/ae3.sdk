/*
 * Created on 29.10.2003 To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.ae3.eval.tokens;

import ru.myx.ae3.e4.parse.TokenType;

/** @author myx */
public abstract class TokenOperator implements TokenInstruction {

	@Override
	public final int getResultCount() {

		return 1;
	}

	@Override
	public final TokenType getTokenType() {

		return TokenType.OPERATOR;
	}

	@Override
	public abstract String toCode();
	
	@Override
	public final String toString() {

		return this.getNotation();
	}
}
