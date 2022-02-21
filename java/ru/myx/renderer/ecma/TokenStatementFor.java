/*
 * Created on 15.10.2005
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
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ModifierArgumentA30IMM;
import ru.myx.ae3.exec.ModifierArguments;
import ru.myx.ae3.exec.OperationsA01;
import ru.myx.ae3.exec.OperationsA10;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ProgramPart;
import ru.myx.ae3.exec.ResultHandler;

final class TokenStatementFor extends TokenStatementAbstract {

	private String expression;

	private boolean v677;

	private boolean each;

	private boolean keys;

	private TokenStatement token;

	private BaseMap locals;

	private boolean hasBreak;

	private boolean hasContinue;

	TokenStatementFor(final String identity, final int line) {

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

		return new TokenStatementFor(identity, line);
	}

	@Override
	public final void dump(final int level, final StringBuilder buffer) {

		for (int i = level; i > 0; --i) {
			buffer.append('\t');
		}
		buffer.append("for");
		if (this.v677) {
			buffer.append(" v677");
		}
		if (this.each) {
			buffer.append(" each");
		}
		if (this.keys) {
			buffer.append(" keys");
		}
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

		return "for";
	}

	@Override
	public final boolean isIdentifierPossible() {

		return !this.v677 && !this.each && !this.keys;
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

		if (this.v677 || this.each || this.keys) {
			return false;
		}
		if (identifier.length() == 4) {
			if ("each".equals(identifier)) {
				this.each = true;
				return true;
			}
			if ("keys".equals(identifier)) {
				this.keys = true;
				return true;
			}
			if ("v677".equals(identifier)) {
				this.v677 = true;
				return true;
			}
		}
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

		this.addDebug(
				assembly,
				this.each
					? "for each( " + this.expression + " )"
					: this.keys
						? "for keys( " + this.expression + " )"
						: this.v677
							? "for v677( " + this.expression + " )"
							: "for( " + this.expression + " )");
		if (this.expression == null) {
			assembly.addError("no loop expression!");
			return;
		}
		if (this.token == null) {
			assembly.addError("no body statement!");
			return;
		}
		final int initialOffset = assembly.size();
		int position;
		{
			position = this.expression.indexOf(" in ");
			if (position > 0) {
				final ProgramPart body;
				{
					this.token.toAssembly(assembly, initialOffset);
					if (assembly.size() == initialOffset) {
						return;
					}
					body = assembly.toProgram(initialOffset);
				}
				final int declarationOffset;
				final String iterator;
				{
					final String left = this.expression.substring(0, position).trim();
					if (left.startsWith("var ") || left.startsWith("let ")) {
						iterator = left.substring(4).trim();
						/** loop variables should be declared outside of the actual loop and
						 * accessible after loop is finished */
						this.parent.setLocals(new BaseNativeObject(iterator, BaseObject.FALSE));
						assembly.addInstruction(OperationsA10.XFDECLARE_N.instruction(new ModifierArgumentA30IMM(iterator), 0, ResultHandler.FA_BNN_NXT));
						declarationOffset = assembly.size();
					} else {
						iterator = left;
						declarationOffset = initialOffset;
					}
				}
				/** need to save iteration registers to allow recursion */
				final InstructionEditable frameEntry;
				if (this.locals == null) {
					frameEntry = OperationsA01.XEENTRITER_I.instructionCreate(0, ResultHandler.FA_BNN_NXT);
				} else {
					frameEntry = OperationsA01.XEENTRITRV_P.instructionCreate(0, ResultHandler.FA_BNN_NXT);
				}
				assembly.addInstruction(frameEntry);
				final int frameStart = assembly.size();

				if (this.locals != null) {
					final BaseObject locals = TokenStatementAbstract.toLocalsObjectOrArray(this.locals);
					if (locals != null) {
						assembly.addInstruction(OperationsA10.XFDECLARE_N.instruction(new ModifierArgumentA30IMM(locals), 0, ResultHandler.FA_BNN_NXT));
					}
				}
				{
					final String right = this.expression.substring(
							position //
									+ 4 /* " in ".length() */)
							.trim();
					final TokenInstruction token = Evaluate.compileToken(assembly, right, BalanceType.EXPRESSION);
					final ModifierArgument modifier = token.toDirectModifier();
					{
						/** token.toConstantValue() in [NULL, UNDEFINED] -> skip whole statement */
						final BaseObject value = modifier.argumentConstantValue();
						if (value == BaseObject.UNDEFINED || value == BaseObject.NULL) {
							assembly.truncate(declarationOffset);
							return;
						}
					}
					if (modifier == ModifierArguments.AA0RB) {
						token.toAssembly(assembly, null, null, ResultHandler.FA_BNN_NXT);
						assembly.addInstruction(
								this.each
									? Instructions.INSTR_ITRPREPV_1_R_NN_NEXT
									: this.keys
										? Instructions.INSTR_ITRPREPK_1_R_NN_NEXT
										: this.v677
											? Instructions.INSTR_ITRPREP7_1_R_NN_NEXT
											: Instructions.INSTR_ITRPREP7_1_R_NN_NEXT);
					} else {
						assembly.addInstruction(
								(this.each
									? OperationsA10.XITRPREPV
									: this.keys
										? OperationsA10.XITRPREPK
										: this.v677
											? OperationsA10.ZITRPREP7
											: OperationsA10.ZITRPREP7).instruction(modifier, 0, ResultHandler.FA_BNN_NXT));
					}
				}
				if (this.hasBreak) {
					assembly.addInstruction(
							OperationsA01.XFBTGT_P.instruction(//
									(this.hasContinue
										? 4
										: 3) + body.length(),
									ResultHandler.FA_BNN_NXT //
							));
				}
				if (this.hasContinue) {
					assembly.addInstruction(OperationsA01.XFCTGT_P.instruction(0, ResultHandler.FA_BNN_NXT));
				}
				assembly.addInstruction(OperationsA10.XITRNEXT.instruction(new ModifierArgumentA30IMM(iterator), 0, ResultHandler.FA_BNN_NXT));
				assembly.addInstruction(OperationsA01.XESKIPRB0_P.instruction(body.length() + 1, ResultHandler.FA_BNN_NXT));
				assembly.addInstruction(body);
				assembly.addInstruction(OperationsA01.XESKIP_P.instruction(-body.length() - 3, ResultHandler.FA_BNN_NXT));
				if (frameEntry != null) {
					frameEntry.setConstant(assembly.getInstructionCount(frameStart)).setFinished();
					assembly.addInstruction(Instructions.INSTR_ELEAVE_0_NN_NEXT);
				}
				return;
			}
		}
		/** v677 may be applied to all 'for(' as 'for v677(' blindly, hence following code should be
		 * ommited. <code>
		if (this.v677) {
			assembly.addError( "for v677( ... in ... ) statement does actually require 'in'!" );
			return;
		}
		 </code> */
		if (this.each) {
			assembly.addError("for each( ... in ... ) statement does actually require 'in'!");
			return;
		}
		if (this.keys) {
			assembly.addError("for keys( ... in ... ) statement does actually require 'in'!");
			return;
		}
		if (!this.v677) {
			position = this.expression.indexOf(" of ");
			if (position > 0) {
				final ProgramPart body;
				{
					this.token.toAssembly(assembly, initialOffset);
					if (assembly.size() == initialOffset) {
						return;
					}
					body = assembly.toProgram(initialOffset);
				}
				final int declarationOffset;
				final String iterator;
				{
					final String left = this.expression.substring(0, position).trim();
					if (left.startsWith("var ") || left.startsWith("let ")) {
						iterator = left.substring(4).trim();
						/** loop variables should be declared outside of the actual loop and
						 * accessible after loop is finished */
						this.parent.setLocals(new BaseNativeObject(iterator, BaseObject.FALSE));
						assembly.addInstruction(OperationsA10.XFDECLARE_N.instruction(new ModifierArgumentA30IMM(iterator), 0, ResultHandler.FA_BNN_NXT));
						declarationOffset = assembly.size();
					} else {
						iterator = left;
						declarationOffset = initialOffset;
					}
				}
				/** need to save iteration registers to allow recursion */
				final InstructionEditable frameEntry;
				if (this.locals == null) {
					frameEntry = OperationsA01.XEENTRITER_I.instructionCreate(0, ResultHandler.FA_BNN_NXT);
				} else {
					frameEntry = OperationsA01.XEENTRITRV_P.instructionCreate(0, ResultHandler.FA_BNN_NXT);
				}
				assembly.addInstruction(frameEntry);
				final int frameStart = assembly.size();

				if (this.locals != null) {
					final BaseObject locals = TokenStatementAbstract.toLocalsObjectOrArray(this.locals);
					if (locals != null) {
						assembly.addInstruction(OperationsA10.XFDECLARE_N.instruction(new ModifierArgumentA30IMM(locals), 0, ResultHandler.FA_BNN_NXT));
					}
				}
				{
					final String right = this.expression.substring(
							position //
									+ 4 /* " in ".length() */)
							.trim();
					final TokenInstruction token = Evaluate.compileToken(assembly, right, BalanceType.EXPRESSION);
					final ModifierArgument modifier = token.toDirectModifier();
					{
						/** token.toConstantValue() in [NULL, UNDEFINED] -> skip whole statement */
						final BaseObject value = modifier.argumentConstantValue();
						if (value == BaseObject.UNDEFINED || value == BaseObject.NULL) {
							assembly.truncate(declarationOffset);
							return;
						}
					}
					if (modifier == ModifierArguments.AA0RB) {
						token.toAssembly(assembly, null, null, ResultHandler.FA_BNN_NXT);
						assembly.addInstruction(Instructions.INSTR_ITRPREPV_1_R_NN_NEXT);
					} else {
						assembly.addInstruction(OperationsA10.XITRPREPV.instruction(modifier, 0, ResultHandler.FA_BNN_NXT));
					}
				}
				if (this.hasBreak) {
					assembly.addInstruction(
							OperationsA01.XFBTGT_P.instruction(//
									(this.hasContinue
										? 4
										: 3) + body.length(),
									ResultHandler.FA_BNN_NXT//
							));
				}
				if (this.hasContinue) {
					assembly.addInstruction(OperationsA01.XFCTGT_P.instruction(0, ResultHandler.FA_BNN_NXT));
				}
				assembly.addInstruction(OperationsA10.XITRNEXT.instruction(new ModifierArgumentA30IMM(iterator), 0, ResultHandler.FA_BNN_NXT));
				assembly.addInstruction(OperationsA01.XESKIPRB0_P.instruction(body.length() + 1, ResultHandler.FA_BNN_NXT));
				assembly.addInstruction(body);
				assembly.addInstruction(OperationsA01.XESKIP_P.instruction(-body.length() - 3, ResultHandler.FA_BNN_NXT));
				if (frameEntry != null) {
					frameEntry.setConstant(assembly.getInstructionCount(frameStart)).setFinished();
					assembly.addInstruction(Instructions.INSTR_ELEAVE_0_NN_NEXT);
				}
				return;
			}
		}
		final String[] expressions = new String[3];
		{
			final StringBuilder buffer = new StringBuilder();
			int count = 0;
			int levelBrace = 0;
			boolean isQuote = false;
			boolean isApos = false;
			boolean nextSymbol = false;
			for (final char c : this.expression.toCharArray()) {
				switch (c) {
					case '[' :
					case '(' :
						if (!isQuote && !isApos) {
							levelBrace++;
						}
						nextSymbol = false;
						break;
					case ']' :
					case ')' :
						if (!isQuote && !isApos) {
							levelBrace--;
						}
						nextSymbol = false;
						break;
					case '"' :
						if (!nextSymbol) {
							if (!isApos) {
								isQuote = !isQuote;
							}
						}
						nextSymbol = false;
						break;
					case '\'' :
						if (!nextSymbol) {
							if (!isQuote) {
								isApos = !isApos;
							}
						}
						nextSymbol = false;
						break;
					case '\\' :
						if (isQuote || isApos) {
							nextSymbol = true;
						}
						break;
					case ';' :
						if (!isQuote && !isApos && levelBrace == 0) {
							if (count < 2) {
								expressions[count] = buffer.toString().trim();
								buffer.setLength(0);
								count++;
								continue;
							}
							assembly.addError(
									"Illegal statement count, expression: expression='" + this.expression + "', valid syntax: FOR: <statementInit>; <condition>; <operation>");
							return;
						}
						break;
					default :
						nextSymbol = false;
				}
				buffer.append(c);
			}
			if (count != 2) {
				assembly.addError("Illegal statement count, expression='" + this.expression + "', valid syntax: FOR: <statementInit>; <condition>; <operation>");
				return;
			}
			expressions[2] = buffer.toString().trim();
		}
		final ProgramPart calcInitialize;
		if (expressions[0].startsWith("var ") || expressions[0].startsWith("let ")) {
			final String statement = expressions[0].substring(4).trim();
			final BaseObject locals = new BaseNativeObject();
			final boolean code = TokenStatementAbstract.toLocals(statement, locals);
			/** loop variables should be declared outside of the actual loop and accessible after
			 * loop is finished */
			this.parent.setLocals(locals);
			if (code) {
				Evaluate.compileDeclaration(assembly, initialOffset, statement);
				calcInitialize = assembly.size() == initialOffset
					? null
					: assembly.toProgram(initialOffset);
			} else {
				calcInitialize = null;
			}
		} else {
			Evaluate.compileStatement(assembly, initialOffset, expressions[0]);
			if (assembly.size() == initialOffset) {
				calcInitialize = null;
			} else {
				calcInitialize = assembly.toProgram(initialOffset);
			}
		}
		final TokenInstruction calcCondition;
		final ModifierArgument calcModifier;
		{
			if (expressions[1].length() == 0) {
				calcCondition = null;
				calcModifier = null;
			} else {
				/** ESKIP1 / ESKIP0 - doesn't require 'boolean' */
				final TokenInstruction token = Evaluate.compileToken(
						assembly, //
						expressions[1],
						BalanceType.EXPRESSION);
				calcModifier = token.toDirectModifier();
				final BaseObject modifierConstant = calcModifier.argumentConstantValue();
				if (modifierConstant != null) {
					if (modifierConstant.baseToBoolean() == BaseObject.FALSE) {
						if (calcInitialize != null) {
							assembly.addInstruction(calcInitialize);
						}
						this.addDebug(assembly, "// condition is always false - skip loop");
						return;
					}
					calcCondition = null;
				} else {
					calcCondition = token;
				}
			}
		}
		final ProgramPart calcOperation;
		{
			if (expressions[2].length() == 0) {
				calcOperation = null;
			} else {
				Evaluate.compileStatement(assembly, initialOffset, expressions[2]);
				if (assembly.size() == initialOffset) {
					calcOperation = null;
				} else {
					calcOperation = assembly.toProgram(initialOffset);
				}
			}
		}
		final ProgramPart body;
		{
			final int size = assembly.size();
			this.token.toAssembly(assembly, size);
			if (assembly.size() == size) {
				body = null;
			} else {
				body = assembly.toProgram(size);
			}
		}
		if (body == null) {
			final ProgramPart result;
			{
				if (calcInitialize != null) {
					assembly.addInstruction(calcInitialize);
				}
				int loopSize = 0;
				if (calcCondition != null) {
					final int skipSize = 1 + (calcOperation == null
						? 0
						: calcOperation.length());
					final int size = assembly.size();
					calcCondition.toConditionalSkipSingleton(//
							assembly,
							TokenInstruction.ConditionType.TRUISH_NOT,
							skipSize,
							ResultHandler.FU_BNN_NXT//
					);
					loopSize += assembly.getInstructionCount(size);
					if (calcOperation != null) {
						assembly.addInstruction(calcOperation);
						loopSize += calcOperation.length();
					}
				}
				assembly.addInstruction(OperationsA01.XESKIP_P.instruction(-loopSize - 1, ResultHandler.FA_BNN_NXT));
				if (this.locals == null) {
					return;
				}
				result = assembly.toProgram(initialOffset);
			}
			if (this.locals == null && !this.hasBreak && !this.hasContinue) {
				/** NO ENTRY/LEAVE NEEDED */
				assembly.addInstruction(result);
				return;
			}
			if (this.locals != null) {
				final BaseObject locals = TokenStatementAbstract.toLocalsObjectOrArray(this.locals);
				if (locals == null) {
					assembly.addInstruction(
							(this.hasBreak || this.hasContinue
								? OperationsA01.XEENTRLOOP_P
								: OperationsA01.XEENTRVARS_P).instruction(result.length(), ResultHandler.FA_BNN_NXT));
				} else {
					assembly.addInstruction(
							(this.hasBreak || this.hasContinue
								? OperationsA01.XEENTRLOOP_P
								: OperationsA01.XEENTRVARS_P).instruction(result.length() + 1, ResultHandler.FA_BNN_NXT));
					assembly.addInstruction(OperationsA10.XFDECLARE_N.instruction(new ModifierArgumentA30IMM(locals), 0, ResultHandler.FA_BNN_NXT));
				}
			} else {
				// this.hasBreak || this.hasContinue
				assembly.addInstruction(OperationsA01.XEENTRCTRL_P.instruction(result.length(), ResultHandler.FA_BNN_NXT));
			}
			assembly.addInstruction(result);
			assembly.addInstruction(Instructions.INSTR_ELEAVE_0_NN_NEXT);
			return;
		}

		final ProgramPart result;
		{
			final int bodySize = body.length();
			final int operationSize = calcOperation == null
				? 0
				: calcOperation.length();
			final InstructionEditable continueTarget = this.hasContinue
				? OperationsA01.XFCTGT_P.instructionCreate(0, ResultHandler.FA_BNN_NXT)
				: null;
			if (continueTarget != null) {
				assembly.addInstruction(continueTarget);
			}
			int loopSize = 0;
			if (calcCondition != null) {
				final int skipSize = 1 + bodySize + operationSize;
				// if (calcModifier == ModifierArguments.A07RR) {
				final int size = assembly.size();
				calcCondition.toConditionalSkipSingleton(//
						assembly,
						TokenInstruction.ConditionType.TRUISH_NOT,
						skipSize,
						ResultHandler.FU_BNN_NXT//
				);
				loopSize += assembly.getInstructionCount(size);
			}
			assembly.addInstruction(body);
			loopSize += bodySize;
			if (operationSize > 0) {
				if (continueTarget != null) {
					continueTarget.setConstant(loopSize).setFinished();
				}
				assembly.addInstruction(calcOperation);
				loopSize += operationSize;
			}
			assembly.addInstruction(OperationsA01.XESKIP_P.instruction(-loopSize - 1, ResultHandler.FA_BNN_NXT));
			result = assembly.toProgram(initialOffset);
		}

		if (calcInitialize != null) {
			assembly.addInstruction(calcInitialize);
		}
		if (this.locals == null && !this.hasBreak && !this.hasContinue) {
			/** NO ENTRY/LEAVE NEEDED */
			assembly.addInstruction(result);
			return;
		}
		final InstructionEditable frameEntry;
		int frameSize = 0;
		if (this.locals != null) {
			if (this.hasBreak || this.hasContinue) {
				frameEntry = OperationsA01.XEENTRLOOP_P.instructionCreate(0, ResultHandler.FA_BNN_NXT);
			} else {
				frameEntry = OperationsA01.XEENTRVARS_P.instructionCreate(0, ResultHandler.FA_BNN_NXT);
			}
			assembly.addInstruction(frameEntry);
			final BaseObject locals = TokenStatementAbstract.toLocalsObjectOrArray(this.locals);
			if (locals != null) {
				assembly.addInstruction(OperationsA10.XFDECLARE_N.instruction(new ModifierArgumentA30IMM(locals), 0, ResultHandler.FA_BNN_NXT));
				frameSize++;
			}
		} else {
			// this.hasBreak || this.hasContinue
			frameEntry = OperationsA01.XEENTRCTRL_P.instructionCreate(0, ResultHandler.FA_BNN_NXT);
			assembly.addInstruction(frameEntry);
		}
		if (this.hasBreak) {
			assembly.addInstruction(OperationsA01.XFBTGT_P.instruction(result.length(), ResultHandler.FA_BNN_NXT));
			frameSize++;
		}
		assembly.addInstruction(result);
		frameEntry.setConstant(frameSize + result.length()).setFinished();
		assembly.addInstruction(Instructions.INSTR_ELEAVE_0_NN_NEXT);
	}
}
