/*
 * Created on 15.10.2005
 */
package ru.myx.renderer.ecma;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.eval.BalanceType;
import ru.myx.ae3.eval.Evaluate;
import ru.myx.ae3.eval.tokens.TokenInstruction;
import ru.myx.ae3.exec.InstructionEditable;
import ru.myx.ae3.exec.Instructions;
import ru.myx.ae3.exec.ModifierArguments;
import ru.myx.ae3.exec.OperationsA01;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;

final class TokenStatementOutput extends TokenStatementAbstract {
	
	
	private String expression;
	
	private TokenStatement token;
	
	TokenStatementOutput(final String identity, final int line) {
		super(identity, line);
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
		
		
		return new TokenStatementOutput(identity, line);
	}
	
	@Override
	public final void dump(final int level, final StringBuilder buffer) {
		
		
		for (int i = level; i > 0; --i) {
			buffer.append('\t');
		}
		buffer.append("output");
		buffer.append('\t');
		buffer.append('(');
		buffer.append(this.expression);
		buffer.append(')');
		buffer.append('\n');
		if (this.token != null) {
			this.token.dump(level + 1, buffer);
		}
	}
	
	@Override
	public final String getKeyword() {
		
		
		return "output";
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
	public boolean isNextStatementFromScratch() {
		
		
		return this.expression != null;
	}
	
	@Override
	public boolean isTotallyComplete() {
		
		
		return this.token != null && this.expression != null;
	}
	
	@Override
	public final boolean setArguments(final String expression) {
		
		
		if (this.expression == null) {
			this.expression = expression.trim();
			return true;
		}
		return false;
	}
	
	@Override
	public final boolean setIdentifier(final String identifier) {
		
		
		return false;
	}
	
	@Override
	public final boolean setLocals(final BaseObject locals) {
		
		
		return this.parent == null
			? false
			: this.parent.setLocals(locals);
	}
	
	@Override
	public final void toAssembly(final ProgramAssembly assembly, final int startOffset) throws Exception {
		
		
		this.addDebug(assembly, "$output( " + this.expression + " )");
		if (this.expression == null) {
			assembly.addError("no target expression!");
			return;
		}
		if (this.token == null) {
			assembly.addError("illegal output statement - no body!");
			return;
		}
		final int debugOffset = assembly.size();
		final TokenInstruction reference = this.expression.length() == 4 && "null".equals(this.expression)
			? null
			: Evaluate.compileToken(assembly, this.expression, BalanceType.ARGUMENT_LIST);
		if (reference == null) {
			assembly.addInstruction(Instructions.INSTR_FOTNULL_0_SN_NEXT);
		} else {
			if (!reference.isAccessReference()) {
				assert reference.assertAccessReference();
				assembly.addError("Reference (lvalue) required (or 'null' if output should be ignored)!");
				return;
			}
			reference.toReferenceReadBeforeWrite(assembly, null, null, false, true);
			assembly.addInstruction(Instructions.INSTR_FOTBLDR_0_SN_NEXT);
		}
		final InstructionEditable frameStart = OperationsA01.XEENTRNONE_P.instructionCreate(0, ResultHandler.FA_BNN_NXT);
		assembly.addInstruction(frameStart);
		final int frameStartPosition = assembly.size();
		this.token.toAssembly(assembly, frameStartPosition);
		final int instructionCount = assembly.getInstructionCount(frameStartPosition);
		if (instructionCount == 0) {
			assembly.truncate(debugOffset);
			return;
		}
		frameStart.setConstant(instructionCount).setFinished();
		assembly.addInstruction(Instructions.INSTR_ELEAVE_1_NN_NEXT);
		assembly.addInstruction(Instructions.INSTR_FOTDONE_B_1_S_NN_NEXT);
		if (reference != null) {
			reference.toReferenceWriteAfterRead(assembly, null, null, ModifierArguments.AA0RB, ResultHandler.FA_BNN_NXT);
		}
	}
}
