/*
 * Created on 19.10.2005
 */
package ru.myx.renderer.ecma;

import ru.myx.ae3.base.BaseMap;
import ru.myx.ae3.base.BaseNativeObject;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.eval.BalanceType;
import ru.myx.ae3.eval.Evaluate;
import ru.myx.ae3.eval.tokens.TokenInstruction;
import ru.myx.ae3.exec.InstructionEditable;
import ru.myx.ae3.exec.Instructions;
import ru.myx.ae3.exec.OperationsA01;
import ru.myx.ae3.exec.OperationsA10;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ProgramPart;
import ru.myx.ae3.exec.ResultHandler;

final class TokenStatementDo extends TokenStatementAbstract {

	private static final TokenStatement DUMMY_STATEMENT = new TokenStatementEmpty(null, 0);

	private BaseMap locals;

	private TokenStatement tokenBlock;

	private TokenStatementWhile tokenWhile;

	private boolean hasBreak;

	private boolean hasContinue;

	TokenStatementDo(final String identity, final int line) {
		
		super(identity, line);
	}

	@Override
	public final boolean addStatement(final TokenStatement statement) {

		if (this.tokenBlock == null) {
			this.tokenBlock = statement;
			statement.setLocalsTarget(this);
			return true;
		}
		if (this.tokenWhile == null && statement instanceof TokenStatementWhile) {
			this.tokenWhile = (TokenStatementWhile) statement;
			this.tokenWhile.addStatement(TokenStatementDo.DUMMY_STATEMENT);
			return true;
		}
		return false;
	}

	@Override
	public final TokenStatement createStatement(final String identity, final int line) {

		return new TokenStatementDo(identity, line);
	}

	@Override
	public final void dump(final int level, final StringBuilder buffer) {

		for (int i = level; i > 0; --i) {
			buffer.append('\t');
		}
		buffer.append("do");
		buffer.append('\n');
		if (this.tokenBlock != null) {
			this.tokenBlock.dump(level + 1, buffer);
		}
		if (this.tokenWhile != null) {
			this.tokenWhile.dump(level + 1, buffer);
		}
	}

	@Override
	public final String getKeyword() {

		return "do";
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
	public final boolean isNewLineSemicolon() {

		return this.tokenBlock == null;
	}

	@Override
	public boolean isNextStatementFromScratch() {

		return this.isTotallyComplete();
	}

	@Override
	public boolean isTotallyComplete() {

		return this.tokenBlock != null && this.tokenWhile != null;
	}

	@Override
	public final boolean setArguments(final String expression) {

		return false;
	}

	@Override
	public boolean setControlBreakUsed() {

		this.hasBreak = true;
		return true;
	}

	@Override
	public boolean setControlContinueUsed() {

		this.hasContinue = true;
		return true;
	}

	@Override
	public final boolean setIdentifier(final String identifier) {

		return false;
	}

	@Override
	public final boolean setLocals(final BaseObject locals) {

		if (locals == null || !locals.baseHasKeysOwn()) {
			return true;
		}
		if (this.locals == null) {
			this.locals = new BaseNativeObject();
		}
		this.locals.baseDefineImportOwnEnumerable(locals);
		return true;
	}

	@Override
	public final void toAssembly(final ProgramAssembly assembly, final int startOffset) throws Exception {

		if (this.tokenBlock == null) {
			this.addDebug(assembly, "do{");
			assembly.addError("do statement requires body!");
			return;
		}
		if (this.tokenWhile == null) {
			this.addDebug(assembly, "do{");
			assembly.addError("do statement requires while condition!");
			return;
		}
		final String calc = this.tokenWhile.toCondition();
		if (calc == null) {
			this.addDebug(assembly, "do{");
			assembly.addError("condition expected!");
			return;
		}
		this.addDebug(assembly, "do{...}while(" + calc + ")");
		final int size = assembly.size();
		this.tokenBlock.toAssembly(assembly, size);
		final ProgramPart loop;
		{
			/** ESKIP1 / ESKIP0 - doesn't require 'boolean' */
			final TokenInstruction token = Evaluate.compileToken(assembly, calc, BalanceType.EXPRESSION);
			final BaseObject constantValue = token.toConstantValue();
			if (constantValue != null) {
				if (constantValue.baseToBoolean() == BaseObject.TRUE) {
					final InstructionEditable loopInstruction = OperationsA01.XESKIP_P.instructionCreate(0, ResultHandler.FA_BNN_NXT);
					assembly.addInstruction(loopInstruction);
					loop = assembly.toProgram(size);
					loopInstruction.setConstant(-loop.length()).setFinished();
				} else {
					loop = assembly.toProgram(size);
				}
			} else {
				final InstructionEditable loopInstruction = token.toBooleanConditionalSkip(assembly, startOffset, true, ResultHandler.FU_BNN_NXT);
				loop = assembly.toProgram(size);
				loopInstruction.setConstant(-loop.length()).setFinished();
			}
		}
		final InstructionEditable frameEntry;
		final int breakAndContinue = (this.hasBreak
			? 1
			: 0) + (this.hasContinue
				? 1
				: 0);
		if (this.locals == null) {
			if (breakAndContinue > 0) {
				frameEntry = OperationsA01.XEENTRCTRL_P.instructionCreate(
						0//
								+ breakAndContinue //
								+ loop.length(),
						ResultHandler.FA_BNN_NXT);
			} else {
				assembly.addInstruction(loop);
				return;
			}
			assembly.addInstruction(frameEntry);
		} else {
			if (breakAndContinue > 0) {
				frameEntry = OperationsA01.XEENTRLOOP_P.instructionCreate(
						0//
								+ breakAndContinue //
								+ loop.length(),
						ResultHandler.FA_BNN_NXT);
			} else {
				frameEntry = OperationsA01.XEENTRVARS_P.instructionCreate(loop.length(), ResultHandler.FA_BNN_NXT);
			}
			assembly.addInstruction(frameEntry);
			final BaseObject locals = TokenStatementAbstract.toLocalsObjectOrArray(this.locals);
			if (locals != null) {
				assembly.addInstruction(OperationsA10.XFDECLARE_N.instruction(locals, null, 0, ResultHandler.FA_BNN_NXT));
				frameEntry.setConstant(frameEntry.getConstant() + 1).setFinished();
			}
		}
		if (this.hasBreak) {
			assembly.addInstruction(
					OperationsA01.XFBTGT_P.instruction(//
							(this.hasContinue
								? 1
								: 0) + loop.length(),
							ResultHandler.FA_BNN_NXT//
					));
		}
		if (this.hasContinue) {
			assembly.addInstruction(OperationsA01.XFCTGT_P.instruction(0, ResultHandler.FA_BNN_NXT));
		}
		assembly.addInstruction(loop);
		assembly.addInstruction(Instructions.INSTR_ELEAVE_0_NN_NEXT);
	}
}
