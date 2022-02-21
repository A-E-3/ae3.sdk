/*
 * Created on 15.10.2005
 */
package ru.myx.renderer.ecma;

import static ru.myx.ae3.exec.OperationsA01.XESKIP_P;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.eval.BalanceType;
import ru.myx.ae3.eval.Evaluate;
import ru.myx.ae3.eval.tokens.TokenInstruction;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ProgramPart;
import ru.myx.ae3.exec.ResultHandler;

final class TokenStatementIf extends TokenStatementAbstract {

	private String calc;

	private int statementCount = 0;

	private TokenStatement tokenThen;

	private TokenStatement tokenElse;

	TokenStatementIf(final String identity, final int line) {

		super(identity, line);
	}

	@Override
	public final boolean addStatement(final TokenStatement statement) {

		if (this.statementCount == 0) {
			this.statementCount++;
			this.tokenThen = statement;
			return true;
		}
		if (this.statementCount == 1) {
			this.statementCount++;
			if (statement instanceof TokenStatementElse) {
				return true;
			}
			return false;
		}
		if (this.statementCount == 2) {
			this.statementCount++;
			this.tokenElse = statement;
			return true;
		}
		return false;
	}

	@Override
	public final TokenStatement createStatement(final String identity, final int line) {

		return new TokenStatementIf(identity, line);
	}

	@Override
	public final void dump(final int level, final StringBuilder buffer) {

		for (int i = level; i > 0; --i) {
			buffer.append('\t');
		}
		buffer.append("if");
		buffer.append('\t');
		buffer.append('(');
		buffer.append(this.calc);
		buffer.append(')');
		buffer.append('\n');
		if (this.tokenThen != null) {
			this.tokenThen.dump(level + 1, buffer);
			if (this.tokenElse != null) {
				buffer.append('\n');
				this.tokenElse.dump(level + 1, buffer);
			}
		}
	}

	@Override
	public final String getKeyword() {

		return "if";
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

		/** in any position, count == 0 || count == 1 || count == 3 */
		return true;
	}

	@Override
	public boolean isTotallyComplete() {

		// return this.calc != null && (this.statementCount == 1 ||
		// this.statementCount == 3);
		return this.calc != null && this.statementCount == 3;
	}

	@Override
	public final boolean setArguments(final String expression) {

		if (this.calc == null) {
			this.calc = expression;
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

		this.addDebug(assembly, "if( " + this.calc + " )");
		if (this.calc == null) {
			assembly.addError("no conditional expression!");
			return;
		}
		if (this.tokenThen == null) {
			assembly.addError("illegal if statement!");
			return;
		}

		final int initialOffset = assembly.size();

		/** ESKIP1 / ESKIP0 - doesn't require 'boolean' */
		final TokenInstruction condition = Evaluate.compileToken(assembly, this.calc, BalanceType.EXPRESSION);
		{
			final BaseObject value = condition.toConstantValue();
			if (value != null) {
				if (value.baseToBoolean() == BaseObject.TRUE) {
					this.tokenThen.toAssembly(assembly, startOffset);
					return;
				}
				if (this.tokenElse != null) {
					this.tokenElse.toAssembly(assembly, startOffset);
				}
				return;
			}
		}

		final ProgramPart instructionThen;
		{
			this.tokenThen.toAssembly(assembly, initialOffset);
			if (assembly.size() == initialOffset) {
				instructionThen = null;
			} else {
				instructionThen = assembly.toProgram(initialOffset);
			}
		}
		final ProgramPart instructionElse;
		if (this.tokenElse == null) {
			instructionElse = null;
		} else {
			this.tokenElse.toAssembly(assembly, initialOffset);
			if (assembly.size() == initialOffset) {
				instructionElse = null;
			} else {
				instructionElse = assembly.toProgram(initialOffset);
			}
		}

		if (instructionElse == null) {
			if (instructionThen == null) {
				condition.toAssembly(assembly, null, null, ResultHandler.FA_BNN_NXT);
				return;
			}

			condition.toConditionalSkipSingleton(//
					assembly,
					TokenInstruction.ConditionType.TRUISH_NOT,
					instructionThen.length(),
					ResultHandler.FU_BNN_NXT//
			);
			assembly.addInstruction(instructionThen);
			return;
		}
		if (instructionThen == null) {
			condition.toConditionalSkipSingleton(//
					assembly,
					TokenInstruction.ConditionType.TRUISH_YES,
					instructionElse.length(),
					ResultHandler.FU_BNN_NXT//
			);
			assembly.addInstruction(instructionElse);
			return;
		}
		{
			condition.toConditionalSkipSingleton(//
					assembly,
					TokenInstruction.ConditionType.TRUISH_NOT,
					1 + instructionThen.length(),
					ResultHandler.FU_BNN_NXT//
			);
			assembly.addInstruction(instructionThen);
			assembly.addInstruction(XESKIP_P.instruction(instructionElse.length(), ResultHandler.FA_BNN_NXT));
			assembly.addInstruction(instructionElse);
		}
	}
}
