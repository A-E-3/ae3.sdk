/**
 *
 */
package ru.myx.ae3.exec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BasePrimitive;
import ru.myx.ae3.base.BasePrimitiveString;
import ru.myx.ae3.e4.parse.TokenType;
import ru.myx.ae3.eval.BalanceType;
import ru.myx.ae3.eval.Evaluate;
import ru.myx.ae3.eval.parse.ParseConstants;
import ru.myx.ae3.eval.parse.TKA_FSTORE_BA_SC_S;
import ru.myx.ae3.eval.parse.TKV_ASSIGN;
import ru.myx.ae3.eval.parse.TKV_ASSIGN1;
import ru.myx.ae3.eval.parse.TKV_CARRAY0;
import ru.myx.ae3.eval.parse.TKV_CARRAY1;
import ru.myx.ae3.eval.parse.TKV_CARRAYX_BY_PUSH;
import ru.myx.ae3.eval.parse.TKV_CARRAYX_ON_STACK;
import ru.myx.ae3.eval.parse.TKV_EBAND;
import ru.myx.ae3.eval.parse.TKV_EBOR;
import ru.myx.ae3.eval.parse.TKV_EFLOW2;
import ru.myx.ae3.eval.parse.TKV_EFLOW3;
import ru.myx.ae3.eval.parse.TKV_EFLOWX;
import ru.myx.ae3.eval.parse.TKV_ERROR_A_C_E;
import ru.myx.ae3.eval.parse.TKV_FDECLARE_BA_VC_S;
import ru.myx.ae3.eval.parse.TK_WRAP_STACKX;
import ru.myx.ae3.exec.parse.expression.TokenInstruction;
import ru.myx.ae3.exec.parse.expression.TokenValue;
import ru.myx.ae3.report.Report;

/** @author myx */
public class ProgramAssembly {

	final class PlaceholderSimple implements InstructionPlaceholder {

		Instruction instruction;

		@Override
		public ExecStateCode execCall(final ExecProcess process) throws Exception {

			return process.vmRaise("Placeholder instruction should have been replaced before execution!");
		}

		/** Returns non-editable (optimised?) instruction.
		 *
		 * @return */
		@Override

		public Instruction getFinalIfReady() {

			return this.instruction;
		}

		@Override
		public int getOperandCount() {

			throw new UnsupportedOperationException("Placeholder Instruction should have been replaced before execution!");
		}

		@Override
		public int getResultCount() {

			throw new UnsupportedOperationException("Placeholder Instruction should have been replaced before execution!");
		}

		/** @param instruction
		 * @return this */
		@Override

		public InstructionPlaceholder setInstruction(final Instruction instruction) {

			this.instruction = instruction;
			return this;
		}

		@Override
		public String toCode() {

			return "[PlaceholderInstruction]";
		}

	}

	/**
	 *
	 */
	public static final String FLAG_REPORT_ERRORS = "_report$errors_";

	private static final Instruction VALUE_TRUE = Instructions.INSTR_LOAD_TRUE_SN_NEXT;

	private static final Instruction VALUE_FALSE = Instructions.INSTR_LOAD_FALSE_SN_NEXT;

	private static final Instruction VALUE_UNDEFINED = Instructions.INSTR_LOAD_UNDEFINED_SN_NEXT;

	/** Empty program */
	public static final ProgramPart PROGRAM_EMPTY = new ProgramPart(new Instruction[0]);

	/** Program with the only command to load TRUE in stack */
	public static final ProgramPart PROGRAM_PUSH_TRUE = new ProgramPart(new Instruction[]{
			ProgramAssembly.VALUE_TRUE
	});

	/** Program with the only command to load FALSE in stack */
	public static final ProgramPart PROGRAM_PUSH_FALSE = new ProgramPart(new Instruction[]{
			ProgramAssembly.VALUE_FALSE
	});

	/** Program with the only command to load UNDEFINED in stack */
	public static final ProgramPart PROGRAM_PUSH_UNDEFINED = new ProgramPart(new Instruction[]{
			ProgramAssembly.VALUE_UNDEFINED
	});

	private static final int INITIAL = 64;

	private static int compilerSearchInSource(final List<TokenInstruction> precompiled, final int exprLength, final int i, final int priorityRight) {

		int length = i + 1;
		for (; length < exprLength; length++) {
			final TokenInstruction candidate = precompiled.get(length);
			// if (candidate.getPriorityLeft() < priorityRight) {
			if (!candidate.isStackValue() && candidate.getPriorityLeft() < priorityRight) {
				return length;
			}
		}
		return length;
	}

	private TokenInstruction[] compilerTokens;

	/** operators may have 0..3 operand count and must have 1 result count. */
	private int compilerPointerOperator;

	/** values all must have 0 operand count and 1 execution result. */
	private int compilerPointerValue;

	private Instruction[] assemblyCode;

	private int assemblyCapacity;

	private int assemblySize;

	private String lastDebug = null;

	private Object errors = null;

	private List<TokenValue> constants;

	private Map<BasePrimitive<?>, Integer> constantMap;

	/**
	 *
	 */
	public final ExecProcess ctx;

	private boolean reportErrors = true;

	/**
	 *
	 */
	public ProgramAssembly() {

		final ExecProcess ctx = Exec.currentProcess();
		this.ctx = Exec.createProcess(ctx, "Assembly Builder / Optimizer");
		this.assemblyCode = null;
		this.assemblyCapacity = ProgramAssembly.INITIAL;
		this.assemblySize = 0;
		this.reportErrors = Base.getBoolean(
				ctx, //
				ProgramAssembly.FLAG_REPORT_ERRORS,
				true);
	}

	/** @param container */
	public ProgramAssembly(final ExecProcess container) {

		final ExecProcess ctx = container;
		this.ctx = Exec.createProcess(ctx, "Assembly Builder / Optimizer");
		this.assemblyCode = null;
		this.assemblyCapacity = ProgramAssembly.INITIAL;
		this.assemblySize = 0;
		this.reportErrors = Base.getBoolean(
				ctx, //
				ProgramAssembly.FLAG_REPORT_ERRORS,
				true);
	}

	/** @param offset
	 * @param length */
	public void addCloned(final int offset, final int length) {

		if (this.assemblySize + length > this.assemblyCapacity) {
			this.assemblyCapacity = this.assemblySize + length + ProgramAssembly.INITIAL;
			final Instruction[] newRpn = new Instruction[this.assemblyCapacity];
			System.arraycopy(this.assemblyCode, 0, newRpn, 0, this.assemblySize);
			this.assemblyCode = newRpn;
		}
		System.arraycopy(this.assemblyCode, offset, this.assemblyCode, this.assemblySize, length);
		this.assemblySize += length;
	}

	/** @param message
	 * @throws Evaluate.CompilationException */
	public void addDebug(final String message) throws Evaluate.CompilationException {

		this.lastDebug = message;
		this.addInstruction(new IAVV_VFDEBUG_C(message));
	}

	/** @param error */
	public void addError(final Object error) {

		if (this.reportErrors) {
			if (error instanceof Throwable) {
				Report.exception("ASSEMBLY", "Assembly error", (Throwable) error);
			} else {
				final StringBuilder report = new StringBuilder();
				if (this.assemblySize > 0) {
					if (this.assemblyCode[0] instanceof IAVV_VFDEBUG_C) {
						report.append(((IAVV_VFDEBUG_C) this.assemblyCode[0]).getDebug()).append("\r\n");
					}
					for (int i = this.assemblySize - 1; i > 0; --i) {
						if (this.assemblyCode[i] instanceof IAVV_VFDEBUG_C) {
							report.append(((IAVV_VFDEBUG_C) this.assemblyCode[i]).getDebug()).append("\r\n");
							break;
						}
					}
				}
				report.append(error);
				Report.exception("ASSEMBLY", "Assembly error", new Error(report.toString()));
			}
		}
		final String text = this.lastDebug + " // " + error;
		this.addInstruction(
				OperationsA10.XFLOAD_P.instruction(
						new ModifierArgumentA30IMM(
								error instanceof String
									? text
									: error), //
						0,
						ResultHandler.FB_BNN_ERR) //
		);
		if (this.errors == null) {
			this.errors = text;
			return;
		}
		if (this.errors instanceof String) {
			this.errors = new ListString((String) this.errors, text);
			return;
		}
		((ListString) this.errors).add(text);
	}

	/** @param instruction
	 * @throws Evaluate.CompilationException */
	public void addInstruction(final Instruction instruction) throws Evaluate.CompilationException {

		if (instruction == null) {
			throw new NullPointerException();
		}
		if (instruction instanceof TokenInstruction) {
			assert false;
			((TokenInstruction) instruction).toAssembly(this, null, null, null);
			return;
		}
		if (this.assemblyCode == null) {
			this.assemblyCapacity = ProgramAssembly.INITIAL;
			this.assemblyCode = new Instruction[this.assemblyCapacity];
			this.assemblySize = 0;
		} else //
		if (this.assemblySize == this.assemblyCapacity) {
			final Instruction[] newRpn = new Instruction[this.assemblyCapacity << 2];
			System.arraycopy(this.assemblyCode, 0, newRpn, 0, this.assemblySize);
			this.assemblyCode = newRpn;
			this.assemblyCapacity = this.assemblyCode.length;
		}
		this.assemblyCode[this.assemblySize++] = instruction;
	}

	/** CATCH METHOD - use addTokenInstruction method
	 *
	 * @param instruction */
	@SuppressWarnings("static-method")
	@Deprecated
	public void addInstruction(final TokenInstruction instruction) {

		throw new IllegalStateException("Use addTokenInstruction method");
	}

	/** @return
	 * @throws Exception */
	public InstructionPlaceholder addInstructionPlaceholder() throws Exception {

		final InstructionPlaceholder placeholder = new PlaceholderSimple();
		this.addInstruction(placeholder);
		return placeholder;
	}

	/** TokenInstructions are expanded (and not accepted at all in assert mode) as an argument for
	 * addInstruction method. Use this method if you intentionally want to collect TokenInstructions
	 * using assembly instruction buffer.
	 *
	 * @param token */
	public void addTokenInstruction(final TokenInstruction token) {

		assert token != null;
		if (this.assemblyCode == null) {
			this.assemblyCapacity = ProgramAssembly.INITIAL;
			this.assemblyCode = new Instruction[this.assemblyCapacity];
			this.assemblySize = 0;
		} else //
		if (this.assemblySize == this.assemblyCapacity) {
			final Instruction[] newRpn = new Instruction[this.assemblyCapacity << 2];
			System.arraycopy(this.assemblyCode, 0, newRpn, 0, this.assemblySize);
			this.assemblyCode = newRpn;
			this.assemblyCapacity = newRpn.length;
		}
		this.assemblyCode[this.assemblySize++] = token;
	}

	/** @param precompiled
	 * @param balanceType
	 * @return stack result count
	 * @throws Exception */
	public TokenInstruction compileExpression(final List<TokenInstruction> precompiled, final BalanceType balanceType) throws Exception {

		/** <code>
		System.out.println( ">>>>> expression ("
				+ balanceType
				+ ")  code: "
				+ precompiled
				+ " @\r\n\t"
				+ new Error().getStackTrace()[1] );
		</code> */
		final int exprLength = precompiled.size();
		final boolean sideEffectsOnly = balanceType == BalanceType.STATEMENT;
		if (exprLength == 0) {
			return sideEffectsOnly
				? null
				: balanceType == BalanceType.ARRAY_LITERAL
					? TKV_CARRAY0.INSTANCE
					: balanceType == BalanceType.DECLARATION
						? ParseConstants.TKV_ERROR_DECLARATION_EXPECTED
						: ParseConstants.TKV_ERROR_EXPRESSION_EXPECTED
			/* ParseConstants.TKV_UNDEFINED */;
		}
		this.compilerEnsure(exprLength);
		final int operatorCount = this.compilerPointerOperator;
		final int valueCount = this.compilerTokens.length - this.compilerPointerValue;
		{
			int found = 0, last = 0;
			for (int i = 0;;) {
				final boolean end = i == exprLength;
				if (end && last == 0) {
					break;
				}
				/** FIXME: just support ',' as an operator which clears stack, why not? */
				if (end || precompiled.get(i++) == ParseConstants.TKS_COMMA) {
					final List<TokenInstruction> sub = precompiled.subList(
							last,
							end
								? i
								: i - 1);
					final BalanceType balance = balanceType == BalanceType.DECLARATION
						? balanceType
						: balanceType == BalanceType.ARRAY_LITERAL || balanceType == BalanceType.ARGUMENT_LIST
							? end && found == 0
								? balanceType
								: BalanceType.EXPRESSION
							: end
								? balanceType
								: BalanceType.STATEMENT;
					// System.out.println( ">>> >>> SUB: "
					// + sub
					// + ", balance="
					// + balanceType
					// + ", end="
					// + end
					// + "\r\n\t\tFULL: "
					// + precompiled );
					if (end && balanceType == BalanceType.ARRAY_LITERAL && sub.isEmpty()) {
						/** last empty COMMA is allowed */
						break;
					}
					final TokenInstruction token = this.compileExpression(sub, balance);
					/** possible for VOID balance type */
					if (token == null) {
						assert balance == BalanceType.STATEMENT || balance == BalanceType.DECLARATION;
						if (end) {
							if (found == 0) {
								return null;
							}
							break;
						}
						last = i;
						continue;
					}
					assert balance != BalanceType.STATEMENT || token.toConstantModifier() == null : "Optimizer failure";
					found++;
					if (end) {
						if (found == 1) {
							/** only one left */
							return token;
						}
						this.compilerPushOperator(token);
						break;
					}
					this.compilerPushOperator(token);
					last = i;
				}
			}
			if (last > 0) {
				if (found > 0) {
					/** DEBUG<code>
					System.out.println( ">>> >>> FLOW, balance=" + balanceType + ", itemCount=" + found );
					</code> */
					if (found == 1) {
						assert balanceType == BalanceType.DECLARATION : "Only declaration can produce less than 2 tokens here!";
						return this.compilerPopOperator(operatorCount);
					}
					assert found > 1 : "should be returned before!";
					if (balanceType == BalanceType.ARRAY_LITERAL) {
						final TokenInstruction[] tokens = new TokenInstruction[found];
						System.arraycopy(this.compilerTokens, operatorCount, tokens, 0, found);
						this.compilerPointerOperator = operatorCount;
						return tokens.length < 16
							? new TKV_CARRAYX_ON_STACK(tokens)
							: new TKV_CARRAYX_BY_PUSH(tokens);
					}
					if (balanceType == BalanceType.ARGUMENT_LIST) {
						final TokenInstruction[] tokens = new TokenInstruction[found];
						System.arraycopy(this.compilerTokens, operatorCount, tokens, 0, found);
						this.compilerPointerOperator = operatorCount;
						return new TK_WRAP_STACKX(tokens);
					}
					{
						if (found == 2) {
							final TokenInstruction token2 = this.compilerPopOperator(operatorCount);
							final TokenInstruction token1 = this.compilerPopOperator(operatorCount);
							return new TKV_EFLOW2(token1, token2);
						}
						if (found == 3) {
							final TokenInstruction token3 = this.compilerPopOperator(operatorCount);
							final TokenInstruction token2 = this.compilerPopOperator(operatorCount);
							final TokenInstruction token1 = this.compilerPopOperator(operatorCount);
							return new TKV_EFLOW3(token1, token2, token3);
						}
						final TokenInstruction[] tokens = new TokenInstruction[found];
						System.arraycopy(this.compilerTokens, operatorCount, tokens, 0, found);
						this.compilerPointerOperator = operatorCount;
						return new TKV_EFLOWX(tokens);
					}
				}
				/** Balance type VOID, all constants */
				assert balanceType == BalanceType.STATEMENT;
				return null;
			}
		}
		if (exprLength == 1) {
			final TokenInstruction value = precompiled.get(0);
			if (value.isStackValue()) {
				if (balanceType == BalanceType.ARGUMENT_LIST) {
					// one argument
					return value;
					// return new TK_WRAP_STACK1( value );
					// return value.toStackValue( this, false );
				}
				if (balanceType == BalanceType.DECLARATION) {
					final BasePrimitiveString name = value.toContextPropertyName();
					return name != null
						? null /* new TKV_FDECLARE_A_Cs_S( name ) */
						: ParseConstants.TKV_ERROR_DECLARATION_EXPECTED;
				}
				return balanceType == BalanceType.STATEMENT
					? value.toConstantValue() != null
						? null
						: value
					: balanceType == BalanceType.ARRAY_LITERAL
						? value.getResultType() == InstructionResult.NEVER
							? value
							: new TKV_CARRAY1(value)
						: value;
			}
		}
		if (balanceType == BalanceType.DECLARATION) {
			final TokenInstruction token1 = precompiled.get(0);
			if (token1 instanceof TKA_FSTORE_BA_SC_S) {
				final TokenInstruction rightHand = this.compileExpression(precompiled.subList(1, exprLength), BalanceType.EXPRESSION);
				return new TKV_FDECLARE_BA_VC_S(((TKA_FSTORE_BA_SC_S) token1).getBaseName(), rightHand);
			}
			return ParseConstants.TKV_ERROR_DECLARATION_EXPECTED;
		}
		for (int i = 0; i < exprLength; ++i) {
			final TokenInstruction token = precompiled.get(i);
			// System.out.println( ">>>>> token " + token );
			final TokenType type = token.getTokenType();
			if (type == TokenType.OPERATOR) {
				// System.out.println( ">>>>> operator " + token + ", prio=" +
				// token.getPriority() );
				if (!this.compilerFlushOperators(
						operatorCount, //
						valueCount,
						token.getPriorityLeft(),
						sideEffectsOnly)) {
					break;
				}
				this.compilerPushOperator(token);
				continue;
			}
			if (type == TokenType.VALUE) {
				// System.out.println( ">>>>> value " + token );
				assert token.getResultCount() == 1;
				if (token.getOperandCount() == 0) {
					this.compilerPushValue(token);
				} else {
					this.compilerPushOperator(token);
				}
				continue;
			}
			if (type == TokenType.ASSIGNMENT) {
				/** DEBUG <code>
				System.out.println( ">>>>> assignment "
						+ token
						+ ", class="
						+ token.getClass().getName()
						+ ", operandCount="
						+ token.getOperandCount() );
				</code> */
				/** assertion */
				assert token.getResultCount() == 1;
				if (!this.compilerFlushOperators(operatorCount, valueCount, token.getPriorityLeft(), false)) {
					break;
				}
				final int tokenOperands = token.getOperandCount();
				if (tokenOperands == 0) {
					assert token.assertStackValue();
					assert false : "Why ASSINGMENT? class=" + token.getClass().getName();
					this.compilerPushValue(token);
					continue;
				}
				// if ((tokenOperands == 1) && false) {
				if (tokenOperands == 1 && balanceType == BalanceType.EXPRESSION) {
					/** assignment requires expression on right */
					this.compilerPushOperator(token);
					continue;
				}

				final int length = ProgramAssembly.compilerSearchInSource(precompiled, exprLength, i, token.getPriorityRight());
				final TokenInstruction rightHand = this.compileExpression(precompiled.subList(i + 1, length), BalanceType.EXPRESSION);
				// System.out.println( ">>> >>> assign " + token +
				// " >>>>> rightHand=" + rightHand );
				if (rightHand.getResultType() == InstructionResult.NEVER) {
					this.compilerTruncateAll(operatorCount, valueCount);
					this.compilerPushValue(rightHand);
					break;
				}
				i = length - 1;
				if (tokenOperands == 1) {
					this.compilerPushValue(token.toStackValue(this, rightHand, false));
				} else //
				if (tokenOperands == 2) {
					final TokenInstruction operand = this.compilerPopValue(valueCount);
					assert operand != null : "lvalue expected";
					assert operand.assertStackValue();
					this.compilerPushOperator(new TKV_ASSIGN1(operand, token, rightHand));
				} else {
					assert false : "Too many operands for assignment, operandCount=" + tokenOperands + ", tokenClass=" + token.getClass().getName() + ", token=" + token;
					throw new IllegalStateException("Too many operands for assignment, operandCount=" + tokenOperands);
				}
				continue;
			}
			if (type == TokenType.SYNTAX) {
				/** DEBUG <code>
				System.out.println( ">>>>> syntax " + token );
				</code> */
				if (token == ParseConstants.TKS_EOR) {
					final int length = ProgramAssembly.compilerSearchInSource(precompiled, exprLength, i, token.getPriorityRight());
					/** full expression from what's on left */
					if (!this.compilerFlushOperators(
							operatorCount, //
							valueCount,
							token.getPriorityLeft(),
							/** not only side effects! */
							false)) {
						break;
					}
					if (this.compilerValueCount(valueCount) < 1) {
						final TKV_ERROR_A_C_E error = new TKV_ERROR_A_C_E("TKS_EOR: value expected, value count is " + this.compilerValueCount(valueCount) + " while 1 expected");
						this.compilerTruncateAll(operatorCount, valueCount);
						this.compilerPushValue(error);
						break;
					}
					{
						final BaseObject constantValue = this.compilerTokens[this.compilerPointerValue].toConstantValue();
						if (constantValue != null) {
							if (constantValue.baseToBoolean() == BaseObject.TRUE) {
								/** true - leave calculations, skip || part. */
								if (length == exprLength) {
									break;
								}
								/** skip */
								i = length - 1;
								continue;
							}
							/** false - skip calculations, leave || part. */
							this.compilerTokens[this.compilerPointerValue++] = null;
							/** all clear, anyway we need to compile the rest */
							continue;
						}
					}
					final TokenInstruction leftHand = this.compilerPopValue(valueCount);
					final TokenInstruction rightHand = this.compileExpression(
							precompiled.subList(i + 1, length),
							balanceType != BalanceType.STATEMENT
								? BalanceType.EXPRESSION
								: balanceType);
					i = length - 1;
					if (rightHand == null) {
						assert balanceType == BalanceType.STATEMENT;
						this.compilerPushValue(leftHand);
					} else {
						if (rightHand.getResultType() == InstructionResult.NEVER) {
							this.compilerTruncateAll(operatorCount, valueCount);
							this.compilerPushValue(rightHand);
							break;
						}
						this.compilerPushValue(new TKV_EBOR(leftHand, rightHand));
					}
					continue;
				}
				if (token == ParseConstants.TKS_EAND) {
					final int length = ProgramAssembly.compilerSearchInSource(precompiled, exprLength, i, token.getPriorityRight());
					/** full expression from what's on left */
					if (!this.compilerFlushOperators(
							operatorCount, //
							valueCount,
							token.getPriorityLeft(),
							/** not only side effects! */
							false)) {
						break;
					}
					if (this.compilerValueCount(valueCount) < 1) {
						final TKV_ERROR_A_C_E error = new TKV_ERROR_A_C_E("BAND: value expected, value count is " + this.compilerValueCount(valueCount) + " while 1 expected");
						this.compilerTruncateAll(operatorCount, valueCount);
						this.compilerPushValue(error);
						break;
					}
					{
						final BaseObject constantValue = this.compilerTokens[this.compilerPointerValue].toConstantValue();
						if (constantValue != null) {
							if (constantValue.baseToBoolean() == BaseObject.FALSE) {
								/** false - leave calculations, skip && part. */
								if (length == exprLength) {
									break;
								}
								/** skip */
								i = length - 1;
								continue;
							}
							/** true - skip calculations, leave && part. */
							this.compilerTokens[this.compilerPointerValue++] = null;
							/** all clear, anyway we need to compile the rest */
							continue;
						}
					}
					final TokenInstruction leftHand = this.compilerPopValue(valueCount);
					final TokenInstruction rightHand = this.compileExpression(
							precompiled.subList(i + 1, length),
							balanceType != BalanceType.STATEMENT
								? BalanceType.EXPRESSION
								: balanceType);
					i = length - 1;
					if (rightHand == null) {
						assert balanceType == BalanceType.STATEMENT;
						this.compilerPushValue(leftHand);
					} else {
						if (rightHand.getResultType() == InstructionResult.NEVER) {
							this.compilerTruncateAll(operatorCount, valueCount);
							this.compilerPushValue(rightHand);
							break;
						}
						this.compilerPushValue(new TKV_EBAND(leftHand, rightHand));
					}
					continue;
				}
				if (token == ParseConstants.TKS_BRACE_OPEN) {
					this.compilerTruncateAll(operatorCount, valueCount);
					this.compilerPushValue(ParseConstants.TKV_ERROR_UNMATCHED_BRACE_OPEN);
					break;
				}
				if (token == ParseConstants.TKS_CREATE_OPEN) {
					this.compilerTruncateAll(operatorCount, valueCount);
					this.compilerPushValue(ParseConstants.TKV_ERROR_UNMATCHED_CREATE_OPEN);
					break;
				}
				if (token == ParseConstants.TKS_INDEX_OPEN) {
					this.compilerTruncateAll(operatorCount, valueCount);
					this.compilerPushValue(ParseConstants.TKV_ERROR_UNMATCHED_INDEX_OPEN);
					break;
				}
				if (token == ParseConstants.TKS_QUESTION_MARK) {
					if (!this.compilerFlushOperators(
							operatorCount, //
							valueCount,
							token.getPriorityLeft(),
							/** not only side effects! */
							false)) {
						break;
					}
					this.compilerPushOperator(token);
					/** ESKIP0 / ESKIP1 can do with any object */
					continue;
				}
				if (token == ParseConstants.TKS_QUESTION_MARK_MATCHED) {
					if (!this.compilerFlushOperators(
							operatorCount, //
							valueCount,
							token.getPriorityLeft(),
							/** not only side effects! */
							false)) {
						break;
					}
					/** No compilerPush */
					/** ESKIP0 / ESKIP1 can do with any object */
					continue;
				}
				if (token == ParseConstants.TKS_ASSIGNMENT) {
					/** DEBUG <code>
					System.out.println( ">>>>> syntax assignment " + token );
					</code> */
					final int length = ProgramAssembly.compilerSearchInSource(precompiled, exprLength, i, token.getPriorityRight());
					if (!this.compilerFlushOperators(operatorCount, valueCount, token.getPriorityLeft(), false)) {
						break;
					}
					final TokenInstruction reference = this.compilerPopValue(valueCount);
					assert reference != null : "lvalue expected";
					assert reference.assertAccessReference();
					final TokenInstruction rightHand = this.compileExpression(precompiled.subList(i + 1, length), BalanceType.EXPRESSION);
					if (rightHand.getResultType() == InstructionResult.NEVER) {
						this.compilerTruncateAll(operatorCount, valueCount);
						this.compilerPushValue(rightHand);
						break;
					}
					this.compilerPushValue(new TKV_ASSIGN(reference, rightHand));
					i = length - 1;
					continue;
				}
			}
			throw new IllegalStateException("Invalid compile-time token: " + token);
		}
		return this.compilerFlushToInstruction(operatorCount, valueCount, balanceType);
	}

	private void compilerEnsure(final int count) {

		if (this.compilerTokens == null) {
			final int length = Math.max(32, count);
			this.compilerTokens = new TokenInstruction[length];
			assert this.compilerPointerOperator == 0;
			this.compilerPointerValue = length;
			return;
		}
		final int buffer = this.compilerTokens.length;
		final int operatorCount = this.compilerPointerOperator;
		final int valueCount = buffer - this.compilerPointerValue;
		if (buffer <= valueCount + operatorCount + count) {
			final int length = Math.min(buffer + 1024, buffer << 1) + count;
			final TokenInstruction[] replacement = new TokenInstruction[length];
			if (operatorCount > 0) {
				System.arraycopy(
						this.compilerTokens, //
						0,
						replacement,
						0,
						operatorCount);
			}
			if (valueCount > 0) {
				System.arraycopy(
						this.compilerTokens, //
						buffer - valueCount,
						replacement,
						length - valueCount,
						valueCount);
			}
			this.compilerTokens = replacement;
			this.compilerPointerValue = length - valueCount;
		}
	}

	/** @param operatorCount
	 * @param valueCount
	 * @param priorityLeft
	 * @return <code>false</code> on error or so. when processing should be stopped.<br>
	 *         <code>true</code> otherwise
	 * @throws Exception */
	private boolean compilerFlushOperators(final int operatorCount, final int valueCount, final int priorityLeft, final boolean sideEffectsOnly) throws Exception {

		for (; this.compilerPointerOperator > operatorCount;) {
			final TokenInstruction token = this.compilerTokens[this.compilerPointerOperator - 1];
			if (priorityLeft > token.getPriorityRight()) {
				break;
			}
			this.compilerTokens[--this.compilerPointerOperator] = null;
			final int operandCount = token.getOperandCount();
			{
				final int resultCount = token.getResultCount();
				if (resultCount == 0) {
					assert token.getResultType() == InstructionResult.NEVER : "Invalid instruction result type for no-result instruction: type=" + token.getResultType()
							+ ", tokenClass=" + token.getClass().getName() + ", token=" + token;
					assert token.assertZeroStackOperands();
					this.compilerTruncateAll(operatorCount, valueCount);
					final TokenInstruction value = token.toStackValue(this, false);
					assert value.assertStackValue();
					this.compilerPushValue(value);
					return false;
				}
				assert token.getResultCount() == 1 : "Not implementd yet, operandCount=" + token.getOperandCount() + ", resultCount=" + token.getResultCount() + ", token="
						+ token.getNotation() + ", tokenType=" + token.getTokenType();
			}
			/** <code>
			System.out.println( ">>>>> >>>>> FLUSH operator->value " + token + ", operands: " + operandCount );
			for (int i = operatorCount; i < this.compilerPointerOperator; ++i) {
				System.out.println( ">>>>> >>>>> >>>>> CO[" + i + "]" + this.compilerTokens[i] );
			}
			for (int i = this.compilerPointerValue; i < this.compilerTokens.length - valueCount; ++i) {
				System.out.println( ">>>>> >>>>> >>>>> CV[" + i + "]" + this.compilerTokens[i] );
			}
			System.out.println( ">>>>> >>>>> FLUSH DO" );
			 </code> */
			switch (operandCount) {
				case 0 :
					// System.out.println( ">>> >>> 0: " + token );
					this.compilerPushValue(token);
					break;
				case 1 : {
					final TokenInstruction argumentA = this.compilerPopValue(valueCount);
					if (argumentA == null) {
						this.compilerTruncateAll(operatorCount, valueCount);
						this.compilerPushValue(
								Report.MODE_ASSERT || Report.MODE_DEBUG
									? new TKV_ERROR_A_C_E(
											"Expression expected: operator '" + token.getNotation() + "' (class=" + token.getClass().getSimpleName() + ") needs one argument!")
									: ParseConstants.TKV_ERROR_EXPRESSION_EXPECTED);
						return false;
					}
					final TokenInstruction value = token.toStackValue(this, argumentA, sideEffectsOnly);
					assert value.assertStackValue();
					this.compilerPushValue(value);
					continue;
				}
				case 2 : {
					final TokenInstruction argumentB = this.compilerPopValue(valueCount);
					final TokenInstruction argumentA = this.compilerPopValue(valueCount);
					if (argumentA == null) {
						this.compilerTruncateAll(operatorCount, valueCount);
						this.compilerPushValue(
								Report.MODE_ASSERT || Report.MODE_DEBUG
									? new TKV_ERROR_A_C_E(
											"Expression expected: operator '" + token.getNotation() + "' (class=" + token.getClass().getSimpleName() + ") needs two arguments!")
									: ParseConstants.TKV_ERROR_EXPRESSION_EXPECTED);
						return false;
						/** MORE INFO<code>
						throw new IllegalArgumentException( "Value expected, two operands required but only "
								+ (argumentB == null
										? "zero"
										: "one")
								+ " available, tokenClass="
								+ token.getClass().getName()
								+ ", token="
								+ token );
						</code> */
					}
					final TokenInstruction value = token.toStackValue(this, argumentA, argumentB, sideEffectsOnly);
					assert value.assertStackValue();
					this.compilerPushValue(value);
					continue;
				}
				default :
					assert false : "invalid operand count: " + operandCount + ", tokenClass=" + token.getClass().getName() + ", token=" + token;
					this.compilerPushValue(token);
			}
		}
		return true;
	}

	private TokenInstruction compilerFlushToInstruction(final int operatorCount, final int valueCount, final BalanceType balanceType) throws Exception {

		final boolean sideEffectsOnly = balanceType == BalanceType.STATEMENT;
		/** everything */
		this.compilerFlushOperators(operatorCount, valueCount, -1, sideEffectsOnly);
		final int count = this.compilerValueCount(valueCount);
		if (count == 0) {
			return sideEffectsOnly
				? null
				: ParseConstants.TKV_UNDEFINED;
		}
		if (count == 1) {
			final TokenInstruction value = this.compilerPopValue(valueCount);
			return sideEffectsOnly
				? value.toConstantValue() != null
					? null
					: value
				: balanceType == BalanceType.ARRAY_LITERAL
					? value.getResultType() == InstructionResult.ARRAY
						? value
						: value.getResultType() == InstructionResult.NEVER
							? value
							: new TKV_CARRAY1(value)
					: value;
		}
		if (count == 2) {
			final TokenInstruction token2 = this.compilerPopValue(valueCount);
			final TokenInstruction token1 = this.compilerPopValue(valueCount);
			return new TKV_EFLOW2(token1, token2);
		}
		if (count == 3) {
			final TokenInstruction token3 = this.compilerPopValue(valueCount);
			final TokenInstruction token2 = this.compilerPopValue(valueCount);
			final TokenInstruction token1 = this.compilerPopValue(valueCount);
			return new TKV_EFLOW3(token1, token2, token3);
		}
		/** FIXME isn't it an opposite direction? */
		final TokenInstruction[] result = new TokenInstruction[count];
		for (int i = count, j = 0; i > 0; --i) {
			final int index = this.compilerPointerValue++;
			result[j++] = this.compilerTokens[index];
			this.compilerTokens[index] = null;
		}
		/** just an assumption */
		assert this.compilerValueCount(valueCount) == 0;
		return new TKV_EFLOWX(result);
	}

	private TokenInstruction compilerPopOperator(final int operatorCount) {

		if (this.compilerPointerOperator > operatorCount) {
			try {
				return this.compilerTokens[--this.compilerPointerOperator];
			} finally {
				this.compilerTokens[this.compilerPointerOperator] = null;
			}
		}
		return null;
	}

	private TokenInstruction compilerPopValue(final int valueCount) {

		if (this.compilerPointerValue < this.compilerTokens.length - valueCount) {
			try {
				return this.compilerTokens[this.compilerPointerValue++];
			} finally {
				this.compilerTokens[this.compilerPointerValue - 1] = null;
			}
		}
		return null;
	}

	private void compilerPushOperator(final TokenInstruction token) {

		assert this.compilerPointerOperator < this.compilerPointerValue : "capacity: " + this.compilerTokens.length + ", ptrO=" + this.compilerPointerOperator + ", ptrV="
				+ this.compilerPointerValue;
		assert token != null;
		this.compilerTokens[this.compilerPointerOperator++] = token;
	}

	private void compilerPushValue(final TokenInstruction value) {

		assert this.compilerPointerValue > this.compilerPointerOperator;
		assert value != null;
		this.compilerTokens[--this.compilerPointerValue] = value;
	}

	private void compilerTruncateAll(final int operatorCount, final int valueCount) {

		/** truncate values */
		while (this.compilerPointerValue < this.compilerTokens.length - valueCount) {
			this.compilerTokens[this.compilerPointerValue++] = null;
		}
		/** truncate operators */
		while (this.compilerPointerOperator > operatorCount) {
			this.compilerTokens[--this.compilerPointerOperator] = null;
		}
	}

	private int compilerValueCount(final int valueCount) {

		return this.compilerTokens.length - this.compilerPointerValue - valueCount;
	}

	/** Can be null if actually not a constant
	 *
	 * @param index
	 * @return
	 * @throws IllegalArgumentException */
	public BaseObject constantRead(final int index) throws IllegalArgumentException {

		if (this.constants == null) {
			throw new IllegalArgumentException("No constants!");
		}
		return this.constants.get(index).toConstantValue();
	}

	/** Compares strings as strings - to be compatible and interchangeable with another variant of
	 * this method.
	 *
	 * @param constant
	 * @return */
	public int constantRegister(final BaseObject constant) {

		if (this.constants == null) {
			this.constants = new ArrayList<>();
			this.constantMap = new HashMap<>();
		}
		if (constant.baseIsPrimitive()) {
			final BasePrimitive<?> primitive = constant.baseToPrimitive(null);
			Integer position = this.constantMap.get(primitive);
			if (position == null) {
				position = new Integer(this.constants.size());
				this.constantMap.put(primitive, position);
				this.constants.add(ParseConstants.getConstantValue(constant));
			}
			return position.intValue();
		}
		{
			final int position = this.constants.size();
			this.constants.add(ParseConstants.getConstantValue(constant));
			return position;
		}
	}

	/** Compares with string - kind hope that it would save time on BasePrimitiveString.getString()
	 * execution.
	 *
	 * @param constant
	 * @return */
	public int constantRegister(final String constant) {

		if (this.constants == null) {
			this.constants = new ArrayList<>();
			this.constantMap = new HashMap<>();
		}
		final BasePrimitiveString primitive = Base.forString(constant);
		Integer position = this.constantMap.get(primitive);
		if (position == null) {
			position = new Integer(this.constants.size());
			this.constantMap.put(primitive, position);
			this.constants.add(ParseConstants.getConstantValue(primitive));
		}
		return position.intValue();
	}

	/** Compares strings as strings - to be compatible and interchangeable with another variant of
	 * this method.
	 *
	 * @param constant
	 * @return */
	public int constantRegister(final TokenValue constant) {

		if (this.constants == null) {
			this.constants = new ArrayList<>();
			this.constantMap = new HashMap<>();
		}
		final BaseObject value = constant.toConstantValue();
		if (value != null && value.baseIsPrimitive()) {
			final BasePrimitive<?> primitive = value.baseToPrimitive(null);
			Integer position = this.constantMap.get(primitive);
			if (position == null) {
				position = new Integer(this.constants.size());
				this.constantMap.put(primitive, position);
				this.constants.add(constant);
			}
			return position.intValue();
		}
		{
			final int position = this.constants.size();
			this.constants.add(constant);
			return position;
		}
	}

	/** @param index
	 * @return
	 * @throws IllegalArgumentException */
	public TokenValue constantToken(final int index) throws IllegalArgumentException {

		if (this.constants == null) {
			throw new IllegalArgumentException("No constants!");
		}
		return this.constants.get(index);
	}

	/** @param buffer
	 * @param start
	 * @param end
	 * @return builder */
	public final StringBuilder dumpCode(final StringBuilder buffer, final int start, final int end) {

		for (int j = start; j < end; j++) {
			final String stringValue = this.assemblyCode[j].toCode();
			final String code;
			{
				final int length = stringValue.length();
				if (length <= 256) {
					code = stringValue.replace("\n", "\\n");
				} else {
					code = stringValue.substring(0, 256).replace("\n", "\\n") + "... and " + (length - 256) + " of " + length + " characters left...";
				}
			}
			buffer.append("\t        " + code + "\n");
		}
		return buffer;
	}

	private final void expandPrograms(final int offset) {

		final Instruction[] assemblyCode;
		{
			final int instructionCount = this.getInstructionCount(offset);
			if (instructionCount + offset > this.assemblyCapacity) {
				this.assemblyCapacity = instructionCount + offset;
				final Instruction[] newCode = new Instruction[this.assemblyCapacity];
				System.arraycopy(this.assemblyCode, 0, newCode, 0, this.assemblySize);
				assemblyCode = this.assemblyCode = newCode;
			} else {
				assemblyCode = this.assemblyCode;
			}
		}
		// TODO: optimise array-copying - go reverse and
		int i = offset;
		for (Instruction current; i < this.assemblySize; ++i) {
			current = assemblyCode[i];
			if (current instanceof ProgramPart) {
				final ProgramPart calc = (ProgramPart) current;
				final Instruction[] toInsert = calc.getInstructions();
				if (toInsert == null) {
					throw new NullPointerException();
				}
				final int length = toInsert.length;
				if (length == 1) {
					assemblyCode[i] = toInsert[0];
					continue;
				}
				if (length > 0) {
					System.arraycopy(assemblyCode, i + 1, assemblyCode, i + length, this.assemblySize - i - 1);
					System.arraycopy(toInsert, 0, assemblyCode, i, length);
					this.assemblySize += length - 1;
					i--;
					continue;
				}
				System.arraycopy(assemblyCode, i + 1, assemblyCode, i, this.assemblySize - i - 1);
				this.assemblySize--;
				i--;
				continue;
			}

			if (current instanceof InstructionEditable) {
				if (null != (current = ((InstructionEditable) current).getFinalIfReady())) {
					assemblyCode[i] = current;
				}
				continue;
			}
		}
	}

	/** @return errors */
	public Object getErrors() {

		return this.errors;
	}

	/** @param offset
	 * @return int */
	public final int getInstructionCount(final int offset) {

		int counter = 0;
		for (int i = offset; i < this.assemblySize; ++i) {
			final Instruction instr = this.assemblyCode[i];
			if (instr instanceof ProgramPart) {
				counter += ((ProgramPart) instr).getInstructions().length;
			} else {
				counter++;
			}
		}
		return counter;
	}

	/** @param position
	 * @param error
	 * @throws Exception */
	public void makeError(final int position, final Object error) throws Exception {

		if (!Report.MODE_DEVEL && position != -1) {
			this.truncate(position);
		}
		this.addError(error);
	}

	/** Default value is TRUE. When enabled, compilation errors will be logged.
	 *
	 * @param reportErrors */
	public void setReportErrors(final boolean reportErrors) {

		this.reportErrors = reportErrors;
	}

	/** @return size */
	public final int size() {

		return this.assemblySize;
	}

	/** @param offset
	 * @return program */
	public final Instruction toInstruction(final int offset) {

		if (offset == -1 && this.errors != null) {
			throw new IllegalStateException("Unit has compile errors:\n" + this.errors);
		}
		this.expandPrograms(offset);
		final int size = this.assemblySize - offset;
		if (size == 0) {
			return ProgramAssembly.PROGRAM_EMPTY;
		}
		final Instruction[] code = this.assemblyCode;
		if (size == 1) {
			this.assemblySize = offset;
			return code[offset];
		}
		if (offset == 0 && this.assemblyCapacity == size) {
			this.assemblySize = 0;
			this.assemblyCode = null;
			return new ProgramPart(code);
		}
		final Instruction[] result = new Instruction[size];
		System.arraycopy(code, offset, result, 0, size);
		this.assemblySize = offset;
		return new ProgramPart(result);
	}

	/** @param offset
	 * @return program */
	public final ProgramPart toProgram(final int offset) {

		if (offset == -1 && this.errors != null) {
			throw new IllegalStateException("Unit has compile errors:\n" + this.errors);
		}
		this.expandPrograms(offset);
		final int size = this.assemblySize - offset;
		if (size == 0) {
			return ProgramAssembly.PROGRAM_EMPTY;
		}
		final Instruction[] code = this.assemblyCode;
		if (size == 1) {
			if (code[offset] == ProgramAssembly.VALUE_UNDEFINED) {
				this.assemblySize = offset;
				return ProgramAssembly.PROGRAM_PUSH_UNDEFINED;
			}
			if (code[offset] == ProgramAssembly.VALUE_TRUE) {
				this.assemblySize = offset;
				return ProgramAssembly.PROGRAM_PUSH_TRUE;
			}
			if (code[offset] == ProgramAssembly.VALUE_FALSE) {
				this.assemblySize = offset;
				return ProgramAssembly.PROGRAM_PUSH_FALSE;
			}
		}
		if (offset == 0 && this.assemblyCapacity == size) {
			try {
				this.assemblySize = 0;
				return new ProgramPart(code);
			} finally {
				this.assemblyCode = null;
			}
		}
		final Instruction[] result = new Instruction[size];
		System.arraycopy(code, offset, result, 0, size);
		this.assemblySize = offset;
		return new ProgramPart(result);
	}

	/** @param offset */
	public void truncate(final int offset) {

		assert offset <= this.assemblySize : "Truncate should reduce size!";
		assert offset >= 0 : "Truncation position should be non-negative!";
		while (this.assemblySize > offset) {
			this.assemblyCode[--this.assemblySize] = null;
		}
	}
}
