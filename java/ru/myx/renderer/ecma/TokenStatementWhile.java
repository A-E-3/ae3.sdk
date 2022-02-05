/*
 * Created on 15.10.2005
 */
package ru.myx.renderer.ecma;

import ru.myx.ae3.base.BaseNativeObject;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.eval.BalanceType;
import ru.myx.ae3.eval.Evaluate;
import ru.myx.ae3.eval.tokens.TokenInstruction;
import ru.myx.ae3.exec.InstructionEditable;
import ru.myx.ae3.exec.Instructions;
import ru.myx.ae3.exec.ModifierArgumentA30IMM;
import ru.myx.ae3.exec.OperationsA01;
import ru.myx.ae3.exec.OperationsA10;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ProgramPart;
import ru.myx.ae3.exec.ResultHandler;

final class TokenStatementWhile extends TokenStatementAbstract {
	
	private String expression;
	
	private BaseObject locals;
	
	private TokenStatement token;
	
	private boolean hasBreak;
	
	private boolean hasContinue;
	
	TokenStatementWhile(final String identity, final int line) {
		super(identity, line);
	}
	
	@Override
	public final boolean addStatement(final TokenStatement statement) {
		
		if (this.token == null) {
			this.token = statement;
			statement.setLocalsTarget(this);
			return true;
		}
		return false;
	}
	
	@Override
	public final TokenStatement createStatement(final String identity, final int line) {
		
		return new TokenStatementWhile(identity, line);
	}
	
	@Override
	public final void dump(final int level, final StringBuilder buffer) {
		
		for (int i = level; i > 0; --i) {
			buffer.append('\t');
		}
		buffer.append("while");
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
		
		return "while";
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
			this.expression = expression;
			return true;
		}
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
		
		if (this.expression == null) {
			this.addDebug(assembly, "while()");
			assembly.addError("conditional expression required for while statement!");
			return;
		}
		this.addDebug(assembly, "while(" + this.expression + ")");
		if (this.token == null) {
			assembly.addError("illegal while statement!");
			return;
		}
		final int initialOffset = assembly.size();
		final boolean alwaysTrue;
		{
			/** ESKIP1 / ESKIP0 - doesn't require 'boolean' */
			final TokenInstruction token = Evaluate.compileToken(assembly, this.expression, BalanceType.EXPRESSION);
			final BaseObject constantValue = token.toConstantValue();
			if (constantValue != null) {
				if (constantValue.baseToBoolean() != BaseObject.TRUE) {
					// always false
					return;
				}
				alwaysTrue = true;
			} else {
				alwaysTrue = false;
				token.toAssembly(assembly, null, null, ResultHandler.FA_BNN_NXT);
			}
		}
		final int conditionSize = assembly.getInstructionCount(initialOffset);
		final int conditionEnd = assembly.size();
		final InstructionEditable entryExit;
		if (alwaysTrue) {
			entryExit = null;
		} else {
			entryExit = OperationsA01.XESKIPRB0_P.instructionCreate(0, ResultHandler.FA_BNN_NXT);
			assembly.addInstruction(entryExit);
		}
		final ProgramPart loop;
		{
			final int size = assembly.size();
			this.token.toAssembly(assembly, size);
			loop = assembly.toProgram(size);
		}
		final InstructionEditable frameEntry;
		final int framePosition;
		if (this.locals == null) {
			if (this.hasBreak || this.hasContinue) {
				frameEntry = OperationsA01.XEENTRCTRL_P.instructionCreate(0, ResultHandler.FA_BNN_NXT);
				assembly.addInstruction(frameEntry);
				framePosition = assembly.size();
			} else {
				frameEntry = null;
				framePosition = -1;
			}
		} else {
			if (this.hasBreak || this.hasContinue) {
				frameEntry = OperationsA01.XEENTRLOOP_P.instructionCreate(0, ResultHandler.FA_BNN_NXT);
			} else {
				frameEntry = OperationsA01.XEENTRVARS_P.instructionCreate(0, ResultHandler.FA_BNN_NXT);
			}
			assembly.addInstruction(frameEntry);
			framePosition = assembly.size();
			final BaseObject locals = TokenStatementAbstract.toLocalsObjectOrArray(this.locals);
			if (locals != null) {
				assembly.addInstruction(OperationsA10.XFDECLARE_N.instruction(new ModifierArgumentA30IMM(locals), 0, ResultHandler.FA_BNN_NXT));
			}
		}
		
		if (alwaysTrue) {
			if (this.hasContinue) {
				assembly.addInstruction(OperationsA01.XFCTGT_P.instruction(//
						this.hasBreak
							? 1
							: 0,
						ResultHandler.FA_BNN_NXT));
			}
			if (this.hasBreak) {
				assembly.addInstruction(OperationsA01.XFBTGT_P.instruction(//
						loop.length() + (frameEntry == null
							? 0
							: 1),
						ResultHandler.FA_BNN_NXT));
			}
			assembly.addInstruction(loop);
			assembly.addInstruction(OperationsA01.XESKIP_P.instruction(-(loop.length() + 1), ResultHandler.FA_BNN_NXT));
		} else //
		if (frameEntry == null) {
			assembly.addInstruction(loop);
			assembly.addInstruction(OperationsA01.XESKIP_P.instruction(-(loop.length() + conditionSize + 2), ResultHandler.FA_BNN_NXT));
		} else {
			if (this.hasContinue) {
				assembly.addInstruction(OperationsA01.XFCTGT_P.instruction(//
						1 + (this.hasBreak
							? 1
							: 0),
						ResultHandler.FA_BNN_NXT));
			}
			if (this.hasBreak) {
				assembly.addInstruction(OperationsA01.XFBTGT_P.instruction(conditionSize + 3 + loop.length(), ResultHandler.FA_BNN_NXT));
			}
			assembly.addInstruction(OperationsA01.XESKIP_P.instruction(conditionSize + 1, ResultHandler.FA_BNN_NXT));
			assembly.addCloned(initialOffset, conditionEnd - initialOffset);
			assembly.addInstruction(OperationsA01.XESKIPRB0_P.instruction(1 + loop.length(), ResultHandler.FA_BNN_NXT));
			assembly.addInstruction(loop);
			assembly.addInstruction(OperationsA01.XESKIP_P.instruction(-(loop.length() + conditionSize + 2), ResultHandler.FA_BNN_NXT));
		}
		if (frameEntry == null) {
			//
		} else {
			frameEntry.setConstant(assembly.getInstructionCount(framePosition)).setFinished();
			assembly.addInstruction(Instructions.INSTR_ELEAVE_0_NN_NEXT);
		}
		if (entryExit != null) {
			entryExit.setConstant(assembly.getInstructionCount(conditionEnd + 1)).setFinished();
		}
	}
	
	final String toCondition() {
		
		return this.expression;
	}
	
}
