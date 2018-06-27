/*
 * Created on 15.10.2005
 */
package ru.myx.renderer.ecma;


import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.exec.Instructions;
import ru.myx.ae3.exec.ProgramAssembly;

final class TokenStatementContinue implements TokenStatement {
	protected final String	identity;
	
	protected final int		line;
	
	private TokenStatement	token;
	
	TokenStatementContinue(final String identity, final int line) {
		this.identity = identity;
		this.line = line;
	}
	
	@Override
	public final boolean addStatement(final TokenStatement statement) {
		if (this.token == null) {
			this.token = statement;
			return true;
		}
		return false;
	}
	
	@Override
	public final TokenStatement createStatement(final String identity, final int line) {
		return new TokenStatementContinue( identity, line );
	}
	
	@Override
	public final void dump(final int level, final StringBuilder buffer) {
		for (int i = level; i > 0; --i) {
			buffer.append( '\t' );
		}
		buffer.append( "continue" );
		buffer.append( '\n' );
		if (this.token != null) {
			this.token.dump( level + 1, buffer );
		}
	}
	
	@Override
	public String getIdentity() {
		return this.identity;
	}
	
	@Override
	public final String getKeyword() {
		return "continue";
	}
	
	@Override
	public int getLine() {
		return this.line;
	}
	
	@Override
	public boolean isBlockStatement() {
		return false;
	}
	
	@Override
	public boolean isEmpty() {
		return false;
	}
	
	@Override
	public final boolean isIdentifierPossible() {
		return false;
	}
	
	@Override
	public final boolean isIdentifierRequired() {
		return false;
	}
	
	@Override
	public final boolean isKeywordExpectStatement() {
		return false;
	}
	
	@Override
	public final boolean isLabelStatement() {
		return false;
	}
	
	@Override
	public final boolean isNewLineSemicolon() {
		return this.token == null;
	}
	
	@Override
	public boolean isNextStatementFromScratch() {
		return true;
	}
	
	@Override
	public boolean isOnlyWhenFirstInStatement() {
		return false;
	}
	
	@Override
	public boolean isTotallyComplete() {
		return true;
	}
	
	@Override
	public TokenStatement lastStatement() {
		return null;
	}
	
	@Override
	public final boolean setArguments(final String expression) {
		return false;
	}
	
	@Override
	public boolean setControlBreakUsed() {
		throw new UnsupportedOperationException( "No statments allowed within 'continue' statement!" );
	}
	
	@Override
	public boolean setControlContinueUsed() {
		throw new UnsupportedOperationException( "No statments allowed within 'continue' statement!" );
	}
	
	@Override
	public final boolean setIdentifier(final String identifier) {
		return false;
	}
	
	@Override
	public final boolean setLocals(final BaseObject locals) {
		throw new UnsupportedOperationException( "No locals allowed here!" );
	}
	
	@Override
	public void setLocalsTarget(final TokenStatement target) {
		// ignore
	}
	
	@Override
	public final boolean setParent(final TokenStatement parent) {
		if (!parent.setControlContinueUsed()) {
			throw new UnsupportedOperationException( "'continue' at line "
					+ this.line
					+ " doesn't have a corresponding loop statement, parent token is: "
					+ parent );
		}
		return true;
	}
	
	@Override
	public final void toAssembly(final ProgramAssembly assembly, final int startOffset) throws Exception {
		assembly.addDebug( /* this.identity + ":" + */this.line + " // continue" );
		assembly.addInstruction( Instructions.INSTR_NOP_0_NN_CONTINUE );
	}
}
