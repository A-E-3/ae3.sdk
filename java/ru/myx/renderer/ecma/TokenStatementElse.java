/*
 * Created on 15.10.2005
 */
package ru.myx.renderer.ecma;


import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.exec.ProgramAssembly;

final class TokenStatementElse extends TokenStatementAbstract {
	TokenStatementElse(final String identity, final int line) {
		super( identity, line );
	}
	
	@Override
	public final boolean addStatement(final TokenStatement statement) {
		return false;
	}
	
	@Override
	public final TokenStatement createStatement(final String identity, final int line) {
		return new TokenStatementElse( identity, line );
	}
	
	@Override
	public final void dump(final int level, final StringBuilder buffer) {
		for (int i = level; i > 0; --i) {
			buffer.append( '\t' );
		}
		buffer.append( "else" );
		buffer.append( '\n' );
	}
	
	@Override
	public final String getKeyword() {
		return "else";
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
		return false;
	}
	
	@Override
	public boolean isTotallyComplete() {
		return true;
	}
	
	@Override
	public final boolean setArguments(final String expression) {
		return false;
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
		this.addDebug( assembly, "else{" );
		assembly.addError( "else without if!" );
	}
}
