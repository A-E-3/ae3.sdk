/*
 * Created on 15.10.2005
 */
package ru.myx.renderer.ecma;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.OperationsA10;
import ru.myx.ae3.exec.ProgramAssembly;

final class TokenStatementOut extends TokenStatementAbstract {
	
	
	private String text;
	
	TokenStatementOut(final String text, final String identity, final int line) {
		super(identity, line);
		this.text = text;
	}
	
	@Override
	public final boolean addStatement(final TokenStatement statement) {
		
		
		return false;
	}
	
	final void concatenate(final String text) {
		
		
		this.text += text;
	}
	
	@Override
	public final TokenStatement createStatement(final String identity, final int line) {
		
		
		throw new UnsupportedOperationException();
	}
	
	@Override
	public final void dump(final int level, final StringBuilder buffer) {
		
		
		for (int i = level; i > 0; --i) {
			buffer.append('\t');
		}
		buffer.append("OUTPUT");
		buffer.append('\t');
		buffer.append(this.text);
		buffer.append('\n');
	}
	
	@Override
	public final String getKeyword() {
		
		
		return null;
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
	public boolean isNextStatementFromScratch() {
		
		
		return true;
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
		
		
		throw new UnsupportedOperationException("No locals allowed here!");
	}
	
	@Override
	public final void toAssembly(final ProgramAssembly assembly, final int startOffset) throws Exception {
		
		
		assembly.addInstruction(OperationsA10.XFLOAD_P.instruction(Base.forString(this.text), null, 0, ResultHandler.FB_BNO_NXT));
	}
	
	@Override
	public final String toString() {
		
		
		return "OUT[" + this.text + "]";
	}
}
