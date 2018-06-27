package ru.myx.renderer.ecma;


import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.exec.ProgramAssembly;

/*
 * Created on 19.10.2005
 */
final class TokenStatementCase extends TokenStatementAbstract {
	private String	expression;
	
	TokenStatementCase(final String identity, final int line) {
		super( identity, line );
	}
	
	@Override
	public final boolean addStatement(final TokenStatement statement) {
		if (this.expression == null && statement instanceof TokenStatementSingle) {
			this.expression = ((TokenStatementSingle) statement).toString();
			return true;
		}
		return false;
	}
	
	@Override
	public final TokenStatement createStatement(final String identity, final int line) {
		return new TokenStatementCase( identity, line );
	}
	
	@Override
	public final void dump(final int level, final StringBuilder buffer) {
		for (int i = level; i > 0; --i) {
			buffer.append( '\t' );
		}
		buffer.append( "case\t" );
		buffer.append( this.expression );
		buffer.append( '\n' );
	}
	
	@Override
	public final String getKeyword() {
		return "case";
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
		return true;
	}
	
	@Override
	public final boolean isLabelStatement() {
		return true;
	}
	
	@Override
	public boolean isTotallyComplete() {
		return this.expression != null;
	}
	
	@Override
	public final boolean setArguments(final String expression) {
		if (this.expression == null) {
			this.expression = expression;
			return true;
		}
		return false;
	}
	
	@Override
	public boolean setControlBreakUsed() {
		throw new UnsupportedOperationException( "No statments allowed within 'case' statement!" );
	}
	
	@Override
	public boolean setControlContinueUsed() {
		throw new UnsupportedOperationException( "No statments allowed within 'case' statement!" );
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
	public final void toAssembly(final ProgramAssembly assembly, final int startOffset) throws Exception {
		this.addDebug( assembly, "case " + this.expression + ":" );
		assembly.addError( "case without switch!" );
	}
	
	@Override
	public final String toString() {
		return this.expression;
	}
}
