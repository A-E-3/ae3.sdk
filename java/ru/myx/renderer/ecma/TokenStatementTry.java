/*
 * Created on 15.10.2005
 */
package ru.myx.renderer.ecma;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.exec.Instructions;
import ru.myx.ae3.exec.ModifierArgumentA30IMM;
import ru.myx.ae3.exec.ModifierArguments;
import ru.myx.ae3.exec.OperationsA01;
import ru.myx.ae3.exec.OperationsA2X;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ProgramPart;
import ru.myx.ae3.exec.ResultHandler;

final class TokenStatementTry extends TokenStatementAbstract {

	private int statementCount = 0;

	private TokenStatementBlock token;

	private TokenStatementCatch tokenCatchStatement;

	private TokenStatementBlock tokenCatch;

	private TokenStatementBlock tokenFinally;

	TokenStatementTry(final String identity, final int line) {
		super(identity, line);
	}

	@Override
	public final boolean addStatement(final TokenStatement statement) {

		if (this.statementCount == 0) {
			if (statement instanceof TokenStatementBlock) {
				this.token = (TokenStatementBlock) statement;
				this.statementCount = 1;
				return true;
			}
			return false;
		}
		if (this.statementCount == 1) {
			if (statement instanceof TokenStatementCatch) {
				this.statementCount = 2;
				this.tokenCatchStatement = (TokenStatementCatch) statement;
				return true;
			}
			if (statement instanceof TokenStatementFinally) {
				this.statementCount = 4;
				return true;
			}
			return false;
		}
		if (this.statementCount == 2) {
			if (statement instanceof TokenStatementBlock) {
				this.tokenCatch = (TokenStatementBlock) statement;
				this.statementCount = 3;
				return true;
			}
			return false;
		}
		if (this.statementCount == 3) {
			if (statement instanceof TokenStatementFinally) {
				this.statementCount = 4;
				return true;
			}
			return false;
		}
		if (this.statementCount == 4) {
			if (statement instanceof TokenStatementBlock) {
				this.tokenFinally = (TokenStatementBlock) statement;
				this.statementCount = 5;
				return true;
			}
			return false;
		}
		return false;
	}

	@Override
	public final TokenStatement createStatement(final String identity, final int line) {

		return new TokenStatementTry(identity, line);
	}

	@Override
	public final void dump(final int level, final StringBuilder buffer) {

		for (int i = level; i > 0; --i) {
			buffer.append('\t');
		}
		buffer.append("try");
		buffer.append('\n');
		if (this.token != null) {
			this.token.dump(level, buffer);
			buffer.append('\n');
		}
		if (this.tokenCatchStatement != null) {
			this.tokenCatchStatement.dump(level, buffer);
		}
		if (this.tokenCatch != null) {
			this.tokenCatch.dump(level, buffer);
			buffer.append('\n');
		}
		if (this.tokenFinally != null) {
			for (int i = level; i > 0; --i) {
				buffer.append('\t');
			}
			buffer.append("finally");
			buffer.append('\n');
			this.tokenFinally.dump(level + 1, buffer);
			buffer.append('\n');
		}
	}

	@Override
	public final String getKeyword() {

		return "try";
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

		return this.statementCount == 0 || this.statementCount == 2 || this.statementCount == 4;
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

		return this.statementCount == 3 || this.statementCount == 5;
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

		this.addDebug(assembly, "try{");
		if (this.token == null && this.tokenCatch == null && this.tokenFinally == null) {
			assembly.addError("illegal try statement!");
			return;
		}
		if (this.token == null && this.tokenFinally == null) {
			return;
		}
		if (this.token == null) {
			if (this.tokenFinally == null) {
				return;
			}
			this.tokenFinally.toAssembly(assembly, startOffset);
			return;
		}
		final int initialOffset = assembly.size();
		final ProgramPart body;
		{
			this.token.toAssembly(assembly, initialOffset);
			if (assembly.size() == initialOffset) {
				body = null;
			} else {
				body = assembly.toProgram(initialOffset);
			}
		}
		final ProgramPart bodyFinally;
		if (this.tokenFinally == null) {
			bodyFinally = null;
		} else {
			this.tokenFinally.toAssembly(assembly, initialOffset);
			if (assembly.size() == initialOffset) {
				bodyFinally = null;
			} else {
				final ProgramPart finallyCode = assembly.toProgram(initialOffset);
				this.tokenFinally.addDebug(assembly, "finally");
				assembly.addInstruction(finallyCode);
				bodyFinally = assembly.toProgram(initialOffset);
			}
		}
		final String expressionCatch = this.tokenCatchStatement == null
			? null
			: this.tokenCatchStatement.getCatchExpression();
		final ProgramPart bodyCatch;
		if (this.tokenCatch == null) {
			bodyCatch = null;
		} else {
			this.tokenCatch.toAssembly(assembly, initialOffset);
			if (assembly.size() == initialOffset) {
				this.tokenCatch.addDebug(assembly, "catch(" + expressionCatch + ")");
				/** zero length - nothing to do, but still need 'handler' to mask exception */
				bodyCatch = assembly.toProgram(initialOffset);
			} else {
				final ProgramPart catchCode = assembly.toProgram(initialOffset);
				this.tokenCatch.addDebug(assembly, "catch(" + expressionCatch + ")");
				assembly.addInstruction(OperationsA01.XEENTRCTCH_P.instruction(catchCode.length() + 1, ResultHandler.FA_BNN_NXT));
				assembly.addInstruction(OperationsA2X.XFDECLARE_D.instruction(new ModifierArgumentA30IMM(expressionCatch), ModifierArguments.AA0RB, 0, ResultHandler.FA_BNN_NXT));
				assembly.addInstruction(catchCode);
				assembly.addInstruction(Instructions.INSTR_ELEAVE_0_NN_NEXT);
				bodyCatch = assembly.toProgram(initialOffset);
			}
		}
		if (bodyCatch != null && (expressionCatch == null || expressionCatch.length() == 0)) {
			assembly.addError("catch expression expected!");
			return;
		}
		if (body == null) {
			if (bodyFinally != null) {
				assembly.addInstruction(bodyFinally);
			}
			return;
		}
		if (bodyCatch == null) {
			if (bodyFinally == null) {
				assembly.addInstruction(body);
			} else {
				assembly.addInstruction(OperationsA01.XEENTRNONE_P.instruction(body.length(), ResultHandler.FA_BNN_NXT));
				assembly.addInstruction(body);
				assembly.addInstruction(OperationsA01.XELEAVE_P.instruction(bodyFinally.length(), ResultHandler.FA_BNN_NXT));
				assembly.addInstruction(bodyFinally);
			}
			return;
		}
		assembly.addInstruction(OperationsA01.XEENTRCTRL_P.instruction(body.length() + 2 + bodyCatch.length(), ResultHandler.FA_BNN_NXT));
		assembly.addInstruction(OperationsA01.XFETGT_P.instruction(1 + body.length(), ResultHandler.FA_BNN_NXT));
		assembly.addInstruction(body);
		assembly.addInstruction(OperationsA01.XESKIP_P.instruction(bodyCatch.length(), ResultHandler.FA_BNN_NXT));
		assembly.addInstruction(bodyCatch);
		if (bodyFinally == null) {
			assembly.addInstruction(Instructions.INSTR_ELEAVE_0_NN_NEXT);
		} else {
			assembly.addInstruction(OperationsA01.XELEAVE_P.instruction(bodyFinally.length(), ResultHandler.FA_BNN_NXT));
			assembly.addInstruction(bodyFinally);
		}
	}
}
