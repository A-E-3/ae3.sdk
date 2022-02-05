/*
 * Created on 19.10.2005
 */
package ru.myx.renderer.ecma;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.eval.BalanceType;
import ru.myx.ae3.eval.Evaluate;
import ru.myx.ae3.eval.tokens.TokenInstruction;
import ru.myx.ae3.exec.Instructions;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;

class TokenStatementThrow extends TokenStatementAbstract {
	
	
	private String token;
	
	TokenStatementThrow(final String identity, final int line) {
		super(identity, line);
	}
	
	@Override
	public final boolean addStatement(final TokenStatement statement) {
		
		
		if (this.token == null && statement instanceof TokenStatementSingle) {
			this.token = ((TokenStatementSingle) statement).toCode().trim();
			return true;
		}
		return false;
	}
	
	@Override
	public final TokenStatement createStatement(final String identity, final int line) {
		
		
		return new TokenStatementThrow(identity, line);
	}
	
	@Override
	public final void dump(final int level, final StringBuilder buffer) {
		
		
		for (int i = level; i > 0; --i) {
			buffer.append('\t');
		}
		buffer.append("throw " + this.token);
		buffer.append('\n');
	}
	
	@Override
	public final String getKeyword() {
		
		
		return "throw";
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
		
		
		throw new UnsupportedOperationException("No statments allowed within 'throw' statement!");
	}
	
	@Override
	public boolean setControlContinueUsed() {
		
		
		throw new UnsupportedOperationException("No statments allowed within 'throw' statement!");
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
			this.addDebug(assembly, "throw");
			assembly.addInstruction(Instructions.INSTR_LOAD_1_C_NN_ERROR_NO_MESSAGE);
			return;
		}
		
		final String expression = this.token;
		this.addDebug(assembly, "throw " + expression);
		final TokenInstruction instruction = Evaluate.compileToken(assembly, expression, BalanceType.EXPRESSION);
		assert instruction != null : "Requested balance expected to provide non-empty code";
		assert instruction.assertStackValue();
		instruction.toAssembly(assembly, null, null, ResultHandler.FB_BNN_ERR);
	}
}
