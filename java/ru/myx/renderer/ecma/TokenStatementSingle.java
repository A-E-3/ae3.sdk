/*
 * Created on 15.10.2005
 */
package ru.myx.renderer.ecma;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.eval.BalanceType;
import ru.myx.ae3.eval.Evaluate;
import ru.myx.ae3.eval.tokens.TokenInstruction;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.report.Report;

final class TokenStatementSingle extends TokenStatementAbstract {
	
	
	private final String code;
	
	TokenStatementSingle(final String code, final String identity, final int line) {
		super(identity, line);
		this.code = code;
	}
	
	@Override
	public final boolean addStatement(final TokenStatement statement) {
		
		
		System.out.println(">>>>> " + statement);
		return false;
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
		buffer.append(this.code);
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
		
		
		final String expression = this.code.trim();
		this.addDebug(assembly, expression);
		final int size = assembly.size();
		try {
			final TokenInstruction instruction = Evaluate.compileToken(assembly, expression, BalanceType.STATEMENT);
			if (instruction != null) {
				assert instruction.assertStackValue();
				instruction.toAssembly(assembly, null, null, ResultHandler.FA_BNN_NXT);
			}
		} catch (final Exception e) {
			Report.exception("ECMA:STATEMENT", "Error compiling: line=" + this.line + " // " + this.code, e);
			assembly.makeError(size, e);
		}
	}
	
	/**
	 * @return source text
	 */
	public String toCode() {
		
		
		return this.code;
	}
	
	@Override
	public final String toString() {
		
		
		return this.code;
	}
}
