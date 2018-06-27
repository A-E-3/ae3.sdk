/*
 * Created on 29.10.2003
 * 
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.ae3.exec.parse.expression;

import ru.myx.ae3.e4.parse.TokenType;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ProgramAssembly;

/**
 * @author myx
 * 		
 */
public abstract class TokenValue implements TokenInstruction {
	
	/**
	 * @param tokens
	 * @return
	 */
	protected final static boolean assertAllValues(final TokenInstruction[] tokens) {
		
		for (int i = tokens.length - 1; i >= 0; --i) {
			assert tokens[i].assertStackValue();
		}
		return true;
	}
	
	@Override
	public final int getOperandCount() {
		
		return 0;
	}
	
	@Override
	public final int getPriorityLeft() {
		
		return +10000;
	}
	
	@Override
	public final int getPriorityRight() {
		
		return +10000;
	}
	
	@Override
	public final int getResultCount() {
		
		return 1;
	}
	
	@Override
	public final TokenType getTokenType() {
		
		return TokenType.VALUE;
	}
	
	@Override
	public final boolean isParseValueRight() {
		
		return true;
	}
	
	@Override
	public final boolean isStackValue() {
		
		return true;
	}
	
	@Override
	public abstract String toCode();
	
	@Override
	public final TokenInstruction toStackValue(final ProgramAssembly assembly, final TokenInstruction argumentA, final boolean sideEffectsOnly) {
		
		assert false : "Value doesn't have any operands, notation=" + this.getNotation();
		return null;
	}
	
	@Override
	public final TokenInstruction toStackValue(final ProgramAssembly assembly, final TokenInstruction argumentA, final TokenInstruction argumentB, final boolean sideEffectsOnly) {
		
		assert false : "Value doesn't have any operands, notation=" + this.getNotation();
		return null;
	}
	
	@Override
	public final TokenInstruction toToken(final ModifierArgument modifierA, final ModifierArgument modifierB) {
		
		assert false : "Value doesn't have any operands, notation=" + this.getNotation();
		return null;
	}
}
