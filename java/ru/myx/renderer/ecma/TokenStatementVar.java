/*
 * Created on 15.10.2005
 */
package ru.myx.renderer.ecma;

import ru.myx.ae3.base.BaseNativeObject;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.eval.Evaluate;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.report.Report;

final class TokenStatementVar extends TokenStatementAbstract {
	
	private TokenStatementSingle token;
	
	TokenStatementVar(final String identity, final int line) {
		super(identity, line);
	}
	
	@Override
	public final boolean addStatement(final TokenStatement statement) {
		
		if (this.token == null && statement instanceof TokenStatementSingle) {
			this.token = (TokenStatementSingle) statement;
			return true;
		}
		return false;
	}
	
	@Override
	public final TokenStatement createStatement(final String identity, final int line) {
		
		return new TokenStatementVar(identity, line);
	}
	
	@Override
	public final void dump(final int level, final StringBuilder buffer) {
		
		for (int i = level; i > 0; --i) {
			buffer.append('\t');
		}
		buffer.append("var");
		buffer.append('\n');
		if (this.token != null) {
			this.token.dump(level + 1, buffer);
		}
	}
	
	@Override
	public final String getKeyword() {
		
		return "var";
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
	public boolean isTotallyComplete() {
		
		return this.token != null;
	}
	
	@Override
	public final boolean setArguments(final String expression) {
		
		return false;
	}
	
	@Override
	public boolean setControlBreakUsed() {
		
		throw new UnsupportedOperationException("No statments allowed within 'var' statement!");
	}
	
	@Override
	public boolean setControlContinueUsed() {
		
		throw new UnsupportedOperationException("No statments allowed within 'var' statement!");
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
		
		if (this.token == null) {
			this.addDebug(assembly, "var");
			assembly.addError("no variable list!");
			return;
		}
		final String declaration = this.token.toCode();
		if (this.parent == null) {
			this.addDebug(assembly, "var " + declaration);
			assembly.addError("no parent block!");
			return;
		}
		final BaseObject variables = new BaseNativeObject();
		final boolean code = TokenStatementAbstract.toLocals(declaration, variables);
		this.parent.setLocals(variables);
		if (code) {
			this.addDebug(assembly, "var " + declaration);
			final int size = assembly.size();
			try {
				Evaluate.compileDeclaration(assembly, startOffset, declaration);
			} catch (final Exception e) {
				Report.exception("ECMA:DECLARATION", "Error compiling: line=" + this.line + " // " + declaration, e);
				assembly.makeError(size, e);
			}
		}
	}
	
	@Override
	public final String toString() {
		
		return this.token == null
			? "var"
			: "var " + this.token.toString().trim();
	}
}
