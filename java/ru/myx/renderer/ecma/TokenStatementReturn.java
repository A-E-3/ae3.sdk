/*
 * Created on 15.10.2005
 */
package ru.myx.renderer.ecma;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.eval.BalanceType;
import ru.myx.ae3.eval.Evaluate;
import ru.myx.ae3.eval.tokens.TokenInstruction;
import ru.myx.ae3.exec.Instructions;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;

final class TokenStatementReturn extends TokenStatementAbstract {
	
	
	private TokenStatementSingle token;
	
	TokenStatementReturn(final String identity, final int line) {
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
		
		
		return new TokenStatementReturn(identity, line);
	}
	
	@Override
	public final void dump(final int level, final StringBuilder buffer) {
		
		
		for (int i = level; i > 0; --i) {
			buffer.append('\t');
		}
		buffer.append("return");
		buffer.append('\n');
		if (this.token != null) {
			this.token.dump(level + 1, buffer);
		}
	}
	
	@Override
	public final String getKeyword() {
		
		
		return "return";
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
			this.addDebug(assembly, "return");
			assembly.addInstruction(Instructions.INSTR_LOAD_UNDEFINED_NN_RETURN);
			return;
		}
		
		final String expression = this.token.toString().trim();
		this.addDebug(assembly, "return " + expression);
		final TokenInstruction instruction = Evaluate.compileToken(assembly, expression, BalanceType.EXPRESSION);
		assert instruction != null : "Requested balance expected to provide non-empty code";
		assert instruction.assertStackValue();
		instruction.toAssembly(assembly, null, null, ResultHandler.FC_PNN_RET);
	}
	
	@Override
	public final String toString() {
		
		
		return this.token == null
			? "return"
			: "return " + this.token.toString().trim();
	}
}
