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
import ru.myx.ae3.eval.tokens.TokenInstruction;
import ru.myx.ae3.exec.InstructionEditable;
import ru.myx.ae3.exec.Instructions;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.OperationsA01;
import ru.myx.ae3.exec.OperationsA11;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.util.FifoQueueLinked;

final class TokenStatementSwitch extends TokenStatementAbstract {
	
	private String expression;
	
	private TokenStatementBlock token;
	
	/** TODO: make 'false' by default, but then it should be known already */
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
		
		/** just to check for duplicate labels */
		final Map<BasePrimitive<?>, Boolean> switchLabes = new IdentityHashMap<>();
		
		/** buffer for subsequent case labels **/
		final FifoQueueLinked<ModifierArgument> labelsCollected = new FifoQueueLinked<>();
		// final LinkedList<ModifierArgument> labelsCollected = new LinkedList<>();
		
		/** all case break statements **/
		FifoQueueLinked<InstructionEditable> breaksCollected = null;
		
		/** labelsCollected.size() + defaultLabel? **/
		int labelsCollectedCount = 0;
		
		/** flag that 'default' switch label is already seen, no more labels allowed after that **/
		boolean hasDefault = false;
		
		/** a jump from last switch label to next condition or exit from case **/
		InstructionEditable jumpNext = null;
		
		/** indicates that the our switch statement already has label checks in assembly **/
		boolean hadLabels = false;
		
		/** switch body **/
		final TokenStatement[] statements = this.token.toStatements();
		if (statements != null) {
			for (final TokenStatement statement : statements) {
				
				/** 'case' label of the switch **/
				if (statement instanceof TokenStatementCase) {
					if (hasDefault) {
						assembly.addError("switch label after default case!");
						return;
					}
					
					final String expressionString = statement.toString();
					if (expressionString == null) {
						assembly.addError("invalid case expression!");
						return;
					}
					final ModifierArgument value = Evaluate.compileToken(assembly, expressionString.trim(), BalanceType.EXPRESSION).toConstantModifier();
					if (value == null) {
						assembly.addError("case expression should be constant!");
						return;
					}
					{
						final BaseObject constant = value.argumentConstantValue();
						if (constant == null || !constant.baseIsPrimitive()) {
							assembly.addError("switch label must be primitive!");
							return;
						}
						if (switchLabes.put(constant.baseToPrimitive(null), Boolean.TRUE) != null) {
							assembly.addError("duplicate switch label!");
							return;
						}
					}
					
					/** save label until first non-label statement found **/
					labelsCollected.offerLast(value);
					++labelsCollectedCount;
					continue;
				}
				
				/** 'default' switch block **/
				if (statement instanceof TokenStatementDefault) {
					if (hasDefault) {
						assembly.addError("switch statement already has a default case!");
						return;
					}
					
					++labelsCollectedCount;
					hasDefault = true;
					continue;
				}
				
				/** non-switch label-related statements **/
				{
					int assemblySize = assembly.size();
					if (labelsCollectedCount > 0) {

						/** skip next label group check (fall-through) **/
						if (hadLabels) {
							assembly.addInstruction( //
									labelsCollectedCount == 1
										? Instructions.INSTR_ESKIP_1_NN_NEXT
										: OperationsA01.XESKIP_P.instruction(labelsCollectedCount, ResultHandler.FA_BNN_NXT) //
							);
							++assemblySize;
						}

						/** make label comparison chain to point here **/
						if (jumpNext != null) {
							final int count = assembly.getInstructionCount(jumpNext.getConstant()) - 1;
							jumpNext.setConstant(count).setFinished();
							jumpNext = null;
						}
						
						/** make labels (if needed) **/
						if (hasDefault) {
							/** skip all labels -- anything matches **/
						} else {
							/** all but last **/
							for (int i = labelsCollectedCount - 1; i > 0; --i) {
								final ModifierArgument value = labelsCollected.pollFirst();
								assembly.addInstruction( //
										OperationsA11.XESKIPRB1XA_P.instruction(value, i, ResultHandler.FA_BNN_NXT) //
								);
								++assemblySize;
							}
							/** last **/
							{
								final ModifierArgument value = labelsCollected.pollFirst();
								assembly.addInstruction( //
										jumpNext = OperationsA11.XESKIPRB0XA_P.instructionCreate(value, assemblySize, ResultHandler.FA_BNN_NXT) //
								);
								++assemblySize;
							}
							hadLabels = true;
						}
						labelsCollectedCount = 0;
						
						/** now we can finally add the non switch statements to code **/
					}
					
					if (statement instanceof TokenStatementBreak) {
						if (breaksCollected == null) {
							breaksCollected = new FifoQueueLinked<>();
						}
						final InstructionEditable breakInstruction = OperationsA01.XESKIP_P.instructionCreate(assemblySize, ResultHandler.FA_BNN_NXT);
						breaksCollected.offerLast(breakInstruction);
						assembly.addInstruction(breakInstruction);
						continue;
					}

					statement.toAssembly(assembly, assemblySize);
				}
			}
			
			if (jumpNext != null) {
				final int count = assembly.getInstructionCount(jumpNext.getConstant()) - 1;
				jumpNext.setConstant(count).setFinished();
				jumpNext = null;
			}
		}
		
		/** some direct breaks collected? **/
		if (breaksCollected != null) {
			for (;;) {
				final InstructionEditable breakInstruction = breaksCollected.pollFirst();
				if (breakInstruction == null) {
					break;
				}
				breakInstruction.setConstant(assembly.getInstructionCount(breakInstruction.getConstant()) - 1);
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
