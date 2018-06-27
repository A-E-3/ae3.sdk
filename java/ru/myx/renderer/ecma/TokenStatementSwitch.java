/*
 * Created on 19.10.2005
 */
package ru.myx.renderer.ecma;

import java.util.IdentityHashMap;
import java.util.Map;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BasePrimitive;
import ru.myx.ae3.eval.BalanceType;
import ru.myx.ae3.eval.Evaluate;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.InstructionEditable;
import ru.myx.ae3.exec.Instructions;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.OperationsA01;
import ru.myx.ae3.exec.OperationsA11;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.parse.expression.TokenInstruction;

final class TokenStatementSwitch extends TokenStatementAbstract {
	
	
	private String expression;
	
	private TokenStatementBlock token;
	
	/**
	 * TODO: make 'false' by default, but then it should be known already
	 */
	private boolean hasBreak;
	
	TokenStatementSwitch(final String identity, final int line) {
		super(identity, line);
	}
	
	@Override
	public final boolean addStatement(final TokenStatement statement) {
		
		
		if (this.token == null) {
			if (statement instanceof TokenStatementBlock) {
				this.token = (TokenStatementBlock) statement;
				return true;
			}
			throw new IllegalArgumentException("Illegal switch statement!");
		}
		return false;
	}
	
	@Override
	public final TokenStatement createStatement(final String identity, final int line) {
		
		
		return new TokenStatementSwitch(identity, line);
	}
	
	@Override
	public final void dump(final int level, final StringBuilder buffer) {
		
		
		for (int i = level; i > 0; --i) {
			buffer.append('\t');
		}
		buffer.append("switch");
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
		
		
		return "switch";
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
	public final boolean setIdentifier(final String identifier) {
		
		
		return false;
	}
	
	@Override
	public final boolean setLocals(final BaseObject locals) {
		
		
		throw new UnsupportedOperationException("No locals allowed here!");
	}
	
	@Override
	public final void toAssembly(final ProgramAssembly assembly, final int startOffset) throws Exception {
		
		
		this.addDebug(assembly, "switch( " + this.expression + " )");
		if (this.expression == null) {
			assembly.addError("no switch expression!");
			return;
		}
		final InstructionEditable frameEntry;
		final int frameStart;
		final InstructionEditable breakTarget;
		if (this.hasBreak) {
			frameEntry = OperationsA01.XEENTRCTRL_P.instructionCreate(0, ResultHandler.FA_BNN_NXT);
			assembly.addInstruction(frameEntry);
			frameStart = assembly.size();
			breakTarget = OperationsA01.XFBTGT_P.instructionCreate(0, ResultHandler.FA_BNN_NXT);
			assembly.addInstruction(breakTarget);
		} else {
			frameEntry = null;
			frameStart = assembly.size();
			breakTarget = null;
		}
		{
			final TokenInstruction token = Evaluate.compileToken(assembly, this.expression, BalanceType.EXPRESSION);
			token.toAssembly(assembly, null, null, ResultHandler.FA_BNN_NXT);
		}
		boolean hasDefault = false;
		InstructionEditable jumpNext = null;
		/**
		 * just to check for duplicate labels
		 */
		final Map<BasePrimitive<?>, Boolean> objects = new IdentityHashMap<>();
		final TokenStatement[] statements = this.token.toStatements();
		if (statements != null) {
			for (final TokenStatement statement : statements) {
				if (statement.isLabelStatement()) {
					if (hasDefault) {
						assembly.addError("switch label after default case!");
						return;
					}
					if (statement instanceof TokenStatementCase) {
						final String expressionString = ((TokenStatementCase) statement).toString();
						if (expressionString == null) {
							assembly.addError("invalid case expression!");
							return;
						}
						final String expression = expressionString.trim();
						final TokenInstruction token = Evaluate.compileToken(assembly, expression, BalanceType.EXPRESSION);
						final ModifierArgument value = token.toConstantModifier();
						if (value == null) {
							assembly.addError("case expression should be constant!");
							return;
						}
						final BaseObject constant = value.argumentConstantValue();
						if (!constant.baseIsPrimitive()) {
							assembly.addError("switch label must be primitive!");
							return;
						}
						if (objects.put(constant.baseToPrimitive(null), Boolean.TRUE) != null) {
							assembly.addError("duplicate switch label!");
							return;
						}
						if (jumpNext != null) {
							jumpNext.setConstant(assembly.getInstructionCount(jumpNext.getConstant())).setFinished();
							jumpNext = null;
							assembly.addInstruction(Instructions.INSTR_ESKIP_1_NN_NEXT);
						}
						jumpNext = OperationsA11.XESKIPRB0XA_P.instructionCreate(value, assembly.size(), ResultHandler.FA_BNN_NXT);
						assembly.addInstruction(jumpNext);
						continue;
					}
					if (statement instanceof TokenStatementDefault) {
						hasDefault = true;
						if (jumpNext != null) {
							jumpNext.setConstant(assembly.getInstructionCount(jumpNext.getConstant()) - 1).setFinished();
							jumpNext = null;
						}
						continue;
					}
				} else {
					statement.toAssembly(assembly, assembly.size());
				}
			}
			
			if (jumpNext != null) {
				jumpNext.setConstant(assembly.getInstructionCount(jumpNext.getConstant()) - 1).setFinished();
				jumpNext = null;
			}
		}
		if (!this.hasBreak) {
			return;
		}
		assert frameEntry != null && breakTarget != null //
		: "Must not be NULL when 'hasBreak' is true!";
		
		final int frameSize = assembly.getInstructionCount(frameStart);
		frameEntry.setConstant(frameSize).setFinished();
		breakTarget.setConstant(frameSize - 1).setFinished();
		assembly.addInstruction(Instructions.INSTR_ELEAVE_0_NN_NEXT);
	}
}
